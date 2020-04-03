package hibSerializerApp.view.abstraction;

import hibSerializerApp.model.Language;
import hibSerializerApp.model.LocaleString;
import javafx.event.ActionEvent;
import javafx.scene.image.Image;
import javafx.scene.layout.HBox;

import java.io.File;
import java.util.List;
import java.util.Map;

public interface MainViewController extends ViewController {
    Map<String, String> getLocaleRows(String name);

    void setLocaleRows(LocaleString localeRow, String name);

    void addAdditionalImages(List<Image> images);

    File getPathForSaveHibFile(ActionEvent event);

    File getHibFileFromDisk(ActionEvent event);

    void addPreviews(HBox hBox);

    Language getLanguageFromChoiceBox();

    void setLanguageInChoiceBox(Language language);
}
