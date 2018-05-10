package android.vogella.de.shoppinglist;

import android.os.AsyncTask;
import android.util.Log;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class readJson extends AsyncTask<String, String, String> {

    protected String doInBackground(String... params) {
        HttpURLConnection connection;
        URL url;
        BufferedReader reader = null;
        final List<String> list = new ArrayList<>();

        try {

            url = new URL(params[0]);
            connection = (HttpURLConnection) url.openConnection();
            connection.connect();
            InputStream stream = connection.getInputStream();
            String line = "";
            StringBuffer buffer = new StringBuffer();
            reader = new BufferedReader(new InputStreamReader(stream));



            while ((line = reader.readLine()) != null) {
                list.add(line+"\n");
                line.concat(buffer.toString());
                Log.d("Response: ", "> " + line);   //here u ll get whole response...... :-)
            }
            StringBuilder builder = new StringBuilder();
            for (String value : list) {
                builder.append(value);
            }
            final String text = builder.toString();
            Log.v("msg", text);

        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

}
