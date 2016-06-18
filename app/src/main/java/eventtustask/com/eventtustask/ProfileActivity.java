package eventtustask.com.eventtustask;

import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.twitter.sdk.android.Twitter;
import com.twitter.sdk.android.core.Callback;
import com.twitter.sdk.android.core.Result;
import com.twitter.sdk.android.core.TwitterException;
import com.twitter.sdk.android.core.TwitterSession;
import com.twitter.sdk.android.core.models.User;

import java.util.ArrayList;
import java.util.List;

import eventtustask.com.eventtustask.Adaptors.FollowersAdaptor;
import eventtustask.com.eventtustask.ApiClient.MyTwitterApiClient;
import eventtustask.com.eventtustask.Sqlite.FollowersSqlite;
import eventtustask.com.eventtustask.app.ConnectionDetector;
import eventtustask.com.eventtustask.app.DividerItemDecoration;
import eventtustask.com.eventtustask.app.EndlessRecyclerOnScrollListener;
import eventtustask.com.eventtustask.app.Token;
import eventtustask.com.eventtustask.model.Followers;

public class ProfileActivity extends AppCompatActivity {

    MyTwitterApiClient apiClient;
    //TextView object
    private ProgressBar progressBar;
    SharedPreferences info;
    RecyclerView recyclerView;
    FollowersAdaptor followersAdaptor;
    List<User> ListOfFollowers;
    FollowersSqlite followersSqlite;
    ConnectionDetector connectionDetector;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
        TwitterSession session = Twitter.getSessionManager()
                .getActiveSession();
        recyclerView = (RecyclerView) findViewById(R.id.users_view);
        final LinearLayoutManager mLayoutManager = new LinearLayoutManager(this);
        progressBar = (ProgressBar) findViewById(R.id.loading);
        followersSqlite = new FollowersSqlite(this);
        connectionDetector = new ConnectionDetector(this);


        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.addItemDecoration(new DividerItemDecoration(this));
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.addOnScrollListener(new EndlessRecyclerOnScrollListener(mLayoutManager) {
            @Override
            public void onLoadMore(int current_page) {
                if (connectionDetector.isConnectingToInternet()) {
                    Toast.makeText(ProfileActivity.this,getString(R.string.loading), Toast.LENGTH_LONG).show();
                    FetchFollowers();
                }


            }
        });
        ListOfFollowers = new ArrayList<>();
         followersAdaptor=new FollowersAdaptor(ListOfFollowers, R.layout.item_follower, getApplicationContext());
            recyclerView.setAdapter(followersAdaptor);

         info = Token.sharedPreferencesToken(this);
         apiClient = new MyTwitterApiClient(session);
          if(connectionDetector.isConnectingToInternet()){
              FetchFollowers();
          }else{
            ListOfFollowers = followersSqlite.getAllItmes();
              followersAdaptor=new FollowersAdaptor(ListOfFollowers, R.layout.item_follower, getApplicationContext());
              recyclerView.setAdapter(followersAdaptor);

          }



    }


    public void FetchFollowers(){
        apiClient.getCustomService().show(info.getLong(Token.USER_ID, 0), null, true, true, Token.cursor, 50, new Callback<Followers>() {
            @Override
            public void success(Result<Followers> result) {
                if( Token.cursor.equals("-1")){
                    followersSqlite.EmptyTable();
                }
                Token.cursor = result.data.next;
                for (int  i= 0 ;i<result.data.users.size();i++){
                    ListOfFollowers.add(result.data.users.get(i));
                    followersSqlite.addItem(result.data.users.get(i));
                }
                if(followersAdaptor==null){
                    followersAdaptor=new FollowersAdaptor(ListOfFollowers, R.layout.item_follower, getApplicationContext());
                    recyclerView.setAdapter(followersAdaptor);
                }else{
                    followersAdaptor.notifyDataSetChanged();
                }


                progressBar.setVisibility(View.INVISIBLE);
            }

            @Override
            public void failure(TwitterException exception) {
                progressBar.setVisibility(View.INVISIBLE);
                exception.printStackTrace();
                Toast.makeText(ProfileActivity.this,getString(R.string.connection_error), Toast.LENGTH_LONG).show();

            }
        });
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Token.cursor="-1";
        finish();
    }
}
