CD Store - Docker solutions
===========================


What is Docker?
---------------

Docker is a service that lets us run a bunch of virtual machines building our
development environment in a very light way.

More info [here](https://www.docker.com/what-docker). 


Why Docker?
-----------

To setup complex Integration Tests environment in a way that makes it:
- As much similar to production as possible
- As much isolated from host as possible  

To build this project you need to have docker service/daemon installed.
This setup was verified with default docker installation on Linux and Windows
(docker machine).


HBase in Docker
---------------

Because of the way how HBase registers itself in Zookeeper (always uses
local hostname), we need to do some extra networking setup in host OS to make
HBase regions running inside of the docker container accessible by tested
application.

This needs to be done just once, after docker installation and requires
privileged permissions.

Please follow [this link](hbase.md) for steps to be taken once default
docker installation is completed.


Docker and Maven
----------------

For integration docker with maven lifecycle I used a great
[fabric8io docker-maven-plugin](https://github.com/fabric8io/docker-maven-plugin).
