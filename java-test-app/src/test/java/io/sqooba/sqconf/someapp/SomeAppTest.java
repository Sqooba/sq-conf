package io.sqooba.sqconf.someapp;

import io.sqooba.conf.JavaSqConf;
import io.sqooba.conf.SqConf;
import java.time.Duration;
import java.util.*;
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

    @Test
    public void getIterableOfStrings() {

        Iterable<String> someStrings = conf.getIterable("some.stringList");

        List<String> asList = Util.fromIterToList(someStrings);

        assertEquals(3, asList.size());
        assertEquals(asList.get(0), "string1");
        assertEquals(asList.get(1), "string2");
        assertEquals(asList.get(2), "string3");
    }

    @Test
    public void getListOfStrings() {

        List<String> someStrings = conf.getListOf("some.stringList");

        assertEquals(3, someStrings.size());
        assertEquals(someStrings.get(0), "string1");
        assertEquals(someStrings.get(1), "string2");
        assertEquals(someStrings.get(2), "string3");
    }

    @Test
    public void getListOfDuration() {

        Iterable<Duration> someStrings = conf.getIterable("some.durationList");
        List<Duration> asList = Util.fromIterToList(someStrings);

        assertEquals(3, asList.size());
    }
}
