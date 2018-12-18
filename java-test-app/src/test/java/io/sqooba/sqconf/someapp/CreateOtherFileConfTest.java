package io.sqooba.sqconf.someapp;

import io.sqooba.conf.JavaSqConf;
import io.sqooba.conf.SqConf;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class CreateOtherFileConfTest {

    JavaSqConf conf = SqConf.forFilename("another.conf").asJava();

    @Test
    public void getIntCorrectly() {
        int someInt = conf.getInt("some.int");
        assertEquals(16, someInt);
    }

    @Test
    public void getIntListCorrectly() {
        Iterable<Integer> someInts = conf.getIterable("some.intList");
        List<Integer> ints = new ArrayList<>();
        Iterator<Integer> iter = someInts.iterator();

        while (iter.hasNext()) {
            ints.add(iter.next());
        }
        assertEquals(3, ints.size());
    }

    @Test
    public void getStringCorrectly() {
        String someString = conf.getString("some.string");
        assertEquals("perses", someString);
    }
}
