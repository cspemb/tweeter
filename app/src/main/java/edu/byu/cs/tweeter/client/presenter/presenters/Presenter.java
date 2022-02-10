package edu.byu.cs.tweeter.client.presenter.presenters;

import edu.byu.cs.tweeter.client.model.service.observers.BackgroundTaskObserver;
import edu.byu.cs.tweeter.client.model.service.observers.UserTaskObserver;
import edu.byu.cs.tweeter.model.domain.User;

public abstract class Presenter {
    public interface View {
        void displayErrorMessage(String message);
    }

    protected final View view;

    protected Presenter(View view) {
        this.view = view;
    }

    protected abstract View getView();

    public abstract class TaskObserver implements BackgroundTaskObserver {
        @Override
        public void handleFailure(String message) {
            getView().displayErrorMessage(getErrorPrompt() + ": " + message);
        }

        @Override
        public void handleException(Exception exception) {
            getView().displayErrorMessage(getErrorPrompt() + " because of exception: " + exception.getMessage());
        }

        protected abstract String getErrorPrompt();
    }
}
