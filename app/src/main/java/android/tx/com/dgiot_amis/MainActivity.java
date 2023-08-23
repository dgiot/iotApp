package android.tx.com.dgiot_amis;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import java.io.IOException;

public class MainActivity extends AppCompatActivity {

    private EditText editText;
    private Button btnWeb;
    private Intent intent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);
        editText = findViewById(R.id.edit_url);
        btnWeb = findViewById(R.id.btn_web);

        SharedPreferences sharedPreferences = getSharedPreferences("iotApp_conf", Context.MODE_PRIVATE);
        String webUrl = sharedPreferences.getString("webUrl", "https://prod.dgiotcloud.cn/");
        editText.setText(webUrl);
        AndroidWebServer androidWebServer = new AndroidWebServer(12345);
        try {
            androidWebServer.start();
        } catch (IOException e) {
            e.printStackTrace();
        }
        intent = new Intent(MainActivity.this, WebActivity.class);
        intent.putExtra("webUrl", webUrl);
        startActivity(intent);
        btnWeb.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (editText.getText().toString().trim().equals("")) {
                    intent = new Intent(MainActivity.this, WebActivity.class);
                    intent.putExtra("webUrl", webUrl);
                    startActivity(intent);
                    return;
                }
                SharedPreferences.Editor editor = sharedPreferences.edit();
                editor.putString("webUrl", editText.getText().toString().trim());
                editor.commit();

                intent = new Intent(MainActivity.this, WebActivity.class);
                intent.putExtra("webUrl", editText.getText().toString().trim());
                startActivity(intent);
            }
        });
    }
}
