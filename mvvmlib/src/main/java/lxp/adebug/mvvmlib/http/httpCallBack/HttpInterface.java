package lxp.adebug.mvvmlib.http.httpCallBack;


/**
 * @author lxp
 * @date 2017/7/31 0031
 */

public interface HttpInterface {
    void onSuccess(String data);
    void onFailed(int code, Throwable e);
    void onComplete();
}
