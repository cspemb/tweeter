package edu.byu.cs.tweeter.client.presenter.presenters;

import edu.byu.cs.tweeter.client.cache.Cache;
import edu.byu.cs.tweeter.client.model.service.services.StatusService;
import edu.byu.cs.tweeter.model.domain.Status;
import edu.byu.cs.tweeter.model.domain.User;

public class StoryPresenter extends PagedPresenter<Status> {
    private final StatusService statusService;

    public interface StoryView extends PagedView<Status>{}

    public StoryPresenter(StoryView view) {
        super(view);
        statusService = new StatusService();
    }

    @Override
    protected StoryView getView() {
        return (StoryView) view;
    }

    @Override
    protected void getItems(User user) {
        statusService.getStory(Cache.getInstance().getCurrUserAuthToken(), user, PAGE_SIZE, lastItem, new GetPageObserver("story"));
    }
}
