package hibSerializerApp.controller.abstraction;

import hibSerializerApp.view.abstraction.MainViewController;
import hibSerializerApp.view.abstraction.ViewController;
import javafx.event.ActionEvent;
import javafx.scene.input.MouseEvent;

public interface MainController extends Controller {

    void selectSearchDirectory(ViewController viewController, ActionEvent ae);

    void searchHibFilesFromPath(MainViewController viewController, ActionEvent ae);

    void getAvatarFromDisk(ViewController viewController, ActionEvent event);

    void getAvatarFromWebCam(ViewController viewController, ActionEvent event);

    void deleteAvatar(ViewController viewController, ActionEvent event);

    void addAdditionalFromDisk(MainViewController viewController, ActionEvent event);

    void addAdditionalFromWebCam(MainViewController mainViewController, ActionEvent event);

    void deleteAdditional(ViewController viewController, ActionEvent event);

    void serialize(MainViewController viewController, ActionEvent event);

    void cancel(ViewController viewController, ActionEvent event);

    void deserialize(MainViewController viewController, ActionEvent event);

    void selectPreviewItem(MainViewController viewController, MouseEvent mouseEvent);
}
