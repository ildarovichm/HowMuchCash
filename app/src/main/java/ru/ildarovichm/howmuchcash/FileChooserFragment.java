package ru.ildarovichm.howmuchcash;

import static android.content.Context.MODE_PRIVATE;

import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;

import androidx.core.app.ActivityCompat;

import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;

/**
 * A simple {@link Fragment} subclass.
// * Use the factory method to FileChooserFragment#newInstance}
 * create an instance of this fragment.
 */
public class FileChooserFragment extends Fragment {

    public static final int MY_REQUEST_CODE_PERMISSION = 1000;
    private static final int MY_RESULT_CODE_FILECHOOSER = 2000;

    private Button buttonBrowse;
    private EditText editTextPath;

    SharedPreferences settings;


    private static final String LOG_TAG = "AndroidExample";

    @Override
    public View onCreateView(final LayoutInflater inflater, final ViewGroup container, final Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.fragment_file_chooser, container, false);

        settings = getActivity().getSharedPreferences("ObjectUnitListPrefs", MODE_PRIVATE);

        this.editTextPath = rootView.findViewById(R.id.editText_path);
        this.buttonBrowse = rootView.findViewById(R.id.button_browse);

        this.buttonBrowse.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                askPermissionAndBrowseFile();
            }

        });
        return rootView;
    }

    private void askPermissionAndBrowseFile() {
        // With Android Level >= 23, you have to ask the user
        // for permission to access External Storage.

        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.M) { // Level 23

            // Check if we have Call permission
            int permisson = ActivityCompat.checkSelfPermission(this.getContext(),
                    Manifest.permission.READ_EXTERNAL_STORAGE);

            if (permisson != PackageManager.PERMISSION_GRANTED) {
                // If don't have permission so prompt the user.
                this.requestPermissions(
                        new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},
                        MY_REQUEST_CODE_PERMISSION
                );
                return;
            }
        }
        this.doBrowseFile();
    }

    private void doBrowseFile() {
        Intent chooseFileIntent = new Intent(Intent.ACTION_GET_CONTENT);
        chooseFileIntent.setType("*/*");
        // Only return URIs that can be opened with ContentResolver
        chooseFileIntent.addCategory(Intent.CATEGORY_OPENABLE);

        chooseFileIntent = Intent.createChooser(chooseFileIntent, "Choose a file");
        startActivityForResult(chooseFileIntent, MY_RESULT_CODE_FILECHOOSER);


    }

    // When you have the request results
    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           String permissions[], int[] grantResults) {

        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        //
        switch (requestCode) {
            case MY_REQUEST_CODE_PERMISSION: {

                // Note: If request is cancelled, the result arrays are empty.
                // Permissions granted (CALL_PHONE).
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                    Log.i(LOG_TAG, "Permission granted!");
                    Toast.makeText(this.getContext(), "Permission granted!", Toast.LENGTH_SHORT).show();

                    this.doBrowseFile();
                    try {
                        String myData = "";
                        String filePath = getPath();
                        String[] pathFinder = filePath.split("/");
                        String childPath = pathFinder[pathFinder.length - 1];
                        String parentPath = filePath.substring(0, filePath.lastIndexOf("/") + 1);
                        File myExternalFile = new File(parentPath,childPath);
                        try {
                            FileInputStream fis = new FileInputStream(myExternalFile);
                            DataInputStream in = new DataInputStream(fis);
                            BufferedReader br = new BufferedReader(new InputStreamReader(in));
                            String strLine;
                            while ((strLine = br.readLine()) != null) {
                                myData = myData + strLine + "\n";
                            }
                            br.close();
                            in.close();
                            fis.close();
                            Toast.makeText(getContext(), "YOUR FILE DATA: " + myData, Toast.LENGTH_LONG).show();
                            SharedPreferences.Editor editor = settings.edit();
                            editor.putString("FILEPATH", filePath);
                            editor.putString("OBJECTLISTFROMFILE", myData).apply();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }

                    } catch (Exception e) {
                        Log.e(LOG_TAG, "Error: " + e);
                        Toast.makeText(this.getContext(), "Error: " + e, Toast.LENGTH_SHORT).show();
                    }
                }
                // Cancelled or denied.
                else {
                    Log.i(LOG_TAG, "Permission denied!");
                    Toast.makeText(this.getContext(), "Permission denied!", Toast.LENGTH_SHORT).show();
                }
                break;
            }
        }
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode) {
            case MY_RESULT_CODE_FILECHOOSER:
                if (resultCode == Activity.RESULT_OK) {
                    if (data != null) {
                        Uri fileUri = data.getData();
                        Log.i(LOG_TAG, "Uri: " + fileUri);

                        String filePath = null;
                        try {
                            String myData = "";
                            filePath = FileUtils.getPath(this.getContext(), fileUri);
                            String[] pathFinder = filePath.split("/");
                            String childPath = pathFinder[pathFinder.length - 1];
                            String parentPath = filePath.substring(0, filePath.lastIndexOf("/") + 1);
                            File myExternalFile = new File(parentPath,childPath);
                            try {
                                FileInputStream fis = new FileInputStream(myExternalFile);
                                DataInputStream in = new DataInputStream(fis);
                                BufferedReader br = new BufferedReader(new InputStreamReader(in));
                                String strLine;
                                while ((strLine = br.readLine()) != null) {
                                    myData = myData + strLine + "\n";
                                }
                                br.close();
                                in.close();
                                fis.close();
                                Toast.makeText(getContext(), "YOUR FILE DATA: " + myData, Toast.LENGTH_LONG).show();
                                SharedPreferences.Editor editor = settings.edit();
                                editor.putString("FILEPATH", filePath);
                                editor.putString("OBJECTLISTFROMFILE", myData).apply();
                            } catch (IOException e) {
                                e.printStackTrace();
                            }

                        } catch (Exception e) {
                            Log.e(LOG_TAG, "Error: " + e);
                            Toast.makeText(this.getContext(), "Error: " + e, Toast.LENGTH_SHORT).show();
                        }
                        this.editTextPath.setText(filePath);

                    }
                }
                break;
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    public String getData() {
        String[] pathFinder = this.getPath().split("/");
        String childPath = pathFinder[pathFinder.length - 1];
        String parentPath = this.getPath().substring(0, this.getPath().lastIndexOf("/"));
        File myFile = new File(parentPath + "/" + childPath); //Environment.getExternalStorageDirectory().toString()
        try {
            FileInputStream inputStream = new FileInputStream(myFile);
            /*
             * Буфферезируем данные из выходного потока файла
             */
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
            /*
             * Класс для создания строк из последовательностей символов
             */
            StringBuilder stringBuilder = new StringBuilder();
            String line;
            try {
                /*
                 * Производим построчное считывание данных из файла в конструктор строки,
                 * Псоле того, как данные закончились, производим вывод текста в TextView
                 */
                while ((line = bufferedReader.readLine()) != null) {
                    stringBuilder.append(line);
                }
                return String.valueOf(stringBuilder);
//                textView.setText(stringBuilder);
            } catch (IOException e) {
                e.printStackTrace();
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

        return "";
    }

    public String getPath()  {
        return this.editTextPath.getText().toString();
    }
}