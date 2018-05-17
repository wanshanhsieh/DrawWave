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

import com.jjoe64.graphview.GraphView;
import com.jjoe64.graphview.series.DataPoint;
import com.jjoe64.graphview.series.LineGraphSeries;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.Random;
import java.lang.*;

public class MainActivity extends AppCompatActivity {

    /**
     * On-screen elements
     */
    Button button_GenFile, button_DrawWave;

    /**
     * buffer for reading file
     */
    StringBuilder text = new StringBuilder();

    private FragmentManager fragManager;
    FragmentTransaction trans;
    BlankFragment graph_Fragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        button_GenFile = findViewById(R.id.button_GenFile);
        button_DrawWave = findViewById(R.id.button_DrawWave);

        fragManager = getSupportFragmentManager();

        /**
         * Press Read File and Draw, read file and print the file content on screen
         */
        button_DrawWave.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                /* read file from the sdcard */
                try {
                    File download = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS);
                    File file = new File(download,"testFiles.txt");

                    BufferedReader br = new BufferedReader(new FileReader(file));
                    String line;
                    while ((line = br.readLine()) != null) {
                        text.append(line);
                        text.append('.');
                    }
                    br.close() ;
                }catch (IOException e) {
                    e.printStackTrace();
                }
                /* pass data into graph_Fragment */
                graph_Fragment = BlankFragment.newInstance(text.toString(), "b");
                trans = fragManager.beginTransaction();
                trans.add(R.id.fragment_container, graph_Fragment, "");
                trans.commit();

            }
        });
    }

}
