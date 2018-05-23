package com.example.user.drawwave;


import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.jjoe64.graphview.GraphView;
import com.jjoe64.graphview.series.DataPoint;
import com.jjoe64.graphview.series.LineGraphSeries;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;

import static java.lang.String.valueOf;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link BlankFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class BlankFragment extends DialogFragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    /**
     * on-screen elements
     */
    TextView text_showMsg;
    Button button_close;

    /**
     * graph data
     */
    static List<DataPair> GraphData;

    /**
     * graph drawing
     */
    private final Handler mHandler = new Handler();
    private Runnable mTimer;
    private LineGraphSeries<DataPoint> mSeries;
    private int graphLastIndex = 0;

    public BlankFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment BlankFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static BlankFragment newInstance(String param1, String param2) {
        BlankFragment fragment = new BlankFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setCancelable(true); // if touch non-window area, no reaction
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        /** Inflate the layout for this fragment */
        View rootView = inflater.inflate(R.layout.fragment_blank, container, false);
        text_showMsg = rootView.findViewById(R.id.text_ShowMsg);
        button_close = rootView.findViewById(R.id.button_Close);
        getDialog().requestWindowFeature(Window.FEATURE_NO_TITLE);

        /** Initialize graph view */
        GraphView graph = (GraphView) rootView.findViewById(R.id.graph);
        mSeries = new LineGraphSeries<>();
        graph.addSeries(mSeries);
        graph.getViewport().setXAxisBoundsManual(true);
        graph.getViewport().setMinX(0);
        graph.getViewport().setMaxX(20);

        /** press close, reset and close this DialogFragment */
        button_close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                graphLastIndex = 0;
                GraphData = null;
                dismiss();
            }
        });

        return rootView;
    }

    @Override
    public void onResume() {
        super.onResume();
        mTimer = new Runnable() {
            @Override
            public void run() {
                //text_showMsg.setText("GraphData size = " + String.valueOf(GraphData.size()) + ", Last index = " + String.valueOf(graphLastIndex));
                //text_showMsg.setText("1: GraphData size = " + String.valueOf(GraphData.size()) + ", Last index = " + String.valueOf(graphLastIndex));
                if (GraphData.size() > 0)
                {
                    //text_showMsg.setText("2: GraphData size = " + String.valueOf(GraphData.size()) + ", Last index = " + String.valueOf(graphLastIndex));
                    if (graphLastIndex < GraphData.size()) {
                        text_showMsg.setText(GraphData.get(graphLastIndex).debugx() + " & " + GraphData.get(graphLastIndex).debugy());
                        mSeries.appendData(new DataPoint(GraphData.get(graphLastIndex).getx(), GraphData.get(graphLastIndex).gety()), true, 40);
                        //text_showMsg.setText("3: GraphData size = " + String.valueOf(GraphData.size()) + ", Last index = " + String.valueOf(graphLastIndex));
                        graphLastIndex += 1;
                        mHandler.postDelayed(this, 200);
                    }
                }
                mHandler.postDelayed(mTimer, 1000);
            }
        };
        mTimer.run();
    }

    @Override
    public void onPause() {
        mHandler.removeCallbacks(mTimer);
        super.onPause();
    }

    /** GraphData and dataPairList point to the same memory address */
    public void bindGraphList(List<DataPair> dataPairList)
    {
        GraphData = dataPairList;
    }

}
