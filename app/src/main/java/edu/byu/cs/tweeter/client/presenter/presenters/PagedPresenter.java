package edu.byu.cs.tweeter.client.presenter.presenters;

import java.util.List;

import edu.byu.cs.tweeter.client.cache.Cache;
import edu.byu.cs.tweeter.client.model.service.observers.PagedTaskObserver;
import edu.byu.cs.tweeter.client.model.service.observers.UserTaskObserver;
import edu.byu.cs.tweeter.client.model.service.services.UserService;
import edu.byu.cs.tweeter.model.domain.User;

public abstract class PagedPresenter<T> extends Presenter {
    protected static final int PAGE_SIZE = 10;

    private final UserService userService;

    protected T lastItem;
    protected boolean hasMorePages;
    protected boolean isLoading = false;

    public boolean hasMorePages() {
        return hasMorePages;
    }

    public void setHasMorePages(boolean hasMorePages) {
        this.hasMorePages = hasMorePages;
    }

    public boolean isLoading() {
        return isLoading;
    }

    protected PagedPresenter(PagedView<T> view) {
        super(view);
        userService = new UserService();
    }

    public interface PagedView<U> extends View {
        void setLoadingStatus(boolean isLoading);
        void showUser(User user);
        void addItems(List<U> items);
    }

    protected abstract PagedView<T> getView();

    public void loadMoreItems(User user) {
        if (!isLoading()) {   // This guard is important for avoiding a race condition in the scrolling code.
            isLoading = true;
            getView().setLoadingStatus(true);
            getItems(user);
        }
    }

    protected abstract void getItems(User user);

    public class GetPageObserver implements PagedTaskObserver<T> {
        private String taskName;
        public GetPageObserver(String taskName) {
            this.taskName = taskName;
        }

        @Override
        public void handleSuccess(List<T> items, boolean hasMorePages) {
            isLoading = false;
            getView().setLoadingStatus(false);

            lastItem = (items.size() > 0) ? items.get(items.size() - 1) : null;
            setHasMorePages(hasMorePages);
            getView().addItems(items);
        }

        @Override
        public void handleFailure(String message) {
            isLoading = false;
            getView().setLoadingStatus(false);
            getView().displayErrorMessage("Failed to get " + taskName + ": " + message);
        }

        @Override
        public void handleException(Exception exception) {
            isLoading = false;
            getView().setLoadingStatus(false);
            getView().displayErrorMessage("Failed to get " + taskName + " because of exception: " + exception.getMessage());
        }
    }

    //Get User
    public class GetUserObserver extends TaskObserver implements UserTaskObserver {

        @Override
        public void handleSuccess(User user) {
            getView().showUser(user);
        }

        @Override
        protected String getErrorPrompt() {
            return "Failed to get user's profile";
        }
    }

    public void getUser(String alias) {
        userService.getUser(Cache.getInstance().getCurrUserAuthToken(), alias, new GetUserObserver());
    }
}
