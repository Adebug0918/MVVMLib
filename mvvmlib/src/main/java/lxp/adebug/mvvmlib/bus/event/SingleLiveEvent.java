/*
 *  Copyright 2017 Google Inc.
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 */

package lxp.adebug.mvvmlib.bus.event;

import android.util.Log;

import java.util.concurrent.atomic.AtomicBoolean;

import androidx.annotation.MainThread;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Observer;

/**
 * 一个生命周期感知的可观察对象，在订阅后只发送新的更新，用于诸如
 * 导航和 Snackbar 消息。
 * <p>
 * 这避免了事件的常见问题：在配置更改（如轮换）时更新
 * 如果观察者处于活动状态，则可以发出。 这个 LiveData 只在有
 * 显式调用 setValue() 或 call()。
 * <p>
 * 请注意，只有一名观察员会收到更改通知。
 */
public class SingleLiveEvent<T> extends MutableLiveData<T> {
    private static final String TAG = "SingleLiveEvent";

    private final AtomicBoolean mPending = new AtomicBoolean(false);

    @MainThread
    public void observe(@NonNull LifecycleOwner owner, @NonNull final Observer<? super T> observer) {

        if (hasActiveObservers()) {
            Log.w(TAG, "多个观察员注册，但只有一个会收到更改通知。");
        }

        // 观察内部的 MutableLiveData
        super.observe(owner, new Observer<T>() {
            @Override
            public void onChanged(@Nullable T t) {
                if (mPending.compareAndSet(true, false)) {
                    observer.onChanged(t);
                }
            }
        });
    }

    @MainThread
    public void setValue(@Nullable T t) {
        mPending.set(true);
        super.setValue(t);
    }

    /**
     * 用于 T 为 Void 的情况，以使调用更清晰。
     */
    @MainThread
    public void call() {
        setValue(null);
    }
}
