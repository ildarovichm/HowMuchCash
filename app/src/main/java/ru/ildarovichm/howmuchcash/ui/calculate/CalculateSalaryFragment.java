package ru.ildarovichm.howmuchcash.ui.calculate;

import static android.content.Context.MODE_PRIVATE;

import static androidx.navigation.Navigation.findNavController;

import androidx.lifecycle.ViewModelProvider;

import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CalendarView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.time.LocalDate;
import java.util.ArrayList;

import ru.ildarovichm.howmuchcash.CalendarHandler;
import ru.ildarovichm.howmuchcash.ObjectUnit;
import ru.ildarovichm.howmuchcash.Price;
import ru.ildarovichm.howmuchcash.R;
import ru.ildarovichm.howmuchcash.SalaryCalculation;
import ru.ildarovichm.howmuchcash.Unit;
import ru.ildarovichm.howmuchcash.databinding.FragmentCalculateSalaryBinding;

public class CalculateSalaryFragment extends Fragment {

    private FragmentCalculateSalaryBinding binding;
    SharedPreferences settings;
    private ArrayList<ObjectUnit> listLoadedFromShPrefs;
    private SalaryCalculation salaryCalculation;
    private ArrayList<ArrayList<String>> allDatesList;
    private ArrayList<LocalDate> weekendWatchList;
    private ArrayList<LocalDate> nightWatchList;
    private CalendarHandler calendarHandler;
    Price price;
    private int countWorkShiftInMonth = 1;
    private int countActualDaysWorked = 1;
    private int priceForUnitLift;
    private int priceForUnitElevator;
    private int extraChargeForSkyscraper;
    private int priceForNightWatch;
    private int priceForWeekendWatch;
    private int ratePublicHoliday;
    private int rateOrdinaryDay;
    private int MONTH_;
    private int YEAR_;
    private int month_str;
    private int resSalaryAmountObjectUnitLift;
    private int resSalaryAmountObjectUnitElevator;
    private int resSalaryAmountObjectUnit;
    private int resSalaryAmountWatch;
    private int resSalaryAmountAll;
    private boolean hasVisited;
    private static final Gson gson = new Gson();

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        binding = FragmentCalculateSalaryBinding.inflate(inflater, container, false);
        View root = binding.getRoot();
        return root;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        settings = getActivity().getSharedPreferences("SalaryCalculatingPrefs", MODE_PRIVATE);
        hasVisited = settings.getBoolean("hasVisited", false);
        if (!hasVisited) {
            SharedPreferences.Editor e = settings.edit();
            e.putBoolean("hasVisited", true);
            e.commit(); //подтвердить изменения
        }
    }

    private ArrayList<Integer> loadIntegerArrayList(String name) {
        String json = settings.getString(name, "");
        if (json.isEmpty()) {
            return new ArrayList<>();
        }
        Type type = new TypeToken<ArrayList<Integer>>(){}.getType();
        return gson.fromJson(json, type);
    }

    private ArrayList<ObjectUnit> loadArrayList(String name) {
        String json = settings.getString(name, "");
        if (json.isEmpty()) return new ArrayList<>();
        Type type = new TypeToken<ArrayList<ObjectUnit>>(){}.getType();
        return gson.fromJson(json, type);
    }

    @Override
    public void onStart() {
        super.onStart();
    }

    @Override
    public void onResume() {
        super.onResume();
        SharedPreferences.Editor prefSalCalc = settings.edit();
        listLoadedFromShPrefs = new ArrayList<>();
        allDatesList = new ArrayList<>();
        calendarHandler = new CalendarHandler(allDatesList);
        calendarHandler.clearList();
        binding.editTextNumberCountWorkShiftInMonth.setText("");
        binding.editTextNumberCountActualDaysWorked.setText("");
        binding.calendarView.setOnDateChangeListener((view, year, month, dayOfMonth) -> {

            MONTH_ = (month + 1);
            YEAR_ = year;
            month_str = (month + 1);

            calendarHandler.addDate(String.valueOf(dayOfMonth), String.valueOf(MONTH_), String.valueOf(year));
            binding.textViewWatchDateList.setText(String.valueOf(calendarHandler.getAllWatchDateList()));
        });

        binding.buttonApplyNCalculate.setOnClickListener(v -> {

            String workShiftStr = binding.editTextNumberCountWorkShiftInMonth.getText().toString().trim();
            String actualDaysStr = binding.editTextNumberCountActualDaysWorked.getText().toString().trim();
            boolean calendarHandlerIsEmpty = calendarHandler.getAllWatchDateList().toString().isEmpty();
//            Toast.makeText(getContext(), calendarHandler.getAllWatchDateList().toString(), Toast.LENGTH_LONG).show();
            if (workShiftStr.isEmpty() || actualDaysStr.isEmpty()) {
                Toast.makeText(getContext(), "Заполните оба поля!", Toast.LENGTH_LONG).show();
            } else if (!calendarHandlerIsEmpty) {
                binding.textViewWatchDateList.setText(String.valueOf(calendarHandler.getAllWatchDateList()));
                countWorkShiftInMonth = Integer.parseInt(String.valueOf(binding.editTextNumberCountWorkShiftInMonth.getText()));
                prefSalCalc.putString("COUNT_WORK_SHIFT_IN_MONTH", String.valueOf(binding.editTextNumberCountWorkShiftInMonth));
                countActualDaysWorked = Integer.parseInt(String.valueOf(binding.editTextNumberCountActualDaysWorked.getText()));
                prefSalCalc.putString("COUNT_ACTUAL_DAYS_WORKED", String.valueOf(binding.editTextNumberCountActualDaysWorked));
                prefSalCalc.apply();

                if (calendarHandler.getAllWatchDateList().isEmpty()) {
                    Toast.makeText(getContext(), "Список пустой!", Toast.LENGTH_LONG).show();
                } else {
                    weekendWatchList = calendarHandler.getWeekendWatchList(YEAR_, month_str);
                    nightWatchList = calendarHandler.getNightWatchList(YEAR_, month_str);

                    //БЛОК ЗАГРУЗКИ ДАННЫХ ДЛЯ РАССЧЕТОВ ИЗ SHARED PREFERENCE
                    settings = getActivity().getSharedPreferences("ObjectUnitListPrefs", MODE_PRIVATE);
                    if (settings.contains("OBJECT_UNIT_LIST") && !settings.getString("OBJECT_UNIT_LIST", "").isEmpty()) {
                        listLoadedFromShPrefs = loadArrayList("OBJECT_UNIT_LIST");
                    } else {
                        Toast.makeText(getContext(), "Список объектов пуст. Загрузите данные из файла.", Toast.LENGTH_LONG).show();
                        NavController navController = findNavController(binding.getRoot());
                        navController.navigate(R.id.action_calculateSalaryFragment_to_readWriteObjectFragment);
                        return;
                    }
//                    settings = getActivity().getSharedPreferences("PricePrefs", MODE_PRIVATE);
//
//                    ArrayList<Integer> loadedPrice = loadIntegerArrayList("PRICE_LIST");
//                    if (loadedPrice.isEmpty()) {
//                        Toast.makeText(getContext(), "Не заданы тарифы! Укажите цены в разделе 'Цены'", Toast.LENGTH_LONG).show();
//                        NavController navController = findNavController(binding.getRoot());
//                        navController.navigate(R.id.action_calculateSalaryFragment_to_nav_gallery); // Переход к PricesInputFragment
//                        return;
//                    }
//                    priceForUnitLift = settings.getInt("PREF_PRICE_FOR_UNIT_LIFT", 0);
//                    priceForUnitElevator = settings.getInt("PREF_PRICE_FOR_UNIT_ELEVATOR", 0);
//                    extraChargeForSkyscraper = settings.getInt("PREF_EXTRA_CHARGE_FOR_SKYSCRAPER", 0);
//                    priceForNightWatch = settings.getInt("PREF_PRICE_FOR_NIGHT_WATCH", 0);
//                    priceForWeekendWatch = settings.getInt("PREF_PRICE_FOR_WEEKEND_WATCH", 0);
//                    ratePublicHoliday = settings.getInt("PREF_RATE_PUBLIC_HOLIDAY", 0);
//                    rateOrdinaryDay = settings.getInt("PREF_RATE_ORDINARY_DAY", 0);

                    settings = requireActivity().getSharedPreferences("PricePrefs", MODE_PRIVATE);

                    ArrayList<Integer> loadedPrice = loadIntegerArrayList("PRICE_LIST");
                    if (loadedPrice.isEmpty()) {
                        Toast.makeText(getContext(), "Не заданы тарифы! Укажите цены в разделе 'Настройки -> Тарифы'", Toast.LENGTH_LONG).show();
                        NavController navController = findNavController(binding.getRoot());
                        navController.navigate(R.id.action_calculateSalaryFragment_to_nav_gallery);
                        return;
                    }

                    // Извлекаем цены из списка
                    priceForUnitLift = loadedPrice.get(0);
                    priceForUnitElevator = loadedPrice.get(1);
                    extraChargeForSkyscraper = loadedPrice.get(2);
                    priceForNightWatch = loadedPrice.get(3);
                    priceForWeekendWatch = loadedPrice.get(4);
                    ratePublicHoliday = 2;
                    rateOrdinaryDay = 1;
                    price = new Price(
                            priceForUnitLift,
                            priceForUnitElevator,
                            extraChargeForSkyscraper,
                            priceForNightWatch,
                            priceForWeekendWatch,
                            ratePublicHoliday,
                            rateOrdinaryDay);
                    salaryCalculation = new SalaryCalculation(nightWatchList, weekendWatchList, price, listLoadedFromShPrefs);

                    resSalaryAmountObjectUnitLift = salaryCalculation.getSalaryAmountObjectUnitLift();
                    resSalaryAmountObjectUnitElevator = salaryCalculation.getSalaryAmountObjectUnitElevator();
                    resSalaryAmountObjectUnit = salaryCalculation.getSalaryAmountObjectUnit();
                    resSalaryAmountWatch = salaryCalculation.getSalaryAmountWatch();
                    resSalaryAmountAll = salaryCalculation.getSalaryAmountAll(countWorkShiftInMonth, countActualDaysWorked);

                }
                settings = getActivity().getSharedPreferences("CalculatingResult", MODE_PRIVATE);
                SharedPreferences.Editor resultCalcPrefs = settings.edit();
                resultCalcPrefs.putString("SALARY_AMOUNT_OBJECT_UNIT_LIFT", String.valueOf(resSalaryAmountObjectUnitLift));
                resultCalcPrefs.putString("SALARY_AMOUNT_OBJECT_UNIT_ELEVATOR", String.valueOf(resSalaryAmountObjectUnitElevator));
                resultCalcPrefs.putString("SALARY_AMOUNT_OBJECT_UNIT", String.valueOf(resSalaryAmountObjectUnit));
                resultCalcPrefs.putString("SALARY_AMOUNT_WATCH", String.valueOf(resSalaryAmountWatch));
                resultCalcPrefs.putString("SALARY_AMOUNT_ALL", String.valueOf(resSalaryAmountAll));
                resultCalcPrefs.apply();
                binding.textViewSalaryLift.setVisibility(View.VISIBLE);
                binding.textViewSalaryLift2.setVisibility(View.VISIBLE);
                binding.textViewSalaryLift2.setText(String.valueOf(resSalaryAmountObjectUnitLift));
                binding.textViewSalaryElevator.setVisibility(View.VISIBLE);
                binding.textViewSalaryElevator2.setVisibility(View.VISIBLE);
                binding.textViewSalaryElevator2.setText(String.valueOf(resSalaryAmountObjectUnitElevator));
                binding.textViewSalaryAmountObjects.setVisibility(View.VISIBLE);
                binding.textViewSalaryAmountObjects2.setVisibility(View.VISIBLE);
                binding.textViewSalaryAmountObjects2.setText(String.valueOf(resSalaryAmountObjectUnit));
                binding.textViewSalaryWatch.setVisibility(View.VISIBLE);
                binding.textViewSalaryWatch2.setVisibility(View.VISIBLE);
                binding.textViewSalaryWatch2.setText(String.valueOf(resSalaryAmountWatch));
                binding.textViewSalaryAmountAll.setVisibility(View.VISIBLE);
                binding.textViewSalaryAmountAll2.setVisibility(View.VISIBLE);
                binding.textViewSalaryAmountAll2.setText(String.valueOf(resSalaryAmountAll));

                NavController navController = findNavController(binding.getRoot());
                navController.navigate(R.id.action_calculateSalaryFragment_to_nav_slideshow);
            } else {
                Toast.makeText(getContext(), "Невозможно выполнить. Данные не введены!", Toast.LENGTH_LONG).show();
            }
        });
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}