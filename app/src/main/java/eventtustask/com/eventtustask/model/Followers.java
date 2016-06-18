package eventtustask.com.eventtustask.model;

import com.google.gson.annotations.SerializedName;
import com.twitter.sdk.android.core.models.User;

import java.util.List;

/**
 * Created by ahmed shabaan on 6/13/2016.
 */
public class Followers {
    @SerializedName("users")
    public final List<User> users;

    public Followers(List<User> users) {
        this.users = users;
    }
    @SerializedName("next_cursor_str")
    public String next;
}
