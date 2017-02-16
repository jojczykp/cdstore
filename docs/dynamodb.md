CD Store - DynamoDB solutions
=============================

* [What is DynamoDB?](#what-is-dynamodb)
* [Unit Testing](#unit-testing)
* [Native libraries](#native-libraries)


What is DynamoDB?
-----------------

Amazon DynamoDB is a small NoSQL Cloud Database hosted in cloud.

More info [here](https://aws.amazon.com/dynamodb/).


Unit Testing
------------

DynamoDB comes with _DynamoDBLocal.jar_ that contains classes letting us to run
lightweight, in-memory version of DB that is perfect for testing.

In production code _Repository_ class initializes _real_ connection like follows:

```java
AmazonDynamoDB amazonDynamoDB = ...;
table = new DynamoDB(amazonDynamoDB).getTable(TABLE_NAME);
```

For Unit Testing however, I can stub it using _DynamoDBLocal_:

```java
table = new DynamoDB(amazonDynamoDB).getTable(TABLE_NAME);
```

Native libraries
----------------

_DynamoDBLocal_ uses native libraries. If we want to use it, we need to make sure
that all are reachable from our code. There are different versions for
different OSes. Configuration of this project brings all of them together, so
application should build correctly regardless of the platform.

They are:
- libsqlite4java-linux-amd64.so
- libsqlite4java-linux-i386.so
- libsqlite4java-osx.dylib
- sqlite4java-win32-x64.dll
- sqlite4java-win32-x86.dll

I tested `linux-amd64` and `win32-x64`.

To make build automatically download and use native library, we need to setup
it in `pom.xml` properly:

1. Add maven repository, so that libraries can be downloaded:

    ```xml
    <repository>
        <id>dynamodb-local</id>
        <name>DynamoDB Local Release Repository</name>
        <url>http://dynamodb-local.s3-website-us-west-2.amazonaws.com/release</url>
    </repository>
    ```

2. Download libraries during the build (if not cached by maven yet):

    ```xml
    <plugin>
        <groupId>com.googlecode.maven-download-plugin</groupId>
        <artifactId>download-maven-plugin</artifactId>
        <version>1.2.1</version>
        <executions>
            <execution>
                <id>install-dynamodb_local</id>
                <phase>process-test-resources</phase>
                <goals>
                    <goal>wget</goal>
                </goals>
                <configuration>
                    <url>http://dynamodb-local.s3-website-us-west-2.amazonaws.com/dynamodb_local_latest.zip</url>
                    <unpack>true</unpack>
                    <outputDirectory>${project.build.directory}/dynamodb</outputDirectory>
                </configuration>
            </execution>
        </executions>
    </plugin>
    ```

3. Make UT initialization code aware of libraries location an create in-memory instance:

    ```groovy
    def setup() {
        System.setProperty("sqlite4java.library.path", "target/dynamodb/DynamoDBLocal_lib")
        AmazonDynamoDB dynamoDB = DynamoDBEmbedded.create()
    }
    ```
