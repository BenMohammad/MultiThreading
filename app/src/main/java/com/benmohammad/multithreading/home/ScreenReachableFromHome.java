package com.benmohammad.multithreading.home;

public enum ScreenReachableFromHome {

    UiHandlerDemo("Ui Handler Demo"),
    UiThreadDemo("Ui Thread Demo"),
    EXERCISE_1("Exercise 1"),
    EXERCISE_2("Exercise 2"),
    CUSTOM_HANDLER("Custom Handler Demo"),
    ATOMICITY_DEMO("Atomicity Demo"),
    EXERCISE_3("Exercise 3"),
    EXERCISE_4("Exercise 4"),
    THREAD_WAIT_DEMO("Thread Wait Demo"),
    EXERCISE_5("Exercise 5"),
    DESIGN_WITH_THREAD("Design with Thread"),
    EXERCISE_6("Exercise 6"),
    DESIGN_WITH_THREADPOOL("Design with ThreadPool"),
    EXERCISE_7("Exercise 7"),
    DESIGN_WITH_ASYNC("Design with Async"),
    DESIGN_WITH_THREADPOSTER("Design with ThreadPoster"),
    EXERCISE_8("Exercise 8"),
    DESIGN_WITH_RXJAVA("Deesign with RxJava"),
    EXERCISE_9("Exercise 9"),
    DESIGN_WITH_COROUTINES("Design with Coroutines");

    private String mName;

    ScreenReachableFromHome(String name) {
        this.mName = name;
    }

    public String getName() {
        return mName;
    }
}
