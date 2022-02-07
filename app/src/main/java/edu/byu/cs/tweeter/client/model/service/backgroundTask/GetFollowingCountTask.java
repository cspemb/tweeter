package edu.byu.cs.tweeter.client.model.service.backgroundTask;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;

import edu.byu.cs.tweeter.model.domain.AuthToken;
import edu.byu.cs.tweeter.model.domain.User;

/**
 * Background task that queries how many other users a specified user is following.
 */
public class GetFollowingCountTask extends GetCountTask {
    public GetFollowingCountTask(AuthToken authToken, User targetUser, Handler messageHandler) {
        super(messageHandler, authToken, targetUser);
    }

    @Override
    protected String getLogTag() {
        return "GetFollowingCountTask";
    }

    @Override
    protected void runTask() {
        //TODO milestone 3
    }

    @Override
    protected void loadMessageBundle(Bundle msgBundle) {
        //TODO undo hardcoded 20
        msgBundle.putInt(COUNT_KEY, 20);
    }
}
