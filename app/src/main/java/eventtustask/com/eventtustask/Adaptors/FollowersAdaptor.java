package eventtustask.com.eventtustask.Adaptors;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.squareup.picasso.Picasso;
import com.twitter.sdk.android.core.models.User;

import java.util.List;

import eventtustask.com.eventtustask.R;
import eventtustask.com.eventtustask.app.Token;
import eventtustask.com.eventtustask.profile_details;

/**
 * Created by ahmed shabaan on 6/13/2016.
 */


public class FollowersAdaptor extends RecyclerView.Adapter<FollowersAdaptor.FollowerViewHolder> {

    public static List<User> users;
    private int rowLayout;
    public static Context context;

    public static class FollowerViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener  {
        RelativeLayout usersLayout;
        TextView name;
        ImageView profile;
        TextView bio;


        public FollowerViewHolder(View v) {
            super(v);
            usersLayout = (RelativeLayout) v.findViewById(R.id.users_layout);
            name = (TextView) v.findViewById(R.id.name);
            bio = (TextView) v.findViewById(R.id.bio);
            profile = (ImageView) v.findViewById(R.id.profile_img);
            v.setOnClickListener(this);


        }


        @Override
        public void onClick(View v) {
            Context  c = v.getContext();

            Intent i = new Intent(FollowersAdaptor.context, profile_details.class);
            i.putExtra(Token.SCREEN_NAME,users.get(getAdapterPosition()).screenName);
            i.putExtra(Token.USER_ID,users.get(getAdapterPosition()).getId());
            c.startActivity(i);

        }
    }
    public  static class LoadingViewHolder extends RecyclerView.ViewHolder {
        public ProgressBar progressBar;

        public LoadingViewHolder(View itemView) {
            super(itemView);
            progressBar = (ProgressBar) itemView.findViewById(R.id.progressBar1);
        }
    }

    public FollowersAdaptor(List<User> users, int rowLayout, Context context) {
        this.users = users;
        this.rowLayout = rowLayout;
        this.context = context;
    }

    @Override
    public FollowersAdaptor.FollowerViewHolder onCreateViewHolder(ViewGroup parent,
                                                            int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(rowLayout, parent, false);
        return new FollowerViewHolder(view);
    }


    @Override
    public void onBindViewHolder(FollowerViewHolder holder, final int position) {
       // holder.usersLayout.setBackgroundColor(Color.parseColor("#"+users.get(position).profileBackgroundColor));
        holder.name.setText(users.get(position).screenName);
        holder.bio.setText(users.get(position).description);
        Picasso.with(context)
                .load(users.get(position).profileImageUrl)
                .placeholder(R.drawable.spv1)
                .error(R.drawable.spv1)
                .resize(75,75)
                .into(holder.profile);

    }

    @Override
    public int getItemCount() {
        return users.size();
    }
}
