package hibSerializerApp.view;

import hibSerializerApp.controller.MainController;
import hibSerializerApp.controller.MainControllerImpl;
import hibSerializerApp.view.abstraction.MainViewController;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.ListView;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.control.TextInputControl;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.stage.DirectoryChooser;
import javafx.stage.FileChooser;

import java.io.File;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class MainViewControllerImpl implements MainViewController {
    private final FileChooser fileChooser;
    private final FileChooser hibFileChooser;
    private final DirectoryChooser directoryChooser;
    private final MainController mainController;
    @FXML
    TextField pages;
    @FXML
    private TextField nameRu;
    @FXML
    private TextField authorRu;
    @FXML
    private TextArea descRu;
    @FXML
    private TextField editionRu;
    @FXML
    private TextField nameEn;
    @FXML
    private TextField authorEn;
    @FXML
    private TextArea descEn;
    @FXML
    private TextField editionEn;
    @FXML
    private TextField nameFr;
    @FXML
    private TextField authorFr;
    @FXML
    private TextArea descFr;
    @FXML
    private TextField editionFr;
    @FXML
    private TextField nameIt;
    @FXML
    private TextField authorIt;
    @FXML
    private TextArea descIt;
    @FXML
    private TextField editionIt;
    @FXML
    private TextField nameDe;
    @FXML
    private TextField authorDe;
    @FXML
    private TextArea descDe;
    @FXML
    private TextField editionDe;
    @FXML
    private TextField nameCs;
    @FXML
    private TextField authorCs;
    @FXML
    private TextArea descCs;
    @FXML
    private TextField editionCs;
    @FXML
    private TextField nameGr;
    @FXML
    private TextField authorGr;
    @FXML
    private TextArea descGr;
    @FXML
    private TextField editionGr;
    @FXML
    private TextField year;
    @FXML
    private TextField price;
    @FXML
    private ListView<ImageView> additionalListView;
    @FXML
    private ListView<HBox> previewListView;
    @FXML
    private ImageView avatarImage;
    @FXML
    private TextField searchField;

    public MainViewControllerImpl() {
        fileChooser = new FileChooser();
        fileChooser.getExtensionFilters().add(
                new FileChooser.ExtensionFilter("JPEG Files", "*.jpg")
        );

        hibFileChooser = new FileChooser();
        hibFileChooser.getExtensionFilters().add(
                new FileChooser.ExtensionFilter(".hib Files", "*.hib")
        );

        directoryChooser = new DirectoryChooser();
        directoryChooser.setTitle("Select search directory");
        mainController = MainControllerImpl.getInstance();
    }

    @Override
    public Map<String, String> getLocaleRows(String name) {
        Map<String, String> localeMap = new HashMap<>();
        switch (name) {
            case "name":
                getLocaleText(localeMap, nameRu, nameEn, nameFr, nameIt, nameDe, nameCs, nameGr);
                break;
            case "author":
                getLocaleText(localeMap, authorRu, authorEn, authorFr, authorIt, authorDe, authorCs, authorGr);
                break;
            case "desc":
                getLocaleText(localeMap, descRu, descEn, descFr, descIt, descDe, descCs, descGr);
                break;
            case "edition":
                getLocaleText(localeMap, editionRu, editionEn, editionFr, editionIt, editionDe, editionCs, editionGr);
                break;
            default:
                throw new IllegalArgumentException("Nonexistent name " + name);
        }
        return localeMap;
    }

    @Override
    public void addAdditionalImages(List<Image> images) {
        additionalListView.getItems().addAll(images.stream().map(elem -> {
            ImageView imageView = new ImageView(elem);
            imageView.setFitHeight(600);
            imageView.setFitWidth(400);
            return imageView;
        }).collect(Collectors.toList()));
    }

    @Override
    public File getImageFromFileChooser(ActionEvent ae) {
        Node source = (Node) ae.getSource();
        File file = fileChooser.showOpenDialog(source.getScene().getWindow());
        fileChooser.setInitialDirectory(file.getParentFile());
        return file;
    }

    @Override
    public List<File> getFilesFromFileChooser(ActionEvent ae) {
        Node source = (Node) ae.getSource();
        List<File> files = fileChooser.showOpenMultipleDialog(source.getScene().getWindow());
        if (files.isEmpty()) return Collections.emptyList();
        fileChooser.setInitialDirectory(files.get(0).getParentFile());
        return files;
    }

    @Override
    public File getDirectoryFromDirectoryChooser(ActionEvent ae) {
        Node source = (Node) ae.getSource();
        File file = directoryChooser.showDialog(source.getScene().getWindow());
        directoryChooser.setInitialDirectory(file.getParentFile());
        return file;
    }

    @Override
    public String getTextFromField(String name) {
        switch (name) {
            case "year":
                return year.getText();
            case "pages":
                return pages.getText();
            case "price":
                return price.getText();
            default:
                throw new IllegalArgumentException("Nonexistent name " + name);
        }
    }

    @Override
    public void setImage(String name, Image image) {
        if (name.equals("avatar")) {
            avatarImage.setImage(image);
        } else {
            throw new IllegalArgumentException("Nonexistent name " + name);
        }
    }

    @Override
    public void showError(Exception e) {

    }

    private void getLocaleText(Map<String, String> localeMap, TextInputControl ru,
                               TextInputControl en, TextInputControl fr,
                               TextInputControl it, TextInputControl de,
                               TextInputControl cs, TextInputControl gr) {
        localeMap.put("ru", ru.getText());
        localeMap.put("en", en.getText());
        localeMap.put("fr", fr.getText());
        localeMap.put("it", it.getText());
        localeMap.put("de", de.getText());
        localeMap.put("cs", cs.getText());
        localeMap.put("gr", gr.getText());
    }

    public void selectSearchDirectory(ActionEvent event) {

    }

    public void searchHibFilesFromPath(ActionEvent event) {

    }

    public void getAvatarFromDisk(ActionEvent event) {
        mainController.getAvatarFromDisk(this, event);
    }

    public void getAvatarFromWebCam(ActionEvent event) {

    }

    public void deleteAvatar(ActionEvent event) {

    }

    public void addAdditionalFromDisk(ActionEvent event) {
        mainController.addAdditionalFromDisk(this, event);
    }

    public void addAdditionalFromWebCam(ActionEvent event) {

    }

    public void deleteAdditional(ActionEvent event) {

    }

    public void serialize(ActionEvent event) {

    }

    public void cancel(ActionEvent event) {

    }

    public void deserialize(ActionEvent event) {

    }
}
