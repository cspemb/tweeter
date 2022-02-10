package edu.byu.cs.tweeter.client.presenter.presenters;

import edu.byu.cs.tweeter.client.cache.Cache;
import edu.byu.cs.tweeter.client.model.service.services.FollowService;
import edu.byu.cs.tweeter.model.domain.User;

public class FollowersPresenter extends PagedPresenter<User> {
    private final FollowService followService;

    public interface FollowersView extends PagedView<User> {}

    public FollowersPresenter(FollowersView view) {
        super(view);
        followService = new FollowService();
    }

    protected FollowersView getView() {
        return (FollowersView) view;
    }

    @Override
    protected void getItems(User user) {
        followService.getFollowers(Cache.getInstance().getCurrUserAuthToken(), user, PAGE_SIZE, lastItem, new GetPageObserver("followers"));
    }
}
