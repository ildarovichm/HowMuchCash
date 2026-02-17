package ru.ildarovichm.howmuchcash.ui.home;

import static android.content.Context.MODE_PRIVATE;
import static androidx.navigation.Navigation.findNavController;

import android.app.AlertDialog;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.text.method.ScrollingMovementMethod;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
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
import ru.ildarovichm.howmuchcash.Unit;
import ru.ildarovichm.howmuchcash.databinding.FragmentHomeBinding;

public class HomeFragment extends Fragment
        implements ExpandableObjectAdapter.OnItemClickListener,
        ExpandableObjectAdapter.OnDataChangeListener {
    private FragmentHomeBinding binding;
    SharedPreferences settings;
    ArrayList<ObjectUnit> objectUnitList;
    private ArrayList<ObjectUnit> listLoadedFromShPrefs = new ArrayList<>();
    private ArrayList<Boolean> checkedForDeletion = new ArrayList<>();
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
        adapter.setData(list, ObjectGroup.GroupLevel.CITY_STREET);
        updateStats(list);

    }

    private void updateStats(ArrayList<ObjectUnit> list) {
        TextView textTotalObjects = getView().findViewById(R.id.text_total_objects);
        TextView textTotalAddresses = getView().findViewById(R.id.text_total_addresses);
        TextView textTotalTo = getView().findViewById(R.id.text_total_to);

        int totalObjects = list.size();

        Set<String> addresses = new HashSet<>();
        for (ObjectUnit obj : list) {
            String address = obj.getUnit().getCity() + ", " + obj.getUnit().getStreet();
            addresses.add(address);
        }
        int totalAddresses = addresses.size();

        int totalTO = 0;
        for (ObjectUnit obj : list) {
            if (obj.getTOCheckBoxState()) totalTO++;
        }

        textTotalObjects.setText("Объектов: " + totalObjects);
        textTotalAddresses.setText("Адресов: " + totalAddresses);
        textTotalTo.setText("ТО: " + totalTO);
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
        AlertDialog.Builder builder = new AlertDialog.Builder(requireContext(), R.style.CustomDialogTheme);
        View dialogView = getLayoutInflater().inflate(R.layout.dialog_edit_object, null);

        // Привязка полей
        EditText editFloors = dialogView.findViewById(R.id.editFloors);
        Spinner spinnerParking = dialogView.findViewById(R.id.spinnerParking);
        CheckBox checkBoxTO = dialogView.findViewById(R.id.checkBoxTO);
        Button btnSave = dialogView.findViewById(R.id.btnSave);
        Button btnDelete = dialogView.findViewById(R.id.btnDelete);

        // Установка адреса
        TextView textAddress = dialogView.findViewById(R.id.textAddress);
        textAddress.setText(object.getUnit().toString());

        // === Настройка spinnerTypeUnit ===
        ArrayAdapter<CharSequence> adapterTypeUnit = ArrayAdapter.createFromResource(
                requireContext(),
                R.array.typeOfObjectUnitArray,
                android.R.layout.simple_spinner_item
        );
        adapterTypeUnit.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        Spinner spinnerTypeUnit = dialogView.findViewById(R.id.spinnerTypeUnit);
        spinnerTypeUnit.setAdapter(adapterTypeUnit);

        // Установка текущего значения
        int position = adapterTypeUnit.getPosition(object.getTypeOfObjectUnit());
        if (position == -1) position = 0; // если не найдено — первый элемент
        spinnerTypeUnit.setSelection(position);

        // === Настройка spinnerTypeObject ===
        ArrayAdapter<CharSequence> adapterTypeObject = ArrayAdapter.createFromResource(
                requireContext(),
                R.array.typeOfObjectArrayIfAll,
                android.R.layout.simple_spinner_item
        );
        adapterTypeObject.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        Spinner spinnerTypeObject = dialogView.findViewById(R.id.spinnerTypeObject);
        spinnerTypeObject.setAdapter(adapterTypeObject);

        position = adapterTypeObject.getPosition(object.getTypeOfObject());
        if (position == -1) position = 0;
        spinnerTypeObject.setSelection(position);

        // Остальные поля
        editFloors.setText(String.valueOf(object.getCountNumberOfFloorsOfObject()));
        checkBoxTO.setChecked(object.getTOCheckBoxState());
        spinnerParking.setSelection(object.getParkingAvailability() ? 0 : 1);

        // Создание и настройка диалога
        AlertDialog dialog = builder.setView(dialogView).create();
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

        // Кнопка "Сохранить"
        btnSave.setOnClickListener(v -> {
            object.setTypeOfObjectUnit(spinnerTypeUnit.getSelectedItem().toString());
            object.setTypeOfObject(spinnerTypeObject.getSelectedItem().toString());
            object.setCountNumberOfFloorsOfObject(Integer.parseInt(editFloors.getText().toString()));
            object.setParkingAvailability(spinnerParking.getSelectedItemPosition() == 0);
            object.setTOCheckBoxState(checkBoxTO.isChecked());

            saveArrayList("OBJECT_UNIT_LIST", getAllObjectsFromGroups());
            adapter.notifyDataSetChanged();
            dialog.dismiss();
            Toast.makeText(requireContext(), "Сохранено", Toast.LENGTH_SHORT).show();
        });

        // Кнопка "Удалить"
        btnDelete.setOnClickListener(v -> {
            ArrayList<ObjectUnit> list = getAllObjectsFromGroups();
            list.remove(object);
            saveArrayList("OBJECT_UNIT_LIST", list);
            loadAndRefresh();
            dialog.dismiss();
            Toast.makeText(requireContext(), "Объект удалён", Toast.LENGTH_SHORT).show();
        });

        dialog.show();
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