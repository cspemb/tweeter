package edu.byu.cs.tweeter.client.model.service.backgroundTask;

import android.os.Bundle;
import android.os.Handler;

import java.io.Serializable;
import java.util.List;

import edu.byu.cs.tweeter.model.domain.AuthToken;
import edu.byu.cs.tweeter.model.domain.User;
import edu.byu.cs.tweeter.util.Pair;

public abstract class PagedTask<T> extends AuthorizedTask {
    public static final String ITEMS_KEY = "items";
    public static final String MORE_PAGES_KEY = "more-pages";

    /**
     * Maximum number of items to return (i.e., page size).
     */
    private int limit;
    /**
     * The last item returned in the previous page of results (can be null).
     * This allows the new page to begin where the previous page ended.
     */
    private T lastItem;
    /**
     * The user whose data is being retrieved.
     * (This can be any user, not just the currently logged-in user.)
     */
    private User targetUser;

    private List<T> items;
    private boolean hasMorePages;

    public User getTargetUser() {
        return targetUser;
    }
    public int getLimit() {
        return limit;
    }
    public T getLastItem() {
        return lastItem;
    }

    public PagedTask(Handler messageHandler, AuthToken authToken, int limit, T lastItem, User targetUser) {
        super(messageHandler, authToken);
        this.limit = limit;
        this.lastItem = lastItem;
        this.targetUser = targetUser;
    }

    @Override
    protected void runTask() {
        Pair<List<T>, Boolean> pageOfItems = getItems();

        items = pageOfItems.getFirst();
        hasMorePages = pageOfItems.getSecond();
    }

    @Override
    protected void loadMessageBundle(Bundle msgBundle) {
        msgBundle.putSerializable(ITEMS_KEY, (Serializable) items);
        msgBundle.putBoolean(MORE_PAGES_KEY, hasMorePages);
    }

    protected abstract Pair<List<T>, Boolean> getItems();
}
