package org.dudu.TomcatVuln.request;

import org.dudu.TomcatVuln.utils.UserAgentUtil;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;


/**
 * @description:
 * @author：DUDU
 * @date: 2023/9/26
 **/
public abstract class HttpRequest<T,R>{
    public HttpURLConnection connection;

    public void setConnection(String url) throws IOException {
        URL httpURl = new URL(url);
        connection = (HttpURLConnection)httpURl.openConnection();
        connection.setConnectTimeout(5000); //请求超时时间
        connection.setRequestProperty("Accept","*/*");
        connection.setRequestProperty("Connection","close");
        connection.setRequestProperty("Accept-Language","en");
        connection.setRequestProperty("User-Anget", UserAgentUtil.getRandomUserAgent());
        connection.setUseCaches(false);
        this.connection = connection;

    }

    protected abstract R request(T requestParam) throws IOException;

}
