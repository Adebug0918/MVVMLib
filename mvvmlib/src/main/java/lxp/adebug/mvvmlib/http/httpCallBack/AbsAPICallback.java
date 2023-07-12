package lxp.adebug.mvvmlib.http.httpCallBack;

import android.net.ParseException;

import com.google.gson.JsonParseException;

import org.json.JSONException;

import java.net.ConnectException;
import java.net.SocketTimeoutException;

import io.reactivex.observers.DisposableObserver;
import lxp.adebug.mvvmlib.base.BaseViewModel;
import lxp.adebug.mvvmlib.http.lxp.ResultException;
import lxp.adebug.mvvmlib.utils.ToastUtils;
import retrofit2.HttpException;


/**
 * @author lxp
 * @date 2016/4/19
 */
public class AbsAPICallback<T> extends DisposableObserver<T> {

    private HttpInterface httpInterface;
    private BaseViewModel viewModel;
    private static final int UNAUTHORIZED = 401;
    private static final int FORBIDDEN = 403;
    private static final int NOT_FOUND = 404;
    private static final int REQUEST_TIMEOUT = 408;
    private static final int INTERNAL_SERVER_ERROR = 500;
    private static final int BAD_GATEWAY = 502;
    private static final int SERVICE_UNAVAILABLE = 503;
    private static final int GATEWAY_TIMEOUT = 504;

    public AbsAPICallback(HttpInterface httpInterface, BaseViewModel viewModel) {
        this.httpInterface = httpInterface;
        this.viewModel = viewModel;
    }

    @Override
    public void onError(Throwable e) {
        Throwable throwable = e;
        while (throwable.getCause() != null) {
            e = throwable;
            throwable = throwable.getCause();
        }
        if (e instanceof HttpException) {
            HttpException httpException = (HttpException) e;
            switch (httpException.code()) {
                case UNAUTHORIZED:
                case FORBIDDEN:
                case NOT_FOUND:
                case REQUEST_TIMEOUT:
                case GATEWAY_TIMEOUT:
                case INTERNAL_SERVER_ERROR:
                case BAD_GATEWAY:
                case SERVICE_UNAVAILABLE:
                default:
                    ToastUtils.showShortSafe(httpException.code() + ":" + httpException.getMessage());
                    break;
            }
        } else if (e instanceof SocketTimeoutException) {
            SocketTimeoutException exception = (SocketTimeoutException) e;
            ToastUtils.showShortSafe(exception.getMessage());
        } else if (e instanceof ResultException) {
            ResultException resultException = (ResultException) e;
            ToastUtils.showShortSafe(resultException.getMessage());
        } else if (e instanceof JsonParseException) {
            JsonParseException parseException = (JsonParseException) e;
            ToastUtils.showShortSafe(parseException.getMessage());
        } else if (e instanceof JSONException) {
            JSONException jsonException = (JSONException) e;
            ToastUtils.showShortSafe(jsonException.getMessage());
        } else if (e instanceof ParseException) {
            ParseException parseException = (ParseException) e;
            ToastUtils.showShortSafe(parseException.getMessage());
        } else if (e instanceof ConnectException) {
            ConnectException connectException = (ConnectException) e;
            ToastUtils.showShortSafe(connectException.getMessage());
        } else {
            ToastUtils.showShortSafe("Unknown error, please try again");
        }
        if (e instanceof ResultException) {
            ResultException resultException = (ResultException) e;
            httpInterface.onFailed(resultException.getErrCode(), e);
        } else if (e instanceof HttpException) {
            HttpException httpException = (HttpException) e;
            httpInterface.onFailed(httpException.code(), e);
        } else {
            httpInterface.onFailed(0, e);
        }
        onComplete();
    }

    @Override
    public void onComplete() {
        if (viewModel != null) {
            viewModel.dismissDialog();
        }
    }


    @Override
    public void onNext(T t) {
        httpInterface.onSuccess(t.toString());
        onComplete();
    }
}
