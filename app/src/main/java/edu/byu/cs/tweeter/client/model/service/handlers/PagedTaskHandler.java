package edu.byu.cs.tweeter.client.model.service.handlers;

import android.os.Handler;
import android.os.Message;

import androidx.annotation.NonNull;

import java.util.List;

import edu.byu.cs.tweeter.client.model.service.observers.PagedTaskObserver;
import edu.byu.cs.tweeter.client.model.service.backgroundTask.PagedTask;

public class PagedTaskHandler<T> extends Handler {
    private PagedTaskObserver<T> observer;

    public PagedTaskHandler(PagedTaskObserver<T> observer) {
        this.observer = observer;
    }

    @Override
    public void handleMessage(@NonNull Message msg) {
        boolean success = msg.getData().getBoolean(PagedTask.SUCCESS_KEY);
        if (success) {
            List<T> items = (List<T>) msg.getData().getSerializable(PagedTask.ITEMS_KEY);
            boolean hasMorePages = msg.getData().getBoolean(PagedTask.MORE_PAGES_KEY);
            observer.handleSuccess(items, hasMorePages);
        } else if (msg.getData().containsKey(PagedTask.MESSAGE_KEY)) {
            String message = msg.getData().getString(PagedTask.MESSAGE_KEY);
            observer.handleFailure(message);
        } else if (msg.getData().containsKey(PagedTask.EXCEPTION_KEY)) {
            Exception ex = (Exception) msg.getData().getSerializable(PagedTask.EXCEPTION_KEY);
            observer.handleException(ex);
        }
    }
}
