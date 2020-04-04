package hibSerializerApp.controller.abstraction;

import hibSerializerApp.view.abstraction.ViewController;
import javafx.event.ActionEvent;

public interface Controller {
    void selectSearchDirectory(ViewController viewController, ActionEvent ae);

    void getAvatarFromDisk(ViewController viewController, ActionEvent event);

    void getAvatarFromWebCam(ViewController viewController, ActionEvent event);

    void deleteAvatar(ViewController viewController, ActionEvent event);

    void deleteAdditional(ViewController viewController, ActionEvent event);
}
