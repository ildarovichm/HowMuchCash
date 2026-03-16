package ru.ildarovichm.howmuchcash;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;
import com.google.android.material.navigation.NavigationView;
import android.view.Gravity; // Импортировать класс Gravity

import ru.ildarovichm.howmuchcash.databinding.ActivityMainBinding;

public class MainActivity extends AppCompatActivity {

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

        // Получение NavController
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment_content_main);

        // Проверка токена при старте
        SharedPreferences prefs = getSharedPreferences("auth_prefs", MODE_PRIVATE);
        String token = prefs.getString("auth_token", null);

        if (token == null || token.isEmpty()) {
            // Если токен отсутствует, показываем LoginFragment
            navController.navigate(R.id.loginFragment);  // Переход на LoginFragment
        } else {
            // Если токен существует, показываем nav_home (HomeFragment)
            navController.navigate(R.id.nav_home);  // Переход на HomeFragment
        }

        // Обработка кликов на элементы меню
        navigationView.setNavigationItemSelectedListener(menuItem -> {
            int id = menuItem.getItemId();

            if (id == R.id.loginFragment) {
                // Переход на LoginFragment
                navController.navigate(R.id.loginFragment);  // Переход на LoginFragment
                drawer.closeDrawer(Gravity.START);  // Закрыть меню, заменен на Gravity
                return true;
            }

            // Обработка других элементов меню
            mAppBarConfiguration = new AppBarConfiguration.Builder(
                    R.id.nav_home, R.id.calculateSalaryFragment, R.id.settingsMenuFragment)
                    .setOpenableLayout(drawer)
                    .build();
            NavigationUI.onNavDestinationSelected(menuItem, navController);
            drawer.closeDrawer(Gravity.START);  // Закрыть меню, заменен на Gravity
            return true;
        });

        // Настройка AppBar
        mAppBarConfiguration = new AppBarConfiguration.Builder(
                R.id.nav_home, R.id.calculateSalaryFragment, R.id.settingsMenuFragment)
                .setOpenableLayout(drawer)
                .build();
        NavigationUI.setupActionBarWithNavController(this, navController, mAppBarConfiguration);
        NavigationUI.setupWithNavController(navigationView, navController);
    }

    @Override
    public boolean onSupportNavigateUp() {
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment_content_main);
        return NavigationUI.navigateUp(navController, mAppBarConfiguration)
                || super.onSupportNavigateUp();
    }

    // Удаление токена при выходе
    public void logout() {
        SharedPreferences prefs = getSharedPreferences("auth_prefs", MODE_PRIVATE);
        prefs.edit().remove("auth_token").apply(); // Удаляем токен
        // Переход к экрану логина
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment_content_main);
        navController.navigate(R.id.loginFragment);
    }
}
