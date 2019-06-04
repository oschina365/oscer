package net.oscer.framework;

import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.HttpException;
import org.apache.commons.httpclient.HttpMethod;
import org.apache.commons.httpclient.HttpStatus;
import org.apache.commons.httpclient.methods.*;
import org.apache.commons.httpclient.params.HttpClientParams;
import org.apache.commons.io.IOUtils;
import org.apache.http.HttpEntity;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map.Entry;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class HttpKits {
    public static final String HEAD_USER_AGENT = "User-Agent";
    public static final String USER_AGENT = "Mozilla/5.0 (Windows NT 6.2; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/28.0.1500.95 Safari/537.36 AlexaToolbar/alxg-3.1";
    public static final String HEAD_REFER = "Referer";
    public static final String PROXY_HOST = "127.0.0.1";
    public static final int PROXY_PORT = 8087;
    public static final Pattern REF_REG = Pattern.compile("(^[a-zA-Z]+://[^/]+)[/]?");
    public static final Pattern PROTROL_REG = Pattern.compile("(^[a-zA-Z]+://)");


    private static final Logger log = LoggerFactory.getLogger(HttpKits.class);


    private static HttpClient client(int timeout) {
        HttpClient client = new HttpClient();
        client.getParams().setParameter(HttpClientParams.SO_TIMEOUT, timeout);
        client.getParams().setParameter(
                HttpClientParams.CONNECTION_MANAGER_TIMEOUT, 30000L);
        return client;
    }

    public static InputStream get(String url) throws Exception {
        try {
            return getNotQuite(url);
        } catch (Exception e) {
            return null;
        }
    }

    public static InputStream getNotQuite(String url) throws Exception {
        if (StringUtils.isBlank(url)) {
            return null;
        }
        GetMethod get = new GetMethod(url);
        try {
            get.addRequestHeader(HEAD_USER_AGENT, USER_AGENT);
            get.addRequestHeader(HEAD_REFER, refer(url));
            int code = client(30000).executeMethod(get);
            if (code == HttpStatus.SC_OK) {
                InputStream in = get.getResponseBodyAsStream();
                try {
                    return IOUtils.toBufferedInputStream(in);
                } finally {
                    IOUtils.closeQuietly(in);
                }
            }
            if (code == HttpStatus.SC_MOVED_TEMPORARILY
                    || code == HttpStatus.SC_MOVED_PERMANENTLY
                    || code == HttpStatus.SC_SEE_OTHER
                    || code == HttpStatus.SC_TEMPORARY_REDIRECT) {
                String nurl = get.getResponseHeader("Location").getValue();
                return get(nurl);
            }

            throw new Exception(code + "");
        } finally {
            get.releaseConnection();
        }
    }

    public static InputStream postNotQuite(String url, RequestEntity enty)
            throws Exception {
        if (StringUtils.isBlank(url)) {
            return null;
        }
        long start = System.currentTimeMillis();
        PostMethod post = new PostMethod(url);
        try {
            post.addRequestHeader(HEAD_USER_AGENT, USER_AGENT);
            post.addRequestHeader(HEAD_REFER, refer(url));
            post.setRequestEntity(enty);
            int code = client(30000).executeMethod(post);
            if (code == HttpStatus.SC_OK || code == HttpStatus.SC_CREATED) {
                InputStream in = post.getResponseBodyAsStream();
                try {

                    return IOUtils.toBufferedInputStream(in);
                } finally {
                    IOUtils.closeQuietly(in);
                }
            }
            throw new Exception(code + "");
        } finally {
            long end = System.currentTimeMillis();
            Calendar c = Calendar.getInstance();
            c.setTimeInMillis(end - start);
            log.info("URL=" + url + " 耗费时间：" + c.get(Calendar.MILLISECOND) + " 毫秒");
            post.releaseConnection();
        }
    }

    public static InputStream post(String url, RequestEntity enty)
            throws Exception {
        try {
            return postNotQuite(url, enty);
        } catch (Exception e) {
            return null;
        }
    }

    public static InputStream delete(String url) throws IOException {
        if (StringUtils.isBlank(url)) {
            return null;
        }
        DeleteMethod delete = new DeleteMethod(url);
        try {
            delete.addRequestHeader(HEAD_USER_AGENT, USER_AGENT);
            delete.addRequestHeader(HEAD_REFER, refer(url));
            delete.setRequestHeader("Content-Type", "multipart/form-data");
            int code = client(30000).executeMethod(delete);
            if (code == HttpStatus.SC_OK || code == HttpStatus.SC_CREATED) {
                InputStream in = delete.getResponseBodyAsStream();
                try {
                    return IOUtils.toBufferedInputStream(in);
                } finally {
                    IOUtils.closeQuietly(in);
                }
            }
        } finally {
            delete.releaseConnection();
        }
        return null;
    }

    public static InputStream put(String url, RequestEntity entity) throws HttpException, IOException {
        if (StringUtils.isBlank(url)) {
            return null;
        }
        PutMethod put = new PutMethod(url);
        try {
            put.addRequestHeader(HEAD_USER_AGENT, USER_AGENT);
            put.addRequestHeader(HEAD_REFER, refer(url));
            put.setRequestEntity(entity);
            int code = client(30000).executeMethod(put);
            if (code == HttpStatus.SC_OK || code == HttpStatus.SC_CREATED) {
                InputStream in = put.getResponseBodyAsStream();
                try {
                    return IOUtils.toBufferedInputStream(in);
                } finally {
                    IOUtils.closeQuietly(in);
                }
            }
        } finally {
            put.releaseConnection();
        }
        return null;
    }

    public static String refer(String url) {
        Matcher matcher = REF_REG.matcher(url);
        while (matcher.find()) {
            return matcher.group();
        }
        return "";
    }

    public static String protrol(String url) {
        Matcher matcher = PROTROL_REG.matcher(url);
        while (matcher.find()) {
            return matcher.group();
        }
        return "";
    }

    public static String truePost(String url, LinkedHashMap<String, String> params) {
        try {
            return truePostNotQuite(url, params);
        } catch (Exception e) {
            return null;
        }
    }

    public static String truePostNotQuite(String url, LinkedHashMap<String, String> params) throws Exception {
        CloseableHttpClient httpclient = HttpClients.createDefault();
        try {

            HttpPost httpPost = new HttpPost(url);
            List<NameValuePair> nvps = new ArrayList<NameValuePair>();
            if (params != null) {
                for (Entry<String, String> entry : params.entrySet()) {
                    nvps.add(new BasicNameValuePair(entry.getKey(), entry.getValue()));
                }
            }
            httpPost.addHeader(HEAD_USER_AGENT, USER_AGENT);
            httpPost.addHeader(HEAD_REFER, refer(url));
            httpPost.setEntity(new UrlEncodedFormEntity(nvps, "utf-8"));
            CloseableHttpResponse response = httpclient.execute(httpPost);
            try {
                int code = response.getStatusLine().getStatusCode();

                if (code == HttpStatus.SC_OK || code == HttpStatus.SC_CREATED) {
                    HttpEntity entity = response.getEntity();
                    InputStream is = entity.getContent();
                    BufferedReader br = new BufferedReader(new InputStreamReader(is, "utf-8"));
                    StringBuilder sb = new StringBuilder();
                    String tmp = br.readLine();
                    while (tmp != null) {
                        if (StringUtils.isNotBlank(tmp)) {
                            sb.append(tmp);
                        }
                        tmp = br.readLine();
                    }

                    EntityUtils.consume(entity);
                    return sb.toString();
                }
                throw new Exception(code + "");
            } finally {
                response.close();
            }

        } catch (ClientProtocolException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                httpclient.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        return null;
    }

    /**
     * App接口专用HttpGet方法
     *
     * @param url
     * @return
     * @throws IOException
     */
    public static InputStream getForItags(String url) throws IOException {
        if (StringUtils.isBlank(url)) {
            return null;
        }
        HttpMethod httpMethod = new GetMethod(url);
        httpMethod.addRequestHeader(HEAD_USER_AGENT, USER_AGENT);
        httpMethod.addRequestHeader(HEAD_REFER, refer(url));
        try {
            int statusCode = client(30000).executeMethod(httpMethod);
            if (statusCode == HttpStatus.SC_OK) {
                return IOUtils.toBufferedInputStream(httpMethod.getResponseBodyAsStream());
            } else {
                throw new RuntimeException("status code" + statusCode + "\n"
                        + "cotent:" + IOUtils.toString(httpMethod.getResponseBodyAsStream()));
            }
        } finally {
            httpMethod.releaseConnection();
        }
    }

    public static InputStream getNotQuiteAndTimeOut(String url, int timeOut) throws Exception {
        if (StringUtils.isBlank(url)) {
            return null;
        }
        GetMethod get = new GetMethod(url);
        try {
            get.addRequestHeader(HEAD_USER_AGENT, USER_AGENT);
            get.addRequestHeader(HEAD_REFER, refer(url));
            int code = client(timeOut).executeMethod(get);
            if (code == HttpStatus.SC_OK) {
                InputStream in = get.getResponseBodyAsStream();
                try {
                    return IOUtils.toBufferedInputStream(in);
                } finally {
                    IOUtils.closeQuietly(in);
                }
            }
            if (code == HttpStatus.SC_MOVED_TEMPORARILY
                    || code == HttpStatus.SC_MOVED_PERMANENTLY
                    || code == HttpStatus.SC_SEE_OTHER
                    || code == HttpStatus.SC_TEMPORARY_REDIRECT) {
                String nurl = get.getResponseHeader("Location").getValue();
                return get(nurl);
            }

            throw new Exception(code + "");
        } finally {
            get.releaseConnection();
        }
    }

    public static InputStream getAndSetTimeOut(String url, int timeOut) throws Exception {
        try {
            return getNotQuiteAndTimeOut(url, timeOut);
        } catch (Exception e) {
            return null;
        }
    }
}

