package hibSerializerApp.view.abstraction;

import javafx.event.ActionEvent;
import javafx.scene.control.Alert;
import javafx.scene.image.Image;

import java.io.File;
import java.util.Arrays;
import java.util.List;

public interface ViewController {

    File getImageFromFileChooser(ActionEvent ae);

    List<File> getFilesFromFileChooser(ActionEvent ae);

    File getDirectoryFromDirectoryChooser(ActionEvent ae);

    String getTextFromField(String name);

    void setImage(String name, Image image);

    default void showError(Exception e) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Error");
        alert.setHeaderText(e.getMessage());
        alert.setContentText(Arrays.toString(e.getStackTrace()));
        alert.showAndWait();
    }

    void setTextInField(String textFieldName, String text);

    void deleteImageInView(String name);

    void clearListView(String name);

    void clearAllRows();
}
