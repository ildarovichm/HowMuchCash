package ru.ildarovichm.howmuchcash;

import static android.content.Context.MODE_PRIVATE;

import android.os.Environment;
import android.widget.Toast;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.OutputStream;
import java.nio.file.Path;
import java.time.LocalDate;
import java.util.ArrayList;

import ru.ildarovichm.howmuchcash.CalendarHandler;
import ru.ildarovichm.howmuchcash.ObjectUnit;

public class DataHandler {
    //переменные для передачи объектов в метод
    private ObjectUnit objectUnit;
    private File file;
    private CalendarHandler calendarHandler;
    private ArrayList<ArrayList<ObjectUnit>> amountObjectUnitList;
    private ArrayList<LocalDate> allDatesList;
    //конструктор объекта DataHandler
    public DataHandler() throws IOException {
        ArrayList<ArrayList<ObjectUnit>> amountObjectUnitList = new ArrayList<>();
        this.amountObjectUnitList = amountObjectUnitList;
//        File file = new File(Environment.getExternalStorageDirectory() + "/" + File.separator + "amountObjects.txt");
//        file.createNewFile();
//        this.file = file;
    }

    public ArrayList<ArrayList<ObjectUnit>> getAmountObjectUnitList() {
        return amountObjectUnitList;
    }

    public void addObjectUnit(ObjectUnit objectUnit){
        this.objectUnit = objectUnit;
        ArrayList<ObjectUnit> objectUnitList = new ArrayList<>();
        objectUnitList.add(objectUnit);
        amountObjectUnitList.add(objectUnitList);
    }

    public void addWatchDateList(CalendarHandler calendarHandler) {
        this.calendarHandler = calendarHandler;
    }

    //метод для записи переданного списка объектов в файл общего объема лифтов
    public boolean writeToFileObjectUnit() throws IOException {
        try {
            if(file.exists())
            {
                OutputStream fo = new FileOutputStream(file);
                fo.write(amountObjectUnitList.toString().getBytes());
                fo.close();
//                System.out.println("file created: "+file);
            }
            return true;
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

    }



    //метод для записи списка дежурств в файл дежурств
    public boolean writeToFileWatch(){
        try {
            FileWriter writer = new FileWriter("watch2.txt", false);
            writer.write(calendarHandler.getAllWatchDateList() + "\n");
            writer.close();
            return true;
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

//    public void saveFile() {
//        FileOutputStream fos = null;
//        try {
//            fos = openFileOutput("amountObjects.txt", MODE_PRIVATE);
//            fos.write(text.getBytes());
//        }
//        catch(IOException ex) {
//
//            Toast.makeText(this, ex.getMessage(), Toast.LENGTH_SHORT).show();
//        }
//        finally{
//            try{
//                if(fos!=null)
//                    fos.close();
//            }
//            catch(IOException ex){
//
//                Toast.makeText(this, ex.getMessage(), Toast.LENGTH_SHORT).show();
//            }
//        }
//    }
}