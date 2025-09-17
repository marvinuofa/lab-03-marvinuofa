package com.example.listycitylab3;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

public class AddCityFragment extends DialogFragment {
    interface AddCityDialogListener {
        void addCity(City city);
        void editCity(int position, String newName, String newProvince);
    }

    private AddCityDialogListener listener;

    // newInstance method for editing existing city
    public static AddCityFragment newInstance(String name, String province, int position) {
        AddCityFragment fragment = new AddCityFragment();
        Bundle args = new Bundle();
        args.putString("name", name);
        args.putString("province", province);
        args.putInt("position", position);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        if (context instanceof AddCityDialogListener) {
            listener = (AddCityDialogListener) context;
        } else {
            throw new RuntimeException(context + " must implement AddCityDialogListener");
        }
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        View view = LayoutInflater.from(getContext()).inflate(R.layout.fragment_add_city, null);
        EditText editCityName = view.findViewById(R.id.edit_text_city_text);
        EditText editProvinceName = view.findViewById(R.id.edit_text_province_text);

        // Default: add mode
        String title = "Add a city";
        String positiveBtn = "Add";
        int position = -1;

        // If arguments exist, we are in edit mode
        Bundle args = getArguments();
        if (args != null) {
            String name = args.getString("name", "");
            String province = args.getString("province", "");
            position = args.getInt("position", -1);

            if (!name.isEmpty() && !province.isEmpty()) {
                editCityName.setText(name);
                editProvinceName.setText(province);
                title = "Edit city";
                positiveBtn = "Save";
            }
        }

        int finalPosition = position;
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        return builder
                .setView(view)
                .setTitle(title)
                .setNegativeButton("Cancel", null)
                .setPositiveButton(positiveBtn, (dialog, which) -> {
                    String cityName = editCityName.getText().toString();
                    String provinceName = editProvinceName.getText().toString();
                    if (finalPosition == -1) {
                        // Add mode
                        listener.addCity(new City(cityName, provinceName));
                    } else {
                        // Edit mode
                        listener.editCity(finalPosition, cityName, provinceName);
                    }
                })
                .create();
    }
}
