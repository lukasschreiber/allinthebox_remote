package com.example.allinthebox_remote;

import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.allinthebox_remote.ui.main.BarcodeFragment;
import com.example.allinthebox_remote.ui.main.CameraFragment;
import com.example.allinthebox_remote.ui.main.ConnectFragment;
import com.example.allinthebox_remote.ui.main.ContentFragment;
import com.example.allinthebox_remote.ui.main.EditFragment;
import com.example.allinthebox_remote.ui.main.HelpFragment;
import com.example.allinthebox_remote.ui.main.SectionsPagerAdapter;
import com.getbase.floatingactionbutton.FloatingActionButton;
import com.getbase.floatingactionbutton.FloatingActionsMenu;

public class MainActivity extends AppCompatActivity implements ContentFragment.OnFragmentInteractionListener, CameraFragment.OnFragmentInteractionListener, BarcodeFragment.OnFragmentInteractionListener {

    public static BarcodeFragment barcodeFragment = new BarcodeFragment();
    public static CameraFragment cameraFragment = new CameraFragment();
    public static ContentFragment contentFragment = new ContentFragment();
    public static EditFragment editFragment = new EditFragment();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        SectionsPagerAdapter sectionsPagerAdapter = new SectionsPagerAdapter(this, getSupportFragmentManager(), this);
        ViewPager viewPager = findViewById(R.id.view_pager);
        viewPager.setAdapter(sectionsPagerAdapter);
        TabLayout tabs = findViewById(R.id.tabs);
        tabs.setupWithViewPager(viewPager);

        tabs.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                if(tab.getText().toString().trim().equals(getString(R.string.tab_text_1)))
                {
                    barcodeFragment.startScanning();

                    if(!Values.CURRENT_BARCODE.equals("")){
                        Client.send(Values.CODES.CANCEL, Values.CURRENT_BARCODE.trim());
                        Values.CURRENT_BARCODE = "";
                    }
                }


            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {
                if(tab.getText().toString().trim().equals(getString(R.string.tab_text_1)))
                {

                    barcodeFragment.stopScanning();
                }
            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });


        final FloatingActionButton connectButton = (FloatingActionButton)findViewById(R.id.fab_connect);
        final FloatingActionButton helpButton = (FloatingActionButton)findViewById(R.id.fab_help);
        final FloatingActionsMenu menu = (FloatingActionsMenu)findViewById(R.id.fab_menu);

        connectButton.setOnClickListener(new View.OnClickListener (){
            @Override
            public void onClick(View view){
                if(!Values.CONNECTED){


                    menu.collapse();

                    TextView tv = (TextView) contentFragment.getView().findViewById(R.id.connection_status);
                    tv.setVisibility(View.INVISIBLE);

                    ImageView iv = (ImageView) contentFragment.getView().findViewById(R.id.gif);
                    iv.setVisibility(View.INVISIBLE);

                    FragmentTransaction transaction = contentFragment.getFragmentManager().beginTransaction();

                    ConnectFragment connectFragment = new ConnectFragment();
                    transaction.replace(R.id.frame_layout, connectFragment);
                    transaction.addToBackStack(null);
                    transaction.commit();

                    TabLayout tab = (TabLayout) contentFragment.getActivity().findViewById(R.id.tabs);
                    tab.getTabAt(1).select();
                }
            }
        });

        helpButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                menu.collapse();

                TextView tv = (TextView) contentFragment.getView().findViewById(R.id.connection_status);
                tv.setVisibility(View.INVISIBLE);

                ImageView iv = (ImageView) contentFragment.getView().findViewById(R.id.gif);
                iv.setVisibility(View.INVISIBLE);

                FragmentTransaction transaction = contentFragment.getFragmentManager().beginTransaction();

                HelpFragment helpFragment = new HelpFragment();
                transaction.replace(R.id.frame_layout, helpFragment);
                transaction.addToBackStack(null);
                transaction.commit();

                TabLayout tab = (TabLayout) contentFragment.getActivity().findViewById(R.id.tabs);
                tab.getTabAt(1).select();
            }
        });

    }


    @Override
    public void onPause(){
        super.onPause();
        if (this.isFinishing()){
            if(!Values.CURRENT_BARCODE.equals("")) {
                Client.send(Values.CODES.CANCEL, Values.CURRENT_BARCODE.trim());
                Values.CURRENT_BARCODE.equals("");

            }
        }
    }

    @Override
    public void onResume(){
        super.onResume();
        barcodeFragment.startScanning();
    }

    @Override
    public void onBackPressed(){
        finish();
        if(!Values.CURRENT_BARCODE.equals("")) {
            Client.send(Values.CODES.CANCEL, Values.CURRENT_BARCODE.trim());
            Values.CURRENT_BARCODE.equals("");

        }
    }

    //needed for implementation

    @Override
    public void onFragmentInteraction(CharSequence text){
        //call other fragment
        contentFragment.setText(text);

    }
}