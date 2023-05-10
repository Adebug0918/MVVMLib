package lxp.adebug.mvvmlib.bus;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

import lxp.adebug.mvvmlib.binding.command.BindingAction;
import lxp.adebug.mvvmlib.binding.command.BindingConsumer;

/**
 * About : kelin的Messenger
 */
public class Messenger {

    private static Messenger defaultInstance;

    private HashMap<Type, List<WeakActionAndToken>> recipientsOfSubclassesAction;

    private HashMap<Type, List<WeakActionAndToken>> recipientsStrictAction;

    public static Messenger getDefault() {
        if (defaultInstance == null) {
            defaultInstance = new Messenger();
        }
        return defaultInstance;
    }


    public static void overrideDefault(Messenger newWeakMessenger) {
        defaultInstance = newWeakMessenger;
    }

    public static void reset() {
        defaultInstance = null;
    }

    /**
     * @param recipient 接收者，如果在活动中注册，接收者总是设置“this”，
     *                  和 onDestroy 中的“WeakMessenger.getDefault().unregister(this)”，如果在 ViewModel 中，
     *                  你也可以在 Activity 上下文中注册，也可以在 onDestroy 中注销。
     * @param action    对接收到的消息做一些事情
     */
    public void register(Object recipient, BindingAction action) {
        register(recipient, null, false, action);
    }

    /**
     * @param recipient                 接收者，如果在活动中注册，接收者总是设置“this”，
     *                                  和 onDestroy 中的“WeakMessenger.getDefault().unregister(this)”，如果在 ViewModel 中，
     *                                  你也可以在 Activity 上下文中注册，也可以在 onDestroy 中注销。
     * @param receiveDerivedMessagesToo 接收者的派生类是否可以接收消息
     * @param action                    对接收到的消息做一些事情
     */
    public void register(Object recipient, boolean receiveDerivedMessagesToo, BindingAction action) {
        register(recipient, null, receiveDerivedMessagesToo, action);
    }

    /**
     * @param recipient 接收者，如果在活动中注册，接收者总是设置“this”，
     *                  和 onDestroy 中的“WeakMessenger.getDefault().unregister(this)”，如果在 ViewModel 中，
     *                  你也可以在 Activity 上下文中注册，也可以在 onDestroy 中注销。
     * @param token     注册一个唯一的token，当一个messenger发送一条带有相同token的消息时，它将要接收此消息
     * @param action    对接收到的消息做一些事情
     */
    public void register(Object recipient, Object token, BindingAction action) {
        register(recipient, token, false, action);
    }

    /**
     * @param recipient                 接收者，如果在活动中注册，接收者总是设置“this”，
     *                                  和 onDestroy 中的“WeakMessenger.getDefault().unregister(this)”，如果在 ViewModel 中，
     *                                  你也可以在 Activity 上下文中注册，也可以在 onDestroy 中注销。
     * @param token                     注册一个唯一的token，当一个messenger发送一条带有相同token的消息时，它将要接收此消息
     * @param receiveDerivedMessagesToo 接收者的派生类是否可以接收消息
     * @param action                    对接收到的消息做一些事情
     */
    public void register(Object recipient, Object token, boolean receiveDerivedMessagesToo, BindingAction action) {

        Type messageType = NotMsgType.class;

        HashMap<Type, List<WeakActionAndToken>> recipients;

        if (receiveDerivedMessagesToo) {
            if (recipientsOfSubclassesAction == null) {
                recipientsOfSubclassesAction = new HashMap<Type, List<WeakActionAndToken>>();
            }

            recipients = recipientsOfSubclassesAction;
        } else {
            if (recipientsStrictAction == null) {
                recipientsStrictAction = new HashMap<Type, List<WeakActionAndToken>>();
            }

            recipients = recipientsStrictAction;
        }

        List<WeakActionAndToken> list;

        if (!recipients.containsKey(messageType)) {
            list = new ArrayList<WeakActionAndToken>();
            recipients.put(messageType, list);
        } else {
            list = recipients.get(messageType);
        }

        WeakAction weakAction = new WeakAction(recipient, action);

        WeakActionAndToken item = new WeakActionAndToken(weakAction, token);
        list.add(item);
        cleanup();
    }

    /**
     * @param recipient{}
     * @param tClass      T 类
     * @param action      这个动作有一个 tClass 类型的参数
     * @param <T>         消息数据类型
     */
    public <T> void register(Object recipient, Class<T> tClass, BindingConsumer<T> action) {
        register(recipient, null, false, action, tClass);
    }

    /**
     * see {}
     *
     * @param recipient                 消息接收者
     * @param receiveDerivedMessagesToo 收件人的派生类是否可以收到消息
     * @param tClass                    T类
     * @param action                    这个动作有一个 tClass 类型的参数
     * @param <T>                       消息数据类型
     */
    public <T> void register(Object recipient, boolean receiveDerivedMessagesToo, Class<T> tClass, BindingConsumer<T> action) {
        register(recipient, null, receiveDerivedMessagesToo, action, tClass);
    }

    /**
     * see {}
     *
     * @param recipient 消息接收者
     * @param token     使用唯一令牌注册，当信使发送具有相同令牌的消息时，它将要收到这条信息
     * @param tClass    BindingConsumer 的 T 类
     * @param action    这个动作有一个 tClass 类型的参数
     * @param <T>       消息数据类型
     */
    public <T> void register(Object recipient, Object token, Class<T> tClass, BindingConsumer<T> action) {
        register(recipient, token, false, action, tClass);
    }

    /**
     * see {}
     *
     * @param recipient                 消息接收者
     * @param token                     使用唯一令牌注册，当信使发送具有相同令牌的消息时，它将要收到这条信息
     * @param receiveDerivedMessagesToo 收件人的派生类是否可以收到消息
     * @param action                    这个动作有一个 tClass 类型的参数
     * @param tClass                    BindingConsumer 的 T 类
     * @param <T>                       消息数据类型
     */
    public <T> void register(Object recipient, Object token, boolean receiveDerivedMessagesToo, BindingConsumer<T> action, Class<T> tClass) {

        Type messageType = tClass;

        HashMap<Type, List<WeakActionAndToken>> recipients;

        if (receiveDerivedMessagesToo) {
            if (recipientsOfSubclassesAction == null) {
                recipientsOfSubclassesAction = new HashMap<Type, List<WeakActionAndToken>>();
            }

            recipients = recipientsOfSubclassesAction;
        } else {
            if (recipientsStrictAction == null) {
                recipientsStrictAction = new HashMap<Type, List<WeakActionAndToken>>();
            }

            recipients = recipientsStrictAction;
        }

        List<WeakActionAndToken> list;

        if (!recipients.containsKey(messageType)) {
            list = new ArrayList<WeakActionAndToken>();
            recipients.put(messageType, list);
        } else {
            list = recipients.get(messageType);
        }

        WeakAction weakAction = new WeakAction<T>(recipient, action);

        WeakActionAndToken item = new WeakActionAndToken(weakAction, token);
        list.add(item);
        cleanup();
    }


    private void cleanup() {
        cleanupList(recipientsOfSubclassesAction);
        cleanupList(recipientsStrictAction);
    }

    /**
     * @param token 使用唯一令牌发送，当接收方使用相同令牌注册时，它将收到这条信息
     */
    public void sendNoMsg(Object token) {
        sendToTargetOrType(null, token);
    }

    /**
     * 直接发送给收件人没有任何消息
     *
     * @param target WeakMessenger.getDefault().register(this, ..)
     *               在活动中，如果目标设置为活动它会收到消息
     */
    public void sendNoMsgToTarget(Object target) {
        sendToTargetOrType(target.getClass(), null);
    }

    /**
     * 使用令牌向目标发送消息，当接收者使用相同的令牌注册时，它将
     * 收到这条信息
     *
     * @param token  使用唯一令牌发送，当接收方使用相同令牌注册时，它将
     *               接收此消息
     * @param target 直接发送给收件人没有任何消息，
     *               WeakMessenger.getDefault().register(this, ..) 在一个活动中，如果目标设置了这个活动
     *               它会收到消息
     */
    public void sendNoMsgToTargetWithToken(Object token, Object target) {
        sendToTargetOrType(target.getClass(), token);
    }

    /**
     * 发送T的消息类型，所有接收者都能收到该消息
     *
     * @param message 任何对象都可以是消息
     * @param <T>       消息数据类型
     */
    public <T> void send(T message) {
        sendToTargetOrType(message, null, null);
    }

    /**
     * 发送T的消息类型，所有接收者都能收到该消息
     *
     * @param message 任何对象都可以是消息
     * @param token   使用唯一令牌发送，当接收方使用相同令牌注册时，它将
     *                收到这条信息
     * @param <T>     消息数据类型
     */
    public <T> void send(T message, Object token) {
        sendToTargetOrType(message, null, token);
    }

    /**
     * 直接发送消息给收件人
     *
     * @param message 任何对象都可以是消息
     * @param target  直接发送给收件人没有任何消息，
     *                WeakMessenger.getDefault().register(this, ..) 在一个活动中，如果目标设置了这个活动
     *                它会收到消息
     * @param <T>     消息数据类型
     * @param <R>     目标
     */
    public <T, R> void sendToTarget(T message, R target) {
        sendToTargetOrType(message, target.getClass(), null);
    }

    /**
     * 注销接收器，例如：
     * WeakMessenger.getDefault().unregister(this)" 在 onDestroy 中的 Activity 是需要避免的
     * 到内存泄漏！
     *
     *
     * @param recipient 消息接收者
     */
    public void unregister(Object recipient) {
        unregisterFromLists(recipient, recipientsOfSubclassesAction);
        unregisterFromLists(recipient, recipientsStrictAction);
        cleanup();
    }


    public <T> void unregister(Object recipient, Object token) {
        unregisterFromLists(recipient, token, null, recipientsStrictAction);
        unregisterFromLists(recipient, token, null, recipientsOfSubclassesAction);
        cleanup();
    }


    private static <T> void sendToList(
            T message,
            Collection<WeakActionAndToken> list,
            Type messageTargetType,
            Object token) {
        if (list != null) {
            // 克隆以防止人们在“接收消息”方法中注册
            // 错误修正消息 BL0004.007
            ArrayList<WeakActionAndToken> listClone = new ArrayList<>();
            listClone.addAll(list);

            for (WeakActionAndToken item : listClone) {
                WeakAction executeAction = item.getAction();
                if (executeAction != null
                        && item.getAction().isLive()
                        && item.getAction().getTarget() != null
                        && (messageTargetType == null
                        || item.getAction().getTarget().getClass() == messageTargetType
                        || classImplements(item.getAction().getTarget().getClass(), messageTargetType))
                        && ((item.getToken() == null && token == null)
                        || item.getToken() != null && item.getToken().equals(token))) {
                    executeAction.execute(message);
                }
            }
        }
    }

    private static void unregisterFromLists(Object recipient, HashMap<Type, List<WeakActionAndToken>> lists) {
        if (recipient == null
                || lists == null
                || lists.size() == 0) {
            return;
        }
        synchronized (lists) {
            for (Type messageType : lists.keySet()) {
                for (WeakActionAndToken item : lists.get(messageType)) {
                    WeakAction weakAction = item.getAction();

                    if (weakAction != null
                            && recipient == weakAction.getTarget()) {
                        weakAction.markForDeletion();
                    }
                }
            }
        }
        cleanupList(lists);
    }

    private static <T> void unregisterFromLists(
            Object recipient,
            BindingConsumer<T> action,
            HashMap<Type, List<WeakActionAndToken>> lists,
            Class<T> tClass) {
        Type messageType = tClass;

        if (recipient == null
                || lists == null
                || lists.size() == 0
                || !lists.containsKey(messageType)) {
            return;
        }

        synchronized (lists) {
            for (WeakActionAndToken item : lists.get(messageType)) {
                WeakAction<T> weakActionCasted = (WeakAction<T>) item.getAction();

                if (weakActionCasted != null
                        && recipient == weakActionCasted.getTarget()
                        && (action == null
                        || action == weakActionCasted.getBindingConsumer())) {
                    item.getAction().markForDeletion();
                }
            }
        }
    }

    private static void unregisterFromLists(
            Object recipient,
            BindingAction action,
            HashMap<Type, List<WeakActionAndToken>> lists
    ) {
        Type messageType = NotMsgType.class;

        if (recipient == null
                || lists == null
                || lists.size() == 0
                || !lists.containsKey(messageType)) {
            return;
        }

        synchronized (lists) {
            for (WeakActionAndToken item : lists.get(messageType)) {
                WeakAction weakActionCasted = (WeakAction) item.getAction();

                if (weakActionCasted != null
                        && recipient == weakActionCasted.getTarget()
                        && (action == null
                        || action == weakActionCasted.getBindingAction())) {
                    item.getAction().markForDeletion();
                }
            }
        }
    }


    private static <T> void unregisterFromLists(
            Object recipient,
            Object token,
            BindingConsumer<T> action,
            HashMap<Type, List<WeakActionAndToken>> lists, Class<T> tClass) {
        Type messageType = tClass;

        if (recipient == null
                || lists == null
                || lists.size() == 0
                || !lists.containsKey(messageType)) {
            return;
        }

        synchronized (lists) {
            for (WeakActionAndToken item : lists.get(messageType)) {
                WeakAction<T> weakActionCasted = (WeakAction<T>) item.getAction();

                if (weakActionCasted != null
                        && recipient == weakActionCasted.getTarget()
                        && (action == null
                        || action == weakActionCasted.getBindingConsumer())
                        && (token == null
                        || token.equals(item.getToken()))) {
                    item.getAction().markForDeletion();
                }
            }
        }
    }

    private static void unregisterFromLists(
            Object recipient,
            Object token,
            BindingAction action,
            HashMap<Type, List<WeakActionAndToken>> lists) {
        Type messageType = NotMsgType.class;

        if (recipient == null
                || lists == null
                || lists.size() == 0
                || !lists.containsKey(messageType)) {
            return;
        }

        synchronized (lists) {
            for (WeakActionAndToken item : lists.get(messageType)) {
                WeakAction weakActionCasted = (WeakAction) item.getAction();

                if (weakActionCasted != null
                        && recipient == weakActionCasted.getTarget()
                        && (action == null
                        || action == weakActionCasted.getBindingAction())
                        && (token == null
                        || token.equals(item.getToken()))) {
                    item.getAction().markForDeletion();
                }
            }
        }
    }

    private static boolean classImplements(Type instanceType, Type interfaceType) {
        if (interfaceType == null
                || instanceType == null) {
            return false;
        }
        Class[] interfaces = ((Class) instanceType).getInterfaces();
        for (Class currentInterface : interfaces) {
            if (currentInterface == interfaceType) {
                return true;
            }
        }

        return false;
    }

    private static void cleanupList(HashMap<Type, List<WeakActionAndToken>> lists) {
        if (lists == null) {
            return;
        }
        for (Iterator it = lists.entrySet().iterator(); it.hasNext(); ) {
            Object key = it.next();
            List<WeakActionAndToken> itemList = lists.get(key);
            if (itemList != null) {
                for (WeakActionAndToken item : itemList) {
                    if (item.getAction() == null
                            || !item.getAction().isLive()) {
                        itemList.remove(item);
                    }
                }
                if (itemList.size() == 0) {
                    lists.remove(key);
                }
            }
        }
    }

    private void sendToTargetOrType(Type messageTargetType, Object token) {
        Class messageType = NotMsgType.class;
        if (recipientsOfSubclassesAction != null) {
            // 克隆以防止人们在“接收消息”方法中注册
            // 错误修正消息 BL0008.002
            // var listClone = recipientsOfSubclassesAction.Keys.Take(_recipientsOfSubclassesAction.Count()).ToList();
            List<Type> listClone = new ArrayList<>();
            listClone.addAll(recipientsOfSubclassesAction.keySet());
            for (Type type : listClone) {
                List<WeakActionAndToken> list = null;

                if (messageType == type
                        || ((Class) type).isAssignableFrom(messageType)
                        || classImplements(messageType, type)) {
                    list = recipientsOfSubclassesAction.get(type);
                }

                sendToList(list, messageTargetType, token);
            }
        }

        if (recipientsStrictAction != null) {
            if (recipientsStrictAction.containsKey(messageType)) {
                List<WeakActionAndToken> list = recipientsStrictAction.get(messageType);
                sendToList(list, messageTargetType, token);
            }
        }

        cleanup();
    }

    private static void sendToList(
            Collection<WeakActionAndToken> list,
            Type messageTargetType,
            Object token) {
        if (list != null) {
            // 克隆以防止人们在“接收消息”方法中注册
            // 错误修正消息 BL0004.007
            ArrayList<WeakActionAndToken> listClone = new ArrayList<>();
            listClone.addAll(list);

            for (WeakActionAndToken item : listClone) {
                WeakAction executeAction = item.getAction();
                if (executeAction != null
                        && item.getAction().isLive()
                        && item.getAction().getTarget() != null
                        && (messageTargetType == null
                        || item.getAction().getTarget().getClass() == messageTargetType
                        || classImplements(item.getAction().getTarget().getClass(), messageTargetType))
                        && ((item.getToken() == null && token == null)
                        || item.getToken() != null && item.getToken().equals(token))) {
                    executeAction.execute();
                }
            }
        }
    }

    private <T> void sendToTargetOrType(T message, Type messageTargetType, Object token) {
        Class messageType = message.getClass();


        if (recipientsOfSubclassesAction != null) {
            // 克隆以防止人们在“接收消息”方法中注册
            // 错误修正消息 BL0008.002
            // var listClone = recipientsOfSubclassesAction.Keys.Take(_recipientsOfSubclassesAction.Count()).ToList();
            List<Type> listClone = new ArrayList<>();
            listClone.addAll(recipientsOfSubclassesAction.keySet());
            for (Type type : listClone) {
                List<WeakActionAndToken> list = null;

                if (messageType == type
                        || ((Class) type).isAssignableFrom(messageType)
                        || classImplements(messageType, type)) {
                    list = recipientsOfSubclassesAction.get(type);
                }

                sendToList(message, list, messageTargetType, token);
            }
        }

        if (recipientsStrictAction != null) {
            if (recipientsStrictAction.containsKey(messageType)) {
                List<WeakActionAndToken> list = recipientsStrictAction.get(messageType);
                sendToList(message, list, messageTargetType, token);
            }
        }

        cleanup();
    }

    private class WeakActionAndToken {
        private WeakAction action;
        private Object token;

        public WeakActionAndToken(WeakAction action, Object token) {
            this.action = action;
            this.token = token;
        }

        public WeakAction getAction() {
            return action;
        }

        public void setAction(WeakAction action) {
            this.action = action;
        }

        public Object getToken() {
            return token;
        }

        public void setToken(Object token) {
            this.token = token;
        }
    }

    public static class NotMsgType {

    }
}
