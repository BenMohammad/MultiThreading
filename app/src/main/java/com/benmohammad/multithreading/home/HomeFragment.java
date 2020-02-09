package com.benmohammad.multithreading.home;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.benmohammad.multithreading.R;
import com.benmohammad.multithreading.common.BaseFragment;
import com.benmohammad.multithreading.common.ScreenNavigator;

public class HomeFragment extends BaseFragment implements HomeArrayAdapter.Listener{
    public static Fragment newInstance() {
        return new HomeFragment();
    }

    private ScreenNavigator screenNavigator;
    private ListView mListScreenReachableFromHome;
    private HomeArrayAdapter arrayAdapter;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home, container, false);
        screenNavigator = getCompositionRoot().getScreenNavigator();
        arrayAdapter = new HomeArrayAdapter(requireContext(), this);
        mListScreenReachableFromHome = view.findViewById(R.id.list_screens);
        mListScreenReachableFromHome.setAdapter(arrayAdapter);

        arrayAdapter.addAll(ScreenReachableFromHome.values());
        arrayAdapter.notifyDataSetChanged();

        return view;
    }

    @Override
    protected String getScreenTitle() {
        return "Home Screen";
    }


    @Override
    public void onScreenClicked(ScreenReachableFromHome screenReachableFromHome) {
        switch(screenReachableFromHome) {
            case UiHandlerDemo:
                screenNavigator.toUiHandlerDemo();
                break;
            case UiThreadDemo:
                screenNavigator.toUiThreadDemo();
                break;
            case EXERCISE_1:
                screenNavigator.toExercise1();
                break;
            case EXERCISE_2:
                screenNavigator.toExercise2();
                break;
            case CUSTOM_HANDLER:
                screenNavigator.toCustomHandlerDemo();
                break;
            case ATOMICITY_DEMO:
                screenNavigator.toAtomicityDemo();
                break;
            case EXERCISE_3:
                screenNavigator.toExercise3();
                break;
            case EXERCISE_4:
                screenNavigator.toExercise4();
                break;
            case THREAD_WAIT_DEMO:
                screenNavigator.toThreadWaitDemo();
                break;
            case EXERCISE_5:
                screenNavigator.toExercise5();
                break;
            case DESIGN_WITH_THREAD:
                screenNavigator.toDesignWithThreadDemo();
                break;
            case EXERCISE_6:
                screenNavigator.toExercise6();
                break;
            case DESIGN_WITH_THREADPOOL:
                screenNavigator.toDesignWithThreadPoolDemo();
                break;
            case EXERCISE_7:
                screenNavigator.toExercise7();
                break;
            case DESIGN_WITH_ASYNC:
                screenNavigator.toDesignWithAsyncDemo();
                break;
            case DESIGN_WITH_THREADPOSTER:
                screenNavigator.toDesignWithThreadPosterDemo();
                break;
        }
    }

    @Nullable
    @Override
    public Fragment getHierarchicalParentFragment() {
        return null;
    }
}
