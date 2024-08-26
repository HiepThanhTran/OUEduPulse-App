package com.fh.app_student_management.fragments;

import static android.app.Activity.RESULT_OK;
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
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.widget.SwitchCompat;
import androidx.fragment.app.Fragment;

import com.fh.app_student_management.R;
import com.fh.app_student_management.data.AppDatabase;
import com.fh.app_student_management.data.entities.User;
import com.fh.app_student_management.ui.ChangePasswordActivity;
import com.fh.app_student_management.ui.EditProfileActivity;
import com.fh.app_student_management.ui.LoginActivity;
import com.fh.app_student_management.utilities.Constants;
import com.fh.app_student_management.utilities.Utils;

import java.util.Map;
import java.util.Objects;

public class SettingFragment extends Fragment {

    private SharedPreferences preferences;
    private User user;
    private ImageView avatar;
    private TextView txtUsername;
    private final ActivityResultLauncher<Intent> editProfileLauncher = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            result -> {
                if (result.getResultCode() == RESULT_OK && result.getData() != null) {
                    long userId = result.getData().getLongExtra(Constants.USER_ID, 0);
                    user = AppDatabase.getInstance(requireContext()).userDAO().getById(userId);

                    txtUsername.setText(user.getFullName());
                    avatar.setImageBitmap(Utils.getBitmapFromBytes(user.getAvatar()));
                }
            }
    );
    private Button btnEditProfile;
    private RelativeLayout btnChangePassword;
    private RelativeLayout btnLogout;
    private SwitchCompat notificationSwitch;

    public static SettingFragment newInstance(Map<String, String> params) {
        SettingFragment fragment = new SettingFragment();
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
        View view = inflater.inflate(R.layout.z_fragment_settings, container, false);
        Window window = requireActivity().getWindow();
        window.setStatusBarColor(getResources().getColor(R.color.grey, requireActivity().getTheme()));

        initSettingsView(view);
        handleEventListener();

        return view;
    }

    private void initSettingsView(@NonNull View view) {
        avatar = view.findViewById(R.id.avatar);
        txtUsername = view.findViewById(R.id.txtUsername);
        btnEditProfile = view.findViewById(R.id.btnEditProfile);
        btnChangePassword = view.findViewById(R.id.btnChangePassword);
        btnLogout = view.findViewById(R.id.btnLogout);
        notificationSwitch = view.findViewById(R.id.notificationSwitch);

        preferences = requireActivity().getSharedPreferences(Constants.PREFS_NAME, MODE_PRIVATE);
        boolean isSwitchChecked = preferences.getBoolean(Constants.PREF_NOTIFICATION_SWITCH, false);

        avatar.setImageBitmap(Utils.getBitmapFromBytes(user.getAvatar()));
        txtUsername.setText(user.getFullName());
        notificationSwitch.setChecked(isSwitchChecked);
    }

    private void handleEventListener() {
        Bundle bundle = new Bundle();
        bundle.putLong(Constants.USER_ID, user.getId());

        btnEditProfile.setOnClickListener(v -> {
            Intent intent = new Intent(getActivity(), EditProfileActivity.class);
            intent.putExtras(bundle);
            editProfileLauncher.launch(intent);
        });

        btnChangePassword.setOnClickListener(v -> {
            Intent intent = new Intent(getActivity(), ChangePasswordActivity.class);
            intent.putExtras(bundle);
            startActivity(intent);
        });

        notificationSwitch.setOnCheckedChangeListener((buttonView, isChecked) -> preferences.edit()
                .putBoolean(Constants.PREF_NOTIFICATION_SWITCH, isChecked)
                .apply());

        btnLogout.setOnClickListener(v -> new AlertDialog.Builder(requireContext())
                .setTitle("Thông báo")
                .setMessage("Bạn có chắc chắn muốn đăng xuất?")
                .setPositiveButton("Có", (dialog, which) -> performLogout())
                .setNegativeButton("Không", (dialog, which) -> dialog.dismiss())
                .show());
    }

    private void performLogout() {
        SharedPreferences.Editor editor = preferences.edit();
        editor.remove(Constants.USER_ID);
        editor.apply();

        Intent intent = new Intent(getActivity(), LoginActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
        requireActivity().finish();
    }
}