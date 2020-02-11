package com.benmohammad.multithreading.common;

import com.benmohammad.multithreading.demo.AtomicityDemonstrationFragment;
import com.benmohammad.multithreading.demo.CustomHandlerDemonstrationFragment;
import com.benmohammad.multithreading.demo.UiHandlerDemoFragment;
import com.benmohammad.multithreading.demo.UiThreadDemonstrationFragment;
import com.benmohammad.multithreading.demo.designasynctask.DesignWithAsyncTaskDemonstrationFragment;
import com.benmohammad.multithreading.demo.designcoroutines.DesignWithCoroutinesDemoFragment;
import com.benmohammad.multithreading.demo.designrxjava.DesignWithRxJavaDemonstrationFragment;
import com.benmohammad.multithreading.demo.designthread.DesignWithThreadsDemonstrationFragment;
import com.benmohammad.multithreading.demo.designthreadpool.DesignWithThreadPoolDemonstrationFragment;
import com.benmohammad.multithreading.demo.designthreadposter.DesignWithThreadPosterDemonstrationFragment;
import com.benmohammad.multithreading.demo.threadwait.ThreadWaitDemonstrationFragment;
import com.benmohammad.multithreading.exercises.Exercise1Fragment;
import com.benmohammad.multithreading.exercises.Exercise2Fragment;
import com.benmohammad.multithreading.exercises.Exercise3Fragment;
import com.benmohammad.multithreading.exercises.Exercise4Fragment;
import com.benmohammad.multithreading.exercises.Exercise5Fragment;
import com.benmohammad.multithreading.exercises.exercise6.Exercise6Fragment;
import com.benmohammad.multithreading.exercises.exercise7.Exercise7Fragment;
import com.benmohammad.multithreading.exercises.exercise8.Exercise8Fragment;
import com.benmohammad.multithreading.exercises.exercise9.Exercise9Fragment;
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

    public void toThreadWaitDemo() {
        fragmentHelper.replaceFragment(ThreadWaitDemonstrationFragment.newInstance());
    }

    public void toExercise5() {
        fragmentHelper.replaceFragment(Exercise5Fragment.newInstance());
    }

    public void toDesignWithThreadDemo() {
        fragmentHelper.replaceFragment(DesignWithThreadsDemonstrationFragment.newInstance());
    }

    public void toExercise6() {
        fragmentHelper.replaceFragment(Exercise6Fragment.newInstance());
    }

    public void toDesignWithThreadPoolDemo() {
        fragmentHelper.replaceFragment(DesignWithThreadPoolDemonstrationFragment.newInstance());
    }

    public void toExercise7() {
        fragmentHelper.replaceFragment(Exercise7Fragment.newinstance());
    }

    public void toDesignWithAsyncDemo() {
        fragmentHelper.replaceFragment(DesignWithAsyncTaskDemonstrationFragment.newInstance());
    }

    public void toDesignWithThreadPosterDemo() {
        fragmentHelper.replaceFragment(DesignWithThreadPosterDemonstrationFragment.newInstance());
    }

    public void toExercise8() {
        fragmentHelper.replaceFragment(Exercise8Fragment.newInstance());
    }

    public void toDesignWithRxJavaDemo() {
        fragmentHelper.replaceFragment(DesignWithRxJavaDemonstrationFragment.newInstance());
    }

    public void toExercise9() {
        fragmentHelper.replaceFragment(Exercise9Fragment.newInstance());
    }

    public void toDesignWithCoroutines() {
        fragmentHelper.replaceFragment(DesignWithCoroutinesDemoFragment.Companion.newInstance());
    }
}
