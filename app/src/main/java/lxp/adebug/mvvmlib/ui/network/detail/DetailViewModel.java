package lxp.adebug.mvvmlib.ui.network.detail;

import android.app.Application;

import lxp.adebug.mvvmlib.entity.DemoEntity;

import androidx.annotation.NonNull;
import androidx.databinding.ObservableField;
import lxp.adebug.mvvmlib.base.BaseViewModel;

/**
 * Created by goldze on 2017/7/17.
 */

public class DetailViewModel extends BaseViewModel {
    public ObservableField<DemoEntity.ItemsEntity> entity = new ObservableField<>();

    public DetailViewModel(@NonNull Application application) {
        super(application);
    }

    public void setDemoEntity(DemoEntity.ItemsEntity entity) {
        this.entity.set(entity);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        entity = null;
    }
}
