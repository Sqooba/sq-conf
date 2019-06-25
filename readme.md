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
	
    val someIntVal = asProperties.getProperty("some.testIntValue").toInt // properties always returns string
    val someStringVal = asProperties.getProperty("some.testStringValue")
```

## Configure order of preference
Order of preference defines in which order configuration keys are read. By default the order goes:
 1.  valueOverrides given during creation of config
 2.  environment variables
 3.  config file
 Order can be configured freely, for example:
 ```
   val orderOfPreference: List[OrderOfPreference.Value] = List(
     OrderOfPreference.CONF_FIlE,
     OrderOfPreference.ENV_VARIABLE,
     OrderOfPreference.VALUE_OVERRIDES)
   val conf = new SqConf().configureOrder(oop)
 ```
 Now order would be reversed in comparison to the default.

## Getting started
Sq-conf is cross compiled for scala 2.11, 2.12 and 2.13. For the latest of the latest, sq-conf snapshots 
are available in sonatype repo: 
[sonatype-snapshots](https://oss.sonatype.org/content/repositories/snapshots/io/sqooba/). 
Add sqConf to your project, with sbt add this simple line:
```
    libraryDependencies +=  "io.sqooba" %% "sq-conf" % "0.5.2" 
```

Sq-conf works also with maven. Just add this to your pom.xml:
```
    <dependency>
      <groupId>io.sqooba</groupId>
      <artifactId>sq-conf_2.12</artifactId>
      <version>0.5.2</version>
    </dependency>
```

## Future Plans
- improve api documentation/ from nonexistent to something
- yml support
- test more complex type conversions from app.conf, also with java transformers
- getAvailableKeys, already exists, but requires work

## Change History
- 0.5.2 Scala 2.13 support, preliminary 
- 0.5.1 Allow configuring order of preference, bug fix value overrides not being passed on to sub confix, scala 2.13 support
- 0.5.0 Add get config method to java wrapper, deployed first snapshot to sonatype repo.
- 0.4.2 New toProperties convenience method, transformer interface for java to transform from string to any type.
- 0.4.1 Remove dependency to option for the generic getter.
- 0.4.0 Dependency version upgrades, better documentation.
- 0.3.6 Remove options from constructors so java wrapper works without scala standard lib.
