package com.example.expensetracker.ui.earnExpense;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class EarnExpenseViewModel extends ViewModel {

    private MutableLiveData<String> mText;

    public EarnExpenseViewModel() {
        mText = new MutableLiveData<>();
        mText.setValue("This is earn/expense fragment");
    }

    public LiveData<String> getText() {
        return mText;
    }
}
