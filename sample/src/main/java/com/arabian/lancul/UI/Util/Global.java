package com.arabian.lancul.UI.Util;

import com.arabian.lancul.UI.Object.Chat;
import com.arabian.lancul.UI.Object.Client;
import com.arabian.lancul.UI.Object.Guider;
import com.arabian.lancul.UI.Object.Res_Exp;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Comparator;
import java.util.Date;
import java.util.HashSet;

public class Global {

    public static boolean go_profile = false;

    public static boolean user_mode = true;
    public static boolean iamguider = false;
    public static String my_name = "";
    public static String my_email = "";
    public static String chat_guider_name = "";
    public static Client my_user_data;
    public static String partner_photo = "";
    public static ArrayList<Res_Exp> array_experience = new ArrayList<>();
    public static ArrayList<Res_Exp> array_restaurant = new ArrayList<>();
    public static ArrayList<Guider> array_guider = new ArrayList<>();
    public static ArrayList<Client> array_client = new ArrayList<>();
    public static ArrayList<Client> my_clients = new ArrayList<>();
    public static ArrayList<Chat> array_chat_histroy = new ArrayList<>();
    public static ArrayList<String> array_chat_ids = new ArrayList<>();

    public static String getToday() {
        Date currentTime = Calendar.getInstance().getTime();
        SimpleDateFormat sdf = new SimpleDateFormat("HHmmss");
        return currentTime.toString();
    }
    public static class FishNameComparator implements Comparator<Chat>
    {
        public int compare(Chat left, Chat right) {
            return left.getChat_id().compareTo(right.getChat_id());
        }
    }
}
