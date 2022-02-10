package edu.byu.cs.tweeter.client.presenter.presenters;

import edu.byu.cs.tweeter.client.model.service.observers.UserTaskObserver;
import edu.byu.cs.tweeter.client.model.service.services.UserService;
import edu.byu.cs.tweeter.model.domain.User;

public class LoginPresenter extends Presenter {
    public interface LoginView extends View {
        void login(User user);
    }

    private final UserService userService;

    public LoginPresenter(LoginView view) {
        super(view);
        userService = new UserService();
    }

    @Override
    protected LoginView getView() {
        return (LoginView) view;
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

    public class LoginObserver extends TaskObserver implements UserTaskObserver {

        @Override
        public void handleSuccess(User user) {
            getView().login(user);
        }

        @Override
        protected String getErrorPrompt() {
            return "Failed to login";
        }
    }

    public void login(String alias, String password) {
        userService.login(alias, password, new LoginObserver());
    }

}
