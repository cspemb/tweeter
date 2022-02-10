package edu.byu.cs.tweeter.client.presenter.presenters;

import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;

import java.io.ByteArrayOutputStream;
import java.util.Base64;

import edu.byu.cs.tweeter.client.model.service.observers.UserTaskObserver;
import edu.byu.cs.tweeter.client.model.service.services.UserService;
import edu.byu.cs.tweeter.model.domain.User;

public class RegisterPresenter extends Presenter {
    public interface RegisterView extends View {
        void register(User user);
    }

    private final UserService userService;

    public RegisterPresenter(RegisterView view) {
        super(view);
        userService = new UserService();
    }

    @Override
    protected RegisterView getView() {
        return (RegisterView) view;
    }

    public String validateRegistration(String firstName, String lastName, String alias, String password, Drawable imageToUpload) {
        try {
            if (firstName.length() == 0) {
                throw new IllegalArgumentException("First Name cannot be empty.");
            }
            if (lastName.length() == 0) {
                throw new IllegalArgumentException("Last Name cannot be empty.");
            }
            if (alias.length() == 0) {
                throw new IllegalArgumentException("Alias cannot be empty.");
            }
            if (alias.charAt(0) != '@') {
                throw new IllegalArgumentException("Alias must begin with @.");
            }
            if (alias.length() < 2) {
                throw new IllegalArgumentException("Alias must contain 1 or more characters after the @.");
            }
            if (password.length() == 0) {
                throw new IllegalArgumentException("Password cannot be empty.");
            }

            if (imageToUpload == null) {
                throw new IllegalArgumentException("Profile image must be uploaded.");
            }
        }
        catch (Exception e) {
            return e.getMessage();
        }

        return "";
    }

    public class RegisterObserver extends TaskObserver implements UserTaskObserver {
        @Override
        public void handleSuccess(User user) {
            getView().register(user);
        }

        @Override
        protected String getErrorPrompt() {
            return "Failed to register";
        }
    }

    public void register(String firstName, String lastName, String alias, String password, Drawable imageToUpload) {
        // Convert image to byte array.
        Bitmap image = ((BitmapDrawable) imageToUpload).getBitmap();
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        image.compress(Bitmap.CompressFormat.JPEG, 100, bos);
        byte[] imageBytes = bos.toByteArray();

        // Intentionally, Use the java Base64 encoder so it is compatible with M4.
        String imageBytesBase64 = Base64.getEncoder().encodeToString(imageBytes);

        userService.register(firstName, lastName, alias, password, imageBytesBase64, new RegisterObserver());
    }
}
