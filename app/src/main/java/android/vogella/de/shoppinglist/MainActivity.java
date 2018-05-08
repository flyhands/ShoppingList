package android.vogella.de.shoppinglist;

import android.database.Cursor;
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
import android.widget.ListView;

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

public class MainActivity extends AppCompatActivity {

    ArrayList<String> shoppingList = null;
    ArrayAdapter<String> adapter = null;
    ListView lvCart = null;
    DatabaseHelper myDB;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        myDB = new DatabaseHelper(this);

        lvCart = (ListView)findViewById(R.id.listView);
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
            adapter = new ArrayAdapter<String>(this,R.layout.checkable_list_layout,R.id.txt_title,shoppingList);
            lvCart.setAdapter(adapter);
            }




        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
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
                    lvCart.setAdapter(adapter);
                    lvCart.invalidateViews();
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
    }

    private void displayData(){
        Cursor data = myDB.getAllData();
        while(data.moveToNext()){Log.e("msg","there's new item");
            shoppingList.add(data.getString(1));
            adapter = new ArrayAdapter<String>(this,R.layout.checkable_list_layout,R.id.txt_title,shoppingList);
            lvCart.setAdapter(adapter);
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
            lvCart.setAdapter(adapter);
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
                    lvCart.setAdapter(adapter);
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

}
