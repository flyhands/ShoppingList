package android.vogella.de.shoppinglist;

import android.content.Context;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class JSON extends AppCompatActivity {
    ListView listView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_json);
        String paramValue = "http://10.0.2.2//syafi/jsonfile.json";
        listView = findViewById(R.id.listView1);


        JsonTask task = new JsonTask();
        task.execute(paramValue);


    }

    class JsonTask extends AsyncTask<String, Void, String> {
        String jsonResult;

        protected String doInBackground(String... params) {
            HttpURLConnection connection;
            URL url;


            try {

                url = new URL(params[0]);
                connection = (HttpURLConnection) url.openConnection();
                connection.connect();
                InputStream response = connection.getInputStream();
                jsonResult = inputStreamToString(response).toString();

            } catch (Exception e) {
                e.printStackTrace();
            }


            return null;
        }
        private StringBuilder inputStreamToString(InputStream is) {
            String rLine;
            StringBuilder answer = new StringBuilder();
            BufferedReader rd = new BufferedReader(new InputStreamReader(is));
            try {
                while ((rLine = rd.readLine()) != null) {
                    answer.append(rLine);
                }
            }
            catch (IOException e) {
                Toast.makeText(getApplicationContext(),"Error..." + e.toString(),
                        Toast.LENGTH_LONG).show();
            }
            return answer;
        }

        public void onPostExecute(String results) {
            ListDrwaer();
        }

        private void ListDrwaer() {
            List<Map<String, String>> studentList = new ArrayList<Map<String, String>>();
            try {
                JSONObject jsonResponse = new JSONObject(jsonResult);
                JSONArray jsonMainNode = jsonResponse.optJSONArray("shop_list");
                for (int i = 0; i < jsonMainNode.length(); i++) {
                    JSONObject jsonChildNode = jsonMainNode.getJSONObject(i);
                    String item = jsonChildNode.optString("item");
                    String price = jsonChildNode.optString("price");
                    String outPut = item + " - " + price;
                    studentList.add(createStudent("students", outPut));
                }
            } catch (JSONException e) {
                Toast.makeText(getApplicationContext(), "Error" + e.toString(),
                        Toast.LENGTH_SHORT).show();
            }
            SimpleAdapter simpleAdapter = new SimpleAdapter(getApplicationContext(), studentList,
                    android.R.layout.simple_list_item_1, new String[] { "students" }, new int[] { android.R.id.text1 });
            listView.setAdapter(simpleAdapter);
        }

        private HashMap<String, String> createStudent(String name, String number) {
            HashMap<String, String> studentNameNo = new HashMap<String, String>();
            studentNameNo.put(name, number);
            return studentNameNo;
        }

    }
}