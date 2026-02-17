package ru.ildarovichm.howmuchcash.ui.main;

import static android.content.Context.MODE_PRIVATE;

import androidx.lifecycle.ViewModelProvider;

import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;

import ru.ildarovichm.howmuchcash.ObjectUnit;
import ru.ildarovichm.howmuchcash.R;
import ru.ildarovichm.howmuchcash.Unit;
import ru.ildarovichm.howmuchcash.databinding.FragmentInputObjectsBinding;

public class InputObjectsFragment extends Fragment {

    private FragmentInputObjectsBinding binding;
    SharedPreferences settings;
    private ArrayList<ObjectUnit> listLoadedFromShPrefs = new ArrayList<>();
    private static final Gson gson = new Gson();
    public static InputObjectsFragment newInstance() {
        return new InputObjectsFragment();
    }

    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        InputObjectsViewModel inputObjectsViewModel =
                new ViewModelProvider(this).get(InputObjectsViewModel.class);
        settings = getActivity().getSharedPreferences("ObjectUnitListPrefs", MODE_PRIVATE);
        binding = FragmentInputObjectsBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onStart() {
        super.onStart();

        binding.buttonAddObject.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Unit unit = new Unit(
                        String.valueOf(binding.editTextCity.getText()).trim(),
                        String.valueOf(binding.editTextStreet.getText()).trim(),
                        String.valueOf(binding.editTextBuildings.getText()).trim(),
                        Integer.parseInt(String.valueOf(binding.editTextEntrance.getText()))
                );
                ObjectUnit objectUnit = new ObjectUnit(
                        unit,
//                        1,
                        binding.spinnerTypeOfObjectUnit.getSelectedItem().toString().trim(),
                        binding.spinnertypeOfObject.getSelectedItem().toString().trim(),
                        Integer.parseInt(binding.spinnerCountNumberOfFloorsObject.getSelectedItem().toString()),
                        Boolean.parseBoolean(binding.spinnerParkingAvailability.getSelectedItem().toString()),
                        false
                );

                if (settings.contains("OBJECT_UNIT_LIST") && !settings.getString("OBJECT_UNIT_LIST", "").isEmpty()) {
                    listLoadedFromShPrefs = loadArrayList("OBJECT_UNIT_LIST");
                }

                listLoadedFromShPrefs.add(objectUnit);
                saveArrayList("OBJECT_UNIT_LIST", listLoadedFromShPrefs);

                Toast.makeText(getContext(), "Объект добавлен!", Toast.LENGTH_LONG).show();
                NavController navController = Navigation.findNavController(binding.getRoot());
                navController.navigate(R.id.action_inputObjectsFragment_to_nav_home);
            }
        });
    }

    private void saveArrayList(String name, ArrayList<ObjectUnit> list) {
        SharedPreferences.Editor editor = settings.edit();
        String json = gson.toJson(list);
        editor.putString(name, json).apply();
    }

    private ArrayList<ObjectUnit> loadArrayList(String name) {
        String json = settings.getString(name, "");
        if (json.isEmpty()) return new ArrayList<>();
        Type type = new TypeToken<ArrayList<ObjectUnit>>(){}.getType();
        return gson.fromJson(json, type);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}