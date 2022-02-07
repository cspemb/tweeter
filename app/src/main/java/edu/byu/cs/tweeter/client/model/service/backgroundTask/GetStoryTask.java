package edu.byu.cs.tweeter.client.model.service.backgroundTask;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;

import java.io.Serializable;
import java.util.List;

import edu.byu.cs.tweeter.model.domain.AuthToken;
import edu.byu.cs.tweeter.model.domain.Status;
import edu.byu.cs.tweeter.model.domain.User;
import edu.byu.cs.tweeter.util.FakeData;
import edu.byu.cs.tweeter.util.Pair;

/**
 * Background task that retrieves a page of statuses from a user's story.
 */
public class GetStoryTask extends PagedTask<Status> {
    public static final String STATUSES_KEY = "statuses";

    public GetStoryTask(AuthToken authToken, User targetUser, int limit, Status lastStatus,
                        Handler messageHandler) {
        super(messageHandler, authToken, limit, lastStatus, targetUser);
    }

    @Override
    protected String getItemsKey() {
        return STATUSES_KEY;
    }

    @Override
    protected Pair<List<Status>, Boolean> getItems() {
        return getFakeData().getPageOfStatus(getLastItem(), getLimit());
    }

    @Override
    protected String getLogTag() {
        return "GetStoryTask";
    }
}