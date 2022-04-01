package com.github.mhprodev.httpflooder;

import lombok.experimental.UtilityClass;

import java.net.URL;

@UtilityClass
public class Validate {
    public void validArgs(String[] args, int i, String format) throws IllegalAccessException {
        if (args.length < i) {
            throw new IllegalAccessException(format);
        }
    }

    public void validClass(String arg, Class<?> aClass, String format) throws IllegalAccessException {
        try {
            if (Integer.class.equals(aClass)) {
                Integer.parseInt(arg);
            } else if (URL.class.equals(aClass)) {
                new URL(arg);
            }
        } catch (Exception e) {
            throw new IllegalAccessException(format);
        }
    }
}
