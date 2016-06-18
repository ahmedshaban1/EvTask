package eventtustask.com.eventtustask.model;

import com.google.gson.annotations.SerializedName;
import com.twitter.sdk.android.core.models.Tweet;
import com.twitter.sdk.android.core.models.User;

import java.util.List;

/**
 * Created by ahmed shabaan on 6/14/2016.
 */
public class Tweets {
    @SerializedName("tweets")
    public final List<Tweet> Tweets;

    public Tweets(List<Tweet> Tweets) {
        this.Tweets = Tweets;
    }
}
