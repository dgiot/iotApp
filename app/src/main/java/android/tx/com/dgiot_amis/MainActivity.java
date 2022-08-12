package android.tx.com.dgiot_amis;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.webkit.WebView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

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

        btnWeb.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if( editText.getText().toString().trim().equals("") ){
                    intent = new Intent(MainActivity.this , WebActivity.class);
                    intent.putExtra("webUrl","http://dev.iotn2n.com/dgiot-amisp");
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
