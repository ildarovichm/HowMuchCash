package ru.ildarovichm.howmuchcash.ui.home;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.LinearLayout;
import android.widget.TextView;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import ru.ildarovichm.howmuchcash.R;

public class HomeFragment extends Fragment {

    private CheckBox checkBoxSelectAll;
    private LinearLayout objectsContainer;
    private TextView textViewCounter;
    private Button buttonNext;
    private int totalObjects = 5;
    private int checkedCount = 0;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home, container, false);

        // Инициализация UI
        checkBoxSelectAll = view.findViewById(R.id.checkBoxSelectAll);
//        objectsContainer = view.findViewById(R.id.objectsContainer);
//        textViewCounter = view.findViewById(R.id.textViewCounter);
//        buttonNext = view.findViewById(R.id.buttonNext);

        // Создаём чекбоксы для объектов
        setupObjectCheckboxes();

        // Настраиваем мастер-чекбокс
        setupSelectAllListener();

        // Обновляем счётчик
        updateCounter();

        // Кнопка "Далее"
        buttonNext.setOnClickListener(v -> {
            NavController navController = Navigation.findNavController(requireView());
            navController.navigate(R.id.action_nav_home_to_inputObjectsFragment);
        });

        return view;
    }

    private void setupObjectCheckboxes() {
        objectsContainer.removeAllViews(); // Очистка на случай пересоздания

        for (int i = 0; i < totalObjects; i++) {
            CheckBox cb = new CheckBox(requireContext());
            cb.setText("Объект " + (i + 1));
            cb.setTag(i);

            cb.setOnCheckedChangeListener(getChildListener());
            objectsContainer.addView(cb);
        }
    }

    private CompoundButton.OnCheckedChangeListener getChildListener() {
        return (buttonView, isChecked) -> {
            checkedCount = isChecked ? checkedCount + 1 : checkedCount - 1;
            updateCounter();
            updateSelectAllCheckbox();
        };
    }

    private void setupSelectAllListener() {
        checkBoxSelectAll.setOnCheckedChangeListener((buttonView, isChecked) -> {
            checkedCount = 0;
            for (int i = 0; i < objectsContainer.getChildCount(); i++) {
                CheckBox cb = (CheckBox) objectsContainer.getChildAt(i);
                cb.setOnCheckedChangeListener(null); // Временно отключаем
                cb.setChecked(isChecked);
                cb.setOnCheckedChangeListener(getChildListener());
                if (isChecked) checkedCount++;
            }
            updateCounter();
        });
    }

    private void updateCounter() {
        textViewCounter.setText("ТО: " + checkedCount + " из " + totalObjects);
    }

    private void updateSelectAllCheckbox() {
        if (checkedCount == totalObjects) {
            checkBoxSelectAll.setOnCheckedChangeListener(null);
            checkBoxSelectAll.setChecked(true);
            checkBoxSelectAll.setOnCheckedChangeListener((button, isChecked) -> {
                checkedCount = 0;
                for (int i = 0; i < objectsContainer.getChildCount(); i++) {
                    CheckBox cb = (CheckBox) objectsContainer.getChildAt(i);
                    cb.setOnCheckedChangeListener(null);
                    cb.setChecked(isChecked);
                    cb.setOnCheckedChangeListener(getChildListener());
                    if (isChecked) checkedCount++;
                }
                updateCounter();
            });
        } else {
            checkBoxSelectAll.setOnCheckedChangeListener(null);
            checkBoxSelectAll.setChecked(false);
            checkBoxSelectAll.setOnCheckedChangeListener((button, isChecked) -> {
                checkedCount = 0;
                for (int i = 0; i < objectsContainer.getChildCount(); i++) {
                    CheckBox cb = (CheckBox) objectsContainer.getChildAt(i);
                    cb.setOnCheckedChangeListener(null);
                    cb.setChecked(isChecked);
                    cb.setOnCheckedChangeListener(getChildListener());
                    if (isChecked) checkedCount++;
                }
                updateCounter();
            });
        }
    }
}