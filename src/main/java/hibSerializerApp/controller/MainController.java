package hibSerializerApp.controller;

import hibSerializerApp.view.abstraction.MainViewController;
import hibSerializerApp.view.abstraction.ViewController;
import javafx.event.ActionEvent;

public interface MainController extends Controller {

    void selectSearchDirectory(ViewController viewController, ActionEvent ae);

    void searchHibFilesFromPath(ViewController viewController, ActionEvent ae);

    void getAvatarFromDisk(ViewController viewController, ActionEvent event);

    void getAvatarFromWebCam(ViewController viewController, ActionEvent event);

    void deleteAvatar(ViewController viewController, ActionEvent event);

    void addAdditionalFromDisk(MainViewController viewController, ActionEvent event);

    void addAdditionalFromWebCam(ViewController viewController, ActionEvent event);

    void deleteAdditional(ViewController viewController, ActionEvent event);

    void serialize(MainViewController viewController, ActionEvent event);

    void cancel(ViewController viewController, ActionEvent event);

    void deserialize(ViewController viewController, ActionEvent event);
}
