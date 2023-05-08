package lxp.adebug.mvvmlib.utils;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;

import java.util.List;

import lxp.adebug.mvvmlib.utils.defaults.DoubleDefault0Adapter;
import lxp.adebug.mvvmlib.utils.defaults.IntegerDefault0Adapter;
import lxp.adebug.mvvmlib.utils.defaults.LongDefault0Adapter;
import lxp.adebug.mvvmlib.utils.defaults.StringDefault0Adapter;


/**
 * @author lxp
 * @date 2018/5/7
 */

public class GsonUtil {


    private static Gson getGson() {
        Gson gson = new GsonBuilder()
                .registerTypeAdapter(Integer.class, new IntegerDefault0Adapter())
                .registerTypeAdapter(int.class, new IntegerDefault0Adapter())
                .registerTypeAdapter(Double.class, new DoubleDefault0Adapter())
                .registerTypeAdapter(double.class, new DoubleDefault0Adapter())
                .registerTypeAdapter(Long.class, new LongDefault0Adapter())
                .registerTypeAdapter(String.class, new StringDefault0Adapter())
                .registerTypeAdapter(long.class, new LongDefault0Adapter())
                .create();
        return gson;
    }

    public static <T> T parseJson(String jsonData, Class<T> type) {
        T result = getGson().fromJson(jsonData, type);
        return result;
    }

    public static String parseEmpty(Object obj) {
        return getGson().toJson(obj);
    }

    public static <T> List<T> json2List(String jsonData, Class<T> type) {
        return getGson().fromJson(jsonData, new TypeToken<List<T>>() {
        }.getType());
    }

    public static String list2Json(List<Object> data) {
        return getGson().toJson(data);
    }
}
