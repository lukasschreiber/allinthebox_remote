package com.example.allinthebox_remote.ui.main;

import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.example.allinthebox_remote.Client;
import com.example.allinthebox_remote.MainActivity;
import com.example.allinthebox_remote.R;
import com.example.allinthebox_remote.Values;

import static com.example.allinthebox_remote.MainActivity.contentFragment;

public class ConnectFragment extends Fragment {

    private String barcode = "";

    public ConnectFragment(){

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){

        View view = inflater.inflate(R.layout.connect_view, container, false);

        //connect
        Button connect = (Button) view.findViewById(R.id.connect);
        final TextView portView = (TextView) view.findViewById(R.id.port);
        final TextView IPView = (TextView) view.findViewById(R.id.IP);
        final TextView codeView = (TextView) view.findViewById(R.id.code);
        connect.setOnClickListener(new View.OnClickListener() {

            public void onClick(View v) {
                try {
                    Values.PORT_NUMBER = Integer.valueOf(portView.getText().toString().trim());
                }catch(Exception exc){

                }
                Values.IP_ADDRESS = IPView.getText().toString().trim();
                Values.CODE = codeView.getText().toString().trim();
                Values.RECIEVED_CONNECTION_INFO = true;

                ContentFragment contentFragment = new ContentFragment(getString(R.string.please_scan_barcode));

                FragmentTransaction transaction = getFragmentManager().beginTransaction();
                transaction.replace(R.id.frame_layout, contentFragment);
                transaction.addToBackStack(null);
                transaction.commit();

                MainActivity.barcodeFragment.startScanning();
                TabLayout tab = (TabLayout) getActivity().findViewById(R.id.tabs);
                tab.getTabAt(0).select();


                if(!portView.getText().equals("") && !IPView.getText().equals("") && !codeView.getText().equals("")) {
                    Client.connect();
                }

            }
        });

       //cancel
        Button button = (Button) view.findViewById(R.id.cancel);
        button.setOnClickListener(new View.OnClickListener() {

            public void onClick(View v) {
            ContentFragment contentFragment = new ContentFragment(getString(R.string.not_connected));

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

}
