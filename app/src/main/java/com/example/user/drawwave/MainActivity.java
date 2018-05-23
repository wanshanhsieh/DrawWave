package com.example.user.drawwave;

import android.app.ActivityManager;
import android.content.Context;
import android.content.pm.ConfigurationInfo;
import android.graphics.drawable.ColorDrawable;
import android.os.Environment;
import android.os.Handler;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;

import com.firebase.client.ChildEventListener;
import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.firebase.client.ValueEventListener;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.jjoe64.graphview.GraphView;
import com.jjoe64.graphview.series.DataPoint;
import com.jjoe64.graphview.series.LineGraphSeries;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.lang.*;

public class MainActivity extends AppCompatActivity {

    /**
     * On-screen elements
     */
    Button button_Demo1, button_Demo2;
    TextView text_GLver;
    Context mContext;

    /**
     * fragment
     */
    private FragmentManager fragManager;
    FragmentTransaction trans;
    BlankFragment graph_Fragment;
    OpenGLFragment graph3d_Fragment;

    /**
     * graph data
     */
    public List<DataPair> dataPair;

    /**
     * For Firebase Database
     */
    Firebase mRef;
    String url;
    FirebaseDatabase mDatabase;
    DatabaseReference dataRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        button_Demo1 = findViewById(R.id.button_Demo1);
        button_Demo2 = findViewById(R.id.button_Demo2);
        text_GLver = findViewById(R.id.text_GLver);
        mContext = MainActivity.this;
        text_GLver.setText("OpenGL version: " + currentGLVersion(mContext));

        fragManager = getSupportFragmentManager();

        dataPair = new ArrayList<DataPair>();

        /** set Firebase */
        Firebase.setAndroidContext(MainActivity.this);
        /** set Firebase url */
        url = "https://drawwave-613f4.firebaseio.com/DataPair";
        mRef = new Firebase(url);
        mDatabase = FirebaseDatabase.getInstance();
        dataRef = mDatabase.getReference("DataPair");

        /** Initialize graph fragment */
        graph_Fragment = new BlankFragment(); // BlankFragment.newInstance("a", "b");
        graph3d_Fragment = new OpenGLFragment();

        button_Demo1.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                // trans = fragManager.beginTransaction();
                // trans.add(R.id.fragment_container, graph_Fragment, "");
                // trans.commit();
                ((BlankFragment) graph_Fragment).bindGraphList(dataPair);
                graph_Fragment.show(getSupportFragmentManager(), "blank_fragment");
                // ((BlankFragment) graph_Fragment).onRefresh();
            }
        });

        button_Demo2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                graph3d_Fragment.show(getSupportFragmentManager(), "OpenGL_fragment");
            }
        });

        mRef.addChildEventListener(
                new ChildEventListener() {
                    @Override
                    public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                        PreProcessingData_firebase((String)dataSnapshot.child("xyz").getValue());
                    }

                    @Override
                    public void onChildChanged(DataSnapshot dataSnapshot, String s) {

                    }

                    @Override
                    public void onChildRemoved(DataSnapshot dataSnapshot) {

                    }

                    @Override
                    public void onChildMoved(DataSnapshot dataSnapshot, String s) {

                    }

                    @Override
                    public void onCancelled(FirebaseError firebaseError) {

                    }
                }
        );
    }

    public void PreProcessingData_file(String Text)
    {
        String text = Text;
        text = text.trim();
        String[] items = text.split("\\.");
        for (String item : items)
        {
            String[] time_y = item.split(",");
            DataPair tmp = new DataPair();
            tmp.setValue(time_y[0], time_y[1], "0");
            dataPair.add(tmp);
        }

    }

    public void PreProcessingData_firebase(String Text)
    {
        String text = Text;
        text = text.trim();
        String[] items = text.split(",");
        DataPair tmp = new DataPair();
        tmp.setValue(items[0], items[1], "0");
        dataPair.add(tmp);
    }

    public String ReadFromFile(String fileName)
    {
        /* buffer for reading file */
        StringBuilder text = new StringBuilder();

        /* read file from the sdcard */
        try {
            File download = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS);
            File file = new File(download,fileName);

            BufferedReader br = new BufferedReader(new FileReader(file));
            String line;
            while ((line = br.readLine()) != null) {
                text.append(line);
                text.append('.');
            }
            br.close() ;
        }catch (IOException e) {
            Toast toast = Toast.makeText(MainActivity.this,
                    "Cannot find file testsFiles.txt", Toast.LENGTH_SHORT);
            toast.show();
            e.printStackTrace();
        }
        return text.toString();
    }

    public static String currentGLVersion(Context context){
        ActivityManager am = (ActivityManager)
                context.getSystemService(Context.ACTIVITY_SERVICE);
        ConfigurationInfo info = am.getDeviceConfigurationInfo();
        String currentVersion = Integer.toHexString(info.reqGlEsVersion);
        return currentVersion;
    }

}
