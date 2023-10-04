package com.example.primitivephysiolight;

import android.content.Context;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteStatement;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;

public class KeyboardActivity extends AppCompatActivity {

    TextView valueKeyboard;
    EditText editTextName;
    boolean isMillis = true;
    int section = 0;
    int duration, powerOnTime, powerOffTime;
    static SQLiteDatabase database;
    TextView textView;

    public void definePattern(View view) {
        Button buttonPressed = (Button) view;
        textView = findViewById(R.id.textViewSection);
        String tag = (String) buttonPressed.getTag();
        Pattern pattern = new Pattern();
        switch (tag) {
            case "deleteAll":
                valueKeyboard.setText("0");
                break;
            case "deleteDigit":
                if (valueKeyboard.length() < 2) {
                    valueKeyboard.setText("0");
                    break;
                } else {
                    valueKeyboard.setText(valueKeyboard.getText().subSequence(0, valueKeyboard.getText().length() - 1));
                }
                break;
            /*case "changeMeasureUnit":
                if (valueKeyboard.length() == 0) {
                    break;
                }

                if (isMillis == true) {
                    try {
                        d = Integer.parseInt(valueKeyboard.getText().toString()) / 1_000;
                        valueKeyboard.setText(d + "");
                        isMillis = false;
                        buttonPressed.setText("MS");
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                } else {
                    try {
                        d = Integer.parseInt(valueKeyboard.getText().toString()) * 1_000;
                        isMillis = true;
                        buttonPressed.setText("S");
                        if (d > 30000) {
                            valueKeyboard.setText("30000");
                            break;
                        }
                        valueKeyboard.setText(d + "");
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
                break;*/
            /*case "split":
                if (valueKeyboard.length() == 0 || valueKeyboard.getText().toString().equals("0")) {
                    break;
                } else {
                    try {
                        d = 1 / Integer.parseInt(valueKeyboard.getText().toString());
                        valueKeyboard.setText(d + "");
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
                break;*/
            case "one":
            case "two":
            case "three":
            case "four":
            case "five":
            case "six":
            case "seven":
            case "eight":
            case "nine":
                if (section != 0) {
                    if (Integer.parseInt(valueKeyboard.getText().toString().concat(Integer.parseInt(buttonPressed.getText().toString()) + "")) > 30_000) {
                        valueKeyboard.setText("30000");
                        Toast.makeText(getApplicationContext(), "Max: 30 Secondi", Toast.LENGTH_LONG).show();
                    } else {
                        if (!valueKeyboard.getText().toString().equals("0")) {
                            valueKeyboard.setText(valueKeyboard.getText().toString() + String.format("%d", Integer.parseInt(buttonPressed.getText().toString())));
                        } else {
                            valueKeyboard.setText(String.format("%d", Integer.parseInt(buttonPressed.getText().toString())));
                        }
                    }
                } else {
                    if (Integer.parseInt(valueKeyboard.getText().toString().concat(Integer.parseInt(buttonPressed.getText().toString()) + "")) > 30) {
                        valueKeyboard.setText("20");
                        Toast.makeText(getApplicationContext(), "Max: 30 Minuti", Toast.LENGTH_LONG).show();
                    } else {
                        if (!valueKeyboard.getText().toString().equals("0")) {
                            valueKeyboard.setText(valueKeyboard.getText().toString() + String.format("%d", Integer.parseInt(buttonPressed.getText().toString())));
                        } else {
                            valueKeyboard.setText(String.format("%d", Integer.parseInt(buttonPressed.getText().toString())));
                        }
                    }
                }
                break;
            case "zero":
                if (section != 0) {
                    if (Integer.parseInt(valueKeyboard.getText().toString().concat(Integer.parseInt(buttonPressed.getText().toString()) + "")) > 30_000) {
                        valueKeyboard.setText("30000");
                        Toast.makeText(getApplicationContext(), "Max: 30 Secondi", Toast.LENGTH_LONG).show();
                    } else {
                        if (!valueKeyboard.getText().toString().equals("0")) {
                            valueKeyboard.setText(valueKeyboard.getText().toString() + String.format("%d", Integer.parseInt(buttonPressed.getText().toString())));
                        }
                    }
                } else {
                    if (Integer.parseInt(valueKeyboard.getText().toString().concat(Integer.parseInt(buttonPressed.getText().toString()) + "")) > 30) {
                        valueKeyboard.setText("20");
                        Toast.makeText(getApplicationContext(), "Max: 30 Minuti", Toast.LENGTH_LONG).show();
                    } else {
                        if (!valueKeyboard.getText().toString().equals("0")) {
                            valueKeyboard.setText(valueKeyboard.getText().toString() + String.format("%d", Integer.parseInt(buttonPressed.getText().toString())));
                        }
                    }
                }
                break;
            case "back":
                switch (section) {
                    case 0:
                        Toast.makeText(this, "Sei già nella prima schermata", Toast.LENGTH_LONG).show();
                        break;
                    case 1:
                        textView.setText("Durata:");
                        valueKeyboard.setText("0");
                        section--;
                        break;
                    case 2:
                        textView.setText("Acceso:");
                        valueKeyboard.setText("0");
                        section--;
                        break;
                }
                break;
            case "ahead":
                switch (section) {
                    case 0:
                        if (Integer.parseInt(valueKeyboard.getText().toString()) > 0) {
                            textView.setText("Acceso:");
                            duration = Integer.parseInt(valueKeyboard.getText().toString());
                            MainActivity.duration = Integer.parseInt(valueKeyboard.getText().toString());
                            valueKeyboard.setText("0");
                            section++;
                        } else {
                            Toast.makeText(this, "Durata Non Valida", Snackbar.LENGTH_LONG).show();
                        }
                        break;
                    case 1:
                        if (Integer.parseInt(valueKeyboard.getText().toString()) > 32) {
                            textView.setText("Spento:");
                            powerOnTime = Integer.parseInt(valueKeyboard.getText().toString());
                            MainActivity.powerOnTime = Integer.parseInt(valueKeyboard.getText().toString());
                            valueKeyboard.setText("0");
                            section++;
                        } else {
                            Toast.makeText(this, "Tempo di Accensione Non Valido", Snackbar.LENGTH_LONG).show();
                        }
                        break;
                    case 2:
                        if (Integer.parseInt(valueKeyboard.getText().toString()) > 32) {
                            pattern.setPowerOffTime(Integer.parseInt(valueKeyboard.getText().toString()));
                            MainActivity.powerOffTime = Integer.parseInt(valueKeyboard.getText().toString());
                            if (!editTextName.getText().toString().isEmpty()) {
                                Intent intent = new Intent(KeyboardActivity.this, MainActivity.class);
                                pattern.setPowerOnTime(powerOnTime);
                                pattern.setDuration(duration);
                                pattern.setName(editTextName.getText().toString());
                                MainActivity.arrayPattern.add(pattern);

                                String sql = "INSERT INTO pattern (name, duration, powerOnTime, powerOffTime) VALUES (?, ?, ?, ?)";
                                SQLiteStatement statement = MainActivity.databasePattern.compileStatement(sql);
                                statement.bindString(1, MainActivity.arrayPattern.get(MainActivity.arrayPattern.size() - 1).getName());
                                statement.bindLong(2, MainActivity.arrayPattern.get(MainActivity.arrayPattern.size() - 1).getDuration());
                                statement.bindLong(3, MainActivity.arrayPattern.get(MainActivity.arrayPattern.size() - 1).getPowerOnTime());
                                statement.bindLong(4, MainActivity.arrayPattern.get(MainActivity.arrayPattern.size() - 1).getPowerOffTime());

                                statement.execute();

                                MainActivity.isManual = false;
                                Log.i("note", MainActivity.duration + " " +
                                        MainActivity.powerOffTime + " " +
                                        MainActivity.powerOnTime);
                                Toast.makeText(this, editTextName.getText().toString() + " Impostato", Snackbar.LENGTH_LONG).show();
//                                Log.i("note", Arrays.toString(MainActivity.arrayPattern.toArray()));
                                startActivity(intent);
                            } else {
                                Toast.makeText(this, "Nome Non Valido", Snackbar.LENGTH_LONG).show();
                            }
                        } else {
                            Toast.makeText(this, "Tempo di Spegnimento Non Valido", Snackbar.LENGTH_LONG).show();
                        }
                        break;
                }
                break;
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_keyboard);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        /*FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });*/

        valueKeyboard = findViewById(R.id.valueKeyboard);
        editTextName = findViewById(R.id.editText);
    }
}