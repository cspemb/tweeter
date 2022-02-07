package edu.byu.cs.tweeter.client.model.service.backgroundTask;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;

import androidx.annotation.NonNull;

import edu.byu.cs.tweeter.util.FakeData;

public abstract class BackgroundTask implements Runnable {
    public static final String SUCCESS_KEY = "success";
    public static final String MESSAGE_KEY = "message";
    public static final String EXCEPTION_KEY = "exception";

    private final String LOG_TAG;
    /**
     * Message handler that will receive task results.
     */
    protected Handler messageHandler;

    public BackgroundTask(Handler messageHandler) {
        LOG_TAG = getLogTag();
        this.messageHandler = messageHandler;
    }

    protected abstract String getLogTag();

    @Override
    public void run() {
        try {
            runTask();

            sendSuccessMessage();

        } catch (Exception ex) {
            Log.e(LOG_TAG, ex.getMessage(), ex);
            sendExceptionMessage(ex);
        }
    }

    protected abstract void runTask();

    private void sendSuccessMessage() {
        Bundle msgBundle = createBundle(true);
        loadMessageBundle(msgBundle);

        sendMessage(msgBundle);
    }

    private void sendMessage(Bundle msgBundle) {
        Message msg = Message.obtain();
        msg.setData(msgBundle);

        messageHandler.sendMessage(msg);
    }

    @NonNull
    private Bundle createBundle(boolean value) {
        Bundle msgBundle = new Bundle();
        msgBundle.putBoolean(SUCCESS_KEY, value);
        return msgBundle;
    }

    protected abstract void loadMessageBundle(Bundle msgBundle);

    private void sendFailedMessage(String message) {
        Bundle msgBundle = createBundle(false);
        msgBundle.putString(MESSAGE_KEY, message);

        sendMessage(msgBundle);
    }

    private void sendExceptionMessage(Exception exception) {
        Bundle msgBundle = createBundle(false);
        msgBundle.putSerializable(EXCEPTION_KEY, exception);

        sendMessage(msgBundle);
    }

    protected FakeData getFakeData() {
        return new FakeData();
    }
}
