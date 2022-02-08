package edu.byu.cs.tweeter.client.model.service.observers;

import edu.byu.cs.tweeter.model.domain.User;

public interface UserTaskObserver extends BackgroundTaskObserver {
    void handleSuccess(User user);
}
