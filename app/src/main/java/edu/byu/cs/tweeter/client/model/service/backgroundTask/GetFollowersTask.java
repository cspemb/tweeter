package edu.byu.cs.tweeter.client.model.service.backgroundTask;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;

import java.io.Serializable;
import java.util.List;

import edu.byu.cs.tweeter.model.domain.AuthToken;
import edu.byu.cs.tweeter.model.domain.User;
import edu.byu.cs.tweeter.util.FakeData;
import edu.byu.cs.tweeter.util.Pair;

/**
 * Background task that retrieves a page of followers.
 */
public class GetFollowersTask extends PagedTask<User> {
    public static final String FOLLOWERS_KEY = "followers";
    public GetFollowersTask(AuthToken authToken, User targetUser, int limit, User lastFollower,
                            Handler messageHandler) {
        super(messageHandler, authToken, limit, lastFollower, targetUser);
    }

    @Override
    protected String getItemsKey() {
        return FOLLOWERS_KEY;
    }

    @Override
    protected Pair<List<User>, Boolean> getItems() {
        return getFakeData().getPageOfUsers(getLastItem(), getLimit(), getTargetUser());
    }

    @Override
    protected String getLogTag() {
        return "GetFollowersTask";
    }
}
