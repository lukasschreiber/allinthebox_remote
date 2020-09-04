package com.example.allinthebox_remote;

public class Values {

    public static String IP_ADDRESS = "";
    public static int PORT_NUMBER = 13000;
    public static String LOGGED_IN_USER = "System";
    public static Boolean CONNECTED = false;
    public static Boolean RECIEVED_CONNECTION_INFO = false;
    public static String CODE = "";
    public static String CURRENT_BARCODE = "";

    public static class CODES{
        public static int CONNECTED = 0;
        public static int BARCODE = 1;
        public static int CANCEL = 2;
        public static int SAVE = 3;
        public static int ADD = 4;
        public static int NOT_IN_DB = 5;
        public static int CURRENTLY_IN_USE = 100;
    }
}
