package com.eshuix.javademo.http;

import android.app.Application;
import android.os.Environment;
import android.widget.ProgressBar;

import org.jetbrains.annotations.NotNull;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Objects;
import java.util.concurrent.TimeUnit;

import okhttp3.Cache;
import okhttp3.FormBody;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import okhttp3.ResponseBody;
import okio.BufferedSink;

public class HttpHelper {

    private static Application httpApplication;

    private static final String MEDIA_TYPE = "application/json; charset=utf-8";

    private static final Long MAX_CACHE_SIZE = 1024 * 1024 * 50L;// 50M 的缓存大小

    private static final Long DEFAULT_TIMEOUT = 15L;

    private static class HttpHelperHolder {
        private static final HttpHelper INSTANCE = new HttpHelper();
    }

    private HttpHelper() {
    }

    public static HttpHelper getInstance(Application application) {
        if (httpApplication == null){
            httpApplication = application;
        }
        return HttpHelperHolder.INSTANCE;
    }

    private OkHttpClient getOkHttpClient() {
        OkHttpClient.Builder builder = new OkHttpClient().newBuilder();

        //设置 请求的缓存的大小跟位置
        File cacheFile = new File(httpApplication.getCacheDir(), "cache");
        Cache cache = new Cache(cacheFile, MAX_CACHE_SIZE);
        builder.cache(cache)
                .connectTimeout(DEFAULT_TIMEOUT, TimeUnit.SECONDS)
                .readTimeout(DEFAULT_TIMEOUT, TimeUnit.SECONDS)
                .writeTimeout(DEFAULT_TIMEOUT, TimeUnit.SECONDS)
                .retryOnConnectionFailure(true);

        return builder.build();
    }

    /**
     * 获取键值对FormBody.Builder
     */
    public FormBody.Builder getBuilder(){
        return new FormBody.Builder();
    }

    /**
     * post方式提交键值对
     * 参数1：[url] url ，参数2：[body] 键值对
     * 返回字符串,如果数据为空抛出空指针异常
     */
    public String post(String url, RequestBody body) throws IOException{
        Request request = new Request.Builder()
                .url(url)
                .post(body)
                .build();

        Response response = getOkHttpClient().newCall(request).execute();
        ResponseBody responseBody = response.body();
        if (responseBody != null) {
            return responseBody.string();
        }
        return "";
    }

    /**
     * post方式提交json字符串
     * 参数1：[url] url，参数2：[json] json字符串
     * 返回字符串,如果数据为空抛出空指针异常
     */
    public String post(String url, String json) throws IOException {
        ///okHttp4 拓展功能（Kotlin）
        ///String.toMediaType()替换MediaType.get(String)，需要导入import okhttp3.MediaType.Companion.toMediaType
        ///String.toRequestBody(MediaType)替换RequestBody.create(String,MediaType),import okhttp3.RequestBody.Companion.toRequestBody
        return post(url, RequestBody.create(json, MediaType.get(MEDIA_TYPE)));
    }

    /**
     * post方式提交流
     * 参数1：[url] url，参数2：[bis] 缓冲字节流
     * 返回字符串,如果数据为空抛出空指针异常
     */
    public String post(String url, final BufferedInputStream bis) throws IOException {
        RequestBody body = new RequestBody() {
            @Override
            public void writeTo(@NotNull BufferedSink bufferedSink) {
                byte[] bytes = new byte[1024 * 8];
                int length;
                try {
                    while ((length = bis.read(bytes)) != -1) {
                        bufferedSink.write(bytes, 0, length);
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                } finally {
                    try {
                        bis.close();
                        bufferedSink.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }

            @NotNull
            @Override
            public MediaType contentType() {
                return MediaType.get(MEDIA_TYPE);
            }

        };
        return post(url, body);
    }

    /**
     * post方式提交文件
     * 参数1：[url] url，参数2：[file] 文件
     * 返回字符串,如果数据为空抛出空指针异常
     */
    public String post(String url, File file) throws IOException {
        //okHttp4 拓展功能
        //需要import okhttp3.RequestBody.Companion.asRequestBody
        //替换RequestBody.create(MediaType.parse("text/x-markdown; charset=utf-8"), file)
        RequestBody body = RequestBody.create(MediaType.parse("text/x-markdown; charset=utf-8"), file);
        return post(url, body);
    }

    /**
     * Post方式提交分块请求，可以上传多个文件
     * 参数1：[imageUrl] url，参数2：[file] 多个文件
     * 返回字符串,如果数据为空抛出空指针异常
     */
    public String post(String url, File... files) throws IOException {
        MultipartBody.Builder requestBody = new MultipartBody.Builder()
                //设置分块提交模式
                .setType(MultipartBody.FORM)
                //分块提交，标题
                .addFormDataPart("title", "block");

        for (File file : files) {
            requestBody.addFormDataPart(file.getName(), file.getName(), RequestBody.create(MediaType.parse("image/png"), file));
        }

        return post(url, requestBody.build());
    }

    /**
     * post方式提交键值对
     * 参数1：[url] url ，参数2：[body] 键值对
     * 返回字符串,如果数据为空抛出空指针异常
     */
    public String get(String url) throws IOException{
        Request request = new Request.Builder()
                .url(url)
                .build();

        Response response = getOkHttpClient().newCall(request).execute();
        ResponseBody responseBody = response.body();
        if (responseBody != null) {
            return responseBody.string();
        }
        return "";
    }

    /**
     * 下载文件
     * 参数1：[url] 文件url,参数2：[file] 保存到指定目录
     * 必须在线程中调用，不然主线程会卡住，返回是否下载成功布尔值
     */
    public boolean downloadFile(String url, File file) throws IOException {
        Request request = new Request.Builder()
                .url(url)
                .build();

        Response response = getOkHttpClient().newCall(request).execute();
        ResponseBody responseBody = response.body();
        if (responseBody != null) {
            try (BufferedInputStream bis = new BufferedInputStream(responseBody.byteStream()); BufferedOutputStream bos = new BufferedOutputStream(new FileOutputStream(file))) {
                byte[] bytes = new byte[1024 * 8];
                int length;
                while ((length = bis.read(bytes)) != -1) {
                    bos.write(bytes, 0, length);
                }
                return true;
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return false;
    }

    /**
     * 顺序下载
     * 需要参数：url
     * 必须在线程中调用，不然主线程会卡住，返回是否下载成功布尔值
     */
    public boolean sequentialDownload(String... urls) throws IOException {
        //失败次数
        int failure = 0;

        for (String url : urls) {
            if (failure == 3) {
                return false;
            }
            String[] fileNames = url.split("/");
            if (fileNames.length > 0) {
                File file = createFilePath(fileNames[fileNames.length - 1]);
                if (downloadFile(url, file)) {
                    failure = 0;
                } else {
                    failure++;
                }
            }
        }
        return true;
    }

    /**
     * 顺序下载
     * 需要参数：[progressBar]进度条 [url]url
     * 必须在线程中调用，不然主线程会卡住，返回是否下载成功布尔值
     */
    public boolean sequentialDownload(ProgressBar progressBar, String... urls) throws IOException {
        //失败次数
        int failure = 0;
        int i = 0;
        progressBar.setMax(100);

        for (String url : urls) {
            if (failure == 3) {
                return false;
            }
            String[] fileNames = url.split("/");
            if (fileNames.length > 0) {
                File file = createFilePath(fileNames[fileNames.length - 1]);
                if (downloadFile(url, file)) {
                    failure = 0;
                    i++;
                    int progress = (int) (i * 1.0f / urls.length * 100);
                    progressBar.setProgress(progress);
                } else {
                    failure++;
                }
            }
        }
        return true;
    }

    /**
     * 创建文件路径
     */
    public File createFilePath(String fileName) {
        String path = Objects.requireNonNull(httpApplication.getExternalFilesDir(Environment.DIRECTORY_PICTURES)).getAbsolutePath();
        File file = new File(path, fileName);

        try {
            //如果有同名文件则删除
            if (file.exists()) {
                file.delete();
            }
            if (file.createNewFile()) {
                return file;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }
}
