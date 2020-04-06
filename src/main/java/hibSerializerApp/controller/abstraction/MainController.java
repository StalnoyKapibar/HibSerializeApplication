package hibSerializerApp.controller.abstraction;

import hibSerializerApp.view.MainViewControllerImpl;
import hibSerializerApp.view.abstraction.MainViewController;
import hibSerializerApp.view.abstraction.ViewController;
import javafx.event.ActionEvent;
import javafx.scene.input.MouseEvent;

import java.io.File;

public interface MainController extends Controller {

    void deserializeFromDisk(MainViewController mainViewController, File file);

    void searchHibFilesFromPath(MainViewController mainViewController, ActionEvent event);

    void addAdditionalFromDisk(MainViewController mainViewController, ActionEvent event);

    void addAdditionalFromWebCam(MainViewController mainViewController, ActionEvent event);

    void serialize(MainViewController mainViewController, ActionEvent event);

    void deserialize(MainViewController mainViewController, ActionEvent event);

    void selectPreviewItem(MainViewController mainViewController, MouseEvent mouseEvent);

    void selectPreviewItemAction(MainViewController mainViewController, MouseEvent mouseEvent);

    void createNewFile(MainViewController mainViewController, ActionEvent event);

    void delete(MainViewController mainViewController, ActionEvent event);

    void enableNightMode(MainViewController viewController, ActionEvent event);

    void enableLightMode(MainViewController viewController, ActionEvent event);

    void clear(ViewController viewController, ActionEvent event);

    void cancel(MainViewController MainViewController, ActionEvent event);

    void createNew(MainViewController mainViewController, ActionEvent event);

    void openImage(MainViewControllerImpl mainViewController, MouseEvent mouseEvent);
}
