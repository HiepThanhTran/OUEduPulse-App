package com.fh.app_student_management.ui;

import android.os.Bundle;
import androidx.fragment.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import com.fh.app_student_management.R;

public class HomeFragment extends Fragment {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home, container, false);

        Window window = getActivity().getWindow();
        window.setStatusBarColor(getResources().getColor(R.color.grey_sub, getActivity().getTheme()));

        return view;
    }
}
