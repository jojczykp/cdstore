CD Store - Lombok solutions
===========================


What is Lombok
--------------

Lombok is a nice technology saving us from writing a lot of boilerplate,
'obvious' code.

It generates code implementing (depending on annotations
used) methods like _equals()_, _hashCode()_, _toString()_, constructors,
builders, getters and/or setters.

More info [here](https://projectlombok.org/).


How it is used here?
--------------------

In this project I used:
- [@Data](https://projectlombok.org/features/Data.html),
- [@RequiredArgsConstructor](https://projectlombok.org/features/Constructor.html),
- [@ToString](https://projectlombok.org/features/ToString.html),
- [@EqualsAndHashCode](https://projectlombok.org/features/EqualsAndHashCode.html),

Also version with _of()_, narrowing down the set of fields used by generated methods:

```java
@EqualsAndHashCode(of = "uuid");
```

- [@Builder(toBuilder = true, builderMethodName = "anAlbum")](https://projectlombok.org/features/Builder.html)

So i.e. to create a new object from existing one (applying single field change here),
we can write:

```java
Album album1 = anAlbum()
    .id(new UUID(1, 2))
    .title("A Title 1")
    .build();

Album album2 = album1.toBuilder()
    .title("A Title 1")
    .build();
```


_Lomboked_ sources location
---------------------------

During the development I prefer to treat _lomboked_ java files in the same way
as _regular_ ones, keeping all of them together in the _src_ folder.

Alternatively, we could store them separately and setup maven configuration to generate
_plain java_ files to _target/generated-sources_ in pre-compile phase.
This would give us view in what exactly Lombok generates, however it seems to impede
code navigation a little bit.

So I prefer the first approach. :)


Lombok and IntelliJ
-------------------

Unfortunately, Lombok plugin 0.14.16 doesn't support my approach very smoothly with IntelliJ 2016.3,
so I needed some extra configuration here:

- Install Lombok Plugin:

  ```File -> Settings -> Plugins: Search for 'Lombok Plugin'```

- Setup IntelliJ to use eclipse compiler:

  ```Build, Execution, Deployment -> Compiler -> Java Compiler -> Use Compiler: Eclipse```

- Instrument Eclipse compiler with Lombok (also, surprisingly, I had to specify path to _ecj.jar_ (no spaces in paths!))

  ```Build, Execution, Deployment -> Compiler -> User-local build process VM options (overrides Shared options):```

E.g.:

  ```-javaagent:~/.m2/repository/org/projectlombok/lombok/1.16.12/lombok-1.16.12.jar -Xbootclasspath/a:~/.m2/repository/org/projectlombok/lombok/1.16.12/lombok-1.16.12.jar -Xbootclasspath/a:~/intellij/lib/ecj-4.6.1.jar ```

I hope newer version of IntelliJ Lombok plugin will simplify this setup. ;-)


Lombok and Maven
----------------

To 'teach' maven to use Lombok in a way that I like, following compiler settings in
_pom.xml_ were needed.
Configuration below for Java8 + Groovy + Lombok:

```xml
<plugin>
    <groupId>org.apache.maven.plugins</groupId>
    <artifactId>maven-compiler-plugin</artifactId>
    <version>3.6.0</version>
    <configuration>
        <compilerId>groovy-eclipse-compiler</compilerId>
        <fork>true</fork>
        <compilerArguments>
            <javaAgentClass>lombok.launch.Agent</javaAgentClass>
        </compilerArguments>
        <source>1.8</source>
        <target>1.8</target>
    </configuration>
    <dependencies>
        <dependency>
            <groupId>org.codehaus.groovy</groupId>
            <artifactId>groovy-eclipse-compiler</artifactId>
            <version>2.9.2-01</version>
        </dependency>
        <dependency>
            <groupId>org.codehaus.groovy</groupId>
            <artifactId>groovy-eclipse-batch</artifactId>
            <version>2.4.3-01</version>
        </dependency>
        <dependency>
            <groupId>org.projectlombok</groupId>
            <artifactId>lombok</artifactId>
            <version>${lombok.version}</version>
        </dependency>
    </dependencies>
</plugin>
```

Also Lombok dependency scope can be `provided`:

```xml
</project>
    ...
    </dependencies>
        ....
        <dependency>
            <groupId>org.projectlombok</groupId>
            <artifactId>lombok</artifactId>
            <version>${lombok.version}</version>
            <scope>provided</scope>
        </dependency>
    </dependencies>
</project>
```
