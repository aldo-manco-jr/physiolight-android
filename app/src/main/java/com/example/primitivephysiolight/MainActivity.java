package com.example.primitivephysiolight;

import android.Manifest;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Camera;
import android.hardware.camera2.CameraAccessException;
import android.hardware.camera2.CameraManager;
import android.inputmethodservice.Keyboard;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.view.View;
import android.support.v4.view.GravityCompat;
import android.support.v7.app.ActionBarDrawerToggle;
import android.view.MenuItem;
import android.support.design.widget.NavigationView;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;

import android.support.v4.widget.ImageViewCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.GridLayout;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    private ImageView imageFlashlight;
    private static final int CAMERA_REQUEST = 50;
    private boolean flashLightStatus = false;
    static int duration, powerOffTime, powerOnTime;
    static boolean isManual = true;
    static boolean isWorking = false;
    boolean isOn = false;
    static FloatingActionButton fab;
    static SQLiteDatabase databasePattern;
    static ArrayList<Pattern> arrayPattern = new ArrayList<>();
    CountDownTimer start = null;
    Runnable runnable;

    /*@Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if (requestCode == CAMERA_REQUEST) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

            }
        }
    }*/

    public void launchKeyboard(View view) {
        Intent intent = new Intent(MainActivity.this, ListPattern.class);
        startActivity(intent);
    }

    public void launchManual(View view) {
        isManual = true;
        Toast.makeText(this, "Modalità Manuale Impostata", Snackbar.LENGTH_LONG).show();
    }

    public void power(View view) {
        CameraManager cameraManager = (CameraManager) getSystemService(Context.CAMERA_SERVICE);
        String cameraId;

        if (flashLightStatus == false) {
            try {
                imageFlashlight.setImageResource(R.drawable.button_on);
                imageFlashlight.animate().translationY(-225);
                cameraId = cameraManager.getCameraIdList()[0];
                cameraManager.setTorchMode(cameraId, true);
                flashLightStatus = true;
            } catch (CameraAccessException cae) {
                Toast.makeText(this, cae.getMessage(), Toast.LENGTH_LONG).show();
            } catch (Exception e) {
                Toast.makeText(this, e.getMessage(), Toast.LENGTH_LONG).show();
            }
        } else {
            try {
                imageFlashlight.setImageResource(R.drawable.button_off);
                imageFlashlight.animate().translationY(0);
                cameraId = cameraManager.getCameraIdList()[0];
                cameraManager.setTorchMode(cameraId, false);
                flashLightStatus = false;
            } catch (CameraAccessException cae) {
                Toast.makeText(this, cae.getMessage(), Toast.LENGTH_LONG).show();
            } catch (Exception e) {
                Toast.makeText(this, e.getMessage(), Toast.LENGTH_LONG).show();
            }
        }
    }

    private void flashLightOn() {
        CameraManager cameraManager = (CameraManager) getSystemService(Context.CAMERA_SERVICE);

        try {
            String cameraId = cameraManager.getCameraIdList()[0];
            cameraManager.setTorchMode(cameraId, true);
            flashLightStatus = true;
        } catch (CameraAccessException e) {
        }
    }

    private void flashLightOff() {
        CameraManager cameraManager = (CameraManager) getSystemService(Context.CAMERA_SERVICE);

        try {
            String cameraId = cameraManager.getCameraIdList()[0];
            cameraManager.setTorchMode(cameraId, false);
            flashLightStatus = false;
        } catch (CameraAccessException e) {
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        /*
         year    the year minus 1900.
     * @param   month   the month between 0-11.
     * @param   date    the day of the month between 1-31.
     * @param   hrs     the hours between 0-23.
     * @param   min     the minutes between 0-59.
         */

        if (arrayPattern.isEmpty()) {
            databasePattern = this.openOrCreateDatabase("Pattern", MODE_PRIVATE, null);
            databasePattern.execSQL("CREATE TABLE IF NOT EXISTS pattern (id INTEGER PRIMARY KEY, name VARCHAR, duration INTEGER, powerOnTime INTEGER, powerOffTime INTEGER)");

            Cursor cursor = databasePattern.rawQuery("SELECT * FROM pattern", null);
            int nameIndex = cursor.getColumnIndex("name");
            int durationIndex = cursor.getColumnIndex("duration");
            int powerOnTimeIndex = cursor.getColumnIndex("powerOnTime");
            int powerOffTimeIndex = cursor.getColumnIndex("powerOffTime");

            if (cursor.moveToFirst()) {
                Pattern pattern;
                do {
                    pattern = new Pattern(cursor.getString(nameIndex), cursor.getInt(durationIndex), cursor.getInt(powerOnTimeIndex), cursor.getInt(powerOffTimeIndex));
                    arrayPattern.add(pattern);
                } while (cursor.moveToNext());
            }
        }

        fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG).setAction("Action", null).show();
                Intent intent = new Intent(MainActivity.this, KeyboardActivity.class);
                startActivity(intent);
            }
        });
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        NavigationView navigationView = findViewById(R.id.nav_view);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();
        navigationView.setNavigationItemSelectedListener(this);

        ActivityCompat.requestPermissions(MainActivity.this, new String[]{Manifest.permission.CAMERA}, CAMERA_REQUEST);

        imageFlashlight = findViewById(R.id.imageViewPower);
        final boolean hasCameraFlash = getPackageManager().hasSystemFeature(PackageManager.FEATURE_CAMERA_FLASH);
        boolean isEnabled = ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED;
        final Handler handler = new Handler();

        if (!isManual) {
            start = new CountDownTimer(duration * 60 * 1000, duration * 60 * 1000) {
                @Override
                public void onTick(long millisUntilFinished) {
                    Log.i("note", "ewewe");
                    runnable = new Runnable() {
                        @Override
                        public void run() {
                            isWorking = true;
                            if (!isOn) {
                                flashLightOn();
                                isOn = true;
                                Log.i("note", "on");
                                handler.postDelayed(this, powerOnTime);
                            } else {
                                flashLightOff();
                                isOn = false;
                                Log.i("note", "off");
                                handler.postDelayed(this, powerOffTime);
                            }
                        }
                    };
                    runnable.run();
                }

                @Override
                public void onFinish() {
                    imageFlashlight.setImageResource(R.drawable.button_off);
                    imageFlashlight.animate().translationY(0);
                    start.cancel();
                    handler.removeCallbacks(runnable);
                    flashLightOff();
                    isWorking=false;
                }
            };



            /*if (onOff == false) {
                start = new CountDownTimer(duration * 60 * 1000, smaller) {
                    public void onTick(long millisecondsUntil) {
                        isWorking = true;
                        if (!isOn) {
                            flashLightOn();
                            isOn = true;
                            Log.i("note", "on");
                        } else {
                            flashLightOff();
                            handler.postDelayed(new Runnable() {
                                @Override
                                public void run() {
                                    flashLightOff();
                                }
                            }, bigger - smaller);
                            Log.i("note", bigger - smaller + "");
                            isOn = false;
                            Log.i("note", "off");
                        }
                    }

                    public void onFinish() {
                        flashLightOff();
                        isWorking = false;
                    }
                };
            } else {


                *//*start = new CountDownTimer(duration * 60 * 1000, smaller) {
                    public void onTick(long millisecondsUntil) {
                        isWorking = true;
                        if (!isOn) {
                            flashLightOn();
                            handler.postDelayed(new Runnable() {
                                @Override
                                public void run() {
                                    flashLightOn();
                                }
                            }, bigger - smaller);
                            isOn = true;
                            Log.i("note", "on");
                        } else {
                            flashLightOff();
                            Log.i("note", bigger - smaller + "");
                            isOn = false;
                            Log.i("note", "off");
                        }
                    }

                    public void onFinish() {
                        flashLightOff();
                        isWorking = false;
                    }
                };*//*
            }*/
        }

        imageFlashlight.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (hasCameraFlash) {
                    if (isWorking == false && isManual == false) {
                        imageFlashlight.setImageResource(R.drawable.button_on);
                        imageFlashlight.animate().translationY(-225);
                        start.start();
                    } else if (isManual == true) {
                        power(null);
                    } else if (isWorking == true && isManual == false) {
                        start.cancel();
                        handler.removeCallbacks(runnable);
                        imageFlashlight.setImageResource(R.drawable.button_off);
                        imageFlashlight.animate().translationY(0);
                        flashLightOff();
                        isWorking = false;
                    }
                } else {
                    Toast.makeText(MainActivity.this, "No flash available on your device",
                            Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
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

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_home) {
            // Handle the camera action
        } else if (id == R.id.nav_gallery) {

        } else if (id == R.id.nav_slideshow) {

        } else if (id == R.id.nav_tools) {

        } else if (id == R.id.nav_share) {

        } else if (id == R.id.nav_send) {

        }

        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
}