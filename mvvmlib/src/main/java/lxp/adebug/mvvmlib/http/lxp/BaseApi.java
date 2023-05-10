package lxp.adebug.mvvmlib.http.lxp;


import java.util.List;
import java.util.Map;

import io.reactivex.Observable;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.FieldMap;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.HeaderMap;
import retrofit2.http.Headers;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Part;
import retrofit2.http.Path;
import retrofit2.http.QueryMap;
import retrofit2.http.Streaming;
import retrofit2.http.Url;



/**
 *
 * @author lxp
 * @date 2016/4/19
 */
public interface BaseApi {


    @GET("{link}")
    Observable<String> baseGet(@HeaderMap Map<String, Object> headers, @Path(value = "link", encoded = true) String link);

    @GET("{link}")
    Observable<String> baseGet(@HeaderMap Map<String, Object> headers, @Path(value = "link", encoded = true) String link, @QueryMap Map<String, Object> map);

    @Headers({"Content-Type: application/json", "Accept: application/json"})
    @GET("{link}")
    Observable<String> baseGet(@HeaderMap Map<String, Object> headers, @Path(value = "link", encoded = true) String link, @Body RequestBody requestBody);

    @POST("{link}")
    Observable<String> basePost(@HeaderMap Map<String, Object> headers, @Path(value = "link", encoded = true) String link);

    @Headers({"Content-Type: application/json", "Accept: application/json"})
    @POST("{link}")
    Observable<String> basePost(@HeaderMap Map<String, Object> headers, @Path(value = "link", encoded = true) String link, @Body RequestBody requestBody);

    @FormUrlEncoded
    @POST("{link}")
    Observable<String> basePost(@HeaderMap Map<String, Object> headers, @Path(value = "link", encoded = true) String link, @FieldMap Map<String, Object> map);

    @Multipart
    @POST("{link}")
    Observable<String> postFile(@HeaderMap Map<String, Object> headers, @Path(value = "link", encoded = true) String link, @Part() MultipartBody.Part file);

    @Multipart
    @POST("{link}")
    Observable<String> postFiles(@HeaderMap Map<String, Object> headers, @Path(value = "link", encoded = true) String link, @Part() List<MultipartBody.Part> parts);

    @Streaming
    Call<ResponseBody> downloadFile(@Url String fileUrl);

    @DELETE("{link}")
    Observable<String> baseDelete(@HeaderMap Map<String, Object> headers, @Path(value = "link", encoded = true) String link);

    @PUT("{link}")
    Observable<String> basePut(@HeaderMap Map<String, Object> headers, @Path(value = "link", encoded = true) String link, @Body RequestBody requestBody);

}
