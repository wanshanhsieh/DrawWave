package com.example.user.drawwave;

import android.os.Environment;
import android.os.Handler;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
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
    Button button_GenFile, button_DrawWave;

    /**
     * fragment
     */
    private FragmentManager fragManager;
    FragmentTransaction trans;
    BlankFragment graph_Fragment;

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

        button_GenFile = findViewById(R.id.button_GenFile);
        button_DrawWave = findViewById(R.id.button_DrawWave);

        fragManager = getSupportFragmentManager();

        dataPair = new ArrayList<DataPair>();

        /**
         * set Firebase
         */
        Firebase.setAndroidContext(MainActivity.this);
        /** set Firebase url */
        url = "https://drawwave-613f4.firebaseio.com/DataPair";
        mRef = new Firebase(url);
        mDatabase = FirebaseDatabase.getInstance();
        dataRef = mDatabase.getReference("DataPair");

        /**
         * Initialize graph fragment
         */
        graph_Fragment = BlankFragment.newInstance("a", "b");
        ((BlankFragment) graph_Fragment).bindGraphList(dataPair);
        trans = fragManager.beginTransaction();
        trans.add(R.id.fragment_container, graph_Fragment, "");
        trans.commit();

        /**
         * Press Read File and Draw, read file and print the file content on screen
         */
        button_DrawWave.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                // String text = ReadFromFile("testFiles.txt");
                // PreProcessingData(text);
                /* Toast toast = Toast.makeText(MainActivity.this,
                        String.valueOf(dataPair.size()), Toast.LENGTH_SHORT);
                toast.show(); */
                /* pass data into graph_Fragment */
                ((BlankFragment) graph_Fragment).onRefresh();
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

}
