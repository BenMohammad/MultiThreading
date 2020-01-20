package com.benmohammad.multithreading.home;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.benmohammad.multithreading.R;

public class HomeArrayAdapter extends ArrayAdapter<ScreenReachableFromHome> {

    public interface Listener {
        void onScreenClicked(ScreenReachableFromHome screenReachableFromHome);
    }

    private final Listener mListener;

    public HomeArrayAdapter(@NonNull Context context, Listener listener) {
        super(context, 0);
        this.mListener = listener;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        if(convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.list_item_screen_reachable_from_home, parent, false);

        }

        final ScreenReachableFromHome screenReachableFromHome = getItem(position);
        TextView txtName = convertView.findViewById(R.id.txt_screen_name);
        txtName.setText(screenReachableFromHome.getName());
        convertView.setOnClickListener(v -> mListener.onScreenClicked(screenReachableFromHome));

        return convertView;
    }
}
