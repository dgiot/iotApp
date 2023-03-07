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
        String PreflightRequest;
        webActivity.setnewset(false);
        Log.d("hallow", "session=" + session.getHeaders().toString());
        if (isPreflightRequest(session)) {
//              如果是则发送CORS响应告诉浏览HTTP服务支持的METHOD及HEADERS和请求源
            return responseCORS(session);
        }
        if (session.getUri().equals("/photo")) {
//              如果是则发送CORS响应告诉浏览HTTP服务支持的METHOD及HEADERS和请求源
            return photo(session);
        }

        if (session.getUri().equals("/scancode")) {
//              如果是则发送CORS响应告诉浏览HTTP服务支持的METHOD及HEADERS和请求源
            return scancode(session);
        }

        ;
        //可以看到是什么请求方式
        Method method = session.getMethod();
        Response resp = newFixedLengthResponse(session.getUri());

        return resp;

    }





    public Response photo(IHTTPSession session) {

        JSONObject map = new JSONObject();
        JSONObject data = new JSONObject();

        String Json = "{'code':200,'datetimes':'','deviceid':'1db7727cc6','instruct':'photo','msg':'success','objectId':'1db7727cc6','username':'username'}";
        ReceiveMsgBean msgBean = JSONObject.parseObject(Json, ReceiveMsgBean.class);
        Log.d("hallow", "Json=" + Json);
        Log.d("hallow", "msgBean=" + msgBean);
        EventBus.getDefault().post(msgBean);
        String text=webActivity.geturl();

        data.put("photo", text);
        map.put("status", 0);
        map.put("msg", "");
        map.put("data", data);
        Response resp = newFixedLengthResponse(map.toJSONString());
        resp.addHeader("Access-Control-Allow-Methods", "GET, PUT, POST, DELETE, HEAD, OPTIONS, TRACE, CONNECT, PATCH, PROPFIND, PROPPATCH, MKCOL, MOVE, COPY, LOCK, UNLOCK");
        Log.d("hallow", "map=" + text);

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




    public Response scancode(IHTTPSession session) {

        JSONObject map = new JSONObject();
        JSONObject data = new JSONObject();
        String Json = "{'code':200,'datetimes':'','deviceid':'1db7727cc6','instruct':'scancode','msg':'success','objectId':'1db7727cc6','username':'username'}";
        ReceiveMsgBean msgBean = JSONObject.parseObject(Json, ReceiveMsgBean.class);
//        Log.d("hallow", "Json=" + Json);
//        Log.d("hallow", "msgBean=" + msgBean);
        EventBus.getDefault().post(msgBean);

        String text=webActivity.geturl();

        data.put("scancode", text);
//        data.put("text","asd");
        map.put("status", 0);
        map.put("msg", "");
        map.put("data", data);
        Log.d("scancode", "map=" + map.toJSONString());
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
