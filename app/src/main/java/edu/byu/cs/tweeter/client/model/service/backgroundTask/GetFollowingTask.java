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
 * Background task that retrieves a page of other users being followed by a specified user.
 */
public class GetFollowingTask extends PagedTask<User> {
    public static final String FOLLOWEES_KEY = "followees";
    public GetFollowingTask(AuthToken authToken, User targetUser, int limit, User lastFollowee,
                            Handler messageHandler) {
        super(messageHandler, authToken, limit, lastFollowee, targetUser);
    }

    @Override
    protected String getItemsKey() {
        return FOLLOWEES_KEY;
    }

    @Override
    protected Pair<List<User>, Boolean> getItems() {
        return getFakeData().getPageOfUsers((User) getLastItem(), getLimit(), getTargetUser());
    }

    @Override
    protected String getLogTag() {
        return "GetFollowingTask";
    }
}
