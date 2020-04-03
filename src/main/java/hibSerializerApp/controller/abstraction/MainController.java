package hibSerializerApp.controller.abstraction;

import hibSerializerApp.view.MainViewControllerImpl;
import hibSerializerApp.view.abstraction.MainViewController;
import javafx.event.ActionEvent;
import javafx.scene.input.MouseEvent;

public interface MainController extends Controller {

    void searchHibFilesFromPath(MainViewController mainViewController, ActionEvent event);

    void addAdditionalFromDisk(MainViewController mainViewController, ActionEvent event);

    void addAdditionalFromWebCam(MainViewController mainViewController, ActionEvent event);

    void serialize(MainViewController mainViewController, ActionEvent event);

    void deserialize(MainViewController mainViewController, ActionEvent event);

    void selectPreviewItem(MainViewController mainViewController, MouseEvent mouseEvent);

    void createNewFile(MainViewControllerImpl mainViewController, ActionEvent event);
}
