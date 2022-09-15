package android.tx.com.dgiot_amis;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import fi.iki.elonen.NanoHTTPD;

public class AndroidWebServer extends NanoHTTPD {
    public AndroidWebServer(int port) {
        super(port);
    }

    public AndroidWebServer(String hostname, int port) {
        super(hostname, port);
    }

    @Override
    public Response serve(IHTTPSession session) {
        String msg = "<html><body><h1>Hello server</h1>\n";
        if(isPreflightRequest(session)){
            // 如果是则发送CORS响应告诉浏览HTTP服务支持的METHOD及HEADERS和请求源
            return responseCORS(session);
        }
        //可以看到是什么请求方式
        Method method = session.getMethod();
        System.out.println(method);
        try {
            /*
             * 对于post请求，你需要先调用parseBody()方法，
             * 直接传一个简单的新构造的map就行了
             */
            session.parseBody(new HashMap());
            Map parms = new HashMap();
            //然后再调用getParams()方法
            parms = session.getParms();
            System.out.println(parms);
            //ReceiveMsgBean msgBean = JSONObject.parseObject(message.getPayload(), ReceiveMsgBean.class);
            //EventBus.getDefault().post(msgBean);
            if (parms.get("username") == null) {
                msg += "<form action='?' method='get'>\n";
                msg += "<p>Your name: <input type='text' name='username'></p>\n";
                msg += "</form>\n";
            } else {
                msg += "<p>Hello, " + parms.get("username") + "!</p>";
            }

            //获取请求uri
            String uri = session.getUri();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ResponseException e) {
            e.printStackTrace();
        }
        return newFixedLengthResponse(msg + "</body></html>\n");
    }

    /**
     * 判断是否为CORS 预检请求请求(Preflight)
     * @param session
     * @return
     */
    private static boolean isPreflightRequest(IHTTPSession session) {
        Map<String, String> headers = session.getHeaders();
        return Method.OPTIONS.equals(session.getMethod())
                && headers.containsKey("origin")
                && headers.containsKey("access-control-request-method")
                && headers.containsKey("access-control-request-headers");
    }
    /**
     * 向响应包中添加CORS包头数据
     * @param session
     * @return
     */
    private Response responseCORS(IHTTPSession session) {
        Response resp = wrapResponse(session,newFixedLengthResponse(""));
        Map<String, String> headers = session.getHeaders();
        resp.addHeader("Access-Control-Allow-Methods","POST,GET,OPTIONS");

        String requestHeaders = headers.get("access-control-request-headers");
        //String allowHeaders = MoreObjects.firstNonNull(requestHeaders, "Content-Type");
        //resp.addHeader("Access-Control-Allow-Headers", allowHeaders);
        //resp.addHeader("Access-Control-Max-Age", "86400");
        resp.addHeader("Access-Control-Max-Age", "0");
        return resp;
    }
    /**
     * 封装响应包
     * @param session http请求
     * @param resp 响应包
     * @return resp
     */
    private Response wrapResponse(IHTTPSession session,Response resp) {
        if(null != resp){
            Map<String, String> headers = session.getHeaders();
            resp.addHeader("Access-Control-Allow-Credentials", "true");
            // 如果请求头中包含'Origin',则响应头中'Access-Control-Allow-Origin'使用此值否则为'*'
            // nanohttd将所有请求头的名称强制转为了小写
            //String origin = MoreObjects.firstNonNull(headers.get("origin", "*");
           // resp.addHeader("Access-Control-Allow-Origin", origin);
            String  requestHeaders = headers.get("access-control-request-headers");
            if(requestHeaders != null){
                resp.addHeader("Access-Control-Allow-Headers", requestHeaders);
            }
        }
        return resp;
    }
}