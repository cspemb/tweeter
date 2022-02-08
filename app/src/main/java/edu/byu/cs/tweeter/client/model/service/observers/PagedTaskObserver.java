package edu.byu.cs.tweeter.client.model.service.observers;

import java.util.List;

public interface PagedTaskObserver<T> extends BackgroundTaskObserver {
    void handleSuccess(List<T> items, boolean hasMorePages);
}
