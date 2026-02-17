package ru.ildarovichm.howmuchcash.ui.readwriteobjects;

import static android.content.Context.MODE_PRIVATE;

import androidx.lifecycle.ViewModelProvider;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.lang.reflect.Type;
import java.util.ArrayList;

import ru.ildarovichm.howmuchcash.ObjectUnit;
import ru.ildarovichm.howmuchcash.Unit;
import ru.ildarovichm.howmuchcash.databinding.FragmentReadWriteObjectBinding;

public class ReadWriteObjectFragment extends Fragment {

    private static final int REQUEST_CODE_CREATE_FILE = 43;
    private static final int REQUEST_CODE_OPEN_FILE = 42;
    private FragmentReadWriteObjectBinding binding;
    private String textToSave;
    private Gson gson;
    SharedPreferences settings;
    ArrayList<ObjectUnit> objectUnitList;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        ReadWriteObjectViewModel readWriteObjectViewModel =
                new ViewModelProvider(this).get(ReadWriteObjectViewModel.class);
        settings = getActivity().getSharedPreferences("ObjectUnitListPrefs", MODE_PRIVATE);
        gson = new GsonBuilder()
                .setPrettyPrinting()  // Для читаемого форматирования JSON
                .create();
        binding = FragmentReadWriteObjectBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    private void openFilePicker() {
        Intent intent = new Intent(Intent.ACTION_OPEN_DOCUMENT);
        intent.addCategory(Intent.CATEGORY_OPENABLE);
        intent.setType("text/plain");
        startActivityForResult(intent, REQUEST_CODE_OPEN_FILE);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode != getActivity().RESULT_OK || data == null) {
            return;
        }

        Uri uri = data.getData();

        switch (requestCode) {
            case REQUEST_CODE_OPEN_FILE:
                readTextFileFromUri(uri);
                break;

            case REQUEST_CODE_CREATE_FILE:
                settings = getActivity().getSharedPreferences("ObjectUnitListPrefs", MODE_PRIVATE);
                textToSave = settings.getString("OBJECT_UNIT_LIST", "");
                if (textToSave != null) {
                    saveStringToUri(uri, textToSave);
                    textToSave = null;
                } else {
                    Toast.makeText(getContext(), "Ошибка: нет текста для сохранения", Toast.LENGTH_SHORT).show();
                }
                break;
        }
    }

    private void saveStringToUri(Uri uri, String text) {
        Context context = getContext();
        if (context == null) return;

        try (OutputStream outputStream = context.getContentResolver().openOutputStream(uri);
             BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(outputStream))) {

            writer.write(text);
            writer.flush();

            Toast.makeText(context, "Файл сохранен", Toast.LENGTH_SHORT).show();

        } catch (IOException e) {
            e.printStackTrace();
            Toast.makeText(context, "Ошибка сохранения", Toast.LENGTH_SHORT).show();
        }
    }

    private void readTextFileFromUri(Uri uri) {
        try (InputStream inputStream = getContext().getContentResolver().openInputStream(uri);
             BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream))) {

            StringBuilder content = new StringBuilder();
            String line;
            while ((line = reader.readLine()) != null) {
                content.append(line).append("\n");
            }
            reader.close();
            inputStream.close();
            Toast.makeText(getContext(), "Файл прочитан, длина: " + content.length(), Toast.LENGTH_SHORT).show();
            loadArrayListJSON(content);
        } catch (IOException e) {
            e.printStackTrace();
            Toast.makeText(getContext(), "Ошибка чтения: " + e.getMessage(), Toast.LENGTH_SHORT).show();
        } catch (NullPointerException e) {
            e.printStackTrace();
            Toast.makeText(getContext(), "Context is null", Toast.LENGTH_SHORT).show();
        }
    }

    private ArrayList<ObjectUnit> loadArrayListJSON(StringBuilder content) {
        String json = content.toString();
        Type type = new TypeToken<ArrayList<ObjectUnit>>(){}.getType();
        objectUnitList = gson.fromJson(json, type);
        
        SharedPreferences.Editor editor = settings.edit();
        editor.putString("OBJECT_UNIT_LIST", json);
        editor.apply();

        Toast.makeText(getContext(),
                "Загружено " + objectUnitList.size() + " объектов из файла",
                Toast.LENGTH_SHORT).show();
        return objectUnitList;
    }

    private void createFile(String fileName) {

        Intent intent = new Intent(Intent.ACTION_CREATE_DOCUMENT);
        intent.addCategory(Intent.CATEGORY_OPENABLE);
        intent.setType("text/plain");
        intent.putExtra(Intent.EXTRA_TITLE, fileName);

        startActivityForResult(intent, REQUEST_CODE_CREATE_FILE);
    }

    @Override
    public void onResume() {
        super.onResume();
        binding.buttonReadFile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openFilePicker();
            }
        });

        binding.buttonWriteFile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                createFile("object_list.txt");
            }
        });
    }
}


