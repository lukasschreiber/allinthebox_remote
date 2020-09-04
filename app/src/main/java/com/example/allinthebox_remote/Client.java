package com.example.allinthebox_remote;

import android.content.Context;
import android.os.Handler;
import android.support.design.widget.TabLayout;
import android.support.v4.app.FragmentTransaction;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.allinthebox_remote.ui.main.AddFragment;
import com.example.allinthebox_remote.ui.main.ConnectFragment;
import com.example.allinthebox_remote.ui.main.ContentFragment;
import com.example.allinthebox_remote.ui.main.EditFragment;
import com.jaredrummler.android.device.DeviceName;

import org.w3c.dom.Text;

import java.io.DataInputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.net.Socket;
import java.nio.ByteBuffer;



public class Client {


    public static void connect(){

        final Handler handler = new Handler();

        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {

                try {

                    Socket s = new Socket(Values.IP_ADDRESS, Values.PORT_NUMBER);

                    OutputStream out = s.getOutputStream();

                    PrintWriter output = new PrintWriter(out);


                    String name = DeviceName.getDeviceName();

                    output.println(Values.CODES.CONNECTED + ";" + name);
                    output.flush();

                    boolean end = false;
                    byte[] messageByte = new byte[2560000];
                    String dataString = "";

                    int length = 0;

                    try{
                        DataInputStream in = new DataInputStream(s.getInputStream());
                        int bytesRead = 0;

                        messageByte[0] = in.readByte();
                        messageByte[1] = in.readByte();
                        messageByte[2] = in.readByte();
                        messageByte[3] = in.readByte();
                        ByteBuffer byteBuffer = ByteBuffer.wrap(messageByte, 0, 4);

                        length = Integer.reverseBytes(byteBuffer.getInt());

                        while(!end){
                            bytesRead = in.read(messageByte);
                            dataString += new String(messageByte, 0, bytesRead);
                            if(dataString.length() == length){
                                end = true;
                            }
                        }

                    }catch(Exception exc){
                        exc.printStackTrace();
                    }

                    final String st = dataString;

                    handler.post(new Runnable() {
                        @Override
                        public void run() {

                            //handle result
                            if (st.trim().length() != 0) {

                                //connection
                                if(st.equals(Values.CODE) && !Values.CONNECTED){
                                    Values.CONNECTED = true;

                                    final TextView tv = (TextView)MainActivity.contentFragment.getView().findViewById(R.id.connection_status);
                                    tv.setText(MainActivity.contentFragment.getString(R.string.connected_success,Values.IP_ADDRESS));

                                    final ImageView gf = (ImageView) MainActivity.contentFragment.getView().findViewById(R.id.gif);
                                    gf.setVisibility(View.INVISIBLE);

                                    /*ProgressBar pb = (ProgressBar) cf.getView().findViewById(R.id.progressBar);
                                    pb.setVisibility(View.INVISIBLE);*/

                                    final Handler handler = new Handler();
                                    handler.postDelayed(new Runnable() {
                                        @Override
                                        public void run() {

                                            tv.setText(MainActivity.contentFragment.getString(R.string.please_scan_barcode));
                                            gf.setVisibility(View.VISIBLE);


                                        }
                                    }, 1500);


                                }

                            }

                        }
                    });

                    //close all streams
                    output.close();
                    out.close();
                    s.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });

        thread.start();

    }

    public static void send(final int mode, final String data){

        final Handler handler = new Handler();

        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {

                try {

                    Socket s = new Socket(Values.IP_ADDRESS, Values.PORT_NUMBER);

                    OutputStream out = s.getOutputStream();

                    PrintWriter output = new PrintWriter(out);

                    output.println(mode + ";" + convertStringToUTF8(data));
                    output.flush();

                    boolean end = false;
                    byte[] messageByte = new byte[2560000]; //2.56 MB
                    String dataString = "";

                    int length = 0;

                    try{
                        DataInputStream in = new DataInputStream(s.getInputStream());
                        int bytesRead = 0;

                        messageByte[0] = in.readByte();
                        messageByte[1] = in.readByte();
                        messageByte[2] = in.readByte();
                        messageByte[3] = in.readByte();
                        ByteBuffer byteBuffer = ByteBuffer.wrap(messageByte, 0, 4);

                        length = Integer.reverseBytes(byteBuffer.getInt());

                        while(!end){
                            bytesRead = in.read(messageByte);
                            dataString += new String(messageByte, 0, bytesRead, "utf-8");
                            System.out.println(dataString+" " +length + " " + dataString.length());

                            if(dataString.length() == length){
                                end = true;
                            }
                        }

                    }catch(Exception exc){
                        exc.printStackTrace();
                    }

                    final String st = dataString;

                    handler.post(new Runnable() {
                        @Override
                        public void run() {

                            //handle result
                            if (st.trim().length() != 0) {

                                //results

                                if(mode == Values.CODES.BARCODE){

                                    String[] split = st.split(";");


                                    if(split[0].equals(String.valueOf(Values.CODES.NOT_IN_DB))){
                                        TextView tv = (TextView) MainActivity.contentFragment.getView().findViewById(R.id.connection_status);
                                        tv.setVisibility(View.INVISIBLE);

                                        ImageView iv = (ImageView) MainActivity.contentFragment.getView().findViewById(R.id.gif);
                                        iv.setVisibility(View.INVISIBLE);

                                        FragmentTransaction transaction = MainActivity.contentFragment.getFragmentManager().beginTransaction();

                                        AddFragment addFragment = new AddFragment(split[1]);

                                        transaction.replace(R.id.frame_layout, addFragment);
                                        transaction.addToBackStack(null);
                                        transaction.commit();

                                        MainActivity.barcodeFragment.stopScanning();
                                        TabLayout tab = (TabLayout) MainActivity.contentFragment.getActivity().findViewById(R.id.tabs);
                                        tab.getTabAt(1).select();

                                    }else if(split[0].equals(String.valueOf(Values.CODES.CURRENTLY_IN_USE))){

                                        ContentFragment contentFragment = new ContentFragment(MainActivity.contentFragment.getString(R.string.error100));

                                        FragmentTransaction transaction = MainActivity.contentFragment.getFragmentManager().beginTransaction();

                                        try{
                                            TextView tv = (TextView)MainActivity.contentFragment.getView().findViewById(R.id.connection_status);
                                            tv.setVisibility(View.INVISIBLE);
                                        }catch(Exception e){

                                        }

                                        transaction.replace(R.id.frame_layout, contentFragment);
                                        transaction.addToBackStack(null);
                                        transaction.commit();

                                        MainActivity.barcodeFragment.stopScanning();
                                        TabLayout tab = (TabLayout) MainActivity.contentFragment.getActivity().findViewById(R.id.tabs);
                                        tab.getTabAt(1).select();

                                    }else{

                                        TextView tv = (TextView) MainActivity.contentFragment.getView().findViewById(R.id.connection_status);
                                        tv.setVisibility(View.INVISIBLE);

                                        ImageView iv = (ImageView) MainActivity.contentFragment.getView().findViewById(R.id.gif);
                                        iv.setVisibility(View.INVISIBLE);

                                        FragmentTransaction transaction = MainActivity.contentFragment.getFragmentManager().beginTransaction();

                                        EditFragment ef = new EditFragment(split);

                                        transaction.replace(R.id.frame_layout, ef);
                                        transaction.addToBackStack(null);
                                        transaction.commit();

                                        MainActivity.barcodeFragment.stopScanning();
                                        TabLayout tab = (TabLayout) MainActivity.contentFragment.getActivity().findViewById(R.id.tabs);
                                        tab.getTabAt(1).select();
                                    }

                                }

                            }

                        }
                    });


                    //close all streams
                    output.close();
                    out.close();
                    s.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });

        thread.start();

    }
    public static String convertStringToUTF8(String s) {
        String out = null;
        try {
            out = new String(s.getBytes("UTF-8"));
        } catch (java.io.UnsupportedEncodingException e) {
            return null;
        }

        return out;
    }
}
