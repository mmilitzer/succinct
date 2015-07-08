
Succinct
========

Succinct is a minimal templating engine designed for concurrency and efficiency. It has an
especially abstract data model, allowing you to back your templates by fetching template
data on-demand, during render-time, according to your own complex synchronization protocols.
It comes with lots of tools to help you make your data "templatable."  

Please see the [Succinct web site] (http://threecrickets.com/succinct/) for more
information.

[![Download](http://threecrickets.com/media/download.png "Download")](http://threecrickets.com/succinct/download/)

Maven:

    <repository>
        <id>threecrickets</id>
        <name>Three Crickets Repository</name>
        <url>http://repository.threecrickets.com/maven/</url>
    </repository>
    <dependency>
        <groupId>com.threecrickets.succinct</groupId>
        <artifactId>succinct</artifactId>
    </dependency>


Building Succinct
-----------------

All you need to build Succinct is [Ant] (http://ant.apache.org/).

Simply change to the "/build/" directory and run "ant".

During the build process, build and distribution dependencies will be downloaded from
an online repository at http://repository.threecrickets.com/, so you will need Internet
access.

The result of the build will go into the "/build/distribution/" directory. Temporary
files used during the build process will go into "/build/cache/", which you are free to
delete.


Configuring the Build
---------------------

The "/build/custom.properties" file contains configurable settings, along with some
commentary on what they are used for. You are free to edit that file, however to avoid
git conflicts, it would be better to create your own "/build/private.properties"
instead, in which you can override any of the settings. That file will be ignored by
git.

To avoid the "bootstrap class path not set" warning during compilation
(harmless), configure the "compile.boot" setting in "private.properties" to the
location of an "rt.jar" file belonging to JVM version 6.


Deploying to Maven
------------------

You do *not* need Maven to build Succinct, however you can deploy your build to
a Maven repository using the "deploy-maven" Ant target. To enable this, you must
install [Maven] (http://maven.apache.org/) and configure its path in
"private.properties".


Still Having Trouble?
---------------------

Join the [Prudence Community]
(http://groups.google.com/group/prudence-community), and tell us where you're
stuck! We're very happy to help newcomers get up and running.