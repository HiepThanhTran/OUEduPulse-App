package com.fh.app_student_management.fragments;

import static android.app.Activity.RESULT_OK;
import static android.content.Context.MODE_PRIVATE;

import android.app.Activity;
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

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.widget.SwitchCompat;
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

public class SettingFragment extends Fragment {

    private static final int REQUEST_CODE_EDIT_PROFILE = 1;

    private User user;
    private SharedPreferences preferences;

    private ImageView avatar;
    private TextView txtUsername;
    private Button btnEditProfile;
    private LinearLayout btnLogout;
    private SwitchCompat notificationSwitch;

    public static SettingFragment newInstance(Map<String, String> params) {
        SettingFragment fragment = new SettingFragment();
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
        View view = inflater.inflate(R.layout.fragment_setting, container, false);

        Window window = requireActivity().getWindow();
        window.setStatusBarColor(getResources().getColor(R.color.grey,
                requireActivity().getTheme()));

        initSettingsView(view);
        handleEventListener();

        return view;
    }

    private final ActivityResultLauncher<Intent> editProfileLauncher = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            result -> {
                if (result.getResultCode() == RESULT_OK && result.getData() != null) {
                    String userEmail = result.getData().getStringExtra(Constants.USER_EMAIL);
                    UserDAO userDAO = AppDatabase.getInstance(requireContext()).userDAO();
                    user = userDAO.getByEmail(userEmail);

                    txtUsername.setText(user.getFullName());
                    avatar.setImageBitmap(ImageUtils.getBitmapFromBytes(user.getAvatar()));
                }
            }
    );

    private void initSettingsView(View view) {
        avatar = view.findViewById(R.id.avatar);
        txtUsername = view.findViewById(R.id.txtUsername);

        btnEditProfile = view.findViewById(R.id.btnEditProfile);
        btnLogout = view.findViewById(R.id.btnLogout);
        notificationSwitch = view.findViewById(R.id.notificationSwitch);

        preferences = requireActivity().getSharedPreferences(Constants.PREFS_NAME, MODE_PRIVATE);
        boolean isSwitchChecked = preferences.getBoolean(Constants.PREF_NOTIFICATION_SWITCH, false);

        txtUsername.setText(user.getFullName());
        avatar.setImageBitmap(ImageUtils.getBitmapFromBytes(user.getAvatar()));
        notificationSwitch.setChecked(isSwitchChecked);
    }

    private void handleEventListener() {
        btnEditProfile.setOnClickListener(v -> {
            Intent intent = new Intent(getActivity(), EditProfileActivity.class);
            intent.putExtra(Constants.USER_EMAIL, user.getEmail());
            editProfileLauncher.launch(intent);
        });

        notificationSwitch.setOnCheckedChangeListener((buttonView, isChecked) -> {
            SharedPreferences.Editor editor = preferences.edit();
            editor.putBoolean(Constants.PREF_NOTIFICATION_SWITCH, isChecked);
            editor.apply();
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
        SharedPreferences.Editor editor = preferences.edit();
        editor.remove(Constants.USER_EMAIL);
        editor.apply();

        Intent intent = new Intent(getActivity(), LoginActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
        requireActivity().finish();
    }
}