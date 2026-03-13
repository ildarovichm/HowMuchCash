package ru.ildarovichm.howmuchcash;

import android.app.AlertDialog;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.navigation.NavigationView;

import androidx.core.view.GravityCompat;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.appcompat.app.AppCompatActivity;
import android.content.SharedPreferences;

import ru.ildarovichm.howmuchcash.databinding.ActivityMainBinding;

public class MainActivity extends AppCompatActivity {
    private String annotationToastText = "Запустился HomeFragment";
    private AppBarConfiguration mAppBarConfiguration;
    private ActivityMainBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        setSupportActionBar(binding.appBarMain.toolbar);
        DrawerLayout drawer = binding.drawerLayout;
        NavigationView navigationView = binding.navView;

        // Обработка кликов на элементы меню
        navigationView.setNavigationItemSelectedListener(menuItem -> {
            int id = menuItem.getItemId();

            if (id == R.id.loginFragment) {
                // Переход на LoginFragment
                NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment_content_main);
                navController.navigate(R.id.loginFragment);  // Переход на LoginFragment
                drawer.closeDrawer(GravityCompat.START);  // Закрыть меню
                return true;
            }

            // Обработка других элементов меню
            NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment_content_main);
            mAppBarConfiguration = new AppBarConfiguration.Builder(
                    R.id.nav_home, R.id.calculateSalaryFragment, R.id.settingsMenuFragment)
                    .setOpenableLayout(drawer)
                    .build();
            NavigationUI.onNavDestinationSelected(menuItem, navController);
            drawer.closeDrawer(GravityCompat.START);  // Закрыть меню
            return true;
        });

        // Настройка AppBar
        mAppBarConfiguration = new AppBarConfiguration.Builder(
                R.id.nav_home, R.id.calculateSalaryFragment, R.id.settingsMenuFragment)
                .setOpenableLayout(drawer)
                .build();
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment_content_main);
        NavigationUI.setupActionBarWithNavController(this, navController, mAppBarConfiguration);
        NavigationUI.setupWithNavController(navigationView, navController);

        SharedPreferences pricesSettings = getSharedPreferences("PricesSettings", MODE_PRIVATE);
    }

    @Override
    public boolean onSupportNavigateUp() {
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment_content_main);
        return NavigationUI.navigateUp(navController, mAppBarConfiguration)
                || super.onSupportNavigateUp();
    }

    public boolean showAboutPopup(MenuItem item) {
        // Создаём макет вручную
        View dialogView = getLayoutInflater().inflate(R.layout.dialog_about, null);

        // Найдём TextView и установим версию
        TextView versionText = dialogView.findViewById(R.id.text_version);
        versionText.setText("Версия: " + BuildConfig.VERSION_NAME);

        // Найдём кнопку OK
        Button btnOk = dialogView.findViewById(R.id.btnOk);

        // Создаём диалог
        AlertDialog dialog = new AlertDialog.Builder(this, R.style.CustomAboutDialog)
                .setView(dialogView)
                .create();

        // Сделаем фон прозрачным, чтобы был виден CardView
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

        // Установка затемнения фона (от 0.0 — нет затемнения, до 1.0 — чёрный экран)
        dialog.getWindow().setDimAmount(0.9f); // Рекомендуемое значение — 0.5–0.7

        // Обработка кнопки
        btnOk.setOnClickListener(v -> dialog.dismiss());

        dialog.show();
        return true;
    }
}
