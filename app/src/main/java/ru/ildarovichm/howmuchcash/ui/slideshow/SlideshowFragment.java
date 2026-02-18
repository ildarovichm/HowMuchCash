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

        // Добавление детализации (расширенные расчёты)
//        addCalculationDetails(resAmountLift, resAmountElevator, resAmountWatch);
    }

    private String formatCurrency(String amount) {
        try {
            double value = Double.parseDouble(amount);
            return String.format("%.2f ₽", value);
        } catch (NumberFormatException e) {
            return "0.00 ₽";
        }
    }

    private void addCalculationDetails(String liftAmount, String elevatorAmount, String watchAmount) {
        StringBuilder details = new StringBuilder();

        details.append("🧾 Подробный расчёт:\n\n");

        // Загружаем промежуточные значения из SharedPreferences
        SharedPreferences prefs = requireContext().getSharedPreferences("CalculatingResult", MODE_PRIVATE);

        // Лифты
        if (Double.parseDouble(liftAmount) > 0) {
            double baseLift = getDoublePref(prefs, "SALARY_AMOUNT_OBJECT_UNIT_LIFT_BASE", 0);
            double bonusFloors = getDoublePref(prefs, "SALARY_BONUS_FLOORS", 0);
            double bonusParking = getDoublePref(prefs, "SALARY_BONUS_PARKING", 0);
            double bonusTO = getDoublePref(prefs, "SALARY_BONUS_TO", 0);

            details.append("• Лифты: ").append(formatCurrency(liftAmount)).append("\n");
            details.append("  └─ База: ").append(formatCurrency(String.valueOf(baseLift))).append("\n");
            if (bonusFloors > 0) details.append("  └─ + Этажи: ").append(formatCurrency(String.valueOf(bonusFloors))).append("\n");
            if (bonusParking > 0) details.append("  └─ + Парковка: ").append(formatCurrency(String.valueOf(bonusParking))).append("\n");
            if (bonusTO > 0) details.append("  └─ + ТО: ").append(formatCurrency(String.valueOf(bonusTO))).append("\n");
            details.append("\n");
        }

        // Подъёмники
        if (Double.parseDouble(elevatorAmount) > 0) {
            double baseElevator = getDoublePref(prefs, "SALARY_AMOUNT_OBJECT_UNIT_ELEVATOR_BASE", 0);
            double bonusElevator = getDoublePref(prefs, "SALARY_BONUS_ELEVATOR", 0);

            details.append("• Подъёмники: ").append(formatCurrency(elevatorAmount)).append("\n");
            details.append("  └─ База: ").append(formatCurrency(String.valueOf(baseElevator))).append("\n");
            if (bonusElevator > 0) details.append("  └─ + Коэффициент: ").append(formatCurrency(String.valueOf(bonusElevator))).append("\n");
            details.append("\n");
        }

        // Дежурства
        if (Double.parseDouble(watchAmount) > 0) {
            int countWatch = prefs.getInt("COUNT_OF_WATCH", 0);
            double rateWatch = getDoublePref(prefs, "RATE_OF_WATCH", 0);

            details.append("• Дежурства: ").append(formatCurrency(watchAmount)).append("\n");
            details.append("  └─ Объектов: ").append(countWatch).append(" × Ставка: ").append(formatCurrency(String.valueOf(rateWatch))).append("\n");
            details.append("\n");
        }

        details.append("✅ Итого: ").append(binding.textViewSalaryAmountAllTR.getText());

        binding.textCalculationDetails.setText(details.toString());
        binding.textCalculationDetails.setVisibility(View.VISIBLE);
    }

    private double getDoublePref(SharedPreferences prefs, String key, double defValue) {
        return Double.parseDouble(prefs.getString(key, String.valueOf(defValue)));
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}