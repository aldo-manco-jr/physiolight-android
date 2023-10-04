package com.example.primitivephysiolight;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteStatement;
import android.inputmethodservice.Keyboard;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Arrays;

public class ListPattern extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_pattern);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        final ArrayList<String> arrayNamePatterns = new ArrayList<>();

        /*Cursor cursor = KeyboardActivity.database.rawQuery("SELECT * FROM pattern", null);

        int nameIndex = cursor.getColumnIndex("name"),
                durationIndex = cursor.getColumnIndex("duration"),
                powerOnTimeIndex = cursor.getColumnIndex("powerOnTime"),
                powerOffTimeIndex = cursor.getColumnIndex("powerOffTime");

        cursor.moveToFirst();

        while (cursor != null) {
            arrayNamePatterns.add(cursor.getString(nameIndex));
            MainActivity.arrayPattern.add(new Pattern(cursor.getString(nameIndex), cursor.getInt(durationIndex), cursor.getInt(powerOnTimeIndex), cursor.getInt(powerOffTimeIndex)));
            cursor.moveToNext();
        }*/

        for (Pattern tmp : MainActivity.arrayPattern) {
            arrayNamePatterns.add(tmp.getName());
        }

        final ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, arrayNamePatterns);
        ListView listPattern = findViewById(R.id.listPattern);
        listPattern.setAdapter(adapter);

        listPattern.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                MainActivity.duration = MainActivity.arrayPattern.get(position).getDuration();
                MainActivity.powerOnTime = MainActivity.arrayPattern.get(position).getPowerOnTime();
                MainActivity.powerOffTime = MainActivity.arrayPattern.get(position).getPowerOffTime();
                MainActivity.isManual = false;
                Toast.makeText(getApplicationContext(), arrayNamePatterns.get(position) + " Impostata", Toast.LENGTH_LONG).show();
                Intent intent = new Intent(ListPattern.this, MainActivity.class);
                startActivity(intent);
            }
        });

        listPattern.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                final int index = position;
                final long id2 = id;
                new AlertDialog.Builder(ListPattern.this)
                        .setIcon(android.R.drawable.ic_dialog_alert)
                        .setTitle("Sei Sicuro?")
                        .setMessage("Se premi \"Si\", il pattern verra cancellato e non potra essere recuperato.")
                        .setPositiveButton("Si", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                Toast.makeText(getApplicationContext(), "Pattern Cancellato", Toast.LENGTH_LONG).show();

                                //db.delete("tablename","id=? and name=?",new String[]{"1","jack"});
                               // String sql = "DELETE FROM pattern WHERE name = " + arrayNamePatterns.get(index);
                                //MainActivity.databasePattern.execSQL(sql);
                                System.out.println(index+1);
                                MainActivity.databasePattern.delete("pattern", "name=?", new String[]{arrayNamePatterns.get(index)});

                                arrayNamePatterns.remove(index);
                                MainActivity.arrayPattern.remove(index);
                                adapter.notifyDataSetChanged();
                            }
                        })
                        .setNegativeButton("No", null)
                        .show();
                return true;
            }
        });

        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
//                        .setAction("Action", null).show();
                Intent intent = new Intent(ListPattern.this, KeyboardActivity.class);
                startActivity(intent);
            }
        });
    }

}
