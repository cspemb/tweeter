package edu.byu.cs.tweeter.client.presenter;

import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;

import java.io.ByteArrayOutputStream;
import java.util.Base64;

import edu.byu.cs.tweeter.client.model.service.UserService;
import edu.byu.cs.tweeter.model.domain.User;

public class RegisterPresenter {


    public interface View {
        void displayErrorMessage(String message);
        void register(User user);
    }

    private final View view;
    private final UserService userService;

    public RegisterPresenter(View view) {
        this.view = view;
        userService = new UserService();
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

    public class RegisterObserver implements UserService.RegisterObserver {
        @Override
        public void handleSuccess(User user) {
            view.register(user);
        }

        @Override
        public void handleFailure(String message) {
            view.displayErrorMessage("Failed to register: " + message);
        }

        @Override
        public void handleException(Exception exception) {
            view.displayErrorMessage("Failed to register because of exception: " + exception.getMessage());
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
