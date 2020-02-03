package com.benmohammad.multithreading;

import android.app.Application;

import com.benmohammad.multithreading.common.di.ApplicationCompositionRoot;

public class MyApplication extends Application {

    private final ApplicationCompositionRoot applicationCompositionRoot =
            new ApplicationCompositionRoot();

    public ApplicationCompositionRoot getApplicationCompositionRoot() {
        return new ApplicationCompositionRoot();
    }
}
