# Sq-Conf

Wrapper around typesafe-config library ("com.typesafe.config" % "1.3.3") that allows overriding variables with 
commandline parameters or environment variables. Reads file called application.conf from resources -folder on default.
```
    // reads application.conf
    val sqConf = new SqConf() 
    val sqConf = SqConf.default()
    
    val sqConf = SqConf.forFilename("other.conf") // reads other.conf from classpath instead.
    
    val sqConf = SqConf.forConfig(typeSafeConfig) // wrap existing instance of typesafe config.
    
    val sqConf = SqConf.forFile(configAsFile) // java.util.File 
```

## Value overWrites example
Values in the config can be programatically overwriten uses
```
	val overwrites = Map("some.testIntValue" -> "15", "some.testStringValue" -> "overridenstring")

	val sqConf: SqConf = SqConf.default.withOverrides(overwrites = overwrites)
	
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

## Java Wrapper
SqConf also provides a java wrapper that returns java data types.
```
	val javaWrapper = new SqConf().asJava()
	
	javaWrapper.getInt("some.testIntValue") // returns java.lang.Integer
	javaWrapper.getString("some.testStringValue") // returns java.lang.String
	
```

# Change History

- 0.4.0 dependency version upgrades
- 0.3.6 remove options from constructors so java wrapper works without scala standard lib

