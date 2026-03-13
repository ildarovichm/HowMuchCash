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

import ru.ildarovichm.howmuchcash.Price;
import ru.ildarovichm.howmuchcash.R;
import ru.ildarovichm.howmuchcash.databinding.FragmentPricesinputBinding;

import android.content.SharedPreferences;

import java.util.ArrayList;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;

public class PricesInputFragment extends Fragment {

    SharedPreferences settings;

    ArrayList<Integer> loadedPrice;

    private Price price;

    private FragmentPricesinputBinding binding;
    private ArrayList<Integer> listOfPrice;
    private final int ratePublicHoliday = 2;
    private final int rateOrdinaryDay = 1;

    private static final Gson gson = new Gson();

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        PricesInputViewModel pricesInputViewModel =
                new ViewModelProvider(this).get(PricesInputViewModel.class);
        binding = FragmentPricesinputBinding.inflate(inflater, container, false);
        View root = binding.getRoot();
        return root;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        settings = getActivity().getSharedPreferences("PricePrefs", MODE_PRIVATE);
    }

    @Override
    public void onStart() {
        super.onStart();

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
        listOfPrice = new ArrayList<>();

        binding.buttonWritePrice.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
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
                navController.navigate(R.id.action_nav_gallery_to_nav_home);
            }
        });
    }

    private void saveArrayList(String name, ArrayList<Integer> list) {
        SharedPreferences.Editor editor = settings.edit();
        String json = gson.toJson(list);
        editor.putString(name, json).apply();
    }

    private ArrayList<Integer> loadArrayList(String name) {
        String json = settings.getString(name, "");
        if (json.isEmpty()) {
            return new ArrayList<>();
        }
        Type type = new TypeToken<ArrayList<Integer>>(){}.getType();
        return gson.fromJson(json, type);
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