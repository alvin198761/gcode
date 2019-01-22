package org.alvin.code.v2.sys.mock.service;

import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.springframework.stereotype.Service;

import java.io.IOException;

@Service
public class HttpClientService {
    /**
     * 简单get 请求
     *
     * @param url
     * @return
     */
    public String get(String url) {
        try (CloseableHttpClient client = HttpClients.createDefault()) {
            HttpGet httpGet = new HttpGet(url);
            CloseableHttpResponse response = client.execute(httpGet);
            if (response.getStatusLine().getStatusCode() == 200) {
                return EntityUtils.toString(response.getEntity());//获得返回的结果
            }
            throw new IOException("请求错误，错误码：" + response.getStatusLine().getStatusCode());
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public String put() {
        return null;
    }

    public String post() {
        return null;
    }

    public String delete() {
        return null;
    }

}
