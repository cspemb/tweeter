package edu.byu.cs.tweeter.client.model.service.backgroundTask;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;

import edu.byu.cs.tweeter.model.domain.AuthToken;
import edu.byu.cs.tweeter.model.domain.User;

/**
 * Background task that removes a following relationship between two users.
 */
public class UnfollowTask extends ToggleFollowTask {
    public UnfollowTask(AuthToken authToken, User followee, Handler messageHandler) {
        super(messageHandler, authToken, followee);
    }

    @Override
    protected String getLogTag() {
        return "UnfollowTask";
    }

    @Override
    protected void runTask() {
        //TODO milestone 3
    }

    @Override
    protected void loadMessageBundle(Bundle msgBundle) {

    }
}
