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

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CalendarView;
import android.widget.Toast;

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
    ArrayList<Integer> loadedPrice;
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

    public static CalculateSalaryFragment newInstance() {
        return new CalculateSalaryFragment();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        Bundle bundle = new Bundle();
//        settings = getActivity().getSharedPreferences("SalaryCalculatingPrefs", MODE_PRIVATE);
        CalculateSalaryViewModel mViewModel = new ViewModelProvider(this).get(CalculateSalaryViewModel.class);
        binding = FragmentCalculateSalaryBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        return root;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        settings = getActivity().getSharedPreferences("SalaryCalculatingPrefs", MODE_PRIVATE);
//        Toast.makeText(getContext(), "onResume HomeFragment", Toast.LENGTH_LONG).show();
        boolean hasVisited = settings.getBoolean("hasVisited", false);

        if (!hasVisited) {
            SharedPreferences.Editor e = settings.edit();
            e.putBoolean("hasVisited", true);
            e.commit(); //подтвердить изменения
        } else if (hasVisited) {
//            listLoadedFromShPrefs = loadArrayList("OBJECT_UNIT_LIST");
        }
    }

    private void saveArrayList(String name, ArrayList<ArrayList<String>> list) {
        settings = getActivity().getSharedPreferences("SalaryCalculatingPrefs", MODE_PRIVATE);
        SharedPreferences.Editor editor = settings.edit();
        StringBuilder sb = new StringBuilder();
        for (ArrayList<String> dateList : list) {
            for (String s : dateList) {
                sb.append(s).append("<s>");
            }
        }
        sb.delete(sb.length() - 3, sb.length());
        editor.putString(name, sb.toString()).apply();
    }

    private ArrayList<Integer> loadIntegerArrayList(String name) {
        settings = getActivity().getSharedPreferences("PricePrefs", MODE_PRIVATE);
        String[] strings = settings.getString(name, "").split("<s>");
        ArrayList<Integer> list = new ArrayList<>();
        for (String s : strings) {
            list.add(Integer.parseInt(s));
        }
        return list;
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

//            int countOfObjectUnit = Integer.parseInt(subStr[1].trim());
            String typeOfObjectUnit = subStr[2].trim();
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
//    private ArrayList<String> loadArrayList(String name) {
//        settings = getActivity().getSharedPreferences("SalaryCalculatingPrefs", MODE_PRIVATE);
//        String[] strings = settings.getString(name, "").split("<s>");
//        ArrayList<String> list = new ArrayList<>();
//        list.addAll(Arrays.asList(strings));
//        return list;
//    }


    @Override
    public void onStart() {
        super.onStart();

    }

    @Override
    public void onResume() {
        super.onResume();
        settings = getActivity().getSharedPreferences("SalaryCalculatingPrefs", MODE_PRIVATE);
        SharedPreferences.Editor prefSalCalc = settings.edit();
        Toast.makeText(getContext(), "onResumeCalcFrag", Toast.LENGTH_LONG).show();
        listLoadedFromShPrefs = new ArrayList<>();
        allDatesList = new ArrayList<>();
        calendarHandler = new CalendarHandler(allDatesList);
        calendarHandler.clearList();
        binding.editTextNumberCountWorkShiftInMonth.setText("");
        binding.editTextNumberCountActualDaysWorked.setText("");
        binding.calendarView.setOnDateChangeListener(new CalendarView.OnDateChangeListener() {

            @Override
            public void onSelectedDayChange(CalendarView view, int year, int month,
                                            int dayOfMonth) {

                MONTH_ = (month + 1);
                YEAR_ = year;
                month_str = (month + 1);

                calendarHandler.addDate(String.valueOf(dayOfMonth), String.valueOf(MONTH_), String.valueOf(year));
                binding.textViewWatchDateList.setText(String.valueOf(calendarHandler.getAllWatchDateList()));
            }
        });



        binding.buttonApplyNCalculate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                boolean field1IsEmpty = binding.editTextNumberCountWorkShiftInMonth.getText().toString().trim().isEmpty();
                boolean field2IsEmpty = binding.editTextNumberCountActualDaysWorked.getText().toString().trim().isEmpty();
                boolean calendarHandlerIsEmpty = calendarHandler.getAllWatchDateList().toString().isEmpty();
                Toast.makeText(getContext(), calendarHandler.getAllWatchDateList().toString(), Toast.LENGTH_LONG).show();
//                if (!field1IsEmpty || !field2IsEmpty) {
//                    Toast.makeText(getContext(), "!field1IsEmpty || !field2IsEmpty is false!", Toast.LENGTH_LONG).show();
//                } else
                    if (!calendarHandlerIsEmpty) {
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
                        }
                        settings = getActivity().getSharedPreferences("PricePrefs", MODE_PRIVATE);

                        loadedPrice = loadIntegerArrayList("PRICE_LIST");
                        priceForUnitLift = settings.getInt("PREF_PRICE_FOR_UNIT_LIFT", 0);
                        priceForUnitElevator = settings.getInt("PREF_PRICE_FOR_UNIT_ELEVATOR", 0);
                        extraChargeForSkyscraper = settings.getInt("PREF_EXTRA_CHARGE_FOR_SKYSCRAPER", 0);
                        priceForNightWatch = settings.getInt("PREF_PRICE_FOR_NIGHT_WATCH", 0);
                        priceForWeekendWatch = settings.getInt("PREF_PRICE_FOR_WEEKEND_WATCH", 0);
                        ratePublicHoliday = settings.getInt("PREF_RATE_PUBLIC_HOLIDAY", 0);
                        rateOrdinaryDay = settings.getInt("PREF_RATE_ORDINARY_DAY", 0);
                        price = new Price(
                                priceForUnitLift,
                                priceForUnitElevator,
                                extraChargeForSkyscraper,
                                priceForNightWatch,
                                priceForWeekendWatch,
                                ratePublicHoliday,
                                rateOrdinaryDay);
                        salaryCalculation = new SalaryCalculation(nightWatchList, weekendWatchList, price, listLoadedFromShPrefs);

                        int resCountObjectUnitLift = salaryCalculation.getCountObjectUnitLift();
                        int resCountObjectUnitElevator = salaryCalculation.getCountObjectUnitElevator();
                        int resCountAmountLiftSkyscraper = salaryCalculation.getCountAmountLiftSkyscraper();
                        int resCountAmountObjectUnit = salaryCalculation.getCountAmountObjectUnit();

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
            }
        });
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}