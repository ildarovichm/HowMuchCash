package ru.ildarovichm.howmuchcash.ui.home;

import static android.content.Context.MODE_PRIVATE;
import static androidx.navigation.Navigation.findNavController;

import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.os.Bundle;
import android.text.method.ScrollingMovementMethod;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;

import ru.ildarovichm.howmuchcash.ObjectUnit;
import ru.ildarovichm.howmuchcash.R;
import ru.ildarovichm.howmuchcash.Unit;
import ru.ildarovichm.howmuchcash.databinding.FragmentHomeBinding;

public class HomeFragment extends Fragment {
    private FragmentHomeBinding binding;
    SharedPreferences settings;
    ArrayList<ObjectUnit> objectUnitList;
    private ArrayList<ObjectUnit> listLoadedFromShPrefs = new ArrayList<>();
    private ArrayList<Boolean> checkedForDeletion = new ArrayList<>();
    private Gson gson;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        settings = getActivity().getSharedPreferences("ObjectUnitListPrefs", MODE_PRIVATE);
        binding = FragmentHomeBinding.inflate(inflater, container, false);
        binding.buttonAddLift.setOnClickListener(v -> {
            NavController navController = findNavController(binding.getRoot());
            navController.navigate(R.id.action_nav_home_to_inputObjectsFragment);
        });
        gson = new Gson();
        binding.btnDelete.setOnClickListener(v -> {
            if (listLoadedFromShPrefs.size() != checkedForDeletion.size()) return;

            ArrayList<ObjectUnit> newList = new ArrayList<>();
            for (int i = 0; i < listLoadedFromShPrefs.size(); i++) {
                if (!checkedForDeletion.get(i)) {
                    newList.add(listLoadedFromShPrefs.get(i));
                }
            }

            saveArrayList("OBJECT_UNIT_LIST", newList);
            drawTable(); // перерисовываем таблицу
            Toast.makeText(getContext(), "Отмеченные объекты удалены", Toast.LENGTH_SHORT).show();
        });
        return binding.getRoot();
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
        //Очищаем таблицу кроме заголовка
        binding.table1.removeViews(1, Math.max(0, binding.table1.getChildCount() - 1));

        if (settings.contains("OBJECT_UNIT_LIST") && !settings.getString("OBJECT_UNIT_LIST", "").isEmpty()) {
            listLoadedFromShPrefs = loadArrayList();
            drawTable();
            TableLayout tableLayout1 = binding.table1;
            tableLayout1.setStretchAllColumns(true);
            tableLayout1.bringToFront();
            binding.checkBoxTO.setOnCheckedChangeListener((buttonView, isChecked) -> {
                if (isChecked) {
                    for (ObjectUnit object : listLoadedFromShPrefs) {
                        object.setTOCheckBoxState(true);
                    }
                    saveArrayList("OBJECT_UNIT_LIST", listLoadedFromShPrefs);
                    drawTable();
                }
                else {
                    for (ObjectUnit object : listLoadedFromShPrefs) {
                        object.setTOCheckBoxState(false);
                    }
                    saveArrayList("OBJECT_UNIT_LIST", listLoadedFromShPrefs);
                    drawTable();
                }
            });
        }

    }

    private void saveArrayList(String name, ArrayList<ObjectUnit> list) {
        SharedPreferences.Editor editor = settings.edit();
        String json = gson.toJson(list);
        editor.putString(name, json).apply();
    }

    private ArrayList<ObjectUnit> loadArrayList() {
        String json = settings.getString("OBJECT_UNIT_LIST", "");
        Type type = new TypeToken<ArrayList<ObjectUnit>>(){}.getType();
        objectUnitList = gson.fromJson(json, type);
        return objectUnitList;
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

    public void drawTable() {
        binding.table1.removeViews(1, Math.max(0, binding.table1.getChildCount() - 1));
        listLoadedFromShPrefs = loadArrayList();



        checkedForDeletion.clear();
        for (int i = 0; i < listLoadedFromShPrefs.size(); i++) {
            checkedForDeletion.add(false);
        }

        TableLayout tableLayout1 = binding.table1;
        tableLayout1.setStretchAllColumns(true);
        tableLayout1.bringToFront();

        for (int i = 0; i < listLoadedFromShPrefs.size(); i++) {
            ObjectUnit object = listLoadedFromShPrefs.get(i);

            TableRow tr = new TableRow(getContext());

            TextView tv1 = new TextView(getContext());
            tv1.setVerticalScrollBarEnabled(true);
            tv1.setMovementMethod(new ScrollingMovementMethod());
            tv1.setText(object.getUnit().toString());

            TextView tv2 = new TextView(getContext());
            tv2.setTextAlignment(TextView.TEXT_ALIGNMENT_CENTER);
            tv2.setText(String.valueOf(object.getCountNumberOfFloorsOfObject()));
            tv2.setTextColor(Color.LTGRAY);

            CheckBox cb3 = new CheckBox(getContext());
            cb3.setChecked(object.getTOCheckBoxState());

            cb3.setOnCheckedChangeListener((buttonView, isChecked) -> {
                object.setTOCheckBoxState(isChecked);
                saveArrayList("OBJECT_UNIT_LIST", listLoadedFromShPrefs);
            });

            CheckBox cb4 = new CheckBox(getContext());
            cb4.setChecked(checkedForDeletion.get(i)); // всегда false при перерисовке
            int finalI = i;
            cb4.setOnCheckedChangeListener((buttonView, isChecked) -> {
                checkedForDeletion.set(finalI, isChecked); // обновляем временный список
            });

            tr.addView(tv1);
            tr.addView(tv2);
            tr.addView(cb3);
            tr.addView(cb4);
            tableLayout1.addView(tr);
        }
    }
}