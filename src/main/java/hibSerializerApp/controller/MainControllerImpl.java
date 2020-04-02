package hibSerializerApp.controller;

import hibSerializerApp.model.Book;
import hibSerializerApp.model.LocaleString;
import hibSerializerApp.service.FileSystemServiceImpl;
import hibSerializerApp.service.abstraction.FileSystemService;
import hibSerializerApp.view.abstraction.MainViewController;
import hibSerializerApp.view.abstraction.ViewController;
import javafx.embed.swing.SwingFXUtils;
import javafx.event.ActionEvent;
import javafx.scene.image.Image;

import javax.imageio.ImageIO;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.stream.Collectors;

//TODO: реализация
public class MainControllerImpl implements MainController {
    private static MainControllerImpl instance = new MainControllerImpl();
    private final FileSystemService fileSystemService;
    private Book currentBook;

    private MainControllerImpl() {
        currentBook = new Book();
        currentBook.setAdditionalPhotos(new ArrayList<>());
        fileSystemService = new FileSystemServiceImpl();
    }

    public static MainControllerImpl getInstance() {
        return instance;
    }

    public void serialize(MainViewController viewController, ActionEvent ae) {
        currentBook.setName(new LocaleString(viewController.getLocaleRows("name")));
        currentBook.setAuthor(new LocaleString(viewController.getLocaleRows("author")));
        currentBook.setDesc(new LocaleString(viewController.getLocaleRows("desc")));
        currentBook.setEdition(new LocaleString(viewController.getLocaleRows("edition")));
        currentBook.setYearOfEdition(viewController.getTextFromField("year"));
        currentBook.setPages(Long.parseLong(viewController.getTextFromField("pages")));
        currentBook.setPrice(Long.parseLong(viewController.getTextFromField("price")));
        //TODO: сериализация
    }

    @Override
    public void cancel(ViewController viewController, ActionEvent event) {

    }

    @Override
    public void deserialize(ViewController viewController, ActionEvent event) {

    }

    @Override
    public void selectSearchDirectory(ViewController viewController, ActionEvent ae) {

    }

    @Override
    public void searchHibFilesFromPath(ViewController viewController, ActionEvent ae) {

    }

    public void getAvatarFromDisk(ViewController viewController, ActionEvent ae) {
        try {
            currentBook.setAvatar(fileSystemService.getFileBytes(viewController.getImageFromFileChooser(ae)));
            setAvatarImage(viewController,
                    SwingFXUtils
                            .toFXImage(ImageIO.read(new ByteArrayInputStream(currentBook.getAvatar())), null));
        } catch (IOException e) {
            viewController.showError(e);
            e.printStackTrace();
        }
    }

    @Override
    public void getAvatarFromWebCam(ViewController viewController, ActionEvent event) {

    }

    @Override
    public void deleteAvatar(ViewController viewController, ActionEvent event) {

    }

    @Override
    public void addAdditionalFromWebCam(ViewController viewController, ActionEvent event) {

    }

    @Override
    public void deleteAdditional(ViewController viewController, ActionEvent event) {

    }

    @Override
    public void addAdditionalFromDisk(MainViewController viewController, ActionEvent ae) {
        try {
            currentBook.getAdditionalPhotos().addAll(fileSystemService.getFilesBytes(viewController.getFilesFromFileChooser(ae)));
            viewController.addAdditionalImages(currentBook.getAdditionalPhotos().stream().map(e -> {
                try {
                    return SwingFXUtils.toFXImage(ImageIO.read(new ByteArrayInputStream(e)), null);
                } catch (IOException ex) {
                    viewController.showError(ex);
                    ex.printStackTrace();
                    return null;
                }
            }).collect(Collectors.toList()));
        } catch (IOException e) {
            viewController.showError(e);
            e.printStackTrace();
        }
    }

    private void setAvatarImage(ViewController viewController, Image image) {
        viewController.setImage("avatar", image);
    }
}
