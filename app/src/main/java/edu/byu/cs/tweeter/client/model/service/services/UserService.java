package edu.byu.cs.tweeter.client.model.service.services;

import android.os.Message;

import edu.byu.cs.tweeter.client.cache.Cache;
import edu.byu.cs.tweeter.client.model.service.handlers.BackgroundTaskHandler;
import edu.byu.cs.tweeter.client.model.service.observers.BackgroundTaskObserver;
import edu.byu.cs.tweeter.client.model.service.backgroundTask.GetUserTask;
import edu.byu.cs.tweeter.client.model.service.backgroundTask.LoginTask;
import edu.byu.cs.tweeter.client.model.service.backgroundTask.LogoutTask;
import edu.byu.cs.tweeter.client.model.service.backgroundTask.RegisterTask;
import edu.byu.cs.tweeter.client.model.service.observers.SetterTaskObserver;
import edu.byu.cs.tweeter.client.model.service.observers.UserTaskObserver;
import edu.byu.cs.tweeter.model.domain.AuthToken;
import edu.byu.cs.tweeter.model.domain.User;

public class UserService extends Service {

    //LOGIN
    /**
     * Message handler (i.e., observer) for LoginTask and RegisterTask
     */
    private static class LoginHandler extends BackgroundTaskHandler {
        public LoginHandler(UserTaskObserver observer) {
            super(observer);
        }

        @Override
        protected UserTaskObserver getObserver() {
            return (UserTaskObserver) observer;
        }

        @Override
        protected void handleSuccess(Message msg) {
            User loggedInUser = (User) msg.getData().getSerializable(LoginTask.USER_KEY);
            AuthToken authToken = (AuthToken) msg.getData().getSerializable(LoginTask.AUTH_TOKEN_KEY);

            // Cache user session information
            Cache.getInstance().setCurrUser(loggedInUser);
            Cache.getInstance().setCurrUserAuthToken(authToken);

            getObserver().handleSuccess(loggedInUser);
        }
    }

    public void login(String alias, String password, UserTaskObserver observer) {
        LoginTask loginTask = new LoginTask(alias,
                password,
                new LoginHandler(observer));
        executor.execute(loginTask);
    }

    public void register(String firstName, String lastName, String alias, String password, String imageBytesBase64, UserTaskObserver observer) {
        // Send register request.
        RegisterTask registerTask = new RegisterTask(firstName, lastName,
                alias, password, imageBytesBase64, new LoginHandler(observer));
        executor.execute(registerTask);
    }

    // LogoutHandler
    public void logout(SetterTaskObserver observer) {
        LogoutTask logoutTask = new LogoutTask(Cache.getInstance().getCurrUserAuthToken(), new LogoutHandler(observer));
        executor.execute(logoutTask);
    }

    private static class LogoutHandler extends BackgroundTaskHandler {
        public LogoutHandler(SetterTaskObserver observer) {
            super(observer);
        }

        @Override
        protected SetterTaskObserver getObserver() {
            return (SetterTaskObserver) observer;
        }

        @Override
        protected void handleSuccess(Message msg) {
            getObserver().handleSuccess();
        }
    }

    //Get User
    /**
     * Message handler (i.e., observer) for GetUserTask.
     */
    private static class GetUserHandler extends BackgroundTaskHandler {
        public GetUserHandler(UserTaskObserver observer) {
            super(observer);
        }

        @Override
        protected UserTaskObserver getObserver() {
            return (UserTaskObserver) observer;
        }

        @Override
        protected void handleSuccess(Message msg) {
            User user = (User) msg.getData().getSerializable(GetUserTask.USER_KEY);

            getObserver().handleSuccess(user);
        }
    }
    public void getUser(AuthToken currUserAuthToken, String alias, UserTaskObserver getUserObserver) {
        GetUserTask getUserTask = new GetUserTask(currUserAuthToken,
                alias, new GetUserHandler(getUserObserver));
        executor.execute(getUserTask);
    }
}
