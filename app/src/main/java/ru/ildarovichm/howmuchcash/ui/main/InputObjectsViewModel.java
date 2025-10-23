package ru.ildarovichm.howmuchcash.ui.main;

import android.view.View;
import android.widget.TextView;
import android.widget.Toast;


import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import ru.ildarovichm.howmuchcash.databinding.FragmentInputObjectsBinding;

public class InputObjectsViewModel extends ViewModel {

    private final MutableLiveData<String> mText;

    public InputObjectsViewModel() {
        mText = new MutableLiveData<>();
        mText.setValue("This is home fragment");

    }

//    public LiveData<String> getText() {
//        return mText;
//    }
}