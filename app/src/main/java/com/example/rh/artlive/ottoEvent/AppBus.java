package com.example.rh.artlive.ottoEvent;

import com.squareup.otto.Bus;

/**
 * Created by PC on 2016/7/1.
 */
public class AppBus extends Bus {

    private static AppBus bus;

    public static AppBus getInstance() {
        if (bus == null) {
            bus = new AppBus();
        }
        return bus;
    }
}
