package io.sqooba.sqconf.someapp;

import io.sqooba.conf.JavaSqConf;
import io.sqooba.conf.SqConf;
import java.util.HashMap;
import java.util.Map;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class SomeAppTest {

    JavaSqConf conf = new SqConf().asJava();

    @Test
    public void getIntCorrectly() {
        int someInt = conf.getInt("some.int");
        assertEquals(15, someInt);
    }

    @Test
    public void getStringCorrectly() {
        String someString = conf.getString("some.string");
        assertEquals("perse", someString);
    }

    @Test
    public void useOverWrites() {
        Map<String, String> ow = new HashMap<>();
        ow.put("some.string", "some.other.string");
        JavaSqConf newConf = conf.withOverwrites(ow);

        String newVal = newConf.getString("some.string");
        assertEquals("some.other.string", newVal);
    }
}
