package android.tx.com.dgiot_amis;

import android.content.Intent;
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
        AndroidWebServer androidWebServer = new AndroidWebServer(12345);
        try {
            androidWebServer.start();
        } catch (IOException e) {
            e.printStackTrace();
        }

        btnWeb.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if( editText.getText().toString().trim().equals("") ){
                    intent = new Intent(MainActivity.this , WebActivity.class);
//                    intent.putExtra("webUrl","http://dev.iotn2n.com/dgiot-amisp");
                    //intent.putExtra("webUrl","file:///android_asset/test/index.html");
                    intent.putExtra("webUrl","http://127.0.0.1:12345");
                    startActivity(intent);
                    return;
                }
            intent = new Intent(MainActivity.this , WebActivity.class);
                intent.putExtra("webUrl",editText.getText().toString().trim());
                startActivity(intent);
            }
        });
    }
}
