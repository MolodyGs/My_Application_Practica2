package com.example.myapplication.ui.dashboard;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.example.myapplication.R;
import com.example.myapplication.databinding.FragmentDashboardBinding;

public class DashboardFragment extends Fragment {

    private FragmentDashboardBinding binding;

    private EditText txt_name, txt_rut, txt_age;
    private TextView txt_error;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        DashboardViewModel dashboardViewModel =
                new ViewModelProvider(this).get(DashboardViewModel.class);

        binding = FragmentDashboardBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        View view = inflater.inflate(R.layout.fragment_dashboard, container, false);
        Button btnSave = (Button) binding.btnSave;
        Button btnFind = (Button) binding.btnFind;
        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SaveData(view);
            }
        });

        btnFind.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {FindUser(view);}
        });

        txt_name = (EditText) binding.txtName;
        txt_rut = (EditText) binding.txtRut;
        txt_age = (EditText) binding.txtAge;
        txt_error = (TextView) binding.txtError;

        txt_error.setText("");

        final TextView textView = binding.textDashboard;
        dashboardViewModel.getText().observe(getViewLifecycleOwner(), textView::setText);
        return root;
    }

    @SuppressLint("SetTextI18n")
    public void SaveData(View view) {
        if(txt_name.getText().toString().isEmpty() || txt_rut.getText().toString().isEmpty() || txt_age.getText().toString().isEmpty())
        {
            Toast.makeText(getContext(), "Se deben completar todos los campos", Toast.LENGTH_LONG).show();
            return;
        }
        SharedPreferences preferences = getActivity().getSharedPreferences("data", Context.MODE_PRIVATE);

        if(txt_rut.getText().toString().equals(preferences.getString(txt_rut.getText().toString() + "1", "")))
        {
            Toast.makeText(getContext(), "El rut ingresado ya existe", Toast.LENGTH_LONG).show();
            return;
        }
        Toast.makeText(getContext(), "Usuario Agregado Correctamente", Toast.LENGTH_LONG).show();
        SharedPreferences.Editor editor = preferences.edit();
        editor.putString(txt_rut.getText().toString() + "1", txt_rut.getText().toString());
        editor.putString(txt_rut.getText().toString() + "2", txt_name.getText().toString());
        editor.putString(txt_rut.getText().toString() + "3", txt_age.getText().toString());

        txt_name.setText("");
        txt_age.setText("");

        editor.apply();
    }

    @SuppressLint("SetTextI18n")
    public void FindUser(View view)
    {
        String strRut = txt_rut.getText().toString();
        if(strRut.isEmpty())
        {
            Toast.makeText(getContext(), "Debe ingresar un rut para buscar", Toast.LENGTH_LONG).show();
            return;
        }

        SharedPreferences preferences = getActivity().getSharedPreferences("data", Context.MODE_PRIVATE);

        if(preferences.getString(strRut + "1", "").equals("")){
            Toast.makeText(getContext(), "El rut ingresado no está registrado", Toast.LENGTH_LONG).show();
            return;
        }else {
            txt_name.setText(preferences.getString(strRut + "2", ""));
            txt_age.setText(preferences.getString(strRut + "3", ""));
            Toast.makeText(getContext(), "Usuario Encontrado exitosamente", Toast.LENGTH_LONG).show();
        }
    }
    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

}