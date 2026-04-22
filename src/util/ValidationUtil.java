
package util;

import javafx.scene.control.Alert;

public class ValidationUtil {

    public static boolean isPositiveInt(String str) {
        try {
            int val = Integer.parseInt(str);
            return val > 0;
        } catch (NumberFormatException e) {
            return false;
        }
    }


    public static boolean isValidArrival(String str) {
        try {
            int val = Integer.parseInt(str);
            return val >= 0;
        } catch (NumberFormatException e) {
            return false;
        }
    }


    public static void showWarning(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.WARNING);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}



