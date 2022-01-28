package edu.byu.cs.tweeter.client.presenter;

import edu.byu.cs.tweeter.client.model.service.UserService;
import edu.byu.cs.tweeter.model.domain.User;

public class LoginPresenter {
    public interface View {
        void displayErrorMessage(String message);
        void login(User user);
    }

    private View view;
    private UserService userService;

    public LoginPresenter(View view) {
        this.view = view;
        userService = new UserService();
    }

    /**
     * Checks for valid login credentials.
     *
     * @param alias the alias of the user
     * @param password the password of the user
     *
     * @return an empty string on success, else the message of the exception caused.
     */
    public String validateLogin(String alias, String password) {
        try {
            if (alias.charAt(0) != '@') {
                throw new IllegalArgumentException("Alias must begin with @.");
            }
            if (alias.length() < 2) {
                throw new IllegalArgumentException("Alias must contain 1 or more characters after the @.");
            }
            if (password.length() == 0) {
                throw new IllegalArgumentException("Password cannot be empty.");
            }
        }
        catch (Exception e) {
            return e.getMessage();
        }

        return "";
    }

    public class LoginObserver implements UserService.LoginObserver {

        @Override
        public void handleSuccess(User user) {
            view.login(user);
        }

        @Override
        public void handleFailure(String message) {
            view.displayErrorMessage("Failed to login: " + message);
        }

        @Override
        public void handleException(Exception exception) {
            view.displayErrorMessage("Failed to login because of exception: " + exception.getMessage());
        }
    }

    public void login(String alias, String password) {
        userService.login(alias, password, new LoginObserver());
    }

}
