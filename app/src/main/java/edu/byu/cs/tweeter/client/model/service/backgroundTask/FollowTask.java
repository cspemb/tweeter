package edu.byu.cs.tweeter.client.model.service.backgroundTask;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;

import edu.byu.cs.tweeter.model.domain.AuthToken;
import edu.byu.cs.tweeter.model.domain.User;

/**
 * Background task that establishes a following relationship between two users.
 */
public class FollowTask extends ToggleFollowTask {
    public FollowTask(AuthToken authToken, User followee, Handler messageHandler) {
        super(messageHandler, authToken, followee);
    }

    @Override
    protected String getLogTag() {
        return "FollowTask";
    }

    @Override
    protected void runTask() {
        //TODO pay attention to code duplication with UnfollowTask (put in ToggleFollowTask)
        //TODO milestone 3
    }

    @Override
    protected void loadMessageBundle(Bundle msgBundle) {
        //TODO milestone 3
    }
}
