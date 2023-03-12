package com.ominext.trainning.pharmacy.utils.constant;

import java.util.Random;
import java.util.stream.Collectors;

public class AutoPassGeneraror {
    public static String AutoPass(int a) {
        String password = new Random().ints(a, 33, 122).mapToObj(i -> String.valueOf((char)i)).collect(Collectors.joining());
        return password;
    }
}
