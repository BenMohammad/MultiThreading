package com.benmohammad.multithreading.home;

public enum ScreenReachableFromHome {

    UiHandlerDemo("Ui Handler Demo"),
    UiThreadDemo("Ui Thread Demo"),
    EXERCISE_1("Exercise 1");

    private String mName;

    ScreenReachableFromHome(String name) {
        this.mName = name;
    }

    public String getName() {
        return mName;
    }
}
