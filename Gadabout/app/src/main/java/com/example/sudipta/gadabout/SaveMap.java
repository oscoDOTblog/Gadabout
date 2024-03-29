package com.example.sudipta.gadabout;

import android.content.Intent;
import android.graphics.Typeface;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.method.ScrollingMovementMethod;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import org.w3c.dom.Text;

import java.util.ArrayList;

public class SaveMap extends AppCompatActivity {
    TreasureMap savedMap;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);
        setContentView(R.layout.activity_save_map);
        TextView clues = (TextView) findViewById(R.id.textView);
        clues.setMovementMethod(new ScrollingMovementMethod());
        //Intent i = getIntent();
        savedMap = (TreasureMap)getIntent().getSerializableExtra("savedMap");
        ArrayList<String> clue_desc = savedMap.get_all_clue();
        String clueDisplay = "YOUR CLUES:\n";
        clueDisplay+="Clue 01: "+ clue_desc.get(0) +"\n";
        clueDisplay+="Clue 02: "+ clue_desc.get(1) +"\n";
        clueDisplay+="Clue 03: "+ clue_desc.get(2) +"\n";
        clueDisplay+="Clue 04: "+ clue_desc.get(3) +"\n";
        clueDisplay+="Clue 05: "+ clue_desc.get(4) +"\n";
        clues.setText(clueDisplay);

        // Set fancy fonts!
        Typeface tf = Typeface.createFromAsset(getAssets(), "exo.otf");
        TextView tv1 = (TextView) findViewById(R.id.textView);
        tv1.setTypeface(tf);
        Button b2 = (Button) findViewById(R.id.back);
        b2.setTypeface(tf);
        Button b3 = (Button) findViewById(R.id.play);
        b3.setTypeface(tf);
        EditText et= (EditText) findViewById(R.id.editText2);
        et.setTypeface(tf);
    }

    public void back(View v){
        Intent intent = new Intent(this, MainActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
    }
    public void save(View v){
        EditText mapNameET = (EditText)findViewById(R.id.editText2);
        EditText mapDescET = (EditText)findViewById(R.id.mapDesc);
        String mapName = mapNameET.getText().toString();
        String mapDesc = mapDescET.getText().toString();
        if (!mapName.equals("")&&!mapDesc.equals("")){
            savedMap.set_map_name(mapName);
            savedMap.set_map_desc(mapDesc);
            DatabaseHandler db = new DatabaseHandler(this);
            db.addMap(savedMap);
            Toast.makeText(this, "New map saved!", Toast.LENGTH_SHORT).show();
            Intent intent = new Intent(this, MainActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);
        }
        else{
            if(mapName.equals("")){
                Toast.makeText(this, "Write a map name!", Toast.LENGTH_SHORT).show();
            }
            else if(mapDesc.equals("")){
                Toast.makeText(this, "Write a map description!", Toast.LENGTH_SHORT).show();
            }
        }
    }

    @Override
    public void onBackPressed() {
        Intent intent = new Intent(this, MainActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
    }
}
