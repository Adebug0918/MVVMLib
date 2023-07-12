package lxp.adebug.mvvmlib.app;

import android.os.Build;

import com.goldze.mvvmhabit.BuildConfig;
import com.goldze.mvvmhabit.R;
import com.squareup.leakcanary.LeakCanary;

import java.util.HashMap;

import lxp.adebug.mvvmlib.base.BaseApplication;
import lxp.adebug.mvvmlib.crash.LibConfig;
import lxp.adebug.mvvmlib.http.lxp.BaseHttp;
import lxp.adebug.mvvmlib.ui.login.LoginActivity;
import lxp.adebug.mvvmlib.utils.KLog;

/**
 * @author Adebug
 * @date 2017/7/16
 */

public class AppApplication extends BaseApplication {
    @Override
    public void onCreate() {
        super.onCreate();
        //是否开启打印日志
        KLog.init(BuildConfig.DEBUG);
        //初始化全局异常崩溃
        initCrash();
        //内存泄漏检测
        if (!LeakCanary.isInAnalyzerProcess(this)) {
            LeakCanary.install(this);
        }
    }

    private void initCrash() {
        LibConfig.Builder.create()
                .backgroundMode(LibConfig.BACKGROUND_MODE_SILENT) //背景模式,开启沉浸式
                .enabled(true) //是否启动全局异常捕获
                .showErrorDetails(true) //是否显示错误详细信息
                .showRestartButton(true) //是否显示重启按钮
                .trackActivities(true) //是否跟踪Activity
                .minTimeBetweenCrashesMs(2000) //崩溃的间隔时间(毫秒)
                .errorDrawable(R.mipmap.ic_launcher) //错误图标
                .restartActivity(LoginActivity.class) //重新启动后的activity
//                .errorActivity(YourCustomErrorActivity.class) //崩溃后的错误activity
//                .eventListener(new YourCustomEventListener()) //崩溃后的错误监听
                .apply();
        httpCode = 200;//默认200 后台接口验证成功code
        fieldCode = "code";//后台返回字段名 默认code
        fieldMessage = "message";//后台返回字段名 默认message
        HashMap<String, Object> header = new HashMap<>();
        header.put("model", Build.MODEL);
        header.put("brand", Build.BRAND);
        header.put("release", Build.VERSION.RELEASE);
        header.put("hardware", Build.HARDWARE);
        header.put("custom", "custom");//自定义header头参数
        BaseHttp.init("https://www.baidu.com", header);
    }
}
