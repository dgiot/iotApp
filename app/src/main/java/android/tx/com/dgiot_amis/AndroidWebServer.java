package android.tx.com.dgiot_amis;

import android.tx.com.dgiot_amis.bean.ReceiveMsgBean;
import android.util.Log;
import android.tx.com.dgiot_amis.WebActivity;

import com.alibaba.fastjson.JSONObject;

import org.greenrobot.eventbus.EventBus;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import fi.iki.elonen.NanoHTTPD;


public class AndroidWebServer extends NanoHTTPD {
    public AndroidWebServer(int port) {
        super(port);
    }

    private WebActivity webActivity = new WebActivity();

    public AndroidWebServer(String hostname, int port) {
        super(hostname, port);
    }

    @Override
    public Response serve(IHTTPSession session) {
        webActivity.setnewset(false);
        try {
            if (isPreflightRequest(session)) {
//              如果是则发送CORS响应告诉浏览HTTP服务支持的METHOD及HEADERS和请求源
                return responseCORS(session);
            }
            else
            {
                Method method = session.getMethod();
                Map header = session.getHeaders();
                String url = session.getUri();
                session.parseBody(new HashMap<>());
                Map parms = new HashMap();
                parms = session.getParms();
                parms.put("token",header.get("sessiontoken").toString());
                parms.put("url",header.get("origin").toString());
                JSONObject json = new JSONObject(parms);
                if (method.toString().equals("GET") && url.equals("/photo")) {
               // 如果是则发送CORS响应告诉浏览HTTP服务支持的METHOD及HEADERS和请求源
                    return get_photo(session, parms);
                }
                if (method.toString().equals("GET") && url.equals("/upload")) {
                    // 如果是则发送CORS响应告诉浏览HTTP服务支持的METHOD及HEADERS和请求源
                    return get_upload(session, parms);
                }
                if (url.equals("/scancode")) {
//              如果是则发送CORS响应告诉浏览HTTP服务支持的METHOD及HEADERS和请求源
                    return scancode(session, parms);
                }
            }

        }
        catch (IOException e){
         e.printStackTrace();
        } catch (ResponseException e) {
            throw new RuntimeException(e);
        }

        //可以看到是什么请求方式
        Response resp = newFixedLengthResponse(session.getUri());

        return resp;

    }

    public Response get_photo(IHTTPSession session, Map parms) {
        JSONObject msgBeanJson = new JSONObject(parms);
        msgBeanJson.put("instruct","photo");
        String path = parms.get("type").toString() + "/"+ parms.get("objectId").toString() + "/";
        msgBeanJson.put("path", path);
        ReceiveMsgBean msgBean = JSONObject.parseObject(msgBeanJson.toJSONString(), ReceiveMsgBean.class);
        EventBus.getDefault().post(msgBean);
        String text=webActivity.geturl();
        Log.d("dgiot_log", "text=" + text);
        JSONObject req = new JSONObject();
        req.put("status", 0);
        req.put("msg", "");
        JSONObject data = new JSONObject();
        data.put("photo", text);
        req.put("data", data);
        Response resp = newFixedLengthResponse(req.toJSONString());
        resp.addHeader("Access-Control-Allow-Methods", "GET, PUT, POST, DELETE, HEAD, OPTIONS, TRACE, CONNECT, PATCH, PROPFIND, PROPPATCH, MKCOL, MOVE, COPY, LOCK, UNLOCK");
        String origin=session.getHeaders().get("origin");
        resp.addHeader("Access-Control-Allow-Origin", origin);
        resp.addHeader("Access-Control-Allow-Headers", "*");
        resp.addHeader("Access-Control-Max-Age", "3600");
        resp.addHeader("Access-Control-Allow-Credentials", "true");
        return resp;
    }

    public Response get_upload(IHTTPSession session, Map parms) {
        JSONObject msgBeanJson = new JSONObject(parms);
        msgBeanJson.put("instruct","upload");
        String path = parms.get("type").toString() + "/"+ parms.get("objectId").toString() + "/";
        msgBeanJson.put("path", path);
        ReceiveMsgBean msgBean = JSONObject.parseObject(msgBeanJson.toJSONString(), ReceiveMsgBean.class);
        EventBus.getDefault().post(msgBean);
        String text=webActivity.geturl();
        Log.d("dgiot_log", "text=" + text);
        JSONObject req = new JSONObject();
        req.put("status", 0);
        req.put("msg", "");
        JSONObject data = new JSONObject();
        data.put("photo", text);
        req.put("data", data);
        Response resp = newFixedLengthResponse(req.toJSONString());
        resp.addHeader("Access-Control-Allow-Methods", "GET, PUT, POST, DELETE, HEAD, OPTIONS, TRACE, CONNECT, PATCH, PROPFIND, PROPPATCH, MKCOL, MOVE, COPY, LOCK, UNLOCK");
        String origin=session.getHeaders().get("origin");
        resp.addHeader("Access-Control-Allow-Origin", origin);
        resp.addHeader("Access-Control-Allow-Headers", "*");
        resp.addHeader("Access-Control-Max-Age", "3600");
        resp.addHeader("Access-Control-Allow-Credentials", "true");
        return resp;
    }

    public  String returntext(){
        String text=webActivity.geturl();
        if (text.equals("")){
            try {
                Thread.sleep(50);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            returntext();
        }
        return text;
    }

    public Response scancode(IHTTPSession session, Map parms) {
        JSONObject msgBeanJson = new JSONObject(parms);
        msgBeanJson.put("instruct","upload");
        msgBeanJson.put("instruct","scancode");
        ReceiveMsgBean msgBean = JSONObject.parseObject(msgBeanJson.toJSONString(), ReceiveMsgBean.class);
        EventBus.getDefault().post(msgBean);

        String text=webActivity.geturl();
        JSONObject map = new JSONObject();
        map.put("status", 0);
        map.put("msg", "");
        JSONObject data = new JSONObject();
        data.put("scancode", text);
        map.put("data", data);
        Log.d("dgiot_log", "map=" + map.toJSONString());
        Response resp = newFixedLengthResponse(map.toJSONString());
        resp.addHeader("Access-Control-Allow-Methods", "GET, PUT, POST, DELETE, HEAD, OPTIONS, TRACE, CONNECT, PATCH, PROPFIND, PROPPATCH, MKCOL, MOVE, COPY, LOCK, UNLOCK");
        String origin=session.getHeaders().get("origin");
        resp.addHeader("Access-Control-Allow-Origin", origin);
        resp.addHeader("Access-Control-Allow-Headers", "*");
        resp.addHeader("Access-Control-Max-Age", "3600");
        resp.addHeader("Access-Control-Allow-Credentials", "true");
        return resp;
    }

    /**
     * 判断是否为CORS 预检请求请求(Preflight)
     *
     * @param session
     * @return
     */
    private static boolean isPreflightRequest(IHTTPSession session) {
        Map<String, String> headers = session.getHeaders();
        return Method.OPTIONS.equals(session.getMethod())
                && headers.containsKey("origin")
                && headers.containsKey("access-control-request-method")
                && headers.containsKey("access-control-request-headers");
//    return true;
    }

    /**
     * 向响应包中添加CORS包头数据
     *
     * @param session
     * @return
     */
    private Response responseCORS(IHTTPSession session) {
        Response resp = wrapResponse(session, newFixedLengthResponse(""));
        Map<String, String> headers = session.getHeaders();
        resp.addHeader("Access-Control-Allow-Methods", "GET, PUT, POST, DELETE, HEAD, OPTIONS, TRACE, CONNECT, PATCH, PROPFIND, PROPPATCH, MKCOL, MOVE, COPY, LOCK, UNLOCK");
//        resp.addHeader("Access-Control-Max-Age", "86400");
//        resp.addHeader("Access-Control-Allow-Origin", headers.getOrDefault("origin","*"));
//        resp.addHeader("Access-Control-Allow-Origin", "*");
        String origin=session.getHeaders().get("origin");
        resp.addHeader("Access-Control-Allow-Origin", origin);
        resp.addHeader("Access-Control-Allow-Headers", "email,Connection,author,access-control-max-age,access-control-allow-credentials,access-control-allow-methods,access-control-allow-headers,access-control-allow-origin,platform,$$comments,hagan-token,departmenttoken,token,Content-Type,X-Requested-With,Origin, sessionToken, X-Requested-With, Content-Type, Accept,WG-App-Version, WG-Device-Id, WG-Network-Type, WG-Vendor, WG-OS-Type, WG-OS-Version, WG-Device-Model, WG-CPU, WG-Sid, WG-App-Id, WG-Token");
//        resp.addHeader("Access-Control-Allow-Headers", "*");
        resp.addHeader("Access-Control-Max-Age", "3600");
        resp.addHeader("Access-Control-Allow-Credentials", "true");

        return resp;
    }

    private Response responsS(IHTTPSession session) {
        Response resp = wrapResponse(session, newFixedLengthResponse(""));
        Map<String, String> headers = session.getHeaders();
        String msg = "<html><body><h1>asdfghjk</h1>\n";
        return newFixedLengthResponse(msg);
    }

    /**
     * 封装响应包
     *
     * @param session http请求
     * @param resp    响应包
     * @return resp
     */
    private Response wrapResponse(IHTTPSession session, Response resp) {
        if (null != resp) {
            Map<String, String> headers = session.getHeaders();

            // 如果请求头中包含'Origin',则响应头中'Access-Control-Allow-Origin'使用此值否则为'*'
            // nanohttd将所有请求头的名称强制转为了小写
//            String origin = MoreObjects.firstNonNull(headers.get("origin", "*");

//            String  requestHeaders = headers.get("access-control-request-headers");
//            if(requestHeaders != null){
//
//            }
        }
        return resp;
    }
}
