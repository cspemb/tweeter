package edu.byu.cs.tweeter.client.presenter;

import java.util.List;

import edu.byu.cs.tweeter.client.cache.Cache;
import edu.byu.cs.tweeter.client.model.service.FollowService;
import edu.byu.cs.tweeter.client.model.service.UserService;
import edu.byu.cs.tweeter.model.domain.User;

public class FollowingPresenter {
    private static final int PAGE_SIZE = 10;



    public interface View {
        void displayErrorMessage(String message);
        void setLoadingStatus(boolean isLoading);
        void addFollowees(List<User> followees);
        void showUser(User user);
    }

    private View view;

    private FollowService followService;
    private User lastFollowee;
    private boolean hasMorePages;

    private UserService userService;

    public boolean hasMorePages() {
        return hasMorePages;
    }

    public void setHasMorePages(boolean hasMorePages) {
        this.hasMorePages = hasMorePages;
    }

    public boolean isLoading() {
        return isLoading;
    }

    public void setLoading(boolean loading) {
        isLoading = loading;
    }

    private boolean isLoading = false;

    public FollowingPresenter(View view) {
        this.view = view;
        this.followService = new FollowService();
        this.userService = new UserService();
    }

    // Get Following
    public void loadMoreItems(User user) {
        if (!isLoading) {   // This guard is important for avoiding a race condition in the scrolling code.
            isLoading = true;
            view.setLoadingStatus(isLoading);

            followService.getFollowing(Cache.getInstance().getCurrUserAuthToken(), user, PAGE_SIZE, lastFollowee, new GetFollowingObserver());
        }

    }

    public class GetFollowingObserver implements FollowService.GetFollowingObserver {

        @Override
        public void handleSuccess(List<User> followees, boolean hasMorePages) {
            isLoading = false;
            view.setLoadingStatus(isLoading);

            lastFollowee = (followees.size() > 0) ? followees.get(followees.size() - 1) : null;
            setHasMorePages(hasMorePages);
            view.addFollowees(followees);
        }

        @Override
        public void handleFailure(String message) {
            isLoading = false;
            view.setLoadingStatus(isLoading);

            view.displayErrorMessage("Failed to get following: " + message);
        }

        @Override
        public void handleException(Exception exception) {
            isLoading = false;
            view.setLoadingStatus(isLoading);

            view.displayErrorMessage( "Failed to get following because of exception: " + exception.getMessage());
        }
    }

    // Get User

    public class GetUserObserver implements UserService.GetUserObserver{

        @Override
        public void handleSuccess(User user) {
            view.showUser(user);
        }

        @Override
        public void handleFailure(String message) {
            view.displayErrorMessage("Failed to get user's profile: " + message);
        }

        @Override
        public void handleException(Exception exception) {
            view.displayErrorMessage("Failed to get user's profile because of exception: " + exception.getMessage());
        }
    }

    public void getUser(String alias) {
        userService.getUser(Cache.getInstance().getCurrUserAuthToken(), alias, new GetUserObserver());
    }
}
