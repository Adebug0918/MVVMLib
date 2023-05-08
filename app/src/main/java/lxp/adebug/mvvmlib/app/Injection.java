package lxp.adebug.mvvmlib.app;

import lxp.adebug.mvvmlib.data.DemoRepository;
import lxp.adebug.mvvmlib.data.source.HttpDataSource;
import lxp.adebug.mvvmlib.data.source.LocalDataSource;
import lxp.adebug.mvvmlib.data.source.http.HttpDataSourceImpl;
import lxp.adebug.mvvmlib.data.source.http.service.DemoApiService;
import lxp.adebug.mvvmlib.data.source.local.LocalDataSourceImpl;
import lxp.adebug.mvvmlib.utils.RetrofitClient;


/**
 * 注入全局的数据仓库，可以考虑使用Dagger2。（根据项目实际情况搭建，千万不要为了架构而架构）
 * Created by goldze on 2019/3/26.
 */
public class Injection {
    public static DemoRepository provideDemoRepository() {
        //网络API服务
        DemoApiService apiService = RetrofitClient.getInstance().create(DemoApiService.class);
        //网络数据源
        HttpDataSource httpDataSource = HttpDataSourceImpl.getInstance(apiService);
        //本地数据源
        LocalDataSource localDataSource = LocalDataSourceImpl.getInstance();
        //两条分支组成一个数据仓库
        return DemoRepository.getInstance(httpDataSource, localDataSource);
    }
}
