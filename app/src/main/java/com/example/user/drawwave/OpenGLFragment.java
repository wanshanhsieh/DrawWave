package com.example.user.drawwave;


import android.opengl.GLSurfaceView;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.TextView;

import static android.opengl.GLES20.GL_LINES;
import static android.opengl.GLES32.GL_QUADS;


/**
 * A simple {@link Fragment} subclass.
 * Use the {@link OpenGLFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class OpenGLFragment extends DialogFragment {
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
     * OpenGL
     */
    private GLSurfaceView mGLSurfaceView;;

    public OpenGLFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment OpenGLFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static OpenGLFragment newInstance(String param1, String param2) {
        OpenGLFragment fragment = new OpenGLFragment();
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

        View rootView = inflater.inflate(R.layout.fragment_open_gl, container, false);
        getDialog().requestWindowFeature(Window.FEATURE_NO_TITLE);
        text_showMsg = rootView.findViewById(R.id.text_ShowMsg);
        button_close = rootView.findViewById(R.id.button_Close);

        /** OpenGL */
        mGLSurfaceView = (GLSurfaceView)rootView.findViewById(R.id.gl_view);
        mGLSurfaceView.setEGLContextClientVersion(3);
        mGLSurfaceView.setPreserveEGLContextOnPause(true);
        mGLSurfaceView.setRenderer(new GLES20Renderer());

        /** press close, reset and close this DialogFragment */
        button_close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // graphLastIndex = 0;
                // GraphData = null;
                dismiss();
            }
        });

        // Inflate the layout for this fragment
        return rootView;
    }

}
