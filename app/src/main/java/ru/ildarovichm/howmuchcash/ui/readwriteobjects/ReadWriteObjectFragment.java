package ru.ildarovichm.howmuchcash.ui.readwriteobjects;

import static android.content.Context.MODE_PRIVATE;

import static androidx.core.content.PackageManagerCompat.LOG_TAG;

import static ru.ildarovichm.howmuchcash.FileChooserFragment.MY_REQUEST_CODE_PERMISSION;

import androidx.core.app.ActivityCompat;
import androidx.fragment.app.FragmentManager;
import androidx.lifecycle.ViewModelProvider;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.ContentResolver;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.os.Environment;
import android.os.ParcelFileDescriptor;
import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Objects;

import ru.ildarovichm.howmuchcash.FileChooserFragment;
import ru.ildarovichm.howmuchcash.ObjectUnit;
import ru.ildarovichm.howmuchcash.R;
import ru.ildarovichm.howmuchcash.Unit;
import ru.ildarovichm.howmuchcash.databinding.FragmentReadWriteObjectBinding;

public class ReadWriteObjectFragment extends Fragment {
    private FragmentReadWriteObjectBinding binding;
    public FileChooserFragment fileChooserFragment;

    public int CHOOSE_FILE_REQUESTCODE = 2000;

    final String LOG_TAG = "myLogs";
    final String FILENAME = "file";
    private String clearFileName;
    private String path;
    SharedPreferences settings;
    public FileChooserFragment fileChooserFragment2;

    public static ReadWriteObjectFragment newInstance() {
        return new ReadWriteObjectFragment();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        ReadWriteObjectViewModel readWriteObjectViewModel =
                new ViewModelProvider(this).get(ReadWriteObjectViewModel.class);
        settings = getActivity().getSharedPreferences("ObjectUnitListPrefs", MODE_PRIVATE);
        path = settings.getString("FILEPATH", "");
        binding = FragmentReadWriteObjectBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onResume() {
        super.onResume();

        FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
        this.fileChooserFragment = (FileChooserFragment) fragmentManager.findFragmentById(R.id.fragment_fileChooser);

        binding.buttonReadFile.setOnClickListener(new View.OnClickListener() {
            // Читаем содержимое файла со списком объектов
            @Override
            public void onClick(View v) {
                //Toast.makeText(getContext(), "Path: " + path, Toast.LENGTH_LONG).show();
                if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.M) { // Level 23

                    // Check if we have Call permission
                    int permisson = ActivityCompat.checkSelfPermission(requireContext(),
                            Manifest.permission.READ_EXTERNAL_STORAGE);

                    if (permisson != PackageManager.PERMISSION_GRANTED) {
                        // If don't have permission so prompt the user.
                        requestPermissions(
                                new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},
                                MY_REQUEST_CODE_PERMISSION
                        );
                        return;
                    }
                }
                // *** Код для открытия файлового обозревателя ***
//                Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
//                intent.addCategory(Intent.CATEGORY_OPENABLE);
//                intent.setType("*/*");
//                Intent i = Intent.createChooser(intent, "File");
//                startActivityForResult(i, CHOOSE_FILE_REQUESTCODE);
                // *** ***
//                Uri contentUri = intent.getData();
//                String filePath = null;
//                filePath = FileUtils.getPath(requireContext(), fileUri);
//                Intent intent = new Intent(Intent.ACTION_CREATE_DOCUMENT);
//                intent.addCategory(Intent.CATEGORY_OPENABLE);
//                intent.setType("text/plain");
//                intent.putExtra(Intent.EXTRA_TITLE, cTime+".txt");
//                Toast.makeText(getContext(), "Path: " + filePath, Toast.LENGTH_LONG).show();
                Uri contentUri = MediaStore.Files.getContentUri("external");

                String selection = MediaStore.MediaColumns.RELATIVE_PATH + "=?";

                String[] selectionArgs = new String[]{Environment.DIRECTORY_DOCUMENTS + "/data/"};

                Cursor cursor = requireContext().getContentResolver().query(contentUri, null, selection, selectionArgs, null);

                Uri uri = null;

                assert cursor != null;
                if (cursor.getCount() == 0) {
                    Toast.makeText(v.getContext(), "No file found in \"" + Environment.DIRECTORY_DOCUMENTS + "/data/\"", Toast.LENGTH_LONG).show();
                } else {
                    while (cursor.moveToNext()) {
                        @SuppressLint("Range") String fileName = cursor.getString(cursor.getColumnIndex(MediaStore.MediaColumns.DISPLAY_NAME));

                        if (fileName.equals("list-of-lifts.txt")) {
                            @SuppressLint("Range") long id = cursor.getLong(cursor.getColumnIndex(MediaStore.MediaColumns._ID));

                            uri = ContentUris.withAppendedId(contentUri, id);

                            break;
                        }
                    }

                    if (uri == null) {
                        Toast.makeText(v.getContext(), "\"list-of-lifts.txt\" not found", Toast.LENGTH_SHORT).show();
                    } else {
                        try {
                            InputStream inputStream = requireContext().getContentResolver().openInputStream(uri);

                            assert inputStream != null;
                            int size = inputStream.available();

                            byte[] bytes = new byte[size];

                            inputStream.read(bytes);

                            inputStream.close();

                            String jsonString = new String(bytes, StandardCharsets.UTF_8);

                            AlertDialog.Builder builder = new AlertDialog.Builder(v.getContext());

                            builder.setTitle("File Content");
                            builder.setMessage(jsonString);
                            builder.setPositiveButton("OK", null);

                            builder.create().show();
                        } catch (IOException e) {
                            Toast.makeText(v.getContext(), "Fail to read file", Toast.LENGTH_SHORT).show();
                        }
                    }
                }
            }
        });
        settings = getActivity().getSharedPreferences("ObjectUnitListPrefs", MODE_PRIVATE);
        String strings = settings.getString("OBJECT_UNIT_LIST", "");
        this.fileChooserFragment2 = (FileChooserFragment) fragmentManager.findFragmentById(R.id.fragment_fileChooser2);
        binding.buttonWriteFile.setOnClickListener(new View.OnClickListener() {
            // записываем файл со списком объектов
            @Override
            public void onClick(View v) {
                try {
                    ContentValues values = new ContentValues();

                    values.put(MediaStore.MediaColumns.DISPLAY_NAME, "list-of-lifts");       //file name
                    values.put(MediaStore.MediaColumns.MIME_TYPE, "text/plain");        //file extension, will automatically add to file
                    values.put(MediaStore.MediaColumns.RELATIVE_PATH, Environment.DIRECTORY_DOCUMENTS + "/data/");     //end "/" is not mandatory

                    Uri uri = requireContext().getContentResolver().insert(MediaStore.Files.getContentUri("external"), values);      //important!

                    assert uri != null;
                    OutputStream outputStream = requireContext().getContentResolver().openOutputStream(uri);


                try {
                    settings = getActivity().getSharedPreferences("ObjectUnitListPrefs", MODE_PRIVATE);
                    String[] strings2 = settings.getString("OBJECT_UNIT_LIST", "").split("<s>");
                    assert outputStream != null;
//                    outputStream.write("This is menu category data.".getBytes());
                    outputStream.write(strings.getBytes(StandardCharsets.UTF_8));
//                    writeToFile(Arrays.toString(strings2), requireContext());
                    Toast.makeText(v.getContext(), strings, Toast.LENGTH_SHORT).show();
                    outputStream.close();
                } catch (IOException ex) {
                    throw new RuntimeException(ex);
                }
                    Toast.makeText(v.getContext(), "File created successfully", Toast.LENGTH_SHORT).show();
                } catch (IOException e) {
                    Toast.makeText(v.getContext(), "Fail to create file", Toast.LENGTH_SHORT).show();
                }
//                Toast.makeText(getContext(), "Path: " + path, Toast.LENGTH_LONG).show();
            }
        });
    }



//    private void writeToFile(String data,Context context) {
////        File path = getContext().getFilesDir();
//        File file = new File(path, "List-of-Objects.txt");
//        try {
//            FileOutputStream stream = new FileOutputStream(file);
//            try {
//                stream.write("text-to-write".getBytes());
//            } finally {
//                stream.close();
//            }
//        }
//        catch (IOException e) {
//            Log.e("Exception", "File write failed: " + e.toString());
//        }
//    }
//
//    private String readFromFile(Context context) {
//
//        String ret = "";
//
//        try {
//            InputStream inputStream = context.openFileInput("List-of-Objects.txt");
//
//            if ( inputStream != null ) {
//                InputStreamReader inputStreamReader = new InputStreamReader(inputStream);
//                BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
//                String receiveString = "";
//                StringBuilder stringBuilder = new StringBuilder();
//
//                while ( (receiveString = bufferedReader.readLine()) != null ) {
//                    stringBuilder.append("\n").append(receiveString);
//                }
//
//                inputStream.close();
//                ret = stringBuilder.toString();
//            }
//        }
//        catch (FileNotFoundException e) {
//            Log.e("login activity", "File not found: " + e.toString());
//        } catch (IOException e) {
//            Log.e("login activity", "Can not read file: " + e.toString());
//        }
//
//        return ret;
//    }
//    private void showInfo()  {
//        String path = this.fileChooserFragment.getPath();
//        Toast.makeText(this.getContext(), "Path: " + path, Toast.LENGTH_LONG).show();
//    }

    private void saveArrayList(String name, ArrayList<ObjectUnit> list) {
        settings = getActivity().getSharedPreferences("ObjectUnitListPrefs", MODE_PRIVATE);
        SharedPreferences.Editor editor = settings.edit();
        StringBuilder sb = new StringBuilder();
        for (ObjectUnit s : list) sb.append(s).append("<s>");
        sb.delete(sb.length() - 3, sb.length());
        editor.putString(name, sb.toString()).apply();
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

            int countOfObjectUnit = Integer.parseInt(subStr[1].trim());
            String typeOfObjectUnit = subStr[2];
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
    private void writeToFile(String data,Context context) {
        try {
            OutputStreamWriter outputStreamWriter = new OutputStreamWriter(context.openFileOutput("config.txt", Context.MODE_PRIVATE));
            outputStreamWriter.write(data);
            outputStreamWriter.close();
        }
        catch (IOException e) {
            Log.e("Exception", "File write failed: " + e.toString());
        }
    }
}


