package com.benmohammad.multithreading.home;

public enum ScreenReachableFromHome {

    TEMP("Temp");

    private String mName;

    ScreenReachableFromHome(String name) {
        this.mName = name;
    }

    public String getName() {
        return mName;
    }
}
