package edu.byu.cs.tweeter.client.model.service.services;

import android.os.Message;

import java.util.List;

import edu.byu.cs.tweeter.client.cache.Cache;
import edu.byu.cs.tweeter.client.model.service.handlers.BackgroundTaskHandler;
import edu.byu.cs.tweeter.client.model.service.observers.BackgroundTaskObserver;
import edu.byu.cs.tweeter.client.model.service.handlers.PagedTaskHandler;
import edu.byu.cs.tweeter.client.model.service.observers.PagedTaskObserver;
import edu.byu.cs.tweeter.client.model.service.backgroundTask.GetFeedTask;
import edu.byu.cs.tweeter.client.model.service.backgroundTask.GetStoryTask;
import edu.byu.cs.tweeter.client.model.service.backgroundTask.PostStatusTask;
import edu.byu.cs.tweeter.client.model.service.observers.SetterTaskObserver;
import edu.byu.cs.tweeter.model.domain.AuthToken;
import edu.byu.cs.tweeter.model.domain.Status;
import edu.byu.cs.tweeter.model.domain.User;

public class StatusService extends Service {
    public void getFeed(AuthToken currUserAuthToken, User user, int pageSize, Status lastStatus, PagedTaskObserver<Status> getFeedObserver) {
        GetFeedTask getFeedTask = new GetFeedTask(currUserAuthToken,
                user, pageSize, lastStatus, new PagedTaskHandler<>(getFeedObserver));
        executor.execute(getFeedTask);
    }

    public void getStory(AuthToken currUserAuthToken, User user, int pageSize, Status lastStatus, PagedTaskObserver<Status> getStoryObserver) {
        GetStoryTask getStoryTask = new GetStoryTask(currUserAuthToken,
                user, pageSize, lastStatus, new PagedTaskHandler<>(getStoryObserver));
        executor.execute(getStoryTask);
    }

    // PostStatusHandler
    public void postStatus(String post, String time, List<String> urls, List<String> mentions, SetterTaskObserver observer) {
        Status newStatus = new Status(post, Cache.getInstance().getCurrUser(), time, urls, mentions);
        PostStatusTask statusTask = new PostStatusTask(Cache.getInstance().getCurrUserAuthToken(),
                newStatus, new PostStatusHandler(observer));
        executor.execute(statusTask);
    }

    private class PostStatusHandler extends BackgroundTaskHandler {
        public PostStatusHandler(SetterTaskObserver observer) {
            super(observer);
        }

        @Override
        protected void handleSuccess(Message msg) {
            ((SetterTaskObserver) observer).handleSuccess();
        }
    }
}
