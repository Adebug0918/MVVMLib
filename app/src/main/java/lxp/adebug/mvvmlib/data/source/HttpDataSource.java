package lxp.adebug.mvvmlib.data.source;

import lxp.adebug.mvvmlib.entity.DemoEntity;

import io.reactivex.Observable;
import lxp.adebug.mvvmlib.http.BaseResponse;

/**
 * Created by goldze on 2019/3/26.
 */
public interface HttpDataSource {
    //模拟登录
    Observable<Object> login();

    //模拟上拉加载
    Observable<DemoEntity> loadMore();

    Observable<BaseResponse<DemoEntity>> demoGet();

    Observable<BaseResponse<DemoEntity>> demoPost(String catalog);


}
