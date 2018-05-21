package android.vogella.de.shoppinglist;


import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Toast;

import com.baoyz.swipemenulistview.SwipeMenu;
import com.baoyz.swipemenulistview.SwipeMenuCreator;
import com.baoyz.swipemenulistview.SwipeMenuItem;
import com.baoyz.swipemenulistview.SwipeMenuListView;

import java.util.ArrayList;
import java.util.Collections;


/**
 * A simple {@link Fragment} subclass.
 */
public class CartFragment extends Fragment {

    ArrayList<String> shoppingList = null;
    ArrayAdapter<String> adapter = null;
    DatabaseHelper myDB;
    SwipeMenuListView clv;
    String name;
    int itemID;
    private SensorManager sm;
    private float acelVal;
    private float acelLast;
    private float shake;
    public CartFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_cart, container,false);
        // Inflate the layout for this fragment

        sm = (SensorManager) getContext().getSystemService(Context.SENSOR_SERVICE);
        sm.registerListener(sensorListener, sm.getDefaultSensor(Sensor.TYPE_ACCELEROMETER), SensorManager.SENSOR_DELAY_NORMAL);
        shake = 0.00f;
        acelVal = SensorManager.GRAVITY_EARTH;
        acelLast = SensorManager.GRAVITY_EARTH;



//        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
//        setSupportActionBar(toolbar);

        myDB = new DatabaseHelper(getContext());

        clv = (SwipeMenuListView)view.findViewById(R.id.swipeListCart);
        shoppingList = new ArrayList<>();

        Cursor data = myDB.getAllData();
//        Collections.addAll(shoppingList, )
        Log.e("msg","Data received");
        while(data.moveToNext()){
            shoppingList.add(data.getString(1));
//            adapter = new ArrayAdapter<String>(this,R.layout.checkable_list_layout,R.id.txt_title,shoppingList);
            adapter = new ArrayAdapter<String>(getContext(),android.R.layout.simple_list_item_1,shoppingList);
            clv.setAdapter(adapter);
//            lvCart.setAdapter(adapter);
        }

        SwipeMenuCreator creator = new SwipeMenuCreator() {

            @Override
            public void create(SwipeMenu menu) {
                SwipeMenuItem deleteItem = new SwipeMenuItem(
                        getContext());
                // set item background
                deleteItem.setBackground(new ColorDrawable(Color.rgb(0xff,
                        0xff, 0xff)));
                // set item width
                deleteItem.setWidth(170);
                // set a icon
                deleteItem.setIcon(R.drawable.ic_action_check);
                // add to menu
                menu.addMenuItem(deleteItem);
            }
        };

// set creator
        clv.setMenuCreator(creator);

        clv.setOnMenuItemClickListener(new SwipeMenuListView.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(int position, SwipeMenu menu, int index) {
                switch (index) {
                    case 0:
                        // open
                        Log.e("msg","Clicked item + index");
                        name = clv.getItemAtPosition(position).toString();
                        Cursor item = myDB.getItemID(name);
                        itemID = -1;
                        while(item.moveToNext()){
                            itemID = item.getInt(0);
                        }
                        if(itemID > -1){
                            myDB.deleteData(itemID,name);
                        }
                        shoppingList.remove(position);
                        adapter.notifyDataSetChanged();
                        break;
                }
                // false : close the menu; true : not close the menu
                return false;
            }
        });

        Button but = (Button)view.findViewById(R.id.read);
        but.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getContext(), JSON.class);
                startActivity(intent);
            }
        });
        return view;
    }

    private void addNewItem(String name){
//        String x = input.getText().toString();
        addItem(name);
        shoppingList.add(preferredCase(name.toString()));
        Log.e("msg","item added");
        adapter = new ArrayAdapter<String>(getContext(),android.R.layout.simple_list_item_1,shoppingList);
        clv.setAdapter(adapter);
        clv.invalidateViews();
        Log.e("msg","item refreshed");
    }

    private void displayData(){
        Cursor data = myDB.getAllData();
        while(data.moveToNext()){Log.e("msg","there's new item");
            shoppingList.add(data.getString(1));
            adapter = new ArrayAdapter<String>(getContext(),R.layout.checkable_list_layout,R.id.txt_title,shoppingList);
//            lvCart.setAdapter(adapter);
            clv.setAdapter(adapter);
        }
    }


    public static String preferredCase(String original)//Capitalize first letter and uncapitalize the rest
    {
        if (original.isEmpty())
            return original;

        return original.substring(0, 1).toUpperCase() + original.substring(1).toLowerCase();
    }

    public void addItem(String name){
        boolean isInserted = myDB.insertData(name);
        if(isInserted == true){
            Toast.makeText(getContext(),"Item added", Toast.LENGTH_LONG).show();
        }
        else
            Toast.makeText(getContext(),"Item not added", Toast.LENGTH_LONG).show();


    }

    public void showMessage(String title, String Message){
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        builder.setCancelable(true);
        builder.setTitle(title);
        builder.setMessage(Message);
        builder.show();
        Log.e("msg","Builder created");
    }

    private final SensorEventListener sensorListener = new SensorEventListener() {
        @Override
        public void onSensorChanged(SensorEvent event) {
            float x = event.values[0];
            float y = event.values[1];
            float z = event.values[2];
            acelLast = acelVal;
            acelVal = (float) Math.sqrt((double) (x*x + y*y + z*z));
            float delta = acelVal - acelLast;
            shake = shake * 0.9f + delta; // perform low-cut filter
            if (shake >30 && shoppingList!=null) {
                shoppingList.clear();
                adapter = new ArrayAdapter<String>(getContext(),android.R.layout.simple_list_item_1,shoppingList);
                clv.setAdapter(adapter);
                myDB.removeAll();
                Toast toast =Toast.makeText(getContext(), "List cleared", Toast.LENGTH_LONG);
                toast.show();
            }
        }

        @Override
        public void onAccuracyChanged(Sensor sensor, int accuracy) {

        }
    };

}
