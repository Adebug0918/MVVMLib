package lxp.adebug.mvvmlib.ui.rv_multi;

import androidx.annotation.NonNull;
import lxp.adebug.mvvmlib.base.BaseViewModel;
import lxp.adebug.mvvmlib.base.MultiItemViewModel;
import lxp.adebug.mvvmlib.binding.command.BindingAction;
import lxp.adebug.mvvmlib.binding.command.BindingCommand;
import lxp.adebug.mvvmlib.utils.ToastUtils;

/**
 * Create Author：goldze
 * Create Date：2019/01/25
 * Description：
 */

public class MultiRecycleHeadViewModel extends MultiItemViewModel {

    public MultiRecycleHeadViewModel(@NonNull BaseViewModel viewModel) {
        super(viewModel);
    }

    //条目的点击事件
    public BindingCommand itemClick = new BindingCommand(new BindingAction() {
        @Override
        public void call() {
            ToastUtils.showShort("我是头布局");
        }
    });
}
