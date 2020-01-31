package com.benmohammad.multithreading.home;

public enum ScreenReachableFromHome {

    UiHandlerDemo("Ui Handler Demo"),
    UiThreadDemo("Ui Thread Demo"),
    EXERCISE_1("Exercise 1"),
    EXERCISE_2("Exercise 2"),
    CUSTOM_HANDLER("Custom Handler Demo"),
    ATOMICITY_DEMO("Atomicity Demo"),
    EXERCISE_3("Exercise 3"),
    EXERCISE_4("Exercise 4");

    private String mName;

    ScreenReachableFromHome(String name) {
        this.mName = name;
    }

    public String getName() {
        return mName;
    }
}
