package edu.byu.cs.tweeter.client.model.service.backgroundTask;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;

import edu.byu.cs.tweeter.model.domain.AuthToken;

/**
 * Background task that logs out a user (i.e., ends a session).
 */
public class LogoutTask extends AuthorizedTask {
    public LogoutTask(AuthToken authToken, Handler messageHandler) {
        super(messageHandler, authToken);
    }

    @Override
    protected String getLogTag() {
        return "LogoutTask";
    }

    @Override
    protected void runTask() {
        //TODO milestone 3
    }

    @Override
    protected void loadMessageBundle(Bundle msgBundle) {

    }
}
