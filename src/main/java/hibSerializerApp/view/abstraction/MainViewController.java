package hibSerializerApp.view.abstraction;

import javafx.scene.image.Image;

import java.util.List;
import java.util.Map;

public interface MainViewController extends ViewController {
    Map<String, String> getLocaleRows(String name);

    void addAdditionalImages(List<Image> images);

}
