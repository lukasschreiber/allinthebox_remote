package com.example.allinthebox_remote.ui.main;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.text.HtmlCompat;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RatingBar;
import android.widget.TextView;

import com.example.allinthebox_remote.Client;
import com.example.allinthebox_remote.MainActivity;
import com.example.allinthebox_remote.R;
import com.example.allinthebox_remote.Values;

public class EditFragment extends Fragment {

    private String[] data = null;

    public EditFragment(){

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){

        View view = inflater.inflate(R.layout.edit_view, container, false);
        final EditText editText = view.findViewById(R.id.itemName);
        final RadioButton radioButton = view.findViewById(R.id.radioButton);
        final RadioButton radioButton2 = view.findViewById(R.id.radioButton2);
        final RatingBar rb = view.findViewById(R.id.ratingBar);

        if(this.data != null){
            //set edit text
            if(!data[0].equals("")) {
                data[0] = data[0].replace("?", "-");
                editText.setText(data[0].trim());

            }

            if(!data[2].equals("")) {
                if (data[2].trim().equals("1")) {
                    radioButton2.setChecked(true);
                } else if (data[2].trim().equals("0")) {
                    radioButton.setChecked(true);
                }
            }

            String infoText = "";
            if(!data[1].equals("")){
                infoText += getString(R.string.info_barcode, data[0].trim(), data[1].trim());
                Values.CURRENT_BARCODE = data[1];
            }

            if(!data[4].equals("")){
                infoText += getString(R.string.info_rack,data[4].trim());
            }

            if(!(data[3].equals("")|| data[3].equals("-") || data[3].equals(" ") )){
                infoText += getString(R.string.info_comment,data[3].trim());
            }

            TextView i = view.findViewById(R.id.infoTextView);
            i.setText(HtmlCompat.fromHtml(infoText, HtmlCompat.FROM_HTML_MODE_LEGACY));

            if(!data[5].equals("")){
                rb.setRating(Integer.parseInt(data[5].trim()));
            }

            //cancel
            Button button = (Button) view.findViewById(R.id.cancel);
            button.setOnClickListener(new View.OnClickListener() {

                public void onClick(View v) {
                    Client.send(Values.CODES.CANCEL, data[1].trim());

                    ContentFragment contentFragment = new ContentFragment(getString(R.string.please_scan_barcode));

                    FragmentTransaction transaction = getFragmentManager().beginTransaction();
                    transaction.replace(R.id.frame_layout, contentFragment);
                    transaction.addToBackStack(null);
                    transaction.commit();

                    MainActivity.barcodeFragment.startScanning();
                    TabLayout tab = (TabLayout) getActivity().findViewById(R.id.tabs);
                    tab.getTabAt(0).select();
                    Values.CURRENT_BARCODE = "";

                }
            });

            final String barcode = data[1].trim();

            //save
            Button saveButton = (Button) view.findViewById(R.id.save);
            saveButton.setOnClickListener(new View.OnClickListener() {

                public void onClick(View v) {

                    String stat = "1";
                    if(radioButton.isChecked()){
                        stat = "0";
                    }
                    String data = barcode + ";" + editText.getText() + ";" + stat + ";" + (int)rb.getRating();

                    Client.send(Values.CODES.SAVE, data.trim());

                    ContentFragment contentFragment = new ContentFragment(getString(R.string.please_scan_barcode));

                    FragmentTransaction transaction = getFragmentManager().beginTransaction();
                    transaction.replace(R.id.frame_layout, contentFragment);
                    transaction.addToBackStack(null);
                    transaction.commit();

                    MainActivity.barcodeFragment.startScanning();
                    TabLayout tab = (TabLayout) getActivity().findViewById(R.id.tabs);
                    tab.getTabAt(0).select();
                    Values.CURRENT_BARCODE = "";

                }
            });
        }

        return view;
    }

    @SuppressLint("ValidFragment")
    public EditFragment(String[] data){
        if(data.length == 6){

           this.data = data;

        }

    }

}
