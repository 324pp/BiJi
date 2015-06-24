package com.mh.biji;

import java.util.UUID;

/**
 * Created by MH on 2015-06-24.
 */
public class Fn {
    public static String getGuid() {
        UUID id = UUID.randomUUID();
        return id.toString();
    }
}
