package eventtustask.com.eventtustask.Adaptors;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;
import com.twitter.sdk.android.core.models.Tweet;
import com.twitter.sdk.android.core.models.User;

import java.util.List;

import eventtustask.com.eventtustask.R;
import eventtustask.com.eventtustask.app.Token;
import eventtustask.com.eventtustask.profile_details;

/**
 * Created by ahmed shabaan on 6/13/2016.
 */


public class TweetAdaptor extends RecyclerView.Adapter<TweetAdaptor.FollowerViewHolder> {

    public static List<Tweet> Tweets;
    private int rowLayout;
    public static Context context;


    public static class FollowerViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener  {
        RelativeLayout usersLayout;
        TextView name;
        ImageView profile;
        TextView tweetdescription;


        public FollowerViewHolder(View v) {
            super(v);
            usersLayout = (RelativeLayout) v.findViewById(R.id.users_layout);
            name = (TextView) v.findViewById(R.id.name);
            tweetdescription = (TextView) v.findViewById(R.id.bio);
            profile = (ImageView) v.findViewById(R.id.profile_img);
            v.setOnClickListener(this);
        }


        @Override
        public void onClick(View v) {


        }
    }

    public TweetAdaptor(List<Tweet> Tweets, int rowLayout, Context context) {
        this.Tweets = Tweets;
        this.rowLayout = rowLayout;
        this.context = context;
    }

    @Override
    public TweetAdaptor.FollowerViewHolder onCreateViewHolder(ViewGroup parent,
                                                                  int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(rowLayout, parent, false);
        return new FollowerViewHolder(view);
    }


    @Override
    public void onBindViewHolder(FollowerViewHolder holder, final int position) {
        // holder.usersLayout.setBackgroundColor(Color.parseColor("#"+users.get(position).profileBackgroundColor));
        holder.name.setText(Tweets.get(position).user.screenName);
        holder.tweetdescription.setText(Tweets.get(position).text);
        Picasso.with(context)
                .load(Tweets.get(position).user.profileImageUrl)
                .placeholder(R.drawable.spv1)
                .error(R.drawable.spv1)
                .resize(75,75)
                .into(holder.profile);

    }

    @Override
    public int getItemCount() {
        return Tweets.size();
    }
}
