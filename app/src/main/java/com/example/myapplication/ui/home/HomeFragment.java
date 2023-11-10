package com.example.myapplication.ui.home;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.example.myapplication.R;
import com.example.myapplication.databinding.FragmentHomeBinding;

import java.util.ArrayList;
import java.util.List;

public class HomeFragment extends Fragment {

    private FragmentHomeBinding binding;

    private EditText txtName, txtDes;
    private ListView listView;
    private Button btnSave;

    private final List<String> consoleName = new ArrayList<>();
    private final List<String> consoleDes = new ArrayList<>();
    private ArrayAdapter<String> adapter;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        HomeViewModel homeViewModel =
                new ViewModelProvider(this).get(HomeViewModel.class);

        binding = FragmentHomeBinding.inflate(inflater, container, false);
        View root = binding.getRoot();
        View view = inflater.inflate(R.layout.fragment_dashboard, container, false);

        txtName = (EditText) binding.homeTextName;
        txtDes = (EditText) binding.homeTextDes;
        btnSave = (Button) binding.homeBtnSave;
        listView = (ListView) binding.listView;

        adapter = new ArrayAdapter<>(this.getContext(), R.layout.listview_custom, consoleName);

        listView.setAdapter(adapter);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Toast.makeText(getContext(), consoleName.get(position) + ": " + consoleDes.get(position), Toast.LENGTH_LONG).show();
            }
        });

        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {saveConsole(view);}
        });

        final TextView textView = binding.textHome;
        homeViewModel.getText().observe(getViewLifecycleOwner(), textView::setText);
        return root;
    }

    public void saveConsole(View view)
    {
        if(txtName.getText().toString().isEmpty() || txtDes.getText().toString().isEmpty())
        {
            Toast.makeText(getContext(), "Todos los campos son obligatorios", Toast.LENGTH_SHORT).show();
            return;
        }
        else
        {
            for(String console : consoleName)
            {
                if(txtName.getText().toString().equals(console))
                {
                    Toast.makeText(getContext(), "La consola ya existe", Toast.LENGTH_SHORT).show();
                    return;
                }
            }
            consoleName.add(txtName.getText().toString());
            consoleDes.add(txtDes.getText().toString());

            adapter = new ArrayAdapter<>(getContext(), R.layout.listview_custom, consoleName);
            listView.setAdapter(adapter);

            txtName.getText().clear();
            txtDes.getText().clear();

            Toast.makeText(getContext(), "Consola agregada", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}