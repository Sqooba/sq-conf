# Sq-Conf

Wrapper around typesafe-config library ("com.typesafe.config" % "1.3.2") that allows overriding variables with 
commandline parameters or environment variables. Reads file called application.conf from resources -folder on default.
```
    val sqConf = new SqConf() // reads application.conf
    
    val sqConf = new SqConf("other.conf") reads other.conf from classpath instead. 
```

## Value overrides example
```
	val overrides = Map("some.testIntValue" -> "15", "some.testStringValue" -> "overridenstring")

	val sqConf: SqConf = new SqConf(valueOverrides = overrides)
	
	sqConf.getInt("some.testIntValue") // returns 15
```

## Java Wrapper
SqConf also provides a java wrapper that returns java data types.
```
	val javaWrapper = new SqConf().asJava()
	
	javaWrapper.getInt("some.testIntValue") // returns java.lang.Integer
	javaWrapper.getString("some.testStringValue") // returns java.lang.String
	
```
