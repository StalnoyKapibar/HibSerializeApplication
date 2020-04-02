package hibSerializerApp.controller.abstraction;

import hibSerializerApp.view.abstraction.MainViewController;
import javafx.event.ActionEvent;
import javafx.scene.input.MouseEvent;

public interface MainController extends Controller {


    void searchHibFilesFromPath(MainViewController viewController, ActionEvent ae);

    void addAdditionalFromDisk(MainViewController viewController, ActionEvent event);

    void addAdditionalFromWebCam(MainViewController mainViewController, ActionEvent event);

    void serialize(MainViewController viewController, ActionEvent event);

    void deserialize(MainViewController viewController, ActionEvent event);

    void selectPreviewItem(MainViewController viewController, MouseEvent mouseEvent);
}
