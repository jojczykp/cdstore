CD Store - HBase solutions
==========================


What is HBase?
--------------

Apache HBase (TM) is distributed NoSQL BigData database.

More info [here](https://hbase.apache.org/).

This project uses basic features of HBase:
- Single column family
- Writing single item
- Reading single item
- Scanning items with simple criteria (SingleColumnValueFilter for substring)
- Update cells of single item

Also shows possible configuration of development environment, solving a couple
of issues and making project portable. 


Missing native tools on Windows
-------------------------------

HBase client uses native utils that are available on Linux, but not on Windows.
If we want to make it working on Windows, we need to explicitly load dll files
to compensate this shortage if Windows is detected.

Solution comes with two steps:

1. Maven

   Make sure Windows libraries are available in fat jar, so can be extracted if
   Windows is detected. To do this we should:
    
   1. Add maven repository so that libraries can be downloaded:

      ```xml
      <repository>
          <id>mapr</id>
          <name>Mapr releases repository</name>
          <url>http://repository.mapr.com/nexus/content/groups/mapr-public/releases</url>
      </repository>
      ```
    
    2. Add following dependency to `pom.xml`:

       ```xml
       <dependency>
           <groupId>org.apache.hadoop</groupId>
           <artifactId>hadoop-winutils</artifactId>
           <version>2.7.1</version>
           <type>zip</type>
           <scope>runtime</scope>
       </dependency>
        ```
        
        This brings files to root folder of fat jar.

2. Java

   Once dependency is there, unpack to temporary directory and load libraries
   if running on Windows:

    ```java
        if (SystemUtils.IS_OS_WINDOWS) {  // commons-lang3
            configureWindowsNatives();
        }
    ```

    ```java
    private void configureWindowsNatives() throws IOException {
        Path tempDirectory = Files.createTempDirectory("cdstore-");
        Path winUtils = tempDirectory.resolve("winutils");
        Path winUtilsBin = winUtils.resolve("bin");
        Files.createDirectories(winUtilsBin);
        System.setProperty("hadoop.home.dir", winUtils.toAbsolutePath().toString());
    
        for (String fileName: asList(
                "hadoop.dll", "hadoop.exp", "hadoop.lib", "hadoop.pdb",
                "libwinutils.lib", "winutils.exe", "winutils.pdb"))
            FileUtils.copyURLToFile(
                getClass().getResource("/" + fileName),
                winUtilsBin.resolve(fileName).toFile());
    }
    ```

    Now build/application should run on both: Linux and Windows.


Docker setup update for Integration Testing
-------------------------------------------

Even more interesting is Integration Testing, especially it's setup with
docker.

Many components that our applications connect to, exposes just a single port
which application can use. If that components run in docker container,
we can simply map that ports to ephemeral ports of docker host and configure
our tested application to connect to that mapped ones.

This approach does not work with HBase however. HBase client inside of
application running in host learns about region connection details from
Zookeeper, where region was registered before during HBase process startup.
Unfortunately HBase registers itself in Zookeeper with local (relative
to docker virtual network) host name and port that may not be available
from host (i.e. if we use docker machine).

To overcome this, we need to make sure that hostname that HBase registers
in Zookeeper (by default random docker container name) is resolvable by
host running client application. Moreover, we need to make sure that HBase is
available at that IP on exactly the same port that it was registered in
Zookeeper with. Of course, Zookeeper itself must be reachable as well, but this
is relatively _easy_ ;-)

Example:
- Docker machine interface: 192.168.99.100
- Docker bridge network (initially unreachable from host): 172.17.0.0/16
- HBase and Zookeeper running at 172.17.0.1
  - HBase Hostname: 15efb1840ce0
  - Zookeeper port: 2181/tcp
  - HBase Master port: 60000/tcp
  - HBase Region port: 60020/tcp

To examine what are IPs of docker bridge network and containers, we can use
following commands:
  ```
  $ docker network list
  $ docker network inspect bridge
  ```

With this configuration, HBase registers its region as 15efb1840ce0/60020.
One way to make it reachable from application running in host OS is:

1. Make sure that 172.17.0.1 is reachable from host.

   - If your docker does not use docker machine (default installation on Linux
     and Windows 10 Pro with Hyper-V), this should work out of the box.
     
   - If it does use docker machine, we need to add entry to routing table,
     so that traffic dedicated to 172.17.0.0/16 network is routed to
     192.168.99.100 gateway.
   
2. Make sure that 15efb1840ce0 is resolvable to 172.17.0.1 in host OS. This is
   a little bit challenging, since host names are chosen randomly and differ
   between runs.

   One possible option is to:
   
   - Run extra docker container hosting DNS server being able to resolve docker
     host names.
   
   - Expose it to host (map it's 53/udp port to docker host port).
   
   - Add docker host exposing DNS server as another DNS server to host OS, so
     that all applications running on host can use 15efb1840ce0 as synonym of
     172.17.0.1.
   
   I used one of publicly available docker images hosted DNS, basing on
   [dnsmasq](http://www.thekelleys.org.uk/dnsmasq/doc.html).
   
   Adding `<link/>` elements in DNS container configuration makes linked
   containers resolvable.


More setup details - Linux 
--------------------------

We are starting with default Linux docker installation (no docker machine used).

0. Start containers: ```$ mvn docker:start```

1. Routing - No changes needed. To verify: ```$ ping 172.17.0.1```

2. DNS

   ```# echo "nameserver 127.0.0.1" >> /etc/resolv.conf ```

   To verify:

   ```$ nslookup 15efb1840ce0```
   
   ```$ ping 15efb1840ce0```

3. To verify if HBase region is available from host: ```$ telnet 15efb1840ce0 60020```


More setup details - Windows
----------------------------

We are starting with default Windows docker installation, no Hyper-V (docker
machine used).

0. Start containers: ```C:\> mvn docker:start```

1. Routing -  As Administrator: ```C:\> route ADD 172.17.0.0 MASK 255.255.0.0 192.168.99.100```
   
   To verify: ```C:\>  ping 172.17.0.1 ```

2. DNS
   1. Go to details of network connection that is used to connect to your
      docker machine
   2. On _Networking_ tab select _Internet Protocol Version 4 (TCP/IPv4)_
   3. Click _Properties_
   4. On _General_ tab, section _Use the following DNS server addresses:_
      add IP of your docker machine (127.0.0.1 if you use docker on Windows with
      Hyper-V and no docker machine is needed/used)
   5. Click _Advanced_
   6. On _DNS_ tab add suffix "." (single dot). This prevents Windows from
      skipping using DNS while resolving names with empty domain (being just
      a single word representing hostname, as ones used by docker).
   7. Confirm by clicking _OK_ everywhere.
      See more info about _adding 'dot'_ e.g.
      [here](http://stackoverflow.com/questions/330395/dns-problem-nslookup-works-ping-doesnt#23377946) 
   8. You may also need to refresh your DNS client:

    ```
    C:\> ipconfig /registerdns
    C:\> ipconfig /flushdns
    ```
   
   To verify:

   ```C:\> nslookup 15efb1840ce0```
   
   ```C:\> ping 15efb1840ce0```

3. To verify if HBase region is available from host: ```C:\> telnet 15efb1840ce0 60020```

Now you can start application or run all tests:

```mvn clean verify```

Enjoy :)
