package eventtustask.com.eventtustask;

import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.twitter.sdk.android.Twitter;
import com.twitter.sdk.android.core.TwitterAuthConfig;
import com.twitter.sdk.android.core.TwitterSession;
import com.twitter.sdk.android.core.identity.TwitterLoginButton;
import com.twitter.sdk.android.Twitter;
import com.twitter.sdk.android.core.Callback;
import com.twitter.sdk.android.core.Result;
import com.twitter.sdk.android.core.TwitterAuthConfig;
import com.twitter.sdk.android.core.TwitterAuthToken;
import com.twitter.sdk.android.core.TwitterException;
import com.twitter.sdk.android.core.TwitterSession;
import com.twitter.sdk.android.core.identity.TwitterLoginButton;
import com.twitter.sdk.android.core.models.User;

import eventtustask.com.eventtustask.app.Token;
import io.fabric.sdk.android.Fabric;


public class MainActivity extends AppCompatActivity {

    // Note: Your consumer key and secret should be obfuscated in your source code before shipping.
    //KoZJrMITmLD2UASSFQGi4r0TN
    private static final String TWITTER_KEY = "N47qC7lFxTKNZnk3fFAaexsV4";

    //4lJX9pykbvxJowGTMd2Fc8xajLGPmB5rDopizfY9wvgqYo637x
    private static final String TWITTER_SECRET = "SptqAZanaxeJL924PycCEpFbvszZI1S0ZHH0XENyqO0YV5vfKo";
    //Tags to send the username and image url to next activity using intent
    public static final String KEY_USERNAME = "username";
    public static final String KEY_PROFILE_IMAGE_URL = "image_url";
    TwitterLoginButton twitterLoginButton;

    //Adding callback to the button



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        TwitterAuthConfig authConfig = new TwitterAuthConfig(TWITTER_KEY, TWITTER_SECRET);
        Fabric.with(this, new Twitter(authConfig));

        setContentView(R.layout.activity_main);

        twitterLoginButton = (TwitterLoginButton) findViewById(R.id.twitterLogin);

        //Adding callback to the button
        twitterLoginButton.setCallback(new Callback<TwitterSession>() {
            @Override
            public void success(Result<TwitterSession> result) {
                //If login succeeds passing the Calling the login method and passing Result object
                login(result);
            }

            @Override
            public void failure(TwitterException exception) {
                Toast.makeText(MainActivity.this,getString(R.string.login_error),Toast.LENGTH_LONG).show();
                Log.d("TwitterKit", "Login with Twitter failure", exception);
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        //Adding the login result back to the button
        twitterLoginButton.onActivityResult(requestCode, resultCode, data);
    }

    //The login function accepting the result object
    public void login(Result<TwitterSession> result) {

        //Creating a twitter session with result's data
        TwitterSession session = result.data;

        //Getting the username from session
        final String username = session.getUserName();


        TwitterAuthToken authToken = session.getAuthToken();
        String token = authToken.token;
        String secret = authToken.secret;
        SharedPreferences info = Token.sharedPreferencesToken(MainActivity.this);
        SharedPreferences.Editor editor = info.edit();
        editor.putString(Token.TOKEN,token);
        editor.putString(Token.SECRET,secret);
        editor.putBoolean(Token.Login_State, true);
        editor.putString(Token.USERNAME, username);
        editor.putLong(Token.USER_ID, session.getUserId());
        editor.apply();

        Twitter.getApiClient(session).getAccountService()
                .verifyCredentials(true, false, new Callback<User>() {



                    @Override
                    public void failure(TwitterException e) {
                    }

                    @Override
                    public void success(Result<User> userResult) {

                        User user = userResult.data;


                    }
                });

    }

    @Override
    protected void onResume() {
        super.onResume();
        SharedPreferences info = Token.sharedPreferencesToken(this);
        if(info.getBoolean(Token.Login_State, false)){
            Intent intent = new Intent(MainActivity.this, ProfileActivity.class);
            //Adding the values to intent
            intent.putExtra(KEY_USERNAME, info.getString(Token.USERNAME,null));
            //Starting intent
            startActivity(intent);
            finish();
        }

    }
}

