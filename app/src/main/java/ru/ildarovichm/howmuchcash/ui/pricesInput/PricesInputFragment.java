package ru.ildarovichm.howmuchcash.ui.pricesInput;

import static android.content.Context.MODE_PRIVATE;
import static androidx.navigation.Navigation.findNavController;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;

import ru.ildarovichm.howmuchcash.ObjectUnit;
import ru.ildarovichm.howmuchcash.Price;
import ru.ildarovichm.howmuchcash.R;
import ru.ildarovichm.howmuchcash.databinding.FragmentPricesinputBinding;
import ru.ildarovichm.howmuchcash.CalendarHandler;

import android.content.SharedPreferences;
import android.widget.CalendarView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class PricesInputFragment extends Fragment {

    SharedPreferences settings;
//    ArrayList<String> allDatesList;

//    private CalendarHandler calendarHandler = new CalendarHandler();

    ArrayList<Integer> loadedPrice;

    private Price price;

    private FragmentPricesinputBinding binding;
    private ArrayList<Integer> listOfPrice;
    private final int ratePublicHoliday = 2;
    private final int rateOrdinaryDay = 1;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        PricesInputViewModel pricesInputViewModel =
                new ViewModelProvider(this).get(PricesInputViewModel.class);
        settings = getActivity().getSharedPreferences("PricePrefs", MODE_PRIVATE);

        binding = FragmentPricesinputBinding.inflate(inflater, container, false);
        View root = binding.getRoot();
        return root;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public void onStart() {
        super.onStart();
        settings = getActivity().getSharedPreferences("PricePrefs", MODE_PRIVATE);
        boolean hasVisited = settings.getBoolean("hasVisited", false);

        if (!hasVisited) {
            SharedPreferences.Editor e = settings.edit();
            e.putBoolean("hasVisited", true);
            e.commit(); //подтвердить изменения
        } else {
            loadedPrice = loadArrayList("PRICE_LIST");
            binding.textViewPriceForUnitLift2.setText(String.valueOf(loadedPrice.get(0)));
            binding.textViewPriceForUnitElevator2.setText(String.valueOf(loadedPrice.get(1)));
            binding.textViewExtraChargeForSkyscraper2.setText(String.valueOf(loadedPrice.get(2)));
            binding.textViewPriceForNightWatch2.setText(String.valueOf(loadedPrice.get(3)));
            binding.textViewPriceForWeekendWatch2.setText(String.valueOf(loadedPrice.get(4)));
        }
        Bundle bundle = new Bundle();
        listOfPrice = new ArrayList<>();
        SharedPreferences.Editor prefPrice = settings.edit();

        binding.buttonWritePrice.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                prefPrice.putInt("PREF_PRICE_FOR_UNIT_LIFT", Integer.parseInt(binding.editTextPriceForUnitLift.getText().toString()));
                bundle.putInt("PRICE_FOR_UNIT_LIFT", Integer.parseInt(binding.editTextPriceForUnitLift.getText().toString()));

                prefPrice.putInt("PREF_PRICE_FOR_UNIT_ELEVATOR", Integer.parseInt(binding.editTextPriceForUnitElevator.getText().toString()));
                bundle.putInt("PRICE_FOR_UNIT_ELEVATOR", Integer.parseInt(binding.editTextPriceForUnitElevator.getText().toString()));

                prefPrice.putInt("PREF_EXTRA_CHARGE_FOR_SKYSCRAPER", Integer.parseInt(binding.editTextExtraChargeSkyscraper.getText().toString()));
                bundle.putInt("EXTRA_CHARGE_FOR_SKYSCRAPER", Integer.parseInt(binding.editTextExtraChargeSkyscraper.getText().toString()));

                prefPrice.putInt("PREF_PRICE_FOR_NIGHT_WATCH", Integer.parseInt(binding.editTextPriceForNightWatch.getText().toString()));
                bundle.putInt("PRICE_FOR_NIGHT_WATCH", Integer.parseInt(binding.editTextPriceForNightWatch.getText().toString()));

                prefPrice.putInt("PREF_PRICE_FOR_WEEKEND_WATCH", Integer.parseInt(binding.editTextPriceForWeekendWatch.getText().toString()));
                bundle.putInt("PRICE_FOR_WEEKEND_WATCH", Integer.parseInt(binding.editTextPriceForWeekendWatch.getText().toString()));

                prefPrice.putInt("PREF_RATE_PUBLIC_HOLIDAY", ratePublicHoliday);
                bundle.putInt("RATE_PUBLIC_HOLIDAY", ratePublicHoliday);

                prefPrice.putInt("PREF_RATE_ORDINARY_DAY", rateOrdinaryDay);
                bundle.putInt("RATE_ORDINARY_DAY", rateOrdinaryDay);

//                for (int i = 0; i < calendarHandler.getAllWatchDateList().size(); i++) {
//                    String name = "WATCHDATE_" + i;
//                    prefPrice.putString(name, calendarHandler.getAllWatchDateList().get(i).toString());
//                }

                prefPrice.apply();

                price = new Price(
                        Integer.parseInt(binding.editTextPriceForUnitLift.getText().toString().trim()),
                        Integer.parseInt(binding.editTextPriceForUnitElevator.getText().toString().trim()),
                        Integer.parseInt(binding.editTextExtraChargeSkyscraper.getText().toString().trim()),
                        Integer.parseInt(binding.editTextPriceForNightWatch.getText().toString().trim()),
                        Integer.parseInt(binding.editTextPriceForWeekendWatch.getText().toString().trim()),
                        ratePublicHoliday,
                        rateOrdinaryDay
                );
                listOfPrice.add(price.getPriceForUnitLift());
                listOfPrice.add(price.getPriceForUnitElevator());
                listOfPrice.add(price.getExtraChargeForSkyscraper());
                listOfPrice.add(price.getPriceForNightWatch());
                listOfPrice.add(price.getPriceForWeekendWatch());
                listOfPrice.add(price.getRatePublicHoliday());
                listOfPrice.add(price.getRateOrdinaryDay());

                saveArrayList("PRICE_LIST" , listOfPrice);

                NavController navController = findNavController(binding.getRoot());
                navController.navigate(R.id.action_nav_gallery_to_nav_home, bundle);
            }
        });
    }

    private void saveArrayList(String name, ArrayList<Integer> list) {
        settings = getActivity().getSharedPreferences("PricePrefs", MODE_PRIVATE);
        SharedPreferences.Editor editor = settings.edit();
        StringBuilder sb = new StringBuilder();
        for (int i : list) sb.append(i).append("<s>");
        sb.delete(sb.length() - 3, sb.length());
        editor.putString(name, sb.toString()).apply();
    }

    private ArrayList<Integer> loadArrayList(String name) {
        settings = getActivity().getSharedPreferences("PricePrefs", MODE_PRIVATE);
        String[] strings = settings.getString(name, "").split("<s>");
        ArrayList<Integer> list = new ArrayList<>();
        for (String s : strings) {
            list.add(Integer.parseInt(s));
        }
        return list;
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle saveInstanceState) {
        super.onSaveInstanceState(saveInstanceState);
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}