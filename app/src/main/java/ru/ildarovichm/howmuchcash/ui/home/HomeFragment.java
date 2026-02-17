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
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;

import ru.ildarovichm.howmuchcash.ObjectGroup;
import ru.ildarovichm.howmuchcash.ObjectUnit;
import ru.ildarovichm.howmuchcash.R;
import ru.ildarovichm.howmuchcash.Unit;
import ru.ildarovichm.howmuchcash.databinding.FragmentHomeBinding;

public class HomeFragment extends Fragment implements ExpandableObjectAdapter.OnItemClickListener {
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
        adapter = new ExpandableObjectAdapter(this);
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
        // Здесь можно использовать BottomSheetDialog или отдельный фрагмент
        Toast.makeText(getContext(), "Редактирование: " + object.getUnit(), Toast.LENGTH_SHORT).show();
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