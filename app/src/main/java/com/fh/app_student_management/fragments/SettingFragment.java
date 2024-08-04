package com.fh.app_student_management.fragments;

import static android.content.Context.MODE_PRIVATE;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;

import com.fh.app_student_management.R;
import com.fh.app_student_management.data.AppDatabase;
import com.fh.app_student_management.data.dao.UserDAO;
import com.fh.app_student_management.data.entities.User;
import com.fh.app_student_management.ui.EditProfileActivity;
import com.fh.app_student_management.ui.LoginActivity;
import com.fh.app_student_management.utilities.Constants;
import com.fh.app_student_management.utilities.ImageUtils;

import java.util.Map;
import java.util.Objects;

public class SettingFragment extends Fragment {

    private static final String USER_EMAIL = "userEmail";
    private User user;

    private Button btnEditProfile;
    private LinearLayout btnLogout;

    public static SettingFragment newInstance(Map<String, String> params) {
        SettingFragment fragment = new SettingFragment();
        Bundle args = new Bundle();

        args.putString(USER_EMAIL, params.get(USER_EMAIL));
        fragment.setArguments(args);

        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            String userEmail = getArguments().getString(USER_EMAIL);
            UserDAO userDAO = AppDatabase.getInstance(requireContext()).userDAO();

            user = userDAO.getByEmail(userEmail);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_setting, container, false);

        Window window = requireActivity().getWindow();
        window.setStatusBarColor(getResources().getColor(R.color.grey, requireActivity().getTheme()));

        initSettingsView(view);
        handleEventListener();

        return view;
    }

    private void initSettingsView(View view) {
        TextView txtUsername = view.findViewById(R.id.txtUsername);
        ImageView avatar = view.findViewById(R.id.avatar);

        btnEditProfile = view.findViewById(R.id.btnEditProfile);
        btnLogout = view.findViewById(R.id.btnLogout);

        txtUsername.setText(user.getFullName());
        avatar.setImageBitmap(ImageUtils.getBitmapFromBytes(user.getAvatar()));
    }

    private void handleEventListener() {
        btnEditProfile.setOnClickListener(v -> {
            Intent intent = new Intent(getActivity(), EditProfileActivity.class);
            startActivity(intent);
        });

        btnLogout.setOnClickListener(v -> {
            AlertDialog.Builder builder = new AlertDialog.Builder(requireContext());
            builder.setTitle("Thông báo");
            builder.setMessage("Bạn có chắc chắn muốn đăng xuất?");
            builder.setPositiveButton("Có", (dialog, which) -> logout());
            builder.setNegativeButton("Không", (dialog, which) -> dialog.dismiss());
            builder.show();
        });
    }

    private void logout() {
        SharedPreferences sharedPreferences =
                requireActivity().getSharedPreferences(Constants.KEY_SHARED_PREFERENCES,
                        MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.remove("userEmail");
        editor.apply();

        Intent intent = new Intent(getActivity(), LoginActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
        requireActivity().finish();
    }
}