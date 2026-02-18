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
        binding = FragmentInputObjectsBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    private String capitalize(String str) {
        return str.substring(0, 1).toUpperCase() + str.substring(1);
    }

    @Override
    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        settings = requireContext().getSharedPreferences("ObjectUnitListPrefs", MODE_PRIVATE);

        setupTypeObjectSpinner();
        setupParkingSpinner();

        binding.buttonAddObject.setOnClickListener(v -> {
            if (!validateFields()) {
                return;
            }

            Unit unit = new Unit(
                    capitalize(binding.editTextCity.getText().toString().trim().toLowerCase()),
                    capitalize(binding.editTextStreet.getText().toString().trim().toLowerCase()),
                    capitalize(binding.editTextBuildings.getText().toString().trim().toLowerCase()),
                    Integer.parseInt(binding.editTextEntrance.getText().toString().trim())
            );

            ObjectUnit objectUnit = new ObjectUnit(
                    unit,
                    binding.spinnerTypeOfObjectUnit.getSelectedItem().toString().trim(),
                    binding.spinnertypeOfObject.getSelectedItem().toString().trim(),
                    binding.spinnerCountNumberOfFloorsObject.getSelectedItemPosition(),
                    binding.spinnerParkingAvailability.getSelectedItemPosition() == 0,
                    false
            );

            saveObjectUnit(objectUnit);
            showToast("Объект добавлен!");
            navigateToHome();
        });
    }

    private void saveObjectUnit(ObjectUnit objectUnit) {
        listLoadedFromShPrefs = loadArrayList("OBJECT_UNIT_LIST");
        listLoadedFromShPrefs.add(objectUnit);
        saveArrayList("OBJECT_UNIT_LIST", listLoadedFromShPrefs);
    }

    private void showToast(String message) {
        Toast.makeText(requireContext(), message, Toast.LENGTH_SHORT).show();
    }

    private void navigateToHome() {
        NavController navController = Navigation.findNavController(requireView());
        navController.navigate(R.id.action_inputObjectsFragment_to_nav_home);
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

    private void setupTypeObjectSpinner() {
        updateTypeObjectOptions("Лифт");
        // Адаптер для spinnerTypeOfObjectUnit (Лифт / Подъемник)
        ArrayAdapter<CharSequence> adapterUnit = ArrayAdapter.createFromResource(
                requireContext(),
                R.array.typeOfObjectUnitArray,
                android.R.layout.simple_spinner_item
        );
        adapterUnit.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        binding.spinnerTypeOfObjectUnit.setAdapter(adapterUnit);

        // Слушатель изменения выбора в spinnerTypeOfObjectUnit
        binding.spinnerTypeOfObjectUnit.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String selected = parent.getItemAtPosition(position).toString();
                updateTypeObjectOptions(selected);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                updateTypeObjectOptions("Лифт");
            }
        });

        // Инициализация spinnertypeOfObject
        updateTypeObjectOptions("Лифт");
    }

    private void updateTypeObjectOptions(String unitType) {
        int arrayResId;
        if ("Подъемник".equals(unitType)) {
            arrayResId = R.array.typeOfObjectArrayIfElevator;
        } else {
            arrayResId = R.array.typeOfObjectArrayIfLift;
        }

        // Удаляем старый адаптер
        binding.spinnertypeOfObject.setAdapter(null);

        // Создаём новый
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(
                requireContext(),
                arrayResId,
                android.R.layout.simple_spinner_item
        );
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        binding.spinnertypeOfObject.setAdapter(adapter);
    }

    private void setupParkingSpinner() {
        // Адаптер с отображаемыми значениями
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(
                requireContext(),
                R.array.parking_display_options,
                android.R.layout.simple_spinner_item
        );
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        binding.spinnerParkingAvailability.setAdapter(adapter);

        // Установка слушателя для преобразования значений
        binding.spinnerParkingAvailability.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                // Сохраняем позицию — она будет использоваться при создании объекта
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });
    }

    private boolean validateFields() {
        if (binding.editTextCity.getText().toString().trim().isEmpty()) {
            binding.editTextCity.setError("Поле обязательно");
            return false;
        }
        if (binding.editTextStreet.getText().toString().trim().isEmpty()) {
            binding.editTextStreet.setError("Поле обязательно");
            return false;
        }
        if (binding.editTextBuildings.getText().toString().trim().isEmpty()) {
            binding.editTextBuildings.setError("Поле обязательно");
            return false;
        }
        if (binding.editTextEntrance.getText().toString().trim().isEmpty()) {
            binding.editTextEntrance.setError("Поле обязательно");
            return false;
        }
        return true;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}