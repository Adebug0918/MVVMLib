package lxp.adebug.mvvmlib.http.lxp;


import android.text.TextUtils;

import androidx.annotation.NonNull;

import org.json.JSONObject;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;
import lxp.adebug.mvvmlib.base.BaseViewModel;
import lxp.adebug.mvvmlib.http.httpCallBack.AbsAPICallback;
import lxp.adebug.mvvmlib.http.httpCallBack.HttpInterface;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;


/**
 * @author lxp
 * @date 2018/7/23
 */

public class BaseHttp {

    private static BaseHttp baseHttp;
    private static String baseUrl = "";
    private static Map<String, Object> headers;

    private BaseViewModel viewModel;

    private BaseHttp(BaseViewModel viewModel) {
        this.viewModel = viewModel;
    }

    public synchronized static BaseHttp getInstance(BaseViewModel viewModel) {
        if (baseHttp == null) {
            baseHttp = new BaseHttp(viewModel);
        }
        return baseHttp;
    }

    public static void init(@NonNull String baseUrl, @NonNull Map<String, Object> headers) {
        BaseHttp.baseUrl = baseUrl;
        BaseHttp.headers = headers;
    }

    public void get(String link,boolean showDialog, HttpInterface httpInterface) {
        BaseClient.getApi(baseUrl).baseGet(headers, link)
                .subscribeOn(Schedulers.io())
                .observeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .doOnSubscribe(viewModel)
                .doOnSubscribe(disposable -> {
                    if (showDialog) {
                        viewModel.showDialog("加载中...");
                    }
                })
                .subscribe(new AbsAPICallback<>(httpInterface,viewModel));
    }

    public void get(String link,boolean showDialog, Map<String, Object> map, HttpInterface httpInterface) {
        BaseClient.getApi(baseUrl).baseGet(headers, link, map)
                .subscribeOn(Schedulers.io())
                .observeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .doOnSubscribe(viewModel)
                .doOnSubscribe(disposable -> {
                    if (showDialog) {
                        viewModel.showDialog("加载中...");
                    }
                })
                .subscribe(new AbsAPICallback<>(httpInterface,viewModel));
    }

    public void getJson(String link,boolean showDialog, Map<String, Object> map, HttpInterface httpInterface) {
        JSONObject jsonObject = new JSONObject(map);
        RequestBody body = RequestBody.create(MediaType.parse("application/json; charset=utf-8"), jsonObject.toString());
        BaseClient.getApi(baseUrl).baseGet(headers, link, body)
                .subscribeOn(Schedulers.io())
                .observeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .doOnSubscribe(viewModel)
                .doOnSubscribe(disposable -> {
                    if (showDialog) {
                        viewModel.showDialog("加载中...");
                    }
                })
                .subscribe(new AbsAPICallback<>(httpInterface,viewModel));
    }

    public void post(String link,boolean showDialog, HttpInterface httpInterface) {
        BaseClient.getApi(baseUrl).basePost(headers, link)
                .subscribeOn(Schedulers.io())
                .observeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .doOnSubscribe(viewModel)
                .doOnSubscribe(disposable -> {
                    if (showDialog) {
                        viewModel.showDialog("加载中...");
                    }
                })
                .subscribe(new AbsAPICallback<>(httpInterface,viewModel));
    }

    public void post(String link,boolean showDialog, Map<String, Object> map, HttpInterface httpInterface) {
        BaseClient.getApi(baseUrl).basePost(headers, link, map)
                .subscribeOn(Schedulers.io())
                .observeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .doOnSubscribe(viewModel)
                .doOnSubscribe(disposable -> {
                    if (showDialog) {
                        viewModel.showDialog("加载中...");
                    }
                })
                .subscribe(new AbsAPICallback<>(httpInterface,viewModel));
    }

    public void postJson(String link,boolean showDialog, Map<String, Object> map, HttpInterface httpInterface) {
        JSONObject jsonObject = new JSONObject(map);
        RequestBody body = RequestBody.create(MediaType.parse("application/json; charset=utf-8"), jsonObject.toString());
        BaseClient.getApi(baseUrl).basePost(headers, link, body)
                .subscribeOn(Schedulers.io())
                .observeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .doOnSubscribe(viewModel)
                .doOnSubscribe(disposable -> {
                    if (showDialog) {
                        viewModel.showDialog("加载中...");
                    }
                })
                .subscribe(new AbsAPICallback<>(httpInterface,viewModel));
    }

    public void delete(String link,boolean showDialog, HttpInterface httpInterface) {
        BaseClient.getApi(baseUrl).baseDelete(headers, link)
                .subscribeOn(Schedulers.io())
                .observeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .doOnSubscribe(viewModel)
                .doOnSubscribe(disposable -> {
                    if (showDialog) {
                        viewModel.showDialog("加载中...");
                    }
                })
                .subscribe(new AbsAPICallback<>(httpInterface,viewModel));
    }

    public void put(String link,boolean showDialog, Map<String, Object> map, HttpInterface httpInterface) {
        JSONObject jsonObject = new JSONObject(map);
        RequestBody body = RequestBody.create(MediaType.parse("application/json; charset=utf-8"), jsonObject.toString());
        BaseClient.getApi(baseUrl).basePut(headers, link, body)
                .subscribeOn(Schedulers.io())
                .observeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .doOnSubscribe(viewModel)
                .doOnSubscribe(disposable -> {
                    if (showDialog) {
                        viewModel.showDialog("加载中...");
                    }
                })
                .subscribe(new AbsAPICallback<>(httpInterface,viewModel));
    }


    public void upload(String link,boolean showDialog, String param, String filepath, HttpInterface httpInterface) {
        if (TextUtils.isEmpty(filepath)) {
            return;
        }
        File file = new File(filepath);
        RequestBody requestBody = RequestBody.create(MediaType.parse("multipart/form-data"), file);
        MultipartBody.Part part = MultipartBody.Part.createFormData(param, file.getName(), requestBody);
        BaseClient.getApi(baseUrl).postFile(headers, link, part)
                .subscribeOn(Schedulers.io())
                .observeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .doOnSubscribe(viewModel)
                .doOnSubscribe(disposable -> {
                    if (showDialog) {
                        viewModel.showDialog("加载中...");
                    }
                })
                .subscribe(new AbsAPICallback<>(httpInterface,viewModel));
    }

    public void uploads(String link,boolean showDialog, String param, List<String> pathList, HttpInterface httpInterface) {
        List<MultipartBody.Part> parts = new ArrayList<>(pathList.size());
        for (String filePath : pathList) {
            if (TextUtils.isEmpty(filePath)) {
                return;
            }
            File file = new File(filePath);
            RequestBody requestBody = RequestBody.create(MediaType.parse("multipart/form-data"), file);
            MultipartBody.Part part = MultipartBody.Part.createFormData(param, file.getName(), requestBody);
            parts.add(part);
        }
        BaseClient.getApi(baseUrl).postFiles(headers, link, parts)
                .subscribeOn(Schedulers.io())
                .observeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .doOnSubscribe(viewModel)
                .doOnSubscribe(disposable -> {
                    if (showDialog) {
                        viewModel.showDialog("加载中...");
                    }
                })
                .subscribe(new AbsAPICallback<>(httpInterface,viewModel));
    }
}
