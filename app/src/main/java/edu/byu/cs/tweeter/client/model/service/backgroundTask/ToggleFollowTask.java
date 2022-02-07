package edu.byu.cs.tweeter.client.model.service.backgroundTask;

import android.os.Handler;

import edu.byu.cs.tweeter.model.domain.AuthToken;
import edu.byu.cs.tweeter.model.domain.User;

public abstract class ToggleFollowTask extends AuthorizedTask {
    /**
     * The user that is being followed.
     */
    private User followee;

    public ToggleFollowTask(Handler messageHandler, AuthToken authToken, User followee) {
        super(messageHandler, authToken);
        this.followee = followee;
    }
}
