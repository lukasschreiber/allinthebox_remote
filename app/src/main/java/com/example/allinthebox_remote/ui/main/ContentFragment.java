package com.example.allinthebox_remote.ui.main;

import android.annotation.SuppressLint;
import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.os.TestLooperManager;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.allinthebox_remote.Client;
import com.example.allinthebox_remote.MainActivity;
import com.example.allinthebox_remote.R;

public class ContentFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private String text = "";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private OnFragmentInteractionListener mListener;

    public ContentFragment() {
        // Required empty public constructor

    }

    @SuppressLint("ValidFragment")
    public ContentFragment(String text){
        this.text = text;
    }

    // TODO: Rename and change types and number of parameters
    public static ContentFragment newInstance(String param1, String param2) {
        ContentFragment fragment = new ContentFragment();
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

    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_content, container, false);
        ImageView imageView = (ImageView)view.findViewById(R.id.gif);
        Glide.with(this).load(R.drawable.progressbar_fast).into(imageView);

        final TextView tv = (TextView)view.findViewById(R.id.connection_status);
        final ImageView gf = (ImageView) view.findViewById(R.id.gif);

        if(text != "") {
            tv.setText(text);
        }
        tv.setVisibility(View.VISIBLE);

        return view;
    }

    public void sendTextToMain(CharSequence uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }

    public void setText(CharSequence text){
        TextView tv = (TextView) getView().findViewById(R.id.connection_status);
        Client client = new Client();
        client.connect();
        tv.setText(getString(R.string.connecting_to) + " "+text);

    }


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }


    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }


    public interface OnFragmentInteractionListener {
        void onFragmentInteraction(CharSequence uri);
    }
}
