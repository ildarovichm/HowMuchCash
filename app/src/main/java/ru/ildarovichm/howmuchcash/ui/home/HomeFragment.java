package ru.ildarovichm.howmuchcash.ui.home;

import static android.content.Context.MODE_PRIVATE;
import static androidx.navigation.Navigation.findNavController;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Environment;
import android.text.method.ScrollingMovementMethod;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.JavascriptInterface;
import android.webkit.WebSettings;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.LinearLayout;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;

import ru.ildarovichm.howmuchcash.DataHandler;
import ru.ildarovichm.howmuchcash.ObjectUnit;
import ru.ildarovichm.howmuchcash.R;
import ru.ildarovichm.howmuchcash.Unit;
import ru.ildarovichm.howmuchcash.databinding.FragmentHomeBinding;

public class HomeFragment extends Fragment {

    private FragmentHomeBinding binding;
    SharedPreferences settings;
    private TableLayout tableLayout;
    private String city;
    private String street;
    private String buildings;
    private int entrance;
    private boolean toCheckBoxState;

    private String CHEKBOX_STATUS = "UNCHECKED";

    private int c;
    static Unit unit;
    static ObjectUnit objectUnit;

    static ArrayList<ObjectUnit> objectUnitArrayList = new ArrayList<>();
    private ArrayList<ObjectUnit> listLoadedFromShPrefs = new ArrayList<>();
    private ArrayList<ArrayList<ObjectUnit>> data = new ArrayList<>();

    private String typeOfObjectUnit;
    private String typeOfObject;
    private boolean parkingAvailability;
    private int countNumberOfFloorsOfObject;
//    DatabaseHelper sqlHelper;
    SQLiteDatabase db;
    Cursor userCursor;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        HomeViewModel homeViewModel =
                new ViewModelProvider(this).get(HomeViewModel.class);
        settings = getActivity().getSharedPreferences("ObjectUnitListPrefs", MODE_PRIVATE);
        binding = FragmentHomeBinding.inflate(inflater, container, false);
        binding.buttonAddLift.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                NavController navController = findNavController(binding.getRoot());
                navController.navigate(R.id.action_nav_home_to_inputObjectsFragment);
            }
        });

        return binding.getRoot();
    }


    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
//        Toast.makeText(getContext(), "onViewCreate HomeFragment", Toast.LENGTH_LONG).show();
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
//        Toast.makeText(getContext(), "onSaveInstanceState HomeFragment", Toast.LENGTH_LONG).show();
    }

    @Override
    public void onResume() {
        super.onResume();
        settings = getActivity().getSharedPreferences("ObjectUnitListPrefs", MODE_PRIVATE);

        //Очищаем таблицу кроме заголовка
        binding.table1.removeViews(1, Math.max(0, binding.table1.getChildCount() - 1));


        if (settings.contains("OBJECT_UNIT_LIST") && !settings.getString("OBJECT_UNIT_LIST", "").isEmpty()) {
            listLoadedFromShPrefs = loadArrayList("OBJECT_UNIT_LIST");
            TableLayout tableLayout1 = binding.table1;
            tableLayout1.setStretchAllColumns(true);
            tableLayout1.bringToFront();

            for (int i = 0; i < listLoadedFromShPrefs.size(); i++) {
                TableRow tr =  new TableRow(getContext());
                TextView tv1 = new TextView(getContext());
                tv1.setVerticalScrollBarEnabled(true);
                tv1.setMovementMethod(new ScrollingMovementMethod());
                tv1.setText(listLoadedFromShPrefs.get(i).getUnit().toString());
                TextView tv2 = new TextView(getContext());
                tv2.setTextAlignment(TextView.TEXT_ALIGNMENT_CENTER);
//                String countNumberOfFloors = String.valueOf(listLoadedFromShPrefs.get(i).getCountNumberOfFloorsOfObject());
                tv2.setText(String.valueOf(listLoadedFromShPrefs.get(i).getCountNumberOfFloorsOfObject()));
                tv2.setTextColor(Color.LTGRAY);
                CheckBox cb3 = new CheckBox(getContext());
                if (listLoadedFromShPrefs.get(i).getTOCheckBoxState()) {
                    cb3.setChecked(true);
                }
                CheckBox cb4 = new CheckBox(getContext());

                saveArrayList("OBJECT_UNIT_LIST", listLoadedFromShPrefs);

                tr.addView(tv1);
                tr.addView(tv2);
                tr.addView(cb3);
                tr.addView(cb4);

                tableLayout1.addView(tr);
            }

            binding.checkBoxTO.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                    if (isChecked) {
                        Toast.makeText(getContext(), "isChecked1 True", Toast.LENGTH_LONG).show();
                        binding.table1.removeViews(1, Math.max(0, binding.table1.getChildCount() - 1));
                        for (int i = 0; i < listLoadedFromShPrefs.size(); i++) {
                            TableRow tr =  new TableRow(getContext());
                            TextView tv1 = new TextView(getContext());
                            tv1.setHorizontalScrollBarEnabled(true);
                            tv1.setMovementMethod(new ScrollingMovementMethod());
                            tv1.setText(listLoadedFromShPrefs.get(i).getUnit().toString());
                            TextView tv2 = new TextView(getContext());
                            String countNumberOfFloors = String.valueOf(listLoadedFromShPrefs.get(i).getCountNumberOfFloorsOfObject());
                            tv2.setTextAlignment(TextView.TEXT_ALIGNMENT_CENTER);
                            tv2.setText(countNumberOfFloors);
                            CheckBox cb3 = new CheckBox(getContext());
                            cb3.setChecked(true);
                            listLoadedFromShPrefs.get(i).setTOCheckBoxState(true);
                            Toast.makeText(getContext(), String.valueOf(listLoadedFromShPrefs.get(i).getTOCheckBoxState()), Toast.LENGTH_LONG).show();
                            ;
                            CheckBox cb4 = new CheckBox(getContext());

                            saveArrayList("OBJECT_UNIT_LIST", listLoadedFromShPrefs);
                            tr.addView(tv1);
                            tr.addView(tv2);
                            tr.addView(cb3);
                            tr.addView(cb4);

                            tableLayout1.addView(tr);
                        }
                    }
                    else {
                        Toast.makeText(getContext(), "isChecked1 False", Toast.LENGTH_LONG).show();
                        binding.table1.removeViews(1, Math.max(0, binding.table1.getChildCount() - 1));
                        for (int i = 0; i < listLoadedFromShPrefs.size(); i++) {
                            TableRow tr = new TableRow(getContext());
                            TextView tv1 = new TextView(getContext());
                            tv1.setWidth(180);
                            tv1.setHeight(60);
                            tv1.setHorizontalScrollBarEnabled(true);
                            tv1.setMovementMethod(new ScrollingMovementMethod());
                            tv1.setText(listLoadedFromShPrefs.get(i).getUnit().toString());
                            TextView tv2 = new TextView(getContext());
                            tv2.setWidth(180);
                            tv2.setHeight(60);
                            String countNumberOfFloors = String.valueOf(listLoadedFromShPrefs.get(i).getCountNumberOfFloorsOfObject());
                            tv2.setTextAlignment(TextView.TEXT_ALIGNMENT_CENTER);
                            tv2.setText(countNumberOfFloors);
                            CheckBox cb3 = new CheckBox(getContext());
                            cb3.setWidth(65);
                            cb3.setHeight(60);
                            cb3.setChecked(false);
                            listLoadedFromShPrefs.get(i).setTOCheckBoxState(false);
                            CheckBox cb4 = new CheckBox(getContext());

                            saveArrayList("OBJECT_UNIT_LIST", listLoadedFromShPrefs);

                            tr.addView(tv1);
                            tr.addView(tv2);
                            tr.addView(cb3);
                            tr.addView(cb4);

                            tableLayout1.addView(tr);
                        }
                    }
                }
            });

            binding.checkBoxDel.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                    if (isChecked) {
                        Toast.makeText(getContext(), "isChecked2 True", Toast.LENGTH_LONG).show();
                        binding.table1.removeViews(1, Math.max(0, binding.table1.getChildCount() - 1));
                        for (int i = 0; i < listLoadedFromShPrefs.size(); i++) {
                            TableRow tr =  new TableRow(getContext());
                            TextView tv1 = new TextView(getContext());
                            tv1.setHorizontalScrollBarEnabled(true);
                            tv1.setMovementMethod(new ScrollingMovementMethod());
                            tv1.setText(listLoadedFromShPrefs.get(i).getUnit().toString());
                            TextView tv2 = new TextView(getContext());
                            String countNumberOfFloors = String.valueOf(listLoadedFromShPrefs.get(i).getCountNumberOfFloorsOfObject());
                            tv2.setTextAlignment(TextView.TEXT_ALIGNMENT_CENTER);
                            tv2.setText(countNumberOfFloors);
                            CheckBox cb3 = new CheckBox(getContext());
                            cb3.setChecked(false);
                            CheckBox cb4 = new CheckBox(getContext());
                            cb4.setChecked(true);

                            saveArrayList("OBJECT_UNIT_LIST", listLoadedFromShPrefs);

                            tr.addView(tv1);
                            tr.addView(tv2);
                            tr.addView(cb3);
                            tr.addView(cb4);

                            tableLayout1.addView(tr);
                        }
                    }
                    else {
                        Toast.makeText(getContext(), "isChecked2 False", Toast.LENGTH_LONG).show();
                        binding.table1.removeViews(1, Math.max(0, binding.table1.getChildCount() - 1));
                        for (int i = 0; i < listLoadedFromShPrefs.size(); i++) {
                            TableRow tr = new TableRow(getContext());
                            TextView tv1 = new TextView(getContext());
                            tv1.setWidth(180);
                            tv1.setHeight(60);
                            tv1.setHorizontalScrollBarEnabled(true);
                            tv1.setMovementMethod(new ScrollingMovementMethod());
                            tv1.setText(listLoadedFromShPrefs.get(i).getUnit().toString());
                            TextView tv2 = new TextView(getContext());
                            tv2.setWidth(180);
                            tv2.setHeight(60);
                            String countNumberOfFloors = String.valueOf(listLoadedFromShPrefs.get(i).getCountNumberOfFloorsOfObject());
                            tv2.setTextAlignment(TextView.TEXT_ALIGNMENT_CENTER);
                            tv2.setText(countNumberOfFloors);
                            CheckBox cb3 = new CheckBox(getContext());
                            cb3.setWidth(65);
                            cb3.setHeight(60);
                            cb3.setChecked(false);
                            CheckBox cb4 = new CheckBox(getContext());
                            cb4.setChecked(false);

                            saveArrayList("OBJECT_UNIT_LIST", listLoadedFromShPrefs);

                            tr.addView(tv1);
                            tr.addView(tv2);
                            tr.addView(cb3);
                            tr.addView(cb4);

                            tableLayout1.addView(tr);
                        }
                    }
                }
            });
        }

    }

    private void saveArrayList(String name, ArrayList<ObjectUnit> list) {
        settings = getActivity().getSharedPreferences("ObjectUnitListPrefs", MODE_PRIVATE);
        SharedPreferences.Editor editor = settings.edit();
        StringBuilder sb = new StringBuilder();
        for (ObjectUnit s : list) sb.append(s).append("<s>");
        sb.delete(sb.length() - 3, sb.length());
        editor.putString(name, sb.toString()).apply();
    }

    private ArrayList<ObjectUnit> loadArrayList(String name) {
        settings = getActivity().getSharedPreferences("ObjectUnitListPrefs", MODE_PRIVATE);
        String[] strings = settings.getString(name, "").split("<s>");
        ArrayList<ObjectUnit> list = new ArrayList<>();
        for (int i = 0; i < strings.length; i++) {
            String[] subStr = strings[i].split(";");
            String[] unitSubStr = subStr[0].split(",");
            String city = unitSubStr[0].trim();
            String street = unitSubStr[1].trim();
            String buildings = unitSubStr[2].trim();
            int entrance = Integer.parseInt(unitSubStr[3].trim());
            Unit unitLoad = new Unit(
                    city,
                    street,
                    buildings,
                    entrance
            );

            int countOfObjectUnit = Integer.parseInt(subStr[1].trim());
            String typeOfObjectUnit = subStr[2].trim();
            String typeOfObject = subStr[3].trim();
            int countNumberOfFloors = Integer.parseInt(subStr[4].trim());
            boolean parkingAvailability = Boolean.parseBoolean(subStr[5].trim());
            boolean toCheckBoxState = Boolean.parseBoolean(subStr[6].trim());
            boolean delCheckBoxState = Boolean.parseBoolean(subStr[7].trim());

            ObjectUnit objectUnitLoad = new ObjectUnit(
                    unitLoad,
//                    countOfObjectUnit,
                    typeOfObjectUnit,
                    typeOfObject,
                    countNumberOfFloors,
                    parkingAvailability,
                    toCheckBoxState,
                    delCheckBoxState
            );
            list.add(objectUnitLoad);
        }
        return list;
    }

    @Override
    public void onStart() {
        super.onStart();
        Toast.makeText(getContext(), "onStart HomeFragment", Toast.LENGTH_LONG).show();

    }
    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}