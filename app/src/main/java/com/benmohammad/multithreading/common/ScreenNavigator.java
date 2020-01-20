package com.benmohammad.multithreading.common;

import com.benmohammad.multithreading.common.demo.UiHandlerDemoFragment;
import com.benmohammad.multithreading.common.demo.UiThreadDemonstrationFragment;
import com.benmohammad.multithreading.exercises.Exercise1Fragment;
import com.benmohammad.multithreading.exercises.Exercise2Fragment;
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

    public void toUiThreadDemo() {
        fragmentHelper.replaceFragment(UiThreadDemonstrationFragment.newInstance());
    }

    public void toExercise1() {
        fragmentHelper.replaceFragment(Exercise1Fragment.newInstance());
    }

    public void toExercise2() {
        fragmentHelper.replaceFragment(Exercise2Fragment.newInstance());
    }
}
