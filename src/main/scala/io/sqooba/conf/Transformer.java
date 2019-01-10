package io.sqooba.conf;

public interface Transformer<T> {
    T transform(String src);
}
