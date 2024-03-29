package com.example.sudipta.gadabout;

import android.content.Context;
import android.content.Intent;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import com.google.android.gms.maps.model.LatLng;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;

public class BrowseMaps extends AppCompatActivity {
    ListView lv;
    int index;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_browse_maps);

        this.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);

        Typeface tf = Typeface.createFromAsset(getAssets(), "exo.otf");
        Button b2 = (Button) findViewById(R.id.load);
        b2.setTypeface(tf);
        Button b3 = (Button) findViewById(R.id.send);
        b3.setTypeface(tf);
        EditText et = (EditText) findViewById(R.id.editText);
        et.setTypeface(tf);
        ArrayAdapter<TreasureMap> adapter;
        DatabaseHandler db = new DatabaseHandler(this);
        ArrayList<TreasureMap> allMaps = db.getAllMaps();
        adapter = new ArrayAdapter<TreasureMap>(this,android.R.layout.simple_list_item_1, allMaps);
        lv = (ListView) findViewById(R.id.listView);
        lv.setAdapter(adapter);
        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {
                index = position;
                System.out.println(index);
            }
        });
        lv.requestFocus();
    }

    public void back(View v) {
        Intent intent = new Intent(this, MainActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
    }

    public void send(View v) {

        DatabaseHandler db = new DatabaseHandler(this);
        ArrayList<TreasureMap> allMaps = db.getAllMaps();
        TreasureMap curr_map = allMaps.get(index);
        ArrayList<String> clues = curr_map.get_all_clue();
        String clueAll = "";
        for (String c : clues){
            clueAll += c + ",";
        }
        String output = curr_map.get_map_name() + ":" + curr_map.get_map_desc() + ":" + clueAll;
        Intent emailIntent = new Intent(Intent.ACTION_SEND);
        emailIntent.setType("vnd.android.cursor.dir/email");
        emailIntent.putExtra(Intent.EXTRA_SUBJECT, "GADABOUT MAP - " + curr_map.get_map_name());
        emailIntent.putExtra(Intent.EXTRA_TEXT, "Ahoy adventurer!\n It's your lucky day, a friend has sent you an exciting new treasure map to try out. To play this map, open Gadabout, then copy and paste the following text into the \"Load Map\" area under \"Send and Load Maps\". \n Happy exploring!\n Map Text:\n" + output);
        startActivity(Intent.createChooser(emailIntent, "Send email..."));
    }

    public void load(View v){
        EditText et = (EditText) findViewById(R.id.editText);
        String input = et.getText().toString();
        String[]components = input.split(":");
        String mapName = components[0];
        String madDesc = components[1];
        String[]cluesArr = components[2].split(",");

        DatabaseHandler db = new DatabaseHandler(this);
       //db.clearTable();
        db.addMap(new TreasureMap(mapName,
                madDesc,
                cluesArr[0],
                cluesArr[1],
                cluesArr[2],
                cluesArr[3],
                cluesArr[4]
                ));
        ArrayList<TreasureMap> allMaps = db.getAllMaps();
        ArrayAdapter<TreasureMap> adapter;
        adapter = new ArrayAdapter<TreasureMap>(this,android.R.layout.simple_list_item_1, allMaps);
        lv = (ListView) findViewById(R.id.listView);
        lv.setAdapter(adapter);
        Toast.makeText(this, mapName +" has been added!", Toast.LENGTH_SHORT).show();
    }
}