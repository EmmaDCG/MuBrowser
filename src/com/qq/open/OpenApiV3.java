package com.qq.open;

import org.apache.commons.httpclient.methods.multipart.FilePart;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;


public class OpenApiV3 {
    private String appid;
    private String appkey;
    private String serverName;

    public OpenApiV3(String appid, String appkey) {
        this.appid = appid;
        this.appkey = appkey;
    }

    public void setServerName(String serverName) {
        this.serverName = serverName;
    }

    public String api(String scriptName, HashMap<String, String> params, String protocol)
            throws OpensnsException {
        if (params.get("openid") == null) {
            throw new OpensnsException(1801, "openid is empty");
        }

        if (!isOpenid((String) params.get("openid"))) {
            throw new OpensnsException(1802, "openid is invalid");
        }

        params.remove("sig");

        params.put("appid", appid);

        String method = "post";

        String secret = appkey + "&";

        String sig = SnsSigCheck.makeSig(method, scriptName, params, secret);

        params.put("sig", sig);

        StringBuilder sb = new StringBuilder(64);
        sb.append(protocol).append("://").append(serverName).append(scriptName);
        String url = sb.toString();

        HashMap<String, String> cookies = null;
        String resp = SnsNetwork.postRequest(url, params, cookies, protocol);
        return resp;
    }


    public String apiUploadFile(String scriptName, HashMap<String, String> params, FilePart fp, String protocol)
            throws OpensnsException {
        if (params.get("openid") == null) {
            throw new OpensnsException(1801, "openid is empty");
        }
        if (!isOpenid((String) params.get("openid"))) {
            throw new OpensnsException(1802, "openid is invalid");
        }

        params.remove("sig");

        params.put("appid", appid);

        String method = "post";

        String secret = appkey + "&";

        String sig = SnsSigCheck.makeSig(method, scriptName, params, secret);

        params.put("sig", sig);

        StringBuilder sb = new StringBuilder(64);
        sb.append(protocol).append("://").append(serverName).append(scriptName);
        String url = sb.toString();


        HashMap<String, String> cookies = null;

        long startTime = System.currentTimeMillis();

        String resp = SnsNetwork.postRequestWithFile(url, params, cookies, fp, protocol);

        JSONObject jo = null;
        try {
            jo = new JSONObject(resp);
        } catch (JSONException e) {
            throw new OpensnsException(1803, e);
        }

        int rc = jo.optInt("ret", 0);

        SnsStat.statReport(startTime, serverName, params, method, protocol, rc, scriptName);

        return resp;
    }


    private void printRequest(String url, String method, HashMap<String, String> params)
            throws OpensnsException {
        System.out.println("==========Request Info==========\n");
        System.out.println("method:  " + method);
        System.out.println("url:  " + url);
        System.out.println("params:");
        System.out.println(params);
        System.out.println("querystring:");
        StringBuilder buffer = new StringBuilder(128);
        Iterator iter = params.entrySet().iterator();
        while (iter.hasNext()) {
            Map.Entry entry = (Map.Entry) iter.next();
            try {
                buffer.append(URLEncoder.encode((String) entry.getKey(), "UTF-8").replace("+", "%20").replace("*", "%2A")).append("=").append(URLEncoder.encode((String) entry.getValue(), "UTF-8").replace("+", "%20").replace("*", "%2A")).append("&");
            } catch (UnsupportedEncodingException e) {
                throw new OpensnsException(1804, e);
            }
        }
        String tmp = buffer.toString();
        tmp = tmp.substring(0, tmp.length() - 1);
        System.out.println(tmp);
        System.out.println();
    }

    private void printRespond(String resp) {
        System.out.println("===========Respond Info============");
        System.out.println(resp);
    }

    private boolean isOpenid(String openid) {
        return (openid.length() == 32) && (openid.matches("^[0-9A-Fa-f]+$"));
    }
}
