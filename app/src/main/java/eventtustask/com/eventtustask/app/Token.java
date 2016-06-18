package eventtustask.com.eventtustask.app;
import android.content.Context;
import android.content.SharedPreferences;

/**
 * Created by ahmed shabaan on 3/14/2015

 */
public class Token {

    public static final String Login_State="login";
    public static final String USER_ID="id";
    public  static  final  String TOKEN="manager_id";
    public static final  String SECRET="SECRET";
    public static final  String USERNAME="username";
    public static final  String SCREEN_NAME="screen_name";

    public static final String PREFKRY = "userinfo";

    public static String cursor = "-1";



    public static SharedPreferences notesPrefs;
    public   static SharedPreferences sharedPreferencesToken(Context context){
        notesPrefs = context.getSharedPreferences(PREFKRY, Context.MODE_PRIVATE);
        return notesPrefs;
    }


}
