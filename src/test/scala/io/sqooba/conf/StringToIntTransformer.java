package io.sqooba.conf;

public class StringToIntTransformer implements Transformer<Integer> {

    @Override
    public Integer transform(String src) {
        return Integer.parseInt(src);
    }
}
