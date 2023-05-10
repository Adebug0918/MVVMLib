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

import androidx.annotation.Nullable;
import androidx.annotation.StringRes;
import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.Observer;

/**
 * 翻译结果
 * 用于 Snackbar 消息的 SingleLiveEvent。 像 {@link SingleLiveEvent} 但也可以防止
 * 空消息并使用自定义观察者。
 * <p>
 * 请注意，只有一名观察员会收到更改通知。
 */
public class SnackbarMessage extends SingleLiveEvent<Integer> {

    public void observe(LifecycleOwner owner, final SnackbarObserver observer) {
        super.observe(owner, new Observer<Integer>() {
            @Override
            public void onChanged(@Nullable Integer t) {
                if (t == null) {
                    return;
                }
                observer.onNewMessage(t);
            }
        });
    }

    public interface SnackbarObserver {
        /**
         * 当有新消息显示时调用。
         * @param snackbarMessageResourceId 新消息，非空。
         */
        void onNewMessage(@StringRes int snackbarMessageResourceId);
    }

}
