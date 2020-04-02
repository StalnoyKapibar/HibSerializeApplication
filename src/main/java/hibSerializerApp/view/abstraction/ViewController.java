package hibSerializerApp.view.abstraction;

import javafx.event.ActionEvent;
import javafx.scene.image.Image;

import java.io.File;
import java.util.List;

public interface ViewController {

    File getImageFromFileChooser(ActionEvent ae);

    List<File> getFilesFromFileChooser(ActionEvent ae);

    File getDirectoryFromDirectoryChooser(ActionEvent ae);

    String getTextFromField(String name);

    void setImage(String name, Image image);

    void showError(Exception e);
}
