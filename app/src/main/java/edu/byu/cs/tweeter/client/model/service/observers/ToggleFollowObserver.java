package edu.byu.cs.tweeter.client.model.service.observers;

public interface ToggleFollowObserver extends SetterTaskObserver {
    void updateSelectedUserFollowingAndFollowers();
}
