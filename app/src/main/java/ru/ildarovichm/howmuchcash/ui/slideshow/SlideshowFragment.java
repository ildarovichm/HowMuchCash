package ru.ildarovichm.howmuchcash.ui.slideshow;

import static android.content.Context.MODE_PRIVATE;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import java.util.ArrayList;
import java.util.Objects;

import ru.ildarovichm.howmuchcash.Price;
import ru.ildarovichm.howmuchcash.R;
import ru.ildarovichm.howmuchcash.databinding.FragmentSlideshowBinding;

public class SlideshowFragment extends Fragment {

    private FragmentSlideshowBinding binding;
    private SharedPreferences settings;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentSlideshowBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        settings = requireContext().getSharedPreferences("CalculatingResult", MODE_PRIVATE);
    }

    @Override
    public void onResume() {
        super.onResume();
        loadAndDisplayResults();
    }

    private void loadAndDisplayResults() {
        // Загрузка данных
        String resAmountLift = settings.getString("SALARY_AMOUNT_OBJECT_UNIT_LIFT", "0");
        String resAmountElevator = settings.getString("SALARY_AMOUNT_OBJECT_UNIT_ELEVATOR", "0");
        String resAmountObjectUnit = settings.getString("SALARY_AMOUNT_OBJECT_UNIT", "0");
        String resAmountWatch = settings.getString("SALARY_AMOUNT_WATCH", "0");
        String resAmountAll = settings.getString("SALARY_AMOUNT_ALL", "0");

        // Основные итоги
        binding.textViewSalaryLiftTR.setText(formatCurrency(resAmountLift));
        binding.textViewSalaryElevatorTR.setText(formatCurrency(resAmountElevator));
        binding.textViewSalaryAmountObjectsTR.setText(formatCurrency(resAmountObjectUnit));
        binding.textViewSalaryWatchTR.setText(formatCurrency(resAmountWatch));
        binding.textViewSalaryAmountAllTR.setText(formatCurrency(resAmountAll));

        // Цвет иконки в зависимости от суммы
        int color = Double.parseDouble(resAmountAll) > 0
                ? ContextCompat.getColor(requireContext(), R.color.colorPrimary)
                : ContextCompat.getColor(requireContext(), R.color.colorError);

        binding.iconSummary.setColorFilter(color);
    }

    private String formatCurrency(String amount) {
        try {
            double value = Double.parseDouble(amount);
            return String.format("%.2f ₽", value);
        } catch (NumberFormatException e) {
            return "0.00 ₽";
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}