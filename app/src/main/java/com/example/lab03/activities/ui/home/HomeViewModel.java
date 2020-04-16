package com.example.lab03.activities.ui.home;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class HomeViewModel extends ViewModel {

    private MutableLiveData<String> mText;

    public HomeViewModel() {
        mText = new MutableLiveData<>();
        mText.setValue("Bienvenid@ "+ "\n" +" al Sistema de Gestión");
    }

    public LiveData<String> getText() {
        return mText;
    }
}