package pl.jojczykp.cdstore.tracks;

import io.dropwizard.lifecycle.Managed;
import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.hbase.HBaseConfiguration;
import org.apache.hadoop.hbase.TableName;
import org.apache.hadoop.hbase.client.Connection;
import org.apache.hadoop.hbase.client.ConnectionFactory;
import org.apache.hadoop.hbase.client.Delete;
import org.apache.hadoop.hbase.client.Get;
import org.apache.hadoop.hbase.client.Put;
import org.apache.hadoop.hbase.client.Result;
import org.apache.hadoop.hbase.client.ResultScanner;
import org.apache.hadoop.hbase.client.Scan;
import org.apache.hadoop.hbase.client.Table;
import org.apache.hadoop.hbase.filter.FilterList;
import org.apache.hadoop.hbase.filter.FirstKeyOnlyFilter;
import org.apache.hadoop.hbase.filter.SingleColumnValueFilter;
import org.apache.hadoop.hbase.shaded.org.apache.commons.io.FileUtils;
import org.apache.hadoop.hbase.util.Bytes;
import pl.jojczykp.cdstore.albums.AlbumId;
import pl.jojczykp.cdstore.exceptions.ItemNotFoundException;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.Set;

import static java.util.Arrays.asList;
import static java.util.stream.Collectors.toList;
import static java.util.stream.Collectors.toSet;
import static java.util.stream.StreamSupport.stream;
import static org.apache.commons.lang3.SystemUtils.IS_OS_WINDOWS;
import static org.apache.hadoop.hbase.filter.CompareFilter.CompareOp.EQUAL;
import static org.apache.hadoop.hbase.filter.CompareFilter.CompareOp.NOT_EQUAL;
import static pl.jojczykp.cdstore.tracks.Track.aTrack;
import static pl.jojczykp.cdstore.tracks.TrackId.randomTrackId;

public class TracksRepository implements Managed {

	private static final byte[] F_DATA = toBytes("data");
	private static final byte[] C_ALBUM_ID = toBytes("albumId");
	private static final byte[] C_TITLE = toBytes("title");
	private static final String TABLE_NAME = "cdstore-Tracks";

	private final Configuration configuration;
	private Connection connection;
	private Table table;

	public TracksRepository(Table table) {
		this.configuration = null;
		this.table = table;
	}

	public TracksRepository(TracksConfiguration tracksConfiguration) {
		this.configuration = HBaseConfiguration.create();
		configuration.set("hbase.zookeeper.quorum", tracksConfiguration.getZookeeperQuorum());
		configuration.setInt("hbase.zookeeper.property.clientPort", tracksConfiguration.getZookeeperClientPort());
		configuration.set("hbase.master", tracksConfiguration.getHbaseMaster());
	}

	public Track createTrack(Track track) {
		TrackId trackId = randomTrackId();

		Put p = new Put(toBytes(trackId));
		p.addColumn(F_DATA, C_ALBUM_ID, toBytes(track.getAlbumId()));
		p.addColumn(F_DATA, C_TITLE, toBytes(track.getTitle()));

		try {
			table.put(p);
		} catch (IOException e) {
			throw new RuntimeException(e);
		}

		return track.toBuilder().id(trackId).build();
	}

	public Track getTrack(TrackId trackId) {
		try {
			Get g = new Get(toBytes(trackId));
			Result r = table.get(g);

			if (r.isEmpty()) {
				throw new ItemNotFoundException("track with given id not found");
			}

			return aTrack()
					.id(trackId)
					.albumId(getAlbumId(r))
					.title(getTitle(r))
					.build();
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
	}

	public Set<Track> getTracks(AlbumId albumId) {
		Scan s = new Scan();
		s.setFilter(new SingleColumnValueFilter(F_DATA, C_ALBUM_ID, EQUAL, toBytes(albumId)));

		try(ResultScanner scanner = table.getScanner(s)) {
			return stream(scanner.spliterator(), false)
					.map(r -> aTrack()
							.id(getTrackId(r))
							.albumId(albumId)
							.title(getTitle(r))
							.build())
					.collect(toSet());
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
	}

	public void updateTrack(TrackId trackId, Track patch) {
		Track track = patch.toBuilder().id(trackId).build();

		try {
			Put p = new Put(toBytes(trackId));
			if (patch.getAlbumId() != null) {
				//TODO never update this - fail if different!
				p.addColumn(F_DATA, C_ALBUM_ID, toBytes(track.getAlbumId()));
			}

			if (patch.getTitle() != null) {
				p.addColumn(F_DATA, C_TITLE, toBytes(track.getTitle()));
			}

			//TODO don't call if no changes
			boolean existedBefore = table.checkAndPut(toBytes(trackId), F_DATA, C_ALBUM_ID, NOT_EQUAL, toBytes("not existing"), p);
			if (!existedBefore) {
				throw new ItemNotFoundException("track with given id not found");
			}
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
	}

	public void deleteTrack(TrackId trackId) {
		Delete d = new Delete(toBytes(trackId));

		try {
			boolean existedBefore = table.checkAndDelete(toBytes(trackId), F_DATA, C_ALBUM_ID, NOT_EQUAL, toBytes("not existing"), d);
			if (!existedBefore) {
				throw new ItemNotFoundException("track with given id not found");
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public void deleteAllAlbumTracks(AlbumId albumId) {
		SingleColumnValueFilter albumIdFilter = new SingleColumnValueFilter(F_DATA, C_ALBUM_ID, EQUAL, toBytes(albumId));

		Scan s = new Scan();
		s.setFilter(new FilterList(albumIdFilter, new FirstKeyOnlyFilter()));

		try(ResultScanner scanner = table.getScanner(s)) {
			List<Delete> deletes = stream(scanner.spliterator(), false)
					.map(r -> new Delete(r.getRow()))
					.collect(toList());
			table.delete(deletes);
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
	}

	@Override
	public void start() throws Exception {
		if (IS_OS_WINDOWS) {
			Path tempDirectory = Files.createTempDirectory("cdstore-");
			Path winUtils = tempDirectory.resolve("winutils");
			Path winUtilsBin = winUtils.resolve("bin");
			Files.createDirectories(winUtilsBin);
			for (String fileName: asList("hadoop.dll", "hadoop.exp", "hadoop.lib", "hadoop.pdb", "libwinutils.lib", "winutils.exe", "winutils.pdb")) {
				FileUtils.copyURLToFile(
						getClass().getResource("/" + fileName),
						winUtilsBin.resolve(fileName).toFile());
			}
			System.setProperty("hadoop.home.dir", winUtils.toAbsolutePath().toString());
		}

		connection = ConnectionFactory.createConnection(configuration);
		table = connection.getTable(TableName.valueOf(TABLE_NAME));
	}

	@Override
	public void stop() throws Exception {
		if (table != null) {
			table.close();
		}

		if (connection != null) {
			connection.close();
		}
	}

	private static AlbumId getAlbumId(Result r) {
		byte[] albumIdBin = r.getValue(F_DATA, C_ALBUM_ID);
		return AlbumId.fromString(Bytes.toString(albumIdBin));
	}

	private static TrackId getTrackId(Result r) {
		byte[] trackIdBin = r.getRow();
		return TrackId.fromString(Bytes.toString(trackIdBin));
	}

	private static String getTitle(Result r) {
		byte[] titleBin = r.getValue(F_DATA, C_TITLE);
		return Bytes.toString(titleBin);
	}

	private static byte[] toBytes(AlbumId albumId) {
		return Bytes.toBytes(albumId.toString());
	}

	private static byte[] toBytes(TrackId trackId) {
		return Bytes.toBytes(trackId.toString());
	}

	private static byte[] toBytes(String str) {
		return Bytes.toBytes(str);
	}

}
