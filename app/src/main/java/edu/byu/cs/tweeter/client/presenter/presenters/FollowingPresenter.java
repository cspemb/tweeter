package edu.byu.cs.tweeter.client.presenter.presenters;

import edu.byu.cs.tweeter.client.cache.Cache;
import edu.byu.cs.tweeter.client.model.service.services.FollowService;
import edu.byu.cs.tweeter.model.domain.User;

public class FollowingPresenter extends PagedPresenter<User> {
    private final FollowService followService;

    public interface FollowingView extends PagedView<User> {
    }

    public FollowingPresenter(FollowingView view) {
        super(view);
        this.followService = new FollowService();
    }

    protected FollowingView getView() {
        return (FollowingView) view;
    }

    @Override
    protected void getItems(User user) {
        followService.getFollowing(Cache.getInstance().getCurrUserAuthToken(), user, PAGE_SIZE, lastItem, new GetPageObserver("following"));
    }
}
