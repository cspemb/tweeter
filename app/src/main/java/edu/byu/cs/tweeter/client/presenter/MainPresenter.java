package edu.byu.cs.tweeter.client.presenter;

import android.util.Log;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

import edu.byu.cs.tweeter.client.model.service.observers.CountTaskObserver;
import edu.byu.cs.tweeter.client.model.service.observers.SetterTaskObserver;
import edu.byu.cs.tweeter.client.model.service.observers.ToggleFollowObserver;
import edu.byu.cs.tweeter.client.model.service.services.FollowService;
import edu.byu.cs.tweeter.client.model.service.services.StatusService;
import edu.byu.cs.tweeter.client.model.service.services.UserService;
import edu.byu.cs.tweeter.model.domain.User;

public class MainPresenter {
    private static final String LOG_TAG = "MainActivity";

    public interface View {
        void displayErrorMessage(String message);

        void setFollowButton(boolean isFollower);

        void setFollowerCount(int count);

        void setFolloweeCount(int count);

        void displayPostMessage(String s);

        void logoutUser();
    }

    private final View view;
    private final FollowService followService;
    private final StatusService statusService;
    private final UserService userService;

    private User selectedUser;

    public MainPresenter(View view, User selectedUser) {
        this.view = view;
        followService = new FollowService();
        statusService = new StatusService();
        userService = new UserService();

        this.selectedUser = selectedUser;
    }

    //isFollower
    public class IsFollowerObserver implements FollowService.IsFollowerObserver {

        @Override
        public void handleSuccess(boolean isFollower) {
            view.setFollowButton(isFollower);
        }

        @Override
        public void handleFailure(String message) {
            view.displayErrorMessage("Failed to determine following relationship: " + message);
        }

        @Override
        public void handleException(Exception ex) {
            view.displayErrorMessage("Failed to determine following relationship because of exception: " + ex.getMessage());
        }
    }
    public void isFollower(User selectedUser) {
        this.selectedUser = selectedUser;
        followService.isFollower(selectedUser, new IsFollowerObserver());
    }

    public void updateSelectedUserFollowingAndFollowers() {
        followService.updateSelectedUserFollowingAndFollowers(selectedUser, new GetFollowersCountObserver(), new GetFollowingCountObserver());
    }

    public class GetFollowingCountObserver implements CountTaskObserver {

        @Override
        public void handleSuccess(int count) {
            view.setFolloweeCount(count);
        }

        @Override
        public void handleFailure(String message) {
            view.displayErrorMessage("Failed to get following count: " + message);
        }

        @Override
        public void handleException(Exception ex) {
            view.displayErrorMessage("Failed to get following count because of exception: " + ex.getMessage());
        }
    }

    public class GetFollowersCountObserver implements CountTaskObserver {

        @Override
        public void handleSuccess(int count) {
            view.setFollowerCount(count);
        }

        @Override
        public void handleFailure(String message) {
            view.displayErrorMessage("Failed to get followers count: " + message);
        }

        @Override
        public void handleException(Exception ex) {
            view.displayErrorMessage("Failed to get followers count because of exception: " + ex.getMessage());
        }
    }

    //Unfollow
    public class UnfollowObserver implements ToggleFollowObserver {

        @Override
        public void handleSuccess() {
            view.setFollowButton(false);
        }

        @Override
        public void handleFailure(String message) {
            view.displayErrorMessage("Failed to unfollow: " + message);
        }

        @Override
        public void handleException(Exception ex) {
            view.displayErrorMessage("Failed to unfollow because of exception: " + ex.getMessage());
        }

        @Override
        public void updateSelectedUserFollowingAndFollowers() {
            MainPresenter.this.updateSelectedUserFollowingAndFollowers();
        }
    }

    public void unfollow(User selectedUser) {
        this.selectedUser = selectedUser;
        followService.unfollow(selectedUser, new UnfollowObserver());
    }

    //Follow
    public class FollowObserver implements ToggleFollowObserver {

        @Override
        public void handleSuccess() {
            view.setFollowButton(true);
        }

        @Override
        public void handleFailure(String message) {
            view.displayErrorMessage("Failed to follow: " + message);
        }

        @Override
        public void handleException(Exception ex) {
            view.displayErrorMessage("Failed to follow because of exception: " + ex.getMessage());
        }

        @Override
        public void updateSelectedUserFollowingAndFollowers() {
            MainPresenter.this.updateSelectedUserFollowingAndFollowers();
        }
    }

    public void follow(User selectedUser) {
        this.selectedUser = selectedUser;
        followService.follow(selectedUser, new FollowObserver());
    }

    //PostStatus

    public String getFormattedDateTime() throws ParseException {
        SimpleDateFormat userFormat = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
        SimpleDateFormat statusFormat = new SimpleDateFormat("MMM d yyyy h:mm aaa");

        return statusFormat.format(userFormat.parse(LocalDate.now().toString() + " " + LocalTime.now().toString().substring(0, 8)));
    }

    public List<String> parseURLs(String post) {
        List<String> containedUrls = new ArrayList<>();
        for (String word : post.split("\\s")) {
            if (word.startsWith("http://") || word.startsWith("https://")) {

                int index = findUrlEndIndex(word);

                word = word.substring(0, index);

                containedUrls.add(word);
            }
        }

        return containedUrls;
    }

    public List<String> parseMentions(String post) {
        List<String> containedMentions = new ArrayList<>();

        for (String word : post.split("\\s")) {
            if (word.startsWith("@")) {
                word = word.replaceAll("[^a-zA-Z0-9]", "");
                word = "@".concat(word);

                containedMentions.add(word);
            }
        }

        return containedMentions;
    }

    public int findUrlEndIndex(String word) {
        if (word.contains(".com")) {
            int index = word.indexOf(".com");
            index += 4;
            return index;
        } else if (word.contains(".org")) {
            int index = word.indexOf(".org");
            index += 4;
            return index;
        } else if (word.contains(".edu")) {
            int index = word.indexOf(".edu");
            index += 4;
            return index;
        } else if (word.contains(".net")) {
            int index = word.indexOf(".net");
            index += 4;
            return index;
        } else if (word.contains(".mil")) {
            int index = word.indexOf(".mil");
            index += 4;
            return index;
        } else {
            return word.length();
        }
    }

    public class PostStatusObserver implements SetterTaskObserver {

        @Override
        public void handleSuccess() {
            view.displayPostMessage("Successfully Posted!");
        }

        @Override
        public void handleFailure(String message) {
            view.displayErrorMessage("Failed to post status: " + message);
        }

        @Override
        public void handleException(Exception exception) {
            view.displayErrorMessage("Failed to post status because of exception: " + exception.getMessage());
        }
    }

    public void postStatus(String post) {
        try {
            statusService.postStatus(post, getFormattedDateTime(), parseURLs(post), parseMentions(post), new PostStatusObserver());
        }
        catch (Exception exception) {
            Log.e(LOG_TAG, exception.getMessage(), exception);
            view.displayErrorMessage(exception.getMessage());
        }
    }

    //Logout
    public class LogoutObserver implements SetterTaskObserver {

        @Override
        public void handleSuccess() {
            view.logoutUser();
        }

        @Override
        public void handleFailure(String message) {
            view.displayErrorMessage("Failed to logout: " + message);
        }

        @Override
        public void handleException(Exception exception) {
            view.displayErrorMessage("Failed to logout because of exception: " + exception.getMessage());
        }
    }

    public void logout() {
        userService.logout(new LogoutObserver());
    }
}

