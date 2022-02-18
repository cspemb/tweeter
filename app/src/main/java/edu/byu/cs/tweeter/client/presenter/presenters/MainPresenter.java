package edu.byu.cs.tweeter.client.presenter.presenters;

import android.util.Log;

import edu.byu.cs.tweeter.client.model.service.observers.CountTaskObserver;
import edu.byu.cs.tweeter.client.model.service.observers.SetterTaskObserver;
import edu.byu.cs.tweeter.client.model.service.services.FollowService;
import edu.byu.cs.tweeter.client.model.service.services.StatusService;
import edu.byu.cs.tweeter.client.model.service.services.UserService;
import edu.byu.cs.tweeter.client.presenter.InputParser;
import edu.byu.cs.tweeter.model.domain.User;

public class MainPresenter extends Presenter {
    private static final String LOG_TAG = "MainActivity";

    public interface MainView extends View {
        void setFollowButton(boolean isFollower);
        void setFollowerCount(int count);
        void setFolloweeCount(int count);
        void displayPostMessage(String s);
        void logoutUser();
    }

    private final FollowService followService;
    private StatusService statusService;
    private final UserService userService;

    private InputParser parser;

    private User selectedUser;

    public MainPresenter(MainView view) {
        super(view);
        followService = new FollowService();
        userService = new UserService();
    }

    @Override
    protected MainView getView() {
        return (MainView) view;
    }

    protected StatusService getStatusService() {
        if (statusService == null) {
            statusService = new StatusService();
        }

        return statusService;
    }

    protected InputParser getParser() {
        if (parser == null) {
            parser = new InputParser();
        }

        return parser;
    }

    //isFollower
    public class IsFollowerObserver extends TaskObserver implements FollowService.IsFollowerObserver {
        @Override
        public void handleSuccess(boolean isFollower) {
            getView().setFollowButton(isFollower);
        }

        @Override
        protected String getErrorPrompt() {
            return "Failed to determine following relationship";
        }
    }

    public void isFollower(User selectedUser) {
        this.selectedUser = selectedUser;
        followService.isFollower(selectedUser, new IsFollowerObserver());
    }

    public class GetFollowingCountObserver extends TaskObserver implements CountTaskObserver {

        @Override
        public void handleSuccess(int count) {
            getView().setFolloweeCount(count);
        }

        @Override
        protected String getErrorPrompt() {
            return "Failed to get following count";
        }
    }

    public class GetFollowersCountObserver extends TaskObserver implements CountTaskObserver {

        @Override
        public void handleSuccess(int count) {
            getView().setFollowerCount(count);
        }

        @Override
        protected String getErrorPrompt() {
            return "Failed to get followers count";
        }
    }

    public void updateSelectedUserFollowingAndFollowers(User selectedUser) {
        this.selectedUser = selectedUser;
        followService.updateSelectedUserFollowingAndFollowers(selectedUser, new GetFollowersCountObserver(), new GetFollowingCountObserver());
    }

    public class ToggleFollowObserver extends TaskObserver implements edu.byu.cs.tweeter.client.model.service.observers.ToggleFollowObserver {
        private final boolean newFollowStatus;

        public ToggleFollowObserver(boolean newFollowStatus) {
            this.newFollowStatus = newFollowStatus;
        }

        @Override
        public void handleSuccess() {
            getView().setFollowButton(newFollowStatus);
        }

        @Override
        protected String getErrorPrompt() {
            return "Failed to " + getFollowTask();
        }

        private String getFollowTask() {
            if (newFollowStatus) {
                return "follow";
            }
            else {
                return "unfollow";
            }
        }

        @Override
        public void updateSelectedUserFollowingAndFollowers() {
            MainPresenter.this.updateSelectedUserFollowingAndFollowers(selectedUser);
        }
    }

    public void unfollow(User selectedUser) {
        this.selectedUser = selectedUser;
        followService.unfollow(selectedUser, new ToggleFollowObserver(false));
    }

    public void follow(User selectedUser) {
        this.selectedUser = selectedUser;
        followService.follow(selectedUser, new ToggleFollowObserver(true));
    }

    //PostStatus
    public class PostStatusObserver extends TaskObserver implements SetterTaskObserver {

        @Override
        public void handleSuccess() {
            getView().displayPostMessage("Successfully Posted!");
        }

        @Override
        protected String getErrorPrompt() {
            return "Failed to post status";
        }
    }

    public void postStatus(String post) {
        try {
            getStatusService().postStatus(post, getParser().getFormattedDateTime(), getParser().parseURLs(post), getParser().parseMentions(post), new PostStatusObserver());
        }
        catch (Exception exception) {
            Log.e(LOG_TAG, exception.getMessage(), exception);
            getView().displayErrorMessage(exception.getMessage());
        }
    }

    //Logout
    public class LogoutObserver extends TaskObserver implements SetterTaskObserver {
        @Override
        public void handleSuccess() {
            getView().logoutUser();
        }

        @Override
        protected String getErrorPrompt() {
            return "Failed to logout";
        }
    }

    public void logout() {
        userService.logout(new LogoutObserver());
    }
}

