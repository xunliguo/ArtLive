package com.example.rh.artlive.util;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v4.app.FragmentManager;

import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.StringCallback;
import com.zhy.http.okhttp.request.RequestCall;

import java.util.LinkedHashMap;
import java.util.Map;

import okhttp3.Call;
import okhttp3.MediaType;
import okhttp3.Request;


/**
 * 网络连接工具类
 */
public class HttpUtil {

    private static Map<String, String> headers;

    /**
     * 获取http请求参数
     *
     * @return http请求params 集合
     */
    public static Map<String, String> getHttpParams() {
        Map<String, String> params = new LinkedHashMap<>();
        return params;
    }

    /**
     * 获取http请求参数hearder
     *
     * @return http请求hearder 集合
     */
    public static Map<String, String> getHttpHeaders() {
        if (headers == null) {
            headers = new LinkedHashMap<>();
        }
        return headers;
    }


    public static void setHttpHeaders(Map<String, String> header) {
        headers = header;
    }


    /**
     * get方式网络请求
     *
     * @param url      网络请求地址
     * @param callback 网络请求回调接口
     * @param tag      请求标签
     * @return 请求回调对象
     */
    public static RequestCall getHttpRequst(Context context, String url, StringCallback callback, Object tag, FragmentManager fragmentManager) {
        return getHttpRequstProgess(context, null, url, callback, tag, fragmentManager);
    }


    /**
     * post方式网络请求
     *
     * @param url      网络请求地址
     * @param callback 网络请求回调接口
     * @param tag      请求标签
     * @return 请求回调对象
     */
    public static RequestCall postHttpRequst(String url, Map<String, String> params, StringCallback callback, Object tag) {
        return postHttpRequstProgess(null, null, url, params, callback, tag, null);
    }


    /**
     * post方式网络请求 有加载进图提示
     *
     * @param context    上下文
     * @param loadingMsg 加载中提示文字
     * @param url        网络请求地址
     * @param callback   网络请求回调接口
     * @param tag        请求标签
     * @return 请求回调对象
     */
    public static RequestCall postHttpRequstProgess(final Context context, final String loadingMsg, String url, final Map<String, String> params, final StringCallback callback, final Object tag, final FragmentManager fragmentManager) {

        //设置 http请求参数
        final RequestCall requestCall = OkHttpUtils.post()
                .url(url)
                .params(params)
                .headers(getHttpHeaders())
                .tag(tag)
                .build()
                .connTimeOut(120000)
                .writeTimeOut(240000)
                .readTimeOut(120000);

        OkHttpUtils.getInstance().cancelTag(tag);

        //执行http请求（同步）
        requestCall.execute(new StringCallback() {
            LoadingDialog loadingDialog;

            @Override
            public void onBefore(Request request) {
                super.onBefore(request);
                if (context != null && fragmentManager != null) {
                    loadingDialog = new LoadingDialog();
//                    loadingDialog.setCancelable(false);
                    loadingDialog.show(fragmentManager, loadingMsg);
                }
            }

            @Override
            public void onAfter() {
                super.onAfter();
                if (loadingDialog != null && fragmentManager != null && context != null) {
                    loadingDialog.dismissAllowingStateLoss();
                }
            }

            @Override
            public void onError(Call call, Exception e) {
                if (context != null) {
                    if(!"Socket closed".equals(e.getMessage())){
//                        ToastUtil.showToast(context, "网络不给力,请稍后再试");
                    }
                    e.printStackTrace();
                    callback.onError(call, e);
                    if (loadingDialog != null) {
                        loadingDialog.dismiss();
                    }
                }
            }

            @Override
            public void onResponse(String response) {
                callback.onResponse(response);
            }

        });

        return requestCall;
    }

    public static RequestCall postHttpRequstNoToast(final Context context, final String loadingMsg, String url, final Map<String, String> params, final StringCallback callback, final Object tag, final FragmentManager fragmentManager) {

        //设置 http请求参数
        final RequestCall requestCall = OkHttpUtils.post()
                .url(url)
                .params(params)
                .headers(getHttpHeaders())
                .tag(tag)
                .build()
                .connTimeOut(120000)
                .writeTimeOut(240000)
                .readTimeOut(120000);

        OkHttpUtils.getInstance().cancelTag(tag);

        //执行http请求（同步）
        requestCall.execute(new StringCallback() {
            LoadingDialog loadingDialog;

            @Override
            public void onBefore(Request request) {
                super.onBefore(request);
                if (context != null && fragmentManager != null) {
                    loadingDialog = new LoadingDialog();
//                    loadingDialog.setCancelable(false);
                    loadingDialog.show(fragmentManager, loadingMsg);
                }
            }

            @Override
            public void onAfter() {
                super.onAfter();
                if (loadingDialog != null && fragmentManager != null && context != null) {
                    loadingDialog.dismissAllowingStateLoss();
                }
            }

            @Override
            public void onError(Call call, Exception e) {
                if (context != null) {
                    e.printStackTrace();
                    callback.onError(call, e);
                    if (loadingDialog != null) {
                        loadingDialog.dismiss();
                    }
                }
            }

            @Override
            public void onResponse(String response) {
                callback.onResponse(response);
            }

        });

        return requestCall;
    }

    /**
     * 传递json作为参数
     *
     * @param context
     * @param loadingMsg
     * @param url
     * @param paramsJSON
     * @param callback
     * @param tag
     * @param fragmentManager
     * @return
     */
    public static RequestCall postHttpRequstProgess(final Context context, final String loadingMsg, String url, String paramsJSON, final StringCallback callback, final Object tag, final FragmentManager fragmentManager) {

        MediaType JSON = MediaType.parse("application/json; charset=utf-8");


        //设置 http请求参数
        final RequestCall requestCall = OkHttpUtils.postString()
                .url(url)
                .headers(getHttpHeaders())
                .mediaType(JSON)
                .content(paramsJSON)
                .tag(tag)
                .build()
                .connTimeOut(120000)
                .writeTimeOut(240000)
                .readTimeOut(120000);

        OkHttpUtils.getInstance().cancelTag(tag);

        //执行http请求（同步）
        requestCall.execute(new StringCallback() {
            LoadingDialog loadingDialog;

            @Override
            public void onBefore(Request request) {
                super.onBefore(request);
                if (context != null && fragmentManager != null) {
                    loadingDialog = new LoadingDialog();
                    loadingDialog.show(fragmentManager, loadingMsg);
//                    loadingDialog.setCancelable(false);
                }
            }

            @Override
            public void onAfter() {
                super.onAfter();
                if (loadingDialog != null && fragmentManager != null) {
                    loadingDialog.dismissAllowingStateLoss();
                }
            }

            @Override
            public void onError(Call call, Exception e) {
                if(!"Socket closed".equals(e.getMessage())){
                    ToastUtil.showToast(context, "网络不给力,请稍后再试");
                }
                e.printStackTrace();
                callback.onError(call, e);
                if (loadingDialog != null) {
                    loadingDialog.dismissAllowingStateLoss();
                }
            }

            @Override
            public void onResponse(String response) {
                callback.onResponse(response);
            }

        });

        return requestCall;
    }


    /**
     * get方式网络请求 有加载进图提示
     *
     * @param context    上下文
     * @param loadingMsg 加载中提示文字
     * @param url        网络请求地址
     * @param callback   网络请求回调接口
     * @param tag        请求标签
     * @return 请求回调对象
     */
    public static RequestCall getHttpRequstProgess(final Context context, final String loadingMsg, String url, @NonNull final StringCallback callback, final Object tag, final FragmentManager fragmentManager) {

        final RequestCall requestCall = OkHttpUtils.get()
                .url(url)
                .headers(getHttpHeaders())
                .tag(tag)
                .build()
                .connTimeOut(120000)
                .writeTimeOut(240000)
                .readTimeOut(120000);

        OkHttpUtils.getInstance().cancelTag(tag);

        requestCall.execute(new StringCallback() {
            LoadingDialog loadingDialog;

            @Override
            public void onBefore(Request request) {
                super.onBefore(request);
                if (context != null && fragmentManager != null) {
                    loadingDialog = new LoadingDialog();
                    loadingDialog.show(fragmentManager, loadingMsg);
//                    loadingDialog.setCancelable(false);
                }
                callback.onBefore(request);
            }

            @Override
            public void onAfter() {
                super.onAfter();
                if (loadingDialog != null && fragmentManager != null) {
                    loadingDialog.dismissAllowingStateLoss();
                }
                callback.onAfter();
            }


            @Override
            public void onError(Call call, Exception e) {
//                ToastUtil.showToast(context,"网络不给力,请稍后再试");
                e.printStackTrace();
                callback.onError(call, e);
            }

            @Override
            public void onResponse(String response) {
                callback.onResponse(response);
            }

        });

        return requestCall;
    }


//    public static RequestCall postSendFileHttpRequstProgess(final Context context, final File file, final String fileName, final String loadingMsg, String params, String url, final StringCallback callback, final Object tag, final FragmentManager fragmentManager) {
//        // 先判断是否有网络
////        if (context != null && DialogUtil.CheckInternetDialog(context)) {
////            return null;
////        }
////        //设置 http请求参数
//        RequestCall requestCall = OkHttpUtils.post()//
//                .addFile("img", fileName, file)
//                .addParams(FinalString.API_TOKEN, params)
//                .url(url)
//                .headers(getHttpHeaders())//
//                .tag(tag)
//                .build();//
//        //执行http请求（同步）
//        requestCall.execute(new StringCallback() {
//            LoadingDialog loadingDialog;
//
//            @Override
//            public void onBefore(Request request) {
//                super.onBefore(request);
////                if (context != null && fragmentManager != null) {
////                    loadingDialog = new LoadingDialog();
////                    loadingDialog.show(fragmentManager, loadingMsg);
////                }
//            }
//
//            @Override
//            public void onAfter() {
//                super.onAfter();
////                Log.d(TAG, "onAfter");
////                if (loadingDialog != null && fragmentManager != null) {
////                    loadingDialog.dismiss();
////                }
//            }
//
//            @Override
//            public void onError(Call call, Exception e) {
//                ToastUtil.showToast("网络不给力");
////                Log.d(TAG, "onError");
//                e.printStackTrace();
//                callback.onError(call, e);
////                if (loadingDialog != null) {
////                    loadingDialog.dismiss();
////                }
//            }
//
//            @Override
//            public void onResponse(String response) {
//                callback.onResponse(response);
//            }
//
//        });
//
//        return requestCall;
//    }

}
