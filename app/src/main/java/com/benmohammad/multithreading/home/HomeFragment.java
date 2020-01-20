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
        }
    }

    @Nullable
    @Override
    public Fragment getHierarchicalParentFragment() {
        return null;
    }
}
