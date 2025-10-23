package ru.ildarovichm.howmuchcash.ui.slideshow;

import static android.content.Context.MODE_PRIVATE;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import java.util.ArrayList;

import ru.ildarovichm.howmuchcash.Price;
import ru.ildarovichm.howmuchcash.databinding.FragmentSlideshowBinding;

public class SlideshowFragment extends Fragment {

    private FragmentSlideshowBinding binding;
    SharedPreferences settings;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        if(savedInstanceState != null) {
            Toast.makeText(getContext(), "onCreateView, savedInstanceState != 0", Toast.LENGTH_LONG).show();
        }
        binding = FragmentSlideshowBinding.inflate(inflater, container, false);
        settings = getActivity().getSharedPreferences("CalculatingResult", MODE_PRIVATE);
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
    }

    @Override
    public void onResume() {
        super.onResume();
//        settings = getActivity().getSharedPreferences("CalculatingResult", MODE_PRIVATE);
        String resAmountLift = settings.getString("SALARY_AMOUNT_OBJECT_UNIT_LIFT", String.valueOf(0));
        String resAmountElevator = settings.getString("SALARY_AMOUNT_OBJECT_UNIT_ELEVATOR", String.valueOf(0));
        String resAmountObjectUnit = settings.getString("SALARY_AMOUNT_OBJECT_UNIT", String.valueOf(0));
        String resAmountWatch = settings.getString("SALARY_AMOUNT_WATCH", String.valueOf(0));
        String resAmountAll = settings.getString("SALARY_AMOUNT_ALL", String.valueOf(0));

        binding.textViewSalaryLiftTR.setText(resAmountLift);
        binding.textViewSalaryLift.setText(resAmountLift);
        binding.textViewSalaryElevatorTR.setText(resAmountElevator);
        binding.textViewSalaryElevator.setText(resAmountElevator);
        binding.textViewSalaryAmountObjectsTR.setText(resAmountObjectUnit);
        binding.textViewSalaryAmountObjects.setText(resAmountObjectUnit);
        binding.textViewSalaryWatchTR.setText(resAmountWatch);
        binding.textViewSalaryWatch.setText(resAmountWatch);
        binding.textViewSalaryAmountAllTR.setText(resAmountAll);
        binding.textViewSalaryAmountAll.setText(resAmountAll);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}