package ru.ildarovichm.howmuchcash.ui.auth;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.fragment.NavHostFragment;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import ru.ildarovichm.howmuchcash.R;
import ru.ildarovichm.howmuchcash.network.ApiClient;
import ru.ildarovichm.howmuchcash.network.ApiService;
import ru.ildarovichm.howmuchcash.ui.home.HomeFragment; // ← добавлен импорт

public class LoginFragment extends Fragment {

    private EditText usernameEditText, passwordEditText;
    private Button loginButton;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_login, container, false);

        usernameEditText = view.findViewById(R.id.usernameEditText);
        passwordEditText = view.findViewById(R.id.passwordEditText);
        loginButton = view.findViewById(R.id.loginButton);

        loginButton.setOnClickListener(v -> attemptLogin());

        return view;
    }

    private void attemptLogin() {
        String phone = usernameEditText.getText().toString().trim();
        String password = passwordEditText.getText().toString();

        if (phone.isEmpty() || password.isEmpty()) {
            Toast.makeText(getContext(), "Please fill all fields", Toast.LENGTH_SHORT).show();
            return;
        }

        ApiService.LoginRequest loginRequest = new ApiService.LoginRequest(phone, password);
        ApiService apiService = ApiClient.getClient().create(ApiService.class);
        Call<ApiService.AuthResponse> call = apiService.login(loginRequest);

        call.enqueue(new Callback<ApiService.AuthResponse>() {
            @Override
            public void onResponse(Call<ApiService.AuthResponse> call, Response<ApiService.AuthResponse> response) {
                if (response.isSuccessful()) {
                    ApiService.AuthResponse authResponse = response.body();
                    System.out.println("AuthResponse object: " + authResponse);
                    System.out.println("Token value: " + (authResponse != null ? authResponse.getToken() : "null"));

                    if (authResponse != null && authResponse.getToken() != null) {
                        String token = authResponse.getToken();
                        saveToken(token);
                        Toast.makeText(getContext(), "Login successful!", Toast.LENGTH_SHORT).show();
                        loadHomeFragment();
                    } else {
                        Toast.makeText(getContext(), "Token is null! Check server response.", Toast.LENGTH_LONG).show();
                    }
                } else {
                    try {
                        String errorBody = response.errorBody() != null ? response.errorBody().string() : "No error body";
                        Toast.makeText(getContext(), "Error: " + response.code() + "\n" + errorBody, Toast.LENGTH_LONG).show();
                    } catch (Exception e) {
                        Toast.makeText(getContext(), "Error: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                }
            }

            @Override
            public void onFailure(Call<ApiService.AuthResponse> call, Throwable t) {
                Toast.makeText(getContext(), "Network Error: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void saveToken(String token) {
        SharedPreferences sharedPreferences = getContext().getSharedPreferences("app_prefs", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString("auth_token", token);
        editor.apply();
    }

    private void loadHomeFragment() {
        NavController navController = Navigation.findNavController(requireView());
        navController.navigate(R.id.action_loginFragment_to_nav_home);
    }
}