package top.andnux.sdk;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class MainViewModel extends ViewModel {

    private MutableLiveData<String> name = null;

    public MutableLiveData<String> getName() {
        if (name == null) {
            name = new MutableLiveData<>();
        }
        return name;
    }
}
