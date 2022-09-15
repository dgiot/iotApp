package android.tx.com.dgiot_amis;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

import cn.bingoogolapple.qrcode.core.QRCodeView;
import cn.bingoogolapple.qrcode.zbar.ZBarView;

public class QrActivity extends AppCompatActivity implements QRCodeView.Delegate{

    private Intent intent;
    private ZBarView zBarView;

    int width = 300;
    int height = 300;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_qr);

        initView();
    }

    private void initView() {
        zBarView = findViewById(R.id.zbarview);
        zBarView.setDelegate(this);
    }

    @Override
    protected void onStart() {
        super.onStart();
        zBarView.startCamera(); // 打开后置摄像头开始预览，但是并未开始识别
//        mZBarView.startCamera(Camera.CameraInfo.CAMERA_FACING_FRONT); // 打开前置摄像头开始预览，但是并未开始识别
        zBarView.startSpotAndShowRect(); // 显示扫描框，并开始识别
    }

    @Override
    protected void onStop() {
        super.onStop();
        zBarView.stopCamera();
        zBarView.stopSpot();
    }

    @Override
    protected void onResume() {
        super.onResume();
        zBarView.showScanRect();
        zBarView.startCamera();
        zBarView.startSpot();
    }

    @Override
    protected void onDestroy() {
        zBarView.onDestroy(); // 销毁二维码扫描控件
        super.onDestroy();
    }

    @Override
    public void onScanQRCodeSuccess(String result) {
        intent = new Intent();
        intent.putExtra("mCode",result.trim());
        setResult( 33,intent );
        finish();
    }

    @Override
    public void onCameraAmbientBrightnessChanged(boolean isDark) {

    }

    @Override
    public void onScanQRCodeOpenCameraError() {

    }


}
