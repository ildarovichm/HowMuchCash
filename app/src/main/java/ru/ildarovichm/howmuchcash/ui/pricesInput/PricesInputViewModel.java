package ru.ildarovichm.howmuchcash.ui.pricesInput;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import ru.ildarovichm.howmuchcash.Price;

public class PricesInputViewModel extends ViewModel {
    MutableLiveData<Price> priceViewModel;

    public MutableLiveData<Price> getPriceViewModel() {
        return priceViewModel;
    }

    public void setPriceViewModel(Price priceViewModel) {
        this.priceViewModel = new MutableLiveData<>(priceViewModel);
    }
}