
package eventtustask.com.eventtustask.ApiClient;

        import com.twitter.sdk.android.core.models.Tweet;
        import com.twitter.sdk.android.core.models.User;

        import java.util.List;

        import eventtustask.com.eventtustask.model.Followers;
        import eventtustask.com.eventtustask.model.Tweets;
        import retrofit.Callback;
        import retrofit.http.GET;
        import retrofit.http.Query;

// example users/show service endpoint
public interface CustomService {
    @GET("/1.1/followers/list.json")
    void show(@Query("user_id") Long userId, @Query("screen_name") String
            var, @Query("skip_status") Boolean var1, @Query("include_user_entities") Boolean var2,@Query("cursor") String cursor ,@Query("count") Integer var3, Callback <Followers> cb);
    @GET("/1.1/users/show.json")
    void getUser(@Query("user_id") Long userId, @Query("screen_name") String screen_name  , Callback <User> cb);
    @GET("/1.1/statuses/user_timeline.json")
    void GET_twits(@Query("user_id") Long userId,@Query("screen_name") String screen_name, @Query("count") int count, com.twitter.sdk.android.core.Callback<List<Tweet>> callback);
}

