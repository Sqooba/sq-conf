# Sq-Conf - unified interface for all your configuration needs

[![Build Status](https://travis-ci.org/Sqooba/sq-conf.svg?branch=master)](https://travis-ci.org/Sqooba/sq-conf/) 
[![Coverage Status](https://coveralls.io/repos/github/Sqooba/sq-conf/badge.svg?branch=coverall-test)](https://coveralls.io/github/Sqooba/sq-conf?branch=coverall-test)
[![Maven](https://img.shields.io/maven-central/v/io.sqooba/sq-conf_2.11.svg)](https://maven-badges.herokuapp.com/maven-central/io.sqooba/sq-conf_2.11)

## Description

SqConf is unified interface for all your configuration needs. Essentially it's a thin wrapper for 
[typesafe-config](https://github.com/lightbend/config) library ("com.typesafe.config" % "1.3.3") 
that was mainly created to make configuring applications running on containers easier. 
Mainly overriding values from environment variables is made easier, no need to specifically 
define this in config. Also key does not need to exist in the config in the first place. 

```
    // reads application.conf
    val sqConf = new SqConf() 
    val sqConf = SqConf.default()
    
    val sqConf = SqConf.forFilename("other.conf") // reads other.conf from classpath instead.
    
    val sqConf = SqConf.forConfig(typeSafeConfig) // wrap existing instance of typesafe config.
    
    val sqConf = SqConf.forFile(configAsFile) // java.util.File 
```

## Value overrides example
Values in the config can be programatically overriden. This is convenient especially when testing.
```
    val overrideMap = Map("some.testIntValue" -> "15", "some.testStringValue" -> "overridenstring")

    val sqConf: SqConf = SqConf.default.withOverrides(overrides = overrideMap)
	
    sqConf.getInt("some.testIntValue") // returns 15
```

## Using environment variables
Environment variables are preferred over all other options and work for keys that are not available
in the original config file.
```
    export SOME_TESTINTVALUE="150"

    val sqConf: SqConf = SqConf.default()
	
    sqConf.getInt("some.testIntValue") // returns 150
```

## Other formats
SqConf supports all the formats that the underlying typesafe config also supports.

## Java Wrapper
SqConf also provides a java wrapper that returns java data types.
```
    val javaWrapper = new SqConf().asJava()
	
    javaWrapper.getInt("some.testIntValue") // returns java.lang.Integer
    javaWrapper.getString("some.testStringValue") // returns java.lang.String
```

## Converting to Properties class
Both native scala sqConf and the javaWrapper now have a convenience method to convert to Properties
object.
```
    val asProperties: Properties = new SqConf().toProperties()
	
    javaWrapper.getInt("some.testIntValue") // returns java.lang.Integer
    javaWrapper.getString("some.testStringValue") // returns java.lang.String
```

## Getting started
Sq-conf is cross compiled for scala 2.11 and 2.12. For the latest of the latest, sq-conf snapshots 
are available in sonatype repo: 
[sonatype-snapshots](https://oss.sonatype.org/content/repositories/snapshots/io/sqooba/). 
Add sqConf to your project, with sbt add this simple line:
```
    libraryDependencies +=  "io.sqooba" %% "sq-conf" % "0.4.2" 
```

Sq-conf works also with maven. Just add this to your pom.xml:
```
    <dependency>
      <groupId>io.sqooba</groupId>
      <artifactId>sq-conf_2.12</artifactId>
      <version>0.4.2</version>
    </dependency>
```

## Future Plans
- to be decided

## Change History
- 0.5.0 Add get config method to java wrapper, deployed first snapshot to sonatype repo.
- 0.4.2 New toProperties convenience method, transformer interface for java to transform from string to any type.
- 0.4.1 Remove dependency to option for the generic getter.
- 0.4.0 Dependency version upgrades, better documentation.
- 0.3.6 Remove options from constructors so java wrapper works without scala standard lib.
