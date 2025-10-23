package ru.ildarovichm.howmuchcash.ui.menusettings;

import static androidx.navigation.Navigation.findNavController;

import androidx.lifecycle.ViewModelProvider;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import ru.ildarovichm.howmuchcash.R;
import ru.ildarovichm.howmuchcash.databinding.FragmentSettingsMenuBinding;

public class SettingsMenuFragment extends Fragment {

    private FragmentSettingsMenuBinding binding;

    public static SettingsMenuFragment newInstance() {

        return new SettingsMenuFragment();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        SettingsMenuViewModel mViewModel =
                new ViewModelProvider(this).get(SettingsMenuViewModel.class);
        binding = FragmentSettingsMenuBinding.inflate(inflater, container, false);
        View root = binding.getRoot();
        binding.buttonAddPrices.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                NavController navController = findNavController(binding.getRoot());
                navController.navigate(R.id.action_settingsMenuFragment_to_nav_gallery);
            }
        });

        binding.buttonLoadPreference.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                NavController navController = findNavController(binding.getRoot());
                navController.navigate(R.id.action_settingsMenuFragment_to_readWriteObjectFragment);
            }
        });
        return root;
    }
}