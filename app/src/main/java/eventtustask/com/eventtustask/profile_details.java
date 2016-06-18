package eventtustask.com.eventtustask;

import android.app.Activity;
import android.app.ListActivity;
import android.content.Intent;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;
import com.twitter.sdk.android.Twitter;
import com.twitter.sdk.android.core.Callback;
import com.twitter.sdk.android.core.Result;
import com.twitter.sdk.android.core.TwitterException;
import com.twitter.sdk.android.core.TwitterSession;
import com.twitter.sdk.android.core.models.Tweet;
import com.twitter.sdk.android.core.models.User;
import com.twitter.sdk.android.tweetui.TweetTimelineListAdapter;
import com.twitter.sdk.android.tweetui.UserTimeline;

import java.util.List;

import eventtustask.com.eventtustask.Adaptors.FollowersAdaptor;
import eventtustask.com.eventtustask.Adaptors.TweetAdaptor;
import eventtustask.com.eventtustask.ApiClient.MyTwitterApiClient;
import eventtustask.com.eventtustask.Sqlite.FollowersSqlite;
import eventtustask.com.eventtustask.Sqlite.TweetSqlite;
import eventtustask.com.eventtustask.app.ConnectionDetector;
import eventtustask.com.eventtustask.app.DividerItemDecoration;
import eventtustask.com.eventtustask.app.Token;
import eventtustask.com.eventtustask.model.Tweets;

public class profile_details extends Activity {
    String screen_name;
    long user_id;
    ImageView backdrop;
    ImageView profile_img;

    TextView name ;
    TextView screen_nameView;
    private ProgressBar  progressBar;
    FollowersSqlite followersSqlite;
    ConnectionDetector connectionDetector;
    TweetSqlite tweetSqlite;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile_details);
        TwitterSession session = Twitter.getSessionManager()
                .getActiveSession();
        backdrop= (ImageView) findViewById(R.id.backdrop);
       // profile_img = (ImageView) findViewById(R.id.profile_img);
        name = (TextView) findViewById(R.id.name);
        screen_nameView = (TextView) findViewById(R.id.screen_name);
        followersSqlite = new FollowersSqlite(this);
        connectionDetector = new ConnectionDetector(this);
        tweetSqlite = new TweetSqlite(this);

        final RecyclerView recyclerView = (RecyclerView) findViewById(R.id.users_view);
        final LinearLayoutManager mLayoutManager  = new LinearLayoutManager(this);
        progressBar = (ProgressBar) findViewById(R.id.loading);
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.addItemDecoration(new DividerItemDecoration(this));
        recyclerView.setItemAnimator(new DefaultItemAnimator());

        Intent i = getIntent();
        screen_name = i.getExtras().getString(Token.SCREEN_NAME);
        user_id = i.getExtras().getLong(Token.USER_ID);

        MyTwitterApiClient myTwitterApiClient = new MyTwitterApiClient(session);
        myTwitterApiClient.getCustomService().getUser(user_id, screen_name, new Callback<User>() {
            @Override
            public void success(Result<User> result) {
                User u = result.data;
                update_ui(u);

            }

            @Override
            public void failure(TwitterException exception) {
                exception.printStackTrace();
                update_ui(followersSqlite.get_by_id(user_id));

            }
        });
        myTwitterApiClient.getCustomService().GET_twits(user_id, screen_name, 10, new Callback<List<Tweet>>() {
            @Override
            public void success(Result<List<Tweet>> result) {
                tweetSqlite.EmptyTable();
                recyclerView.setAdapter(new TweetAdaptor(result.data, R.layout.item_follower, getApplicationContext()));
                for (Tweet t:result.data) {
                    tweetSqlite.addItem(t);
                }
                progressBar.setVisibility(View.GONE);

            }

            @Override
            public void failure(TwitterException exception) {
                exception.printStackTrace();
                Toast.makeText(profile_details.this,getString(R.string.error_tweets), Toast.LENGTH_LONG).show();
                progressBar.setVisibility(View.GONE);
                recyclerView.setAdapter(new TweetAdaptor(tweetSqlite.getAllItmes(), R.layout.item_follower, getApplicationContext()));
            }
        });



    }

    private void initCollapsingToolbar(final User u) {
        final CollapsingToolbarLayout collapsingToolbar =
                (CollapsingToolbarLayout) findViewById(R.id.collapsing_toolbar);
        collapsingToolbar.setTitle(" ");
        AppBarLayout appBarLayout = (AppBarLayout) findViewById(R.id.appbar);
        appBarLayout.setExpanded(true);

        // hiding & showing the title when toolbar expanded & collapsed
        appBarLayout.addOnOffsetChangedListener(new AppBarLayout.OnOffsetChangedListener() {
            boolean isShow = false;
            int scrollRange = -1;

            @Override
            public void onOffsetChanged(AppBarLayout appBarLayout, int verticalOffset) {
                if (scrollRange == -1) {
                    scrollRange = appBarLayout.getTotalScrollRange();
                }
                if (scrollRange + verticalOffset == 0) {
                    collapsingToolbar.setTitle(u.screenName);
                    isShow = true;
                } else if (isShow) {
                    collapsingToolbar.setTitle(" ");
                    isShow = false;
                }
            }
        });
    }

    public  void update_ui(User u){

        name.setText(u.name);
        screen_nameView.setText(u.description);
        Picasso.with(profile_details.this)
                .load(u.profileBannerUrl)
                .placeholder(R.drawable.cover)
                .into(backdrop);

        initCollapsingToolbar(u);
    }
}
