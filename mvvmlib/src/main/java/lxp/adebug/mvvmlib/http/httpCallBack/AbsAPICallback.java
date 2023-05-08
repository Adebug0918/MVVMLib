package lxp.adebug.mvvmlib.http.httpCallBack;

import android.content.Context;
import android.net.ParseException;
import android.widget.Toast;

import com.google.gson.JsonParseException;

import org.json.JSONException;

import java.net.ConnectException;
import java.net.SocketTimeoutException;

import io.reactivex.observers.DisposableObserver;
import lxp.adebug.mvvmlib.http.lxp.ResultException;
import lxp.adebug.mvvmlib.utils.Utils;
import retrofit2.HttpException;


/**
 * @author lxp
 * @date 2016/4/19
 */
public class AbsAPICallback<T> extends DisposableObserver<T> {

    private HttpInterface httpInterface;
    private static Context mContext = Utils.getContext();
    private static final int UNAUTHORIZED = 401;
    private static final int FORBIDDEN = 403;
    private static final int NOT_FOUND = 404;
    private static final int REQUEST_TIMEOUT = 408;
    private static final int INTERNAL_SERVER_ERROR = 500;
    private static final int BAD_GATEWAY = 502;
    private static final int SERVICE_UNAVAILABLE = 503;
    private static final int GATEWAY_TIMEOUT = 504;

    public AbsAPICallback(HttpInterface httpInterface) {
        this.httpInterface = httpInterface;
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
                    Toast.makeText(mContext, httpException.code() + ":" + httpException.getMessage(), Toast.LENGTH_SHORT).show();
                    break;
            }
        } else if (e instanceof SocketTimeoutException) {
            SocketTimeoutException exception = (SocketTimeoutException) e;
            Toast.makeText(mContext, exception.getMessage(), Toast.LENGTH_SHORT).show();
        } else if (e instanceof ResultException) {
            ResultException resultException = (ResultException) e;
            Toast.makeText(mContext, resultException.getMessage(), Toast.LENGTH_SHORT).show();
        } else if (e instanceof JsonParseException) {
            JsonParseException parseException = (JsonParseException) e;
            Toast.makeText(mContext, parseException.getMessage(), Toast.LENGTH_SHORT).show();
        } else if (e instanceof JSONException) {
            JSONException jsonException = (JSONException) e;
            Toast.makeText(mContext, jsonException.getMessage(), Toast.LENGTH_SHORT).show();
        } else if (e instanceof ParseException) {
            ParseException parseException = (ParseException) e;
            Toast.makeText(mContext, parseException.getMessage(), Toast.LENGTH_SHORT).show();
        } else if (e instanceof ConnectException) {
            ConnectException connectException = (ConnectException) e;
            Toast.makeText(mContext, connectException.getMessage(), Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(mContext, "Unknown error, please try again", Toast.LENGTH_SHORT).show();
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
    }

    @Override
    public void onComplete() {
        httpInterface.onComplete();
    }


    @Override
    public void onNext(T t) {
        httpInterface.onSuccess(t.toString());
    }
}
