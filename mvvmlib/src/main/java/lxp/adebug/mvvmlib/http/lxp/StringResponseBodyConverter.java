package lxp.adebug.mvvmlib.http.lxp;


import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import lxp.adebug.mvvmlib.http.BaseResponse;
import lxp.adebug.mvvmlib.utils.GsonUtil;
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
            if (jsonObject.getInt("code") == 200 ) {
                return response;
            } else {
                BaseResponse errResponse = GsonUtil.parseJson(response, BaseResponse.class);
                throw new ResultException(errResponse.getCode(), errResponse.getMessage());
            }
        } catch (JSONException e) {
            e.printStackTrace();
            return null;
        } finally {
            value.close();
        }
    }
}
