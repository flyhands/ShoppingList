package android.vogella.de.shoppinglist;


import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
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
import java.util.List;


/**
 * A simple {@link Fragment} subclass.
 */
public class ShoppingList extends Fragment {
    String paramValue = "http://10.0.2.2//syafi/";
    ListView listView;
    DatabaseHelper myDB;

    public ShoppingList() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this
        View v = inflater.inflate(R.layout.fragment_shopping_list, container, false);
        myDB = new DatabaseHelper(getContext());

        JsonTask task = new JsonTask();
        task.execute(paramValue+"jsonfile.json");
        return v;
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
                Toast.makeText(getContext(),"Error..." + e.toString(),
                        Toast.LENGTH_LONG).show();
            }
            return answer;
        }

        public void onPostExecute(String results) {
            ListDrwaer();
        }

        private void ListDrwaer() {
            List<Item> shoppingList = new ArrayList<>();
            try {
                JSONObject jsonResponse = new JSONObject(jsonResult);
                JSONArray jsonMainNode = jsonResponse.optJSONArray("shop_list");
                for (int i = 0; i < jsonMainNode.length(); i++) {
                    JSONObject jsonChildNode = jsonMainNode.getJSONObject(i);
                    String item = jsonChildNode.optString("item");
                    String price = jsonChildNode.optString("price");
                    String image = jsonChildNode.optString("image");

                    shoppingList.add(new Item(item, price, paramValue+image));



                }
            } catch (JSONException e) {
                Toast.makeText(getContext(), "Error" + e.toString(),
                        Toast.LENGTH_SHORT).show();
            }
            CardAdapter adapter = new CardAdapter(getContext(), shoppingList);
            listView.setAdapter(adapter);

            listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    TextView tv = view.findViewById(R.id.cardTitle);
                    addItem(tv.getText().toString());
                }
            });
        }



    }
    public void addItem(String name){
        boolean isInserted = myDB.insertData(name);
        if(isInserted == true){
            Toast.makeText(getContext(),"Item added", Toast.LENGTH_LONG).show();
        }
        else
            Toast.makeText(getContext(),"Item not added", Toast.LENGTH_LONG).show();


    }

}
