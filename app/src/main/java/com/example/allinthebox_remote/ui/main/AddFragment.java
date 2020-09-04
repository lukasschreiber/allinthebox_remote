package com.example.allinthebox_remote.ui.main;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.speech.tts.TextToSpeech;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RatingBar;
import android.widget.TextView;

import com.example.allinthebox_remote.Client;
import com.example.allinthebox_remote.MainActivity;
import com.example.allinthebox_remote.R;
import com.example.allinthebox_remote.Values;

public class AddFragment extends Fragment {

    private String barcode = "";

    public AddFragment(){

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){

        View view = inflater.inflate(R.layout.add_view, container, false);
        final EditText editText = view.findViewById(R.id.itemName);
        final RatingBar rb = view.findViewById(R.id.ratingBar);

        //save
        Button saveButton = (Button) view.findViewById(R.id.save);
        saveButton.setOnClickListener(new View.OnClickListener() {

            public void onClick(View v) {

                if(editText.getText().length() > 0) {
                    String data = barcode + ";" + editText.getText() + ";" + (int) rb.getRating();

                    Client.send(Values.CODES.ADD, data.trim());
                }

                ContentFragment contentFragment = new ContentFragment(getString(R.string.please_scan_barcode));

                FragmentTransaction transaction = getFragmentManager().beginTransaction();
                transaction.replace(R.id.frame_layout, contentFragment);
                transaction.addToBackStack(null);
                transaction.commit();

                MainActivity.barcodeFragment.startScanning();
                TabLayout tab = (TabLayout) getActivity().findViewById(R.id.tabs);
                tab.getTabAt(0).select();

            }
        });

        //cancel
        Button button = (Button) view.findViewById(R.id.cancel);
        button.setOnClickListener(new View.OnClickListener() {

            public void onClick(View v) {
                ContentFragment contentFragment = new ContentFragment(getString(R.string.please_scan_barcode));

                FragmentTransaction transaction = getFragmentManager().beginTransaction();
                transaction.replace(R.id.frame_layout, contentFragment);
                transaction.addToBackStack(null);
                transaction.commit();

                MainActivity.barcodeFragment.startScanning();
                TabLayout tab = (TabLayout) getActivity().findViewById(R.id.tabs);
                tab.getTabAt(0).select();

            }
        });

        return view;
    }

    @SuppressLint("ValidFragment")
    public AddFragment(String barcode){
        if(!barcode.equals("")){

            this.barcode = barcode;

        }

    }

}
