package io.sqooba.conf;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class IterToList {
    public static <T> List<T> fromIterToList(Iterable<T> src) {
        List<T> res = new ArrayList<>();
        Iterator<T> iter = src.iterator();

        while (iter.hasNext()) {
            res.add(iter.next());
        }
        return res;
    }
}
