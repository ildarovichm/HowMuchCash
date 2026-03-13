package ru.ildarovichm.howmuchcash;

import static android.content.Context.MODE_PRIVATE;

import android.app.AlertDialog;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.InputType;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Objects;

public class ObjectUnitListFragment extends Fragment {

    private ObjectUnitAdapter adapter;
    private ArrayList<ObjectUnit> objectUnitList;
    private Gson gson;
    SharedPreferences settings;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        settings = requireActivity().getSharedPreferences("ObjectUnitListPrefs", MODE_PRIVATE);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_object_unit_list, container, false);
        // Инициализация RecyclerView
        RecyclerView recyclerView = view.findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        gson = new GsonBuilder()
                .setPrettyPrinting()  // Для читаемого форматирования JSON
                .create();

        // Заполняем список реальными данными
        loadRealData();

        // Создаем и устанавливаем адаптер
        adapter = new ObjectUnitAdapter(objectUnitList);

        // Обработка кликов
        adapter.setOnItemClickListener(objectUnit -> {
            Toast.makeText(getContext(), "Выбрана карточка: " +
                    objectUnit.getUnit().toString(), Toast.LENGTH_SHORT).show();
        });

        adapter.setOnItemActionListener(new ObjectUnitAdapter.OnItemActionListener() {
            @Override
            public void onEditClick(ObjectUnit objectUnit, int position) {
                // Редактирование объекта
                showEditDialog(objectUnit, position);
            }

            @Override
            public void onDeleteClick(ObjectUnit objectUnit, int position) {
                // Удаление с подтверждением
                showDeleteConfirmation(objectUnit, position);
            }

            @Override
            public void onDetailsClick(ObjectUnit objectUnit, int position) {
                // Просмотр деталей
                showDetailsDialog(objectUnit);
            }
        });

        recyclerView.setAdapter(adapter);

        return view;
    }

    private void showEditDialog(ObjectUnit objectUnit, int position) {
        // Пример диалога редактирования
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        builder.setTitle("Редактирование");

        // Создаем layout для ввода
        LinearLayout layout = new LinearLayout(getContext());
        layout.setOrientation(LinearLayout.VERTICAL);
        layout.setPadding(50, 20, 50, 20);

        final EditText countInput = new EditText(getContext());
        countInput.setHint("Новое количество");
        countInput.setInputType(InputType.TYPE_CLASS_NUMBER);
        countInput.setText(String.valueOf(objectUnit.getCountOfObjectsOnUnit()));
        layout.addView(countInput);

        builder.setView(layout);

        builder.setPositiveButton("Сохранить", (dialog, which) -> {
            int newCount = Integer.parseInt(countInput.getText().toString());
            objectUnit.setCountOfObjectsOnUnit(newCount);
            adapter.updateItem(position, objectUnit);
            saveArrayList(adapter.getObjectUnitList()); // ← добавьте
            Toast.makeText(getContext(), "Обновлено!", Toast.LENGTH_SHORT).show();
        });

        builder.setNegativeButton("Отмена", null);
        builder.show();
    }

    private void showDeleteConfirmation(ObjectUnit objectUnit, int position) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        builder.setTitle("Подтверждение");
        builder.setMessage("Удалить объект по адресу: " + objectUnit.getUnit().toString() + "?");

        builder.setPositiveButton("Удалить", (dialog, which) -> {
            // Удаляем из адаптера (и из внутреннего списка)
            adapter.removeItem(position);

            // Сохраняем обновлённый список в SharedPreferences
            saveArrayList(adapter.getObjectUnitList());

            Toast.makeText(getContext(), "Объект удалён", Toast.LENGTH_SHORT).show();
        });

        builder.setNegativeButton("Отмена", null);
        builder.show();
    }

    private void showDetailsDialog(ObjectUnit objectUnit) {
        String details = "Адрес: " + objectUnit.getUnit().toString() + "\n\n" +
                "Тип: " + objectUnit.getTypeOfObjectUnit() + "\n" +
                "Количество объектов: " + objectUnit.getCountOfObjectsOnUnit() + "\n" +
                "Вид: " + objectUnit.getTypeOfObject() + "\n" +
                "Этажность: " + objectUnit.getCountNumberOfFloorsOfObject() + "\n" +
                "Парковка: " + (objectUnit.getParkingAvailability() ? "Есть" : "Нет") + "\n" +
                "TO: " + (objectUnit.getTOCheckBoxState() ? "Да" : "Нет");

        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        builder.setTitle("Детальная информация");
        builder.setMessage(details);
        builder.setPositiveButton("OK", null);
        builder.show();
    }

    private void loadRealData() {
        objectUnitList = loadArrayList();
    }

    // Метод для обновления данных извне
    public void updateObjectUnitData(ArrayList<ObjectUnit> newObjects) {
        if (adapter != null) {
            adapter.updateList(newObjects);
        }
    }

    private void saveArrayList(ArrayList<ObjectUnit> list) {
        SharedPreferences.Editor editor = settings.edit();
        String json = gson.toJson(list);
        editor.putString("OBJECT_UNIT_LIST", json).apply();
    }

    private ArrayList<ObjectUnit> loadArrayList() {
        settings = requireActivity().getSharedPreferences("ObjectUnitListPrefs", MODE_PRIVATE);
        String json = settings.getString("OBJECT_UNIT_LIST", "");
        Type type = new TypeToken<ArrayList<ObjectUnit>>(){}.getType();
        objectUnitList = gson.fromJson(json, type);
        return objectUnitList;
    }
}
