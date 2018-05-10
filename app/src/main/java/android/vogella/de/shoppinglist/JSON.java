package android.vogella.de.shoppinglist;

import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

public class JSON extends AppCompatActivity {
    String data;
    TextView tv;
    readJson readson;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_json);


        tv = findViewById(R.id.textView);
        String paramValue = "http://10.0.2.2//syafi/jsonfile.json";



        readson = (readJson) new readJson().execute(paramValue);

        data = readson.doInBackground();
        Log.v("msg", readson.getData());

        FloatingActionButton fab = findViewById(R.id.floatingActionButton);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                tv.setText(readson.getData());
            }
        });




    }
}
