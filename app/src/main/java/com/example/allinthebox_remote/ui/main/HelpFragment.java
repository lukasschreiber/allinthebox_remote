package com.example.allinthebox_remote.ui.main;

import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.widget.Button;

import com.example.allinthebox_remote.MainActivity;
import com.example.allinthebox_remote.R;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;

public class HelpFragment extends Fragment {

    private String barcode = "";

    public HelpFragment() {

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.help_fragment, container, false);

        //close
        Button button = (Button) view.findViewById(R.id.close_help);
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


        WebView webView = (WebView) view.findViewById(R.id.helpView);
        webView.loadUrl("file:///android_asset/help.html");

        return view;
    }

}