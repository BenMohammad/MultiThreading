package com.benmohammad.multithreading.common;

import com.benmohammad.multithreading.common.demo.UiHandlerDemoFragment;
import com.benmohammad.multithreading.home.HomeFragment;
import com.techyourchance.fragmenthelper.FragmentHelper;

public class ScreenNavigator {

    private final FragmentHelper fragmentHelper;

    public ScreenNavigator(FragmentHelper fragmentHelper) {
        this.fragmentHelper = fragmentHelper;
    }

    public void toHomeScreen() {
        fragmentHelper.replaceFragmentAndClearHistory(HomeFragment.newInstance());
    }

    public void navigateBack() {
        fragmentHelper.navigateBack();
    }

    public void navigateUp() {
        fragmentHelper.navigateUp();
    }

    public void toUiHandlerDemo() {
        fragmentHelper.replaceFragment(UiHandlerDemoFragment.newInstance());
    }

}
