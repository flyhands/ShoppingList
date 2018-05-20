package android.vogella.de.shoppinglist;

import android.content.Intent;
import android.database.Cursor;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.os.Message;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import android.app.AlertDialog;
import android.widget.EditText;
import android.content.DialogInterface;
import android.content.Context;
import android.content.SharedPreferences;
import android.app.Activity;
import android.widget.Toast;

import com.baoyz.swipemenulistview.SwipeMenu;
import com.baoyz.swipemenulistview.SwipeMenuCreator;
import com.baoyz.swipemenulistview.SwipeMenuItem;
import com.baoyz.swipemenulistview.SwipeMenuListView;

public class MainActivity extends AppCompatActivity {

    ArrayList<String> shoppingList = null;
    ArrayAdapter<String> adapter = null;
//    ListView lvCart = null;
    DatabaseHelper myDB;
    SwipeMenuListView clv;
    String name;
    int itemID;
    private SensorManager sm;
    private float acelVal;
    private float acelLast;
    private float shake;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        sm = (SensorManager)getSystemService(Context.SENSOR_SERVICE);
        sm.registerListener(sensorListener, sm.getDefaultSensor(Sensor.TYPE_ACCELEROMETER), SensorManager.SENSOR_DELAY_NORMAL);
        shake = 0.00f;
        acelVal = SensorManager.GRAVITY_EARTH;
        acelLast = SensorManager.GRAVITY_EARTH;



        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        myDB = new DatabaseHelper(this);

//        lvCart = (ListView)findViewById(R.id.listView);
        clv = (SwipeMenuListView)findViewById(R.id.swipeListCart);
        shoppingList = new ArrayList<>();
//        Collections.addAll(shoppingList, "Eggs", "Yogurt", "Milk", "Bananas", "Apples", "Tide with bleach", "Cascade");
//        shoppingList.addAll(Arrays.asList("Napkins", "Dog food", "Chapstick", "Bread"));
//        shoppingList.add("Sunscreen");
//        shoppingList.add("Toothpaste");


//        adapter = new ArrayAdapter(this, android.R.layout.simple_list_item_1, shoppingList);
//        lvCart = (ListView) findViewById(R.id.listView);
//        lvCart.setAdapter(adapter);
        Cursor data = myDB.getAllData();
//        Collections.addAll(shoppingList, )
        Log.e("msg","Data received");
//        displayData();

//        if(data.getCount() == 0){
//        //show message
//        showMessage("Hey","Your list is empty! Let's add something");
//        Log.e("msg","no item");
//
//        return;
//
//        }
//        else{                    Log.e("msg","there's new item");
//
//            while(data.moveToNext()){
//            shoppingList.add(data.getString(1));
//            adapter = new ArrayAdapter<String>(this,R.layout.checkable_list_layout,R.id.txt_title,shoppingList);
//            lvCart.setAdapter(adapter);
//            }
//        }
        while(data.moveToNext()){
            shoppingList.add(data.getString(1));
//            adapter = new ArrayAdapter<String>(this,R.layout.checkable_list_layout,R.id.txt_title,shoppingList);
            adapter = new ArrayAdapter<String>(this,android.R.layout.simple_list_item_1,shoppingList);
            clv.setAdapter(adapter);
//            lvCart.setAdapter(adapter);
        }

        SwipeMenuCreator creator = new SwipeMenuCreator() {

            @Override
            public void create(SwipeMenu menu) {
                // create "open" item
//                SwipeMenuItem openItem = new SwipeMenuItem(
//                        getApplicationContext());
//                // set item background
//                openItem.setBackground(new ColorDrawable(Color.rgb(0xC9, 0xC9,
//                        0xCE)));
//                // set item width
//                openItem.setWidth(170);
//                // set item title
//                openItem.setTitle("Open");
//                // set item title fontsize
//                openItem.setTitleSize(18);
//                // set item title font color
//                openItem.setTitleColor(Color.WHITE);
//                // add to menu
//                menu.addMenuItem(openItem);

                // create "delete" item
                SwipeMenuItem deleteItem = new SwipeMenuItem(
                        getApplicationContext());
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




        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
                builder.setTitle("Add Item");
                final EditText input = new EditText(MainActivity.this);
                builder.setView(input);
                builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    String name = input.getText().toString();
                    addItem(name);
//                    displayData();
//                    shoppingList.add(preferredCase(input.getText().toString()));
                    shoppingList.add(preferredCase(name.toString()));
                    Log.e("msg","item added");
//                    Collections.sort(shoppingList);

//                    clv.setAdapter(adapter);
                    clv.invalidateViews();
//                    lvCart.setAdapter(adapter);
//                    lvCart.invalidateViews();
                    Log.e("msg","item refreshed");

                }
            });
            builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.cancel();
                }
            });
            builder.show();

            }
        });


        Button but = findViewById(R.id.read);
        but.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), JSON.class);
                startActivity(intent);
            }
        });
    }

    private void displayData(){
        Cursor data = myDB.getAllData();
        while(data.moveToNext()){Log.e("msg","there's new item");
            shoppingList.add(data.getString(1));
            adapter = new ArrayAdapter<String>(this,R.layout.checkable_list_layout,R.id.txt_title,shoppingList);
//            lvCart.setAdapter(adapter);
            clv.setAdapter(adapter);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }
        if (id == R.id.action_sort) {
            Collections.sort(shoppingList);
//            lvCart.setAdapter(adapter);
            clv.setAdapter(adapter);
            return true;
        }
//        if (id == R.id.action_add){
//            AlertDialog.Builder builder = new AlertDialog.Builder(this);
//            builder.setTitle("Add Item");
//            final EditText input = new EditText(this);
//            builder.setView(input);
//            builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
//                @Override
//                public void onClick(DialogInterface dialog, int which) {
//                    String name = input.getText().toString();
//                    addItem(name);
////                    displayData();
////                    shoppingList.add(preferredCase(input.getText().toString()));
////                    shoppingList.add(preferredCase(name.toString()));
//                    Log.e("msg","item added");
////                    Collections.sort(shoppingList);
//                    lvCart.setAdapter(adapter);
//                    lvCart.invalidateViews();
//                    Log.e("msg","item refreshed");
//
//                }
//            });
//            builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
//                @Override
//                public void onClick(DialogInterface dialog, int which) {
//                    dialog.cancel();
//                }
//            });
//            builder.show();
//            return true;
//
//        }
        if (id == R.id.action_clear){

            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setTitle("Clear Entire List");
            builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    shoppingList.clear();
//                    lvCart.setAdapter(adapter);
                    clv.setAdapter(adapter);
                    myDB.removeAll();
                }
            });
            builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.cancel();
                }
            });
            builder.show();
            return true;
        }

        return super.onOptionsItemSelected(item);
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
            Toast.makeText(MainActivity.this,"Item added", Toast.LENGTH_LONG).show();
        }
        else
            Toast.makeText(MainActivity.this,"Item not added", Toast.LENGTH_LONG).show();


    }

    public void showMessage(String title, String Message){
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
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
            if (shake >12) {
                Toast toast =Toast.makeText(getApplicationContext(), "DONT", Toast.LENGTH_LONG);
                toast.show();
            }
        }

        @Override
        public void onAccuracyChanged(Sensor sensor, int accuracy) {

        }
    };
}
