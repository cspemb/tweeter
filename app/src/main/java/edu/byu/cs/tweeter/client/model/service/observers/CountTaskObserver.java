package edu.byu.cs.tweeter.client.model.service.observers;

public interface CountTaskObserver extends BackgroundTaskObserver {
    void handleSuccess(int count);
}
