package com.benmohammad.multithreading.common.di;

import android.os.Handler;
import android.os.Looper;

import androidx.fragment.app.FragmentActivity;

import com.benmohammad.multithreading.common.ScreenNavigator;
import com.benmohammad.multithreading.common.ToolbarManipulator;
import com.techyourchance.fragmenthelper.FragmentContainerWrapper;
import com.techyourchance.fragmenthelper.FragmentHelper;

import java.util.concurrent.ThreadPoolExecutor;

public class PresentationCompositionRoot {

    private final FragmentActivity mActivity;

    private ApplicationCompositionRoot mApplicationCompoitionRoot;
    public PresentationCompositionRoot(FragmentActivity activity, ApplicationCompositionRoot applicationCompositionRoot) {
        this.mActivity = activity;
        this.mApplicationCompoitionRoot = applicationCompositionRoot;
    }

    public ScreenNavigator getScreenNavigator() {
        return new ScreenNavigator(getFragmentHelper());
    }

    private FragmentHelper getFragmentHelper() {
        return new FragmentHelper(mActivity, getFragmentWrapper(), mActivity.getSupportFragmentManager());
    }

    private FragmentContainerWrapper getFragmentWrapper() {
        return (FragmentContainerWrapper)mActivity;
    }

    public ToolbarManipulator getToolbarManipulator() {
        return (ToolbarManipulator) mActivity;
    }

    public Handler getMUIHandler() {
        return new Handler(Looper.getMainLooper());
    }

    public ThreadPoolExecutor getThreadPoolExecutor() {
        return mApplicationCompoitionRoot.getThreadPoolExecutor();
    }

    }
