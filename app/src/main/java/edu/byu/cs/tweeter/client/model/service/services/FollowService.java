package edu.byu.cs.tweeter.client.model.service.services;

import android.os.Message;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import edu.byu.cs.tweeter.client.cache.Cache;
import edu.byu.cs.tweeter.client.model.service.handlers.BackgroundTaskHandler;
import edu.byu.cs.tweeter.client.model.service.observers.BackgroundTaskObserver;
import edu.byu.cs.tweeter.client.model.service.handlers.PagedTaskHandler;
import edu.byu.cs.tweeter.client.model.service.observers.CountTaskObserver;
import edu.byu.cs.tweeter.client.model.service.observers.PagedTaskObserver;
import edu.byu.cs.tweeter.client.model.service.observers.ToggleFollowObserver;
import edu.byu.cs.tweeter.client.model.service.backgroundTask.FollowTask;
import edu.byu.cs.tweeter.client.model.service.backgroundTask.GetFollowersCountTask;
import edu.byu.cs.tweeter.client.model.service.backgroundTask.GetFollowersTask;
import edu.byu.cs.tweeter.client.model.service.backgroundTask.GetFollowingCountTask;
import edu.byu.cs.tweeter.client.model.service.backgroundTask.GetFollowingTask;
import edu.byu.cs.tweeter.client.model.service.backgroundTask.IsFollowerTask;
import edu.byu.cs.tweeter.client.model.service.backgroundTask.UnfollowTask;
import edu.byu.cs.tweeter.model.domain.AuthToken;
import edu.byu.cs.tweeter.model.domain.User;

public class FollowService extends Service{
    public void getFollowing(AuthToken currUserAuthToken, User user, int pageSize, User lastFollowee, PagedTaskObserver<User> getFollowingObserver) {
        GetFollowingTask getFollowingTask = new GetFollowingTask(currUserAuthToken,
                user, pageSize, lastFollowee, new PagedTaskHandler<>(getFollowingObserver));
        executor.execute(getFollowingTask);
    }

    public void getFollowers(AuthToken currUserAuthToken, User user, int pageSize, User lastFollower, PagedTaskObserver<User> getFollowersObserver) {
        GetFollowersTask getFollowersTask = new GetFollowersTask(currUserAuthToken,
                user, pageSize, lastFollower, new PagedTaskHandler<>(getFollowersObserver));
        executor.execute(getFollowersTask);
    }

    // IsFollower
    public interface IsFollowerObserver extends BackgroundTaskObserver {
        void handleSuccess(boolean isFollower);
    }

    public void isFollower(User selectedUser, IsFollowerObserver isFollowerObserver) {
        IsFollowerTask isFollowerTask = new IsFollowerTask(Cache.getInstance().getCurrUserAuthToken(),
                Cache.getInstance().getCurrUser(), selectedUser, new IsFollowerHandler(isFollowerObserver));
        executor.execute(isFollowerTask);
    }

    private class IsFollowerHandler extends BackgroundTaskHandler {

        public IsFollowerHandler(BackgroundTaskObserver observer) {
            super(observer);
        }

        @Override
        protected void handleSuccess(Message msg) {
            boolean isFollower = msg.getData().getBoolean(IsFollowerTask.IS_FOLLOWER_KEY);

            ((IsFollowerObserver) observer).handleSuccess(isFollower);
        }
    }

    public void updateSelectedUserFollowingAndFollowers(User selectedUser, CountTaskObserver getFollowersCountObserver, CountTaskObserver getFollowingCountObserver) {
        ExecutorService executor = Executors.newFixedThreadPool(2);

        // Get count of most recently selected user's followers.
        GetFollowersCountTask followersCountTask = new GetFollowersCountTask(Cache.getInstance().getCurrUserAuthToken(),
                selectedUser, new GetCountHandler(getFollowersCountObserver, GetFollowersCountTask.COUNT_KEY));
        executor.execute(followersCountTask);

        // Get count of most recently selected user's followees (who they are following)
        GetFollowingCountTask followingCountTask = new GetFollowingCountTask(Cache.getInstance().getCurrUserAuthToken(),
                selectedUser, new GetCountHandler(getFollowingCountObserver, GetFollowingCountTask.COUNT_KEY));
        executor.execute(followingCountTask);
    }

    private class GetCountHandler extends BackgroundTaskHandler {
        private final String COUNT_KEY;

        public GetCountHandler(CountTaskObserver observer, String COUNT_KEY) {
            super(observer);
            this.COUNT_KEY = COUNT_KEY;
        }

        @Override
        protected void handleSuccess(Message msg) {
            int count = msg.getData().getInt(COUNT_KEY);
            ((CountTaskObserver) observer).handleSuccess(count);
        }
    }

    // UnfollowHandler
    public void unfollow(User selectedUser, ToggleFollowObserver unfollowObserver) {
        UnfollowTask unfollowTask = new UnfollowTask(Cache.getInstance().getCurrUserAuthToken(),
                selectedUser, new ToggleFollowHandler(unfollowObserver));
        executor.execute(unfollowTask);
    }

    // FollowHandler
    public void follow(User selectedUser, ToggleFollowObserver followObserver) {
        FollowTask followTask = new FollowTask(Cache.getInstance().getCurrUserAuthToken(),
                selectedUser, new ToggleFollowHandler(followObserver));
        executor.execute(followTask);
    }

    private class ToggleFollowHandler extends BackgroundTaskHandler {
        public ToggleFollowHandler(ToggleFollowObserver observer) {
            super(observer);
        }

        @Override
        protected void handleSuccess(Message msg) {
            ((ToggleFollowObserver) observer).updateSelectedUserFollowingAndFollowers();
            ((ToggleFollowObserver) observer).handleSuccess();
        }
    }
}
