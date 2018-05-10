package android.vogella.de.shoppinglist;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;

public class JSON extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_json);

        String paramValue = "http://10.0.2.2//syafi/jsonfile.json";


        readJson readson = (readJson) new readJson().execute(paramValue);
        String data = readson.doInBackground();
        Log.v("msg", data);



    }

}
