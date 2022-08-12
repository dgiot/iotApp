package android.tx.com.dgiot_amis;


import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.Settings;
import android.support.annotation.Nullable;
import android.tx.com.dgiot_amis.bean.ReceiveMsgBean;
import android.tx.com.dgiot_amis.bean.UpImgBackBean;
import android.tx.com.dgiot_amis.utils.DgiotUtils;
import android.tx.com.dgiot_amis.utils.SharedPreUtil;
import android.util.Log;
import android.view.KeyEvent;
import android.webkit.CookieManager;
import android.webkit.JavascriptInterface;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Toast;

import com.alibaba.fastjson.JSONObject;
import com.lzy.okgo.OkGo;
import com.lzy.okgo.callback.StringCallback;
import com.lzy.okgo.cookie.store.CookieStore;
import com.lzy.okgo.model.Response;
import com.xiasuhuei321.loadingdialog.view.LoadingDialog;

import org.devio.takephoto.app.TakePhoto;
import org.devio.takephoto.app.TakePhotoActivity;
import org.devio.takephoto.model.TResult;
import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.io.File;
import java.io.IOException;
import java.util.List;


import okhttp3.Cookie;
import okhttp3.HttpUrl;
import pub.devrel.easypermissions.EasyPermissions;

public class WebActivity extends TakePhotoActivity implements EasyPermissions.PermissionCallbacks {

    private TakePhoto takePhoto;
    private WebView mWebView;
    private String mToken ,mUpImgUrl , mObjId , mIp;
    private Activity mActivity;
    private Intent intent;
    private String webUrl = "http://dev.iotn2n.com/dgiot-amisp";
    private ReceiveMsgBean receiveMsgBean;
    private JSONObject jsonObject;
    public LoadingDialog proDialog;
    private int intType;

    private static final String QR_KEY = "scancode", PHOTO_KEY = "photo";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_web);


        proDialog = new LoadingDialog(this);
        mActivity = this;
        takePhoto =  getTakePhoto();
        mWebView = findViewById(R.id.webView);

        webUrl = getIntent().getStringExtra("webUrl");

        EventBus.getDefault().register(mActivity);

        mToken = SharedPreUtil.getData(mActivity , "token");
        mObjId = SharedPreUtil.getData(mActivity , "objId");
        mIp = SharedPreUtil.getData(mActivity  ,"ip");

        if( !mToken.equals("") && !mIp.equals("") && !mObjId.equals("") ){
            DgiotService.startService(mActivity , mIp , mObjId, mToken);
            Log.e("sssd",mIp +"   " +mObjId +"    "+mToken);
            mUpImgUrl = "http://"+ mIp + ":1250/upload";
            initCookie( mToken );
        }


        initWeb();
    }

    private void initCookie(String token) {
        HttpUrl httpUrl = HttpUrl.parse(mUpImgUrl);
        Cookie.Builder builder = new Cookie.Builder();
        Cookie cookie = builder.name("auth_token").value(token).domain(httpUrl.host()).build();
        CookieStore cookieStore = OkGo.getInstance().getCookieJar().getCookieStore();
        cookieStore.saveCookie(httpUrl, cookie);
    }


    private void initMqtt(int i) {
        intType = i;
        switch (i){
            case 0:
                //拍照
                try {
                    takePhoto.onPickFromCapture( DgiotUtils.createImageFile() );
                } catch (IOException e) {
                    e.printStackTrace();
                }
                break;
            case 1:
                //选择一张照片
                takePhoto.onPickMultiple(1);
                break;
            case 2:
                //扫码
                onInspectJurisdiction();
                break;

                default:break;
        }

    }


    private void initWeb() {
        mWebView.getSettings().setJavaScriptEnabled(true);
        mWebView.getSettings().setJavaScriptCanOpenWindowsAutomatically(true);
        mWebView.getSettings().setLoadsImagesAutomatically(true);
        mWebView.setVerticalScrollBarEnabled(false);
        mWebView.getSettings().setUseWideViewPort(true);


        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            CookieManager  mCookieManager = CookieManager.getInstance();
            mCookieManager.setAcceptCookie(true);
            mCookieManager.setAcceptThirdPartyCookies(mWebView, true);
        }

        if ( Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            WebView.setWebContentsDebuggingEnabled(true);
        }
        //运行webview通过URI获取安卓文件
        mWebView.getSettings().setAllowFileAccess(true);
        mWebView.getSettings().setAllowFileAccessFromFileURLs(true);
        mWebView.getSettings().setAllowUniversalAccessFromFileURLs(true);
        mWebView.getSettings().setDomStorageEnabled(true);//设置H5可以本地存储
        // mWebView.setWebViewClient(new CustomWebViewClient(mWebView));
        mWebView.setWebViewClient(new MyWebViewClient());
        mWebView.setWebChromeClient(new MyWebChromeClient());//设置可以打开图片管理器

        mWebView.getSettings().setJavaScriptEnabled(true);
        mWebView.addJavascriptInterface(new JSToken(),"jstoken");

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
            mWebView.getSettings().setAllowUniversalAccessFromFileURLs(true);
        }
       mWebView.loadUrl( webUrl );
    }


    public class JSToken {
        // @JavascriptInterface 必须加上
        @JavascriptInterface
        public void setJsToken(String ip , String objId ,String token) {
            //Toast.makeText(WebActivity.this,ip,Toast.LENGTH_LONG).show();
            Log.e("sssd","token ===  "+ token);
            mObjId = objId; mToken = token;
            mUpImgUrl = "http://"+ ip + ":1250/upload";

            Log.e("sssd",ip +"   " +objId +"    "+token);
            DgiotService.startService(mActivity , ip , objId, token);

            initCookie( mToken );

            SharedPreUtil.saveData(mActivity , "ip",ip);
            SharedPreUtil.saveData(mActivity , "objId",objId );
            SharedPreUtil.saveData(mActivity , "token",token );
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onReceiveMsg(ReceiveMsgBean msgBean){
        if( msgBean != null ) {
            receiveMsgBean = msgBean;
            switch ( receiveMsgBean.getInstruct() ){
                case PHOTO_KEY:
                    initMqtt(0);
                    break;
                case QR_KEY:
                    initMqtt(2);
                    break;
                    default:break;
            }
        }
    }



    @Override
    public void takeSuccess(TResult result) {
        super.takeSuccess(result);
        String iconPath = result.getImage().getOriginalPath();
        Log.e("sssd","imagePath ===  "+iconPath);
        onUpImg(iconPath);
    }

    @Override
    public void takeFail(TResult result, String msg) {
        super.takeFail(result, msg);
       // Toast.makeText(WebActivity.this, "Error:" + msg, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void takeCancel() {
        super.takeCancel();
    }


    private class MyWebChromeClient extends WebChromeClient {

        @Override
        public void onReceivedTitle(WebView view, String title) {
            super.onReceivedTitle(view, title);

        }

    }


    private class MyWebViewClient extends WebViewClient {
        @Override
        public boolean shouldOverrideUrlLoading(WebView view, String url) {
            //这里进行url拦截  拦截到退出url
            return super.shouldOverrideUrlLoading(view, url);
        }


        @Override
        public void onPageFinished(WebView view, String url) {
            view.getSettings().setJavaScriptEnabled(true);
            super.onPageFinished(view, url);

        }
    }

    @Override
    public boolean dispatchKeyEvent(KeyEvent event) {
        if (event.getAction() == KeyEvent.ACTION_DOWN
                && event.getKeyCode() == KeyEvent.KEYCODE_BACK )  {
            if(mWebView.canGoBack()) {
                mWebView.goBack();
            }else {
                finish();

            }
            return false;
        }
        return super.dispatchKeyEvent(event);
    }


    private void onUpImg( String fileUrl ){
        proDialog.setLoadingText("正在上传").show();
        OkGo.<String>post(mUpImgUrl)
                .tag(WebActivity.this)
                .params("file", new File( fileUrl ))
                .params("name","file")
                .params("auth_token",mToken)
                .params("scene","app")
                .params("filename",System.currentTimeMillis()+".png")
                .params("output","json")
                .params("path","dgiot_file/uni_app/png/")
                .params("code","")
                .params("submit","upload")
                .execute(new StringCallback() {
                    @Override
                    public void onSuccess(Response<String> response) {
                       UpImgBackBean imgBackBean =  JSONObject.parseObject( response.body().toString() ,UpImgBackBean.class );
                       if( receiveMsgBean != null ){
                           jsonObject = new JSONObject();
                           jsonObject.put("deviceid",receiveMsgBean.getDeviceid());
                           jsonObject.put("instruct","photo");
                           jsonObject.put("url",imgBackBean.getPath());
                           DgiotService.publish( JSONObject.toJSONString( jsonObject ) );
                           if( proDialog != null ){
                               proDialog.close();
                           }
                       }
                    }

                    @Override
                    public void onError(Response<String> response) {
                        super.onError(response);
                        Throwable throwable = response.getException();
                        if( proDialog != null ){
                            proDialog.close();
                        }
                        Log.e("sssd", "上传文件错误" + throwable.getMessage());
                    }
                });
    }

    /**
     * 检查权限
     */
    private void onInspectJurisdiction() {
        String[] perms = {
                Manifest.permission.CAMERA ,

        };
        if (EasyPermissions.hasPermissions(mActivity, perms)) {
            if( intType == 2 ) {
                intent = new Intent(mActivity, QrActivity.class);
                startActivityForResult(intent, 101);
            }
        } else {
            EasyPermissions.requestPermissions(this, "APP需要权限", 200, perms);
        }
    }
    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        // Forward results to EasyPermissions
        EasyPermissions.onRequestPermissionsResult(requestCode, permissions, grantResults, this);
    }
    //成功
    @Override
    public void onPermissionsGranted(int requestCode, List<String> list) {
        if( intType == 2 ) {
            intent = new Intent(mActivity, QrActivity.class);
            startActivityForResult(intent, 101);
        }
    }
    //失败
    @Override
    public void onPermissionsDenied(int requestCode, List<String> list) {
        Toast.makeText( mActivity , "未全部授权，部分功能可能无法正常运行！",Toast.LENGTH_LONG ).show();
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch ( resultCode ){
            case 33:
                jsonObject = new JSONObject();
                jsonObject.put("deviceid",receiveMsgBean.getDeviceid());
                jsonObject.put("instruct",QR_KEY);
                jsonObject.put("url",data.getStringExtra("mCode"));
                DgiotService.publish( JSONObject.toJSONString( jsonObject ) );

                break;
                default:break;
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(mActivity);
    }

}
