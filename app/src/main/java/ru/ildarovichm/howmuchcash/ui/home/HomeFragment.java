package ru.ildarovichm.howmuchcash.ui.home;

import static android.content.Context.MODE_PRIVATE;

import android.app.AlertDialog;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

import ru.ildarovichm.howmuchcash.ObjectGroup;
import ru.ildarovichm.howmuchcash.ObjectUnit;
import ru.ildarovichm.howmuchcash.R;
import ru.ildarovichm.howmuchcash.databinding.FragmentHomeBinding;

public class HomeFragment extends Fragment
        implements ExpandableObjectAdapter.OnItemClickListener,
        ExpandableObjectAdapter.OnDataChangeListener {
    private FragmentHomeBinding binding;
    SharedPreferences settings;
    private Gson gson;
    private ExpandableObjectAdapter adapter;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentHomeBinding.inflate(inflater, container, false);
        settings = requireContext().getSharedPreferences("ObjectUnitListPrefs", MODE_PRIVATE);
        gson = new Gson();

        setupRecyclerView();
        setupAddButton();

        return binding.getRoot();
    }

    private void setupRecyclerView() {
        binding.recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        adapter = new ExpandableObjectAdapter(this, this); // ← Передаём this как OnDataChangeListener
        binding.recyclerView.setAdapter(adapter);
    }

    private void setupAddButton() {
        binding.buttonAddLift.setOnClickListener(v -> {
            NavController navController = Navigation.findNavController(requireView());
            navController.navigate(R.id.action_nav_home_to_inputObjectsFragment);
        });
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        settings = getActivity().getSharedPreferences("ObjectUnitListPrefs", MODE_PRIVATE);
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
    }

    @Override
    public void onResume() {
        super.onResume();
        loadAndRefresh();

    }

    private void loadAndRefresh() {
        ArrayList<ObjectUnit> list = loadArrayList();
        adapter.setData(list, ObjectGroup.GroupLevel.CITY_STREET_BUILDING); // ← Изменено: группировка по дому
        updateStats(list);
    }

    private void updateStats(ArrayList<ObjectUnit> list) {
        TextView textTotalObjects = getView().findViewById(R.id.text_total_objects);
        TextView textTotalAddresses = getView().findViewById(R.id.text_total_addresses);
        TextView textTotalLifts = getView().findViewById(R.id.text_total_lifts);
        TextView textTotalElevators = getView().findViewById(R.id.text_total_elevators);
        TextView textTotalTo = getView().findViewById(R.id.text_total_to);

        int totalObjects = list.size();

        Set<String> addresses = new HashSet<>();
        int lifts = 0;
        int elevators = 0;
        int toCount = 0;

        for (ObjectUnit obj : list) {
            String address = obj.getUnit().getCity() + ", " + obj.getUnit().getStreet();
            addresses.add(address);

            if ("Лифт".equals(obj.getTypeOfObjectUnit())) {
                lifts++;
            } else if ("Подъемник".equals(obj.getTypeOfObjectUnit())) {
                elevators++;
            }

            if (obj.getTOCheckBoxState()) toCount++;
        }

        textTotalObjects.setText("Объектов: " + totalObjects);
        textTotalAddresses.setText("Адресов: " + addresses.size());
        textTotalLifts.setText("Лифтов: " + lifts);
        textTotalElevators.setText("Подъёмников: " + elevators);
        textTotalTo.setText("ТО: " + toCount);
    }

    @Override
    public void onDataChanged() {
        saveArrayList("OBJECT_UNIT_LIST", getAllObjectsFromGroups());
    }

    private ArrayList<ObjectUnit> getAllObjectsFromGroups() {
        ArrayList<ObjectUnit> all = new ArrayList<>();
        for (ObjectGroup group : ((ExpandableObjectAdapter) binding.recyclerView.getAdapter()).getGroups().values()) {
            all.addAll(group.getObjects());
        }
        return all;
    }

    private void saveArrayList(String name, ArrayList<ObjectUnit> list) {
        SharedPreferences.Editor editor = settings.edit();
        String json = gson.toJson(list);
        editor.putString(name, json).apply();
    }

    private ArrayList<ObjectUnit> loadArrayList() {
        String json = settings.getString("OBJECT_UNIT_LIST", "");
        if (json.isEmpty()) return new ArrayList<>();
        Type type = new TypeToken<ArrayList<ObjectUnit>>(){}.getType();
        return gson.fromJson(json, type);
    }

    @Override
    public void onObjectClick(ObjectUnit object) {
        // Открываем диалог редактирования
        showEditDialog(object);
    }

    private void saveData(ArrayList<ObjectUnit> list) {
        SharedPreferences.Editor editor = settings.edit();
        String json = gson.toJson(list);
        editor.putString("OBJECT_UNIT_LIST", json).apply();
    }

    private void showEditDialog(ObjectUnit object) {
        AlertDialog.Builder builder = new AlertDialog.Builder(requireContext());
        View dialogView = getLayoutInflater().inflate(R.layout.dialog_edit_object, null);

        // Привязка элементов интерфейса
        TextView textAddress = dialogView.findViewById(R.id.textAddress);
        Spinner spinnerTypeUnit = dialogView.findViewById(R.id.spinnerTypeUnit);
        Spinner spinnerTypeObject = dialogView.findViewById(R.id.spinnerTypeObject);
        EditText editFloors = dialogView.findViewById(R.id.editFloors);
        Spinner spinnerParking = dialogView.findViewById(R.id.spinnerParking);
        Button btnDelete = dialogView.findViewById(R.id.btnDelete);
        Button btnSave = dialogView.findViewById(R.id.btnSave);

        // Установка адреса
        textAddress.setText(object.getUnit().toString());

        // Настройка spinnerTypeUnit
        ArrayAdapter<CharSequence> adapterTypeUnit = ArrayAdapter.createFromResource(
                requireContext(),
                R.array.typeOfObjectUnitArray,
                android.R.layout.simple_spinner_item
        );
        adapterTypeUnit.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerTypeUnit.setAdapter(adapterTypeUnit);

        // Установка выбранного значения для типа юнита
        int position = getPositionInAdapter(adapterTypeUnit, object.getTypeOfObjectUnit());
        spinnerTypeUnit.setSelection(position);

        // Динамическое обновление spinnerTypeObject при изменении spinnerTypeUnit
        setupTypeObjectSpinner(spinnerTypeUnit, spinnerTypeObject, object.getTypeOfObject());

        // Установка этажности
        editFloors.setText(String.valueOf(object.getCountNumberOfFloorsOfObject()));

        // Настройка spinnerParking
        ArrayAdapter<CharSequence> adapterParking = ArrayAdapter.createFromResource(
                requireContext(),
                R.array.parking_display_options,
                android.R.layout.simple_spinner_item
        );
        adapterParking.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerParking.setAdapter(adapterParking);

        // Установка выбранного значения для парковки
        String parkingText = object.getParkingAvailability() ? "Есть" : "Нет";
        int parkingPosition = getPositionInAdapter(adapterParking, parkingText);
        spinnerParking.setSelection(parkingPosition);

        // Создание и настройка диалога
        AlertDialog dialog = builder.setView(dialogView).create();
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

        // Обработка кнопки "Сохранить"
        btnSave.setOnClickListener(v -> {
            // Сохранение изменений
            object.setTypeOfObjectUnit(spinnerTypeUnit.getSelectedItem().toString());
            object.setTypeOfObject(spinnerTypeObject.getSelectedItem().toString());

            try {
                int floors = Integer.parseInt(editFloors.getText().toString().trim());
                if (floors < 0) floors = 0;
                object.setCountNumberOfFloorsOfObject(floors);
            } catch (NumberFormatException e) {
                Toast.makeText(requireContext(), "Некорректное значение этажей", Toast.LENGTH_SHORT).show();
                return;
            }

            object.setParkingAvailability("Есть".equals(spinnerParking.getSelectedItem().toString()));

            // Сохранение в SharedPreferences
            saveArrayList("OBJECT_UNIT_LIST", getAllObjectsFromGroups());
            adapter.notifyDataSetChanged();

            dialog.dismiss();
            Toast.makeText(requireContext(), "Объект сохранён", Toast.LENGTH_SHORT).show();
        });

        // Обработка кнопки "Удалить"
        btnDelete.setOnClickListener(v -> {
            new AlertDialog.Builder(requireContext())
                    .setTitle("Удалить объект")
                    .setMessage("Вы уверены, что хотите удалить этот объект?")
                    .setPositiveButton("Да", (d, w) -> {
                        ArrayList<ObjectUnit> list = getAllObjectsFromGroups();
                        list.remove(object);
                        saveArrayList("OBJECT_UNIT_LIST", list);
                        loadAndRefresh();
                        dialog.dismiss();
                        Toast.makeText(requireContext(), "Объект удалён", Toast.LENGTH_SHORT).show();
                    })
                    .setNegativeButton("Отмена", null)
                    .show();
        });

        dialog.show();
    }

    // Вспомогательный метод для поиска позиции в адаптере
    private int getPositionInAdapter(ArrayAdapter<CharSequence> adapter, String value) {
        for (int i = 0; i < adapter.getCount(); i++) {
            if (adapter.getItem(i).toString().equals(value)) {
                return i;
            }
        }
        return 0;
    }

    // Метод для динамического обновления spinnerTypeObject
    private void setupTypeObjectSpinner(Spinner spinnerTypeUnit, Spinner spinnerTypeObject, String currentTypeObject) {
        spinnerTypeUnit.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String selectedUnit = parent.getItemAtPosition(position).toString();
                updateTypeObjectOptions(spinnerTypeObject, selectedUnit, currentTypeObject);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                updateTypeObjectOptions(spinnerTypeObject, "Лифт", currentTypeObject);
            }
        });

        // Инициализация
        updateTypeObjectOptions(spinnerTypeObject, spinnerTypeUnit.getSelectedItem().toString(), currentTypeObject);
    }

    private void updateTypeObjectOptions(Spinner spinner, String unitType, String currentTypeObject) {
        int arrayResId = "Подъемник".equals(unitType)
                ? R.array.typeOfObjectArrayIfElevator
                : R.array.typeOfObjectArrayIfLift;

        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(
                requireContext(),
                arrayResId,
                android.R.layout.simple_spinner_item
        );
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);

        // Восстанавливаем предыдущее значение, если оно есть в новом списке
        int position = getPositionInAdapter(adapter, currentTypeObject);
        spinner.setSelection(position);
    }

    @Override
    public void onStart() {
        super.onStart();

    }
    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}