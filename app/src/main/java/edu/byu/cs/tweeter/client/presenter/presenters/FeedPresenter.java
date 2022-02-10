package edu.byu.cs.tweeter.client.presenter.presenters;

import edu.byu.cs.tweeter.client.cache.Cache;
import edu.byu.cs.tweeter.client.model.service.services.StatusService;
import edu.byu.cs.tweeter.model.domain.Status;
import edu.byu.cs.tweeter.model.domain.User;

public class FeedPresenter extends PagedPresenter<Status> {

    private final StatusService statusService;

    public interface FeedView extends PagedView<Status> {}

    public FeedPresenter(FeedView view) {
        super(view);

        statusService = new StatusService();
    }

    protected FeedView getView() {
        return (FeedView) view;
    }

    @Override
    protected void getItems(User user) {
        statusService.getFeed(Cache.getInstance().getCurrUserAuthToken(), user, PAGE_SIZE, lastItem, new GetPageObserver("feed"));
    }
}
