package com.fh.app_student_management.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.fh.app_student_management.R;
import com.fh.app_student_management.data.AppDatabase;
import com.fh.app_student_management.data.dao.UserDAO;
import com.fh.app_student_management.data.entities.User;
import com.fh.app_student_management.utilities.Constants;
import com.fh.app_student_management.utilities.ImageUtils;

import java.util.Map;
import java.util.Objects;

public class HomeFragment extends Fragment {

    private User user;

    public static HomeFragment newInstance(Map<String, String> params) {
        HomeFragment fragment = new HomeFragment();
        Bundle args = new Bundle();

        args.putString(Constants.USER_EMAIL, params.get(Constants.USER_EMAIL));
        fragment.setArguments(args);

        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            String userEmail = getArguments().getString(Constants.USER_EMAIL);
            UserDAO userDAO = AppDatabase.getInstance(requireContext()).userDAO();

            user = userDAO.getByEmail(userEmail);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home, container, false);

        Window window = Objects.requireNonNull(requireActivity()).getWindow();
        window.setStatusBarColor(getResources().getColor(R.color.grey_sub,
                requireActivity().getTheme()));

        if (Objects.requireNonNull(user.getRole()) == Constants.Role.LECTURER) {
            loadFragment(new LecturerFragment());
        }

        initHomeView(view);

        return view;
    }

    private void initHomeView(View view) {
        TextView txtUsername = view.findViewById(R.id.txtUsername);
        ImageView avatar = view.findViewById(R.id.avatar);

        txtUsername.setText(user.getFullName());
        avatar.setImageBitmap(ImageUtils.getBitmapFromBytes(user.getAvatar()));
    }

    private void loadFragment(Fragment fragment) {
        FragmentManager fragmentManager = getChildFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.frameLayout, fragment);
        fragmentTransaction.commit();
    }
}
