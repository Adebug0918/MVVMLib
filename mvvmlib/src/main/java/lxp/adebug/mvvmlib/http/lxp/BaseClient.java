package lxp.adebug.mvvmlib.http.lxp;

import android.content.Context;

import java.io.File;
import java.util.concurrent.TimeUnit;

import lxp.adebug.mvvmlib.http.HttpsUtils;
import lxp.adebug.mvvmlib.http.cookie.CookieJarImpl;
import lxp.adebug.mvvmlib.http.cookie.store.PersistentCookieStore;
import lxp.adebug.mvvmlib.http.interceptor.CacheInterceptor;
import lxp.adebug.mvvmlib.http.interceptor.logging.Level;
import lxp.adebug.mvvmlib.http.interceptor.logging.LoggingInterceptor;
import lxp.adebug.mvvmlib.utils.KLog;
import lxp.adebug.mvvmlib.utils.Utils;
import me.goldze.mvvmhabit.BuildConfig;
import okhttp3.Cache;
import okhttp3.ConnectionPool;
import okhttp3.OkHttpClient;
import okhttp3.internal.platform.Platform;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;

/**
 * @author Adebug
 */
public class BaseClient {

    private static Context mContext = Utils.getContext();

    private static Retrofit mRetrofit;
    private static OkHttpClient mOkHttpClient;

    private final static int CONNECT_TIMEOUT = 30;
    private final static int READ_TIMEOUT = 30;
    private final static int WRITE_TIMEOUT = 30;

    private static Cache cache = null;
    private static File httpCacheDirectory;
    private static final int CACHE_TIMEOUT = 100 * 1024 * 1024;

    private static OkHttpClient genericClient() {
        if (httpCacheDirectory == null) {
            httpCacheDirectory = new File(mContext.getCacheDir(), "lxp_cache");
        }
        try {
            if (cache == null) {
                cache = new Cache(httpCacheDirectory, CACHE_TIMEOUT);
            }
        } catch (Exception e) {
            KLog.e("Could not create http cache", e);
        }
        HttpsUtils.SSLParams sslParams = HttpsUtils.getSslSocketFactory();
        OkHttpClient.Builder builder = new OkHttpClient.Builder()
                .cookieJar(new CookieJarImpl(new PersistentCookieStore(mContext)))
                .addInterceptor(new CacheInterceptor(mContext))
                .sslSocketFactory(sslParams.sSLSocketFactory, sslParams.trustManager)
                .readTimeout(READ_TIMEOUT, TimeUnit.SECONDS)
                .writeTimeout(WRITE_TIMEOUT, TimeUnit.SECONDS)
                .connectTimeout(CONNECT_TIMEOUT, TimeUnit.SECONDS)
                .retryOnConnectionFailure(true)
                .addInterceptor(new LoggingInterceptor
                        .Builder()
                        .loggable(BuildConfig.DEBUG)
                        .setLevel(Level.BASIC)
                        .log(Platform.INFO)
                        .request("Request")
                        .response("Response")
                        .addHeader("log-header", "I am the log request header.")
                        .build()
                )
                .connectionPool(new ConnectionPool(8, 15, TimeUnit.SECONDS));
        mOkHttpClient = builder.build();
        return mOkHttpClient;
    }

    public static BaseApi getApi(String baseUrl) {
        return getRetrofit(baseUrl).create(BaseApi.class);
    }

    private static Retrofit getRetrofit(String baseUrl) {
        if (null == mRetrofit) {
            if (null == mOkHttpClient) {
                mOkHttpClient = genericClient();
            }
            mRetrofit = new Retrofit.Builder()
                    .baseUrl(baseUrl)
                    .addConverterFactory(StringConverterFactory.create())
                    .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                    .client(mOkHttpClient)
                    .build();
        }
        return mRetrofit;
    }
}
