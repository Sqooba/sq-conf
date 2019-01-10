# Sq-Conf

SqConf is a thin wrapper for typesafe-config library ("com.typesafe.config" % "1.3.3") that was mainly 
created to make configuring applications running on containers easier. Mainly
overriding values from environment variables is made easier, no need to specifically define this in config.
Also key does not need to exist in the config in the first place. 

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

## Getting started
Add sqConf to your project, with sbt add this simple line:
```
    "io.sqooba" %% "sq-conf" % "0.4.0", 
```

And the whiny masochists using maven, please go get some professional help, you need it. Until meds kick in, 
here's a workaround, add this to your pom.xml:
```
    <dependency>
      <groupId>io.sqooba</groupId>
      <artifactId>sq-conf_2.12</artifactId>
      <version>0.4.0</version>
    </dependency>
```

## Future Plans
- interface based java transformer for custom types
- get as java.util.Properties convenience

## Change History
- 0.4.2 toProperties convenience method, transformer interface for java to transform from string to any time
- 0.4.1 remove dependency to option for the generic getter
- 0.4.0 dependency version upgrades, better documentation.
- 0.3.6 remove options from constructors so java wrapper works without scala standard lib

