package com.benmohammad.multithreading.common;

import com.benmohammad.multithreading.demo.AtomicityDemonstrationFragment;
import com.benmohammad.multithreading.demo.CustomHandlerDemonstrationFragment;
import com.benmohammad.multithreading.demo.UiHandlerDemoFragment;
import com.benmohammad.multithreading.demo.UiThreadDemonstrationFragment;
import com.benmohammad.multithreading.exercises.Exercise1Fragment;
import com.benmohammad.multithreading.exercises.Exercise2Fragment;
import com.benmohammad.multithreading.exercises.Exercise3Fragment;
import com.benmohammad.multithreading.exercises.Exercise4Fragment;
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

    public void toCustomHandlerDemo() {
        fragmentHelper.replaceFragment(CustomHandlerDemonstrationFragment.newInstance());
    }

    public void toAtomicityDemo() {
        fragmentHelper.replaceFragment(AtomicityDemonstrationFragment.newInstance());
    }

    public void toExercise3() {
        fragmentHelper.replaceFragment(Exercise3Fragment.newInstance());
    }

    public void toExercise4() {
        fragmentHelper.replaceFragment(Exercise4Fragment.newInstance());
    }
}
