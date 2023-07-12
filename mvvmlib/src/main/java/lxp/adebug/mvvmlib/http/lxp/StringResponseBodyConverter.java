package lxp.adebug.mvvmlib.http.lxp;


import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import lxp.adebug.mvvmlib.base.BaseApplication;
import okhttp3.ResponseBody;
import retrofit2.Converter;

/**
 * @author : lxp
 * @description ：
 * @date : 2022/4/11
 * @email : 18132001989@189.cn
 */
public class StringResponseBodyConverter implements Converter<ResponseBody, String> {
    @Override
    public String convert(ResponseBody value) throws IOException {
        try {
            String response = value.string();
            JSONObject jsonObject = new JSONObject(response);
            if (jsonObject.getInt(BaseApplication.fieldCode) == BaseApplication.httpCode) {
                return response;
            } else {
                JSONObject errJsonObject = new JSONObject(response);
                throw new ResultException(errJsonObject.getInt(BaseApplication.fieldCode), errJsonObject.getString(BaseApplication.fieldMessage));
            }
        } catch (JSONException e) {
            e.printStackTrace();
            return null;
        } finally {
            value.close();
        }
    }
}
