package com.fh.app_student_management.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.fh.app_student_management.R;
import com.fh.app_student_management.data.AppDatabase;
import com.fh.app_student_management.data.entities.User;
import com.fh.app_student_management.utilities.Constants;
import com.fh.app_student_management.utilities.Utils;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class HomeFragment extends Fragment {

    private User user;

    @NonNull
    public static HomeFragment newInstance(@NonNull Map<String, String> params) {
        HomeFragment fragment = new HomeFragment();
        Bundle args = new Bundle();

        args.putString(Constants.USER_ID, params.get(Constants.USER_ID));
        fragment.setArguments(args);

        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (getArguments() != null) {
            long userId = Long.parseLong(Objects.requireNonNull(requireArguments().getString(Constants.USER_ID)));
            user = AppDatabase.getInstance(requireContext()).userDAO().getById(userId);
        }
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.z_fragment_home, container, false);
        Window window = Objects.requireNonNull(requireActivity()).getWindow();
        window.setStatusBarColor(getResources().getColor(R.color.grey_sub, requireActivity().getTheme()));

        initHomeView(view);

        return view;
    }

    private void initHomeView(View view) {
        Map<String, String> params = new HashMap<>();
        params.put(Constants.USER_ID, String.valueOf(user.getId()));

        switch (user.getRole()) {
            case ADMIN:
                loadFragment(AdminFragment.newInstance(params));
                break;
            case SPECIALIST:
                break;
            case LECTURER:
                loadFragment(LecturerFragment.newInstance(params));
            default:
                break;
        }

        ImageView avatar = view.findViewById(R.id.avatar);
        TextView txtUsername = view.findViewById(R.id.txtUsername);

        avatar.setImageBitmap(Utils.getBitmapFromBytes(user.getAvatar()));
        txtUsername.setText(user.getFullName());
    }

    private void loadFragment(Fragment fragment) {
        FragmentManager fragmentManager = getChildFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.frameLayout, fragment);
        fragmentTransaction.commit();
    }
}