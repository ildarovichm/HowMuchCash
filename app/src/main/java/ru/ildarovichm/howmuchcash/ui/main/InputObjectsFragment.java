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

import java.util.ArrayList;

import ru.ildarovichm.howmuchcash.ObjectUnit;
import ru.ildarovichm.howmuchcash.R;
import ru.ildarovichm.howmuchcash.Unit;
import ru.ildarovichm.howmuchcash.databinding.FragmentInputObjectsBinding;

public class InputObjectsFragment extends Fragment {

    private FragmentInputObjectsBinding binding;
    SharedPreferences settings;
    private ArrayList<ObjectUnit> listLoadedFromShPrefs = new ArrayList<>();
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
        settings = getActivity().getSharedPreferences("ObjectUnitListPrefs", MODE_PRIVATE);
//        binding.spinnerTypeOfObjectUnit.setClickable(false);
//        binding.spinnertypeOfObject.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
//            @Override
//            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
//                if (parent.getItemAtPosition(position).toString().equals("Лифт")) {
//                    ArrayAdapter<CharSequence> adapter =
//                            ArrayAdapter.createFromResource(getContext(), R.array.typeOfObjectArrayIfLift, android.R.layout.simple_spinner_item);
//                    adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
//                    binding.spinnerTypeOfObjectUnit.setClickable(true);
//                } else if (parent.getItemAtPosition(position).toString().equals("Подъемник")) {
//                    ArrayAdapter<CharSequence> adapter =
//                            ArrayAdapter.createFromResource(getContext(), R.array.typeOfObjectArrayIfElevator, android.R.layout.simple_spinner_item);
//                    adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
//                    binding.spinnerTypeOfObjectUnit.setClickable(true);
//                }
//            }
//
//            @Override
//            public void onNothingSelected(AdapterView<?> parent) {
//                binding.spinnerTypeOfObjectUnit.setClickable(false);
//            }
//        });

        binding.buttonAddObject.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Unit unit = new Unit(
                        String.valueOf(binding.editTextCity.getText()),
                        String.valueOf(binding.editTextStreet.getText()),
                        String.valueOf(binding.editTextBuildings.getText()),
                        Integer.parseInt(String.valueOf(binding.editTextEntrance.getText()))
                );
                ObjectUnit objectUnit = new ObjectUnit(
                        unit,
//                        1,
                        binding.spinnerTypeOfObjectUnit.getSelectedItem().toString().trim(),
                        binding.spinnertypeOfObject.getSelectedItem().toString().trim(),
                        Integer.parseInt(binding.spinnerCountNumberOfFloorsObject.getSelectedItem().toString()),
                        Boolean.parseBoolean(binding.spinnerParkingAvailability.getSelectedItem().toString()),
                        false,
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
            String typeOfObjectUnit = subStr[2];
            String typeOfObject = subStr[3];
            int countNumberOfFloors = Integer.parseInt(subStr[4].trim());
            boolean parkingAvailability = Boolean.parseBoolean(subStr[5]);
            boolean toCheckBoxState = Boolean.parseBoolean(subStr[6]);
            boolean delCheckBoxState = Boolean.parseBoolean(subStr[7]);

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
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}