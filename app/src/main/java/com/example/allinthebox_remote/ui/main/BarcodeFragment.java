package com.example.allinthebox_remote.ui.main;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;

import com.example.allinthebox_remote.Client;
import com.example.allinthebox_remote.MainActivity;
import com.example.allinthebox_remote.R;
import com.example.allinthebox_remote.Security;
import com.example.allinthebox_remote.Values;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.Result;

import me.dm7.barcodescanner.zxing.ZXingScannerView;


public class BarcodeFragment extends Fragment implements ZXingScannerView.ResultHandler{

    private ZXingScannerView mScannerView;


    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private boolean scanning = true;

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;



    private OnFragmentInteractionListener mListener;

    public BarcodeFragment() {
        // Required empty public constructor
    }


    // TODO: Rename and change types and number of parameters
    public static BarcodeFragment newInstance(String param1, String param2) {
        BarcodeFragment fragment = new BarcodeFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }

        if( ContextCompat.checkSelfPermission(getContext(), Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                requestPermissions(new String[]{Manifest.permission.CAMERA},5);
            }
        }

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        mScannerView = new ZXingScannerView(getActivity());

        /*mScannerView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                switch (event.getAction()){
                    case MotionEvent.ACTION_DOWN: scanning = false; return true;
                    case MotionEvent.ACTION_UP: scanning = true; return true;
                }

                return false;
            }
        });*/

        return mScannerView;
    }

    @Override
    public void onResume() {
        super.onResume();

        System.out.println("resume");

        mScannerView.startCamera();
        mScannerView.setResultHandler(this);
    }

    @Override
    public void onPause() {
        super.onPause();

        System.out.println("pause");
        mScannerView.stopCamera();
    }


    public void sendTextToMain(CharSequence uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString() + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    @Override
    public void handleResult(Result rawResult) {
        String rawText = rawResult.getText();

        if(!Values.RECIEVED_CONNECTION_INFO && rawResult.getBarcodeFormat().equals(BarcodeFormat.QR_CODE) && rawText.length() > 16 && rawText.substring(0,4).equals("aitb")) {

            //decrypting goes here
            String encrypted = rawText.substring(4).trim();

            String decrypted = Security.decrypt(encrypted);

            String[] textSplit = decrypted.split(";");


            if (textSplit[0].equals("aitb") && textSplit.length == 5 && scanning) {

                Values.IP_ADDRESS = textSplit[1];
                Values.PORT_NUMBER = Integer.parseInt(textSplit[2]);
                Values.LOGGED_IN_USER = textSplit[3];
                Values.CODE = textSplit[4];
                Values.RECIEVED_CONNECTION_INFO = true;

                sendTextToMain(Values.IP_ADDRESS + ":" + Values.PORT_NUMBER);

                stopScanning();
                TabLayout tab = (TabLayout) getActivity().findViewById(R.id.tabs);
                tab.getTabAt(1).select();
            }

        }else if(!rawText.contains(Values.CODE) && !rawText.contains("aitb") && scanning){
            Client.send(Values.CODES.BARCODE, rawText);
        }else if(!Values.CONNECTED){
            Snackbar.make(getView(), getString(R.string.err_not_connected),Snackbar.LENGTH_LONG).show();
        }

        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                mScannerView.resumeCameraPreview(BarcodeFragment.this);
            }
        }, 1000);
    }

    public void stopScanning(){
        scanning = false;
        try {
            mScannerView.setAutoFocus(false);
        }catch(Exception e){

        }
    }

    public void startScanning(){
        scanning = true;
        try{
            mScannerView.setAutoFocus(true);
        }catch (Exception e){

        }
    }

    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(CharSequence input);
    }
}
