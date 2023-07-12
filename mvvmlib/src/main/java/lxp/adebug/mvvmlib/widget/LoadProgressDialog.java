package lxp.adebug.mvvmlib.widget;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.Gravity;
import android.view.Window;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import me.goldze.mvvmhabit.R;


/**
 * Created by lxp on 2016/4/19.
 */

public class LoadProgressDialog extends Dialog {

    private Activity act;
    private LinearLayout mView;
    private TextView text_tv;
    private ProgressBar parogressbar;

    public LoadProgressDialog(Context context) {
        super(context, R.style.progress_dialog);
        this.act = (Activity) context;
        mView = (LinearLayout) act.getLayoutInflater().inflate(R.layout.progress_loaddialog, null, false);
        text_tv = mView.findViewById(R.id.text_tv);
        parogressbar = mView.findViewById(R.id.parogressbar);
        requestWindowFeature(Window.FEATURE_NO_TITLE);

    }

    public void setText(String text) {
        text_tv.setText(text);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Window window = this.getWindow();
        this.setCancelable(false);
        window.setBackgroundDrawableResource(android.R.color.transparent);
        window.setContentView(mView);
        window.setGravity( Gravity.CENTER);
        window.setLayout(dip2px(getContext(),160),dip2px(getContext(),120));
    }
    public  int dip2px(Context context, float dpValue) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (dpValue * scale + 0.5f);
    }

}
