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

    private EditText txtname, txtrut, txtage;

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

        txtname = (EditText) binding.txtName;
        txtrut = (EditText) binding.txtRut;
        txtage = (EditText) binding.txtAge;

        final TextView textView = binding.textDashboard;
        dashboardViewModel.getText().observe(getViewLifecycleOwner(), textView::setText);
        return root;
    }

    //Guarda la información de un usuario: Nombre, rut y edad, utilizando SharedPreferences.
    @SuppressLint("SetTextI18n")
    public void SaveData(View view) {
        if(txtname.getText().toString().isEmpty() || txtrut.getText().toString().isEmpty() || txtage.getText().toString().isEmpty())
        {
            Toast.makeText(getContext(), "Se deben completar todos los campos", Toast.LENGTH_SHORT).show();
            return;
        }
        SharedPreferences preferences = getActivity().getSharedPreferences("data", Context.MODE_PRIVATE);

        if(txtrut.getText().toString().equals(preferences.getString(txtrut.getText().toString() + "1", "")))
        {
            Toast.makeText(getContext(), "El rut ingresado ya existe", Toast.LENGTH_SHORT).show();
            return;
        }
        Toast.makeText(getContext(), "Usuario Agregado Correctamente", Toast.LENGTH_SHORT).show();
        SharedPreferences.Editor editor = preferences.edit();
        editor.putString(txtrut.getText().toString() + "1", txtrut.getText().toString());
        editor.putString(txtrut.getText().toString() + "2", txtname.getText().toString());
        editor.putString(txtrut.getText().toString() + "3", txtage.getText().toString());

        txtname.setText("");
        txtage.setText("");

        editor.apply();
    }

    //Busca a un usuario con respecto a su Rut, si lo encuentra despliega su información.
    @SuppressLint("SetTextI18n")
    public void FindUser(View view)
    {
        String strRut = txtrut.getText().toString();
        if(strRut.isEmpty())
        {
            Toast.makeText(getContext(), "Debe ingresar un rut para buscar", Toast.LENGTH_SHORT).show();
            return;
        }

        SharedPreferences preferences = getActivity().getSharedPreferences("data", Context.MODE_PRIVATE);

        if(preferences.getString(strRut + "1", "").equals("")){
            Toast.makeText(getContext(), "El rut ingresado no está registrado", Toast.LENGTH_SHORT).show();
            return;
        }else {
            txtname.setText(preferences.getString(strRut + "2", ""));
            txtage.setText(preferences.getString(strRut + "3", ""));
            Toast.makeText(getContext(), "Usuario Encontrado exitosamente", Toast.LENGTH_SHORT).show();
        }
    }
    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

}