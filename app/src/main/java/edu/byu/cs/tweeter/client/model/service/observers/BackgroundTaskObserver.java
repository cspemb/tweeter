package edu.byu.cs.tweeter.client.model.service.observers;

public interface BackgroundTaskObserver {
    void handleFailure(String message);
    void handleException(Exception exception);
}
