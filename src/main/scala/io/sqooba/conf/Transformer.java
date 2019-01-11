package io.sqooba.conf;

/**
 * Transformer interface to convert underlying string format to any actual data type. Used by Java
 * -wrapper.
 */
public interface Transformer<T> {
    T transform(String src);
}
