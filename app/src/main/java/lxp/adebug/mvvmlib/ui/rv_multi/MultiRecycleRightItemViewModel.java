package lxp.adebug.mvvmlib.ui.rv_multi;

import androidx.annotation.NonNull;
import androidx.databinding.ObservableField;
import lxp.adebug.mvvmlib.base.MultiItemViewModel;

import lxp.adebug.mvvmlib.binding.command.BindingAction;
import lxp.adebug.mvvmlib.binding.command.BindingCommand;
import lxp.adebug.mvvmlib.utils.ToastUtils;

/**
 * Create Author：goldze
 * Create Date：2019/01/25
 * Description：
 */

public class MultiRecycleRightItemViewModel extends MultiItemViewModel<MultiRecycleViewModel> {
    public ObservableField<String> text = new ObservableField<>("");

    public MultiRecycleRightItemViewModel(@NonNull MultiRecycleViewModel viewModel, String text) {
        super(viewModel);
        this.text.set(text);
    }

    //条目的点击事件
    public BindingCommand itemClick = new BindingCommand(new BindingAction() {
        @Override
        public void call() {
            //拿到position
            int position = viewModel.observableList.indexOf(MultiRecycleRightItemViewModel.this);
            ToastUtils.showShort("position：" + position);
        }
    });
}
