package edu.byu.cs.tweeter.client.model.service.backgroundTask;

import android.os.Handler;

import edu.byu.cs.tweeter.model.domain.AuthToken;
import edu.byu.cs.tweeter.model.domain.User;

public abstract class GetCountTask extends AuthorizedTask {
    public static final String COUNT_KEY = "count";

    /**
     * The user whose count is being retrieved.
     * (This can be any user, not just the currently logged-in user.)
     */
    private User targetUser;

    public GetCountTask(Handler messageHandler, AuthToken authToken, User targetUser) {
        super(messageHandler, authToken);
        this.targetUser = targetUser;
    }
}
