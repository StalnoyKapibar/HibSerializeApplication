package hibSerializerApp.controller;

import hibSerializerApp.HibSerializerApplication;
import hibSerializerApp.controller.abstraction.MainController;
import hibSerializerApp.model.Book;
import hibSerializerApp.model.BookDTO;
import hibSerializerApp.model.LocaleString;
import hibSerializerApp.service.BookServiceImpl;
import hibSerializerApp.service.FileSystemServiceImpl;
import hibSerializerApp.service.abstraction.BookService;
import hibSerializerApp.service.abstraction.FileSystemService;
import hibSerializerApp.view.WebCamPreviewViewController;
import hibSerializerApp.view.abstraction.MainViewController;
import hibSerializerApp.view.abstraction.ViewController;
import javafx.embed.swing.SwingFXUtils;
import javafx.event.ActionEvent;
import javafx.scene.control.Alert;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;
import javafx.scene.text.Text;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class MainControllerImpl implements MainController {
    private static MainControllerImpl instance = new MainControllerImpl();
    private final FileSystemService fileSystemService;
    private final BookService bookService;
    private Book currentBook;
    private List<BookDTO> preview;

    private MainControllerImpl() {
        currentBook = new Book();
        currentBook.setAdditionalPhotos(new ArrayList<>());
        preview = new ArrayList<>();
        fileSystemService = new FileSystemServiceImpl();
        bookService = new BookServiceImpl();
    }

    public static MainControllerImpl getInstance() {
        return instance;
    }

    public void serialize(MainViewController viewController, ActionEvent ae) {
        File file = viewController.getPathForSaveHibFile(ae);
        currentBook.setName(new LocaleString(viewController.getLocaleRows("name")));
        currentBook.setAuthor(new LocaleString(viewController.getLocaleRows("author")));
        currentBook.setDesc(new LocaleString(viewController.getLocaleRows("desc")));
        currentBook.setEdition(new LocaleString(viewController.getLocaleRows("edition")));
        currentBook.setYearOfEdition(viewController.getTextFromField("year"));
        try {
            currentBook.setPages(Long.parseLong(viewController.getTextFromField("pages")));
            currentBook.setPages(Long.parseLong(viewController.getTextFromField("price")));
        } catch (NumberFormatException ignore) {
            //ignored
        }

        try {
            bookService.saveBook(currentBook, file);
        } catch (IOException e) {
            viewController.showError(e);
            e.printStackTrace();
        }
    }

    @Override
    public void cancel(ViewController viewController, ActionEvent event) {
        viewController.clearAllRows();
        currentBook = new Book();
        currentBook.setAdditionalPhotos(new ArrayList<>());
    }

    @Override
    public void deserialize(MainViewController mainViewController, ActionEvent event) {
        File file = mainViewController.getHibFileFromDisk(event);
        deserializeFromDisk(mainViewController, file);
    }

    @Override
    public void selectPreviewItem(MainViewController mainViewController, MouseEvent mouseEvent) {
        Integer index = mainViewController.getSelectedItemIndex("preview");
        File file = new File(preview.get(index).getLocation());
        deserializeFromDisk(mainViewController, file);
    }

    private void deserializeFromDisk(MainViewController mainViewController, File file) {
        try {
            currentBook = bookService.getBook(file);
        } catch (IOException | ClassNotFoundException e) {
            mainViewController.showError(e);
            e.printStackTrace();
            return;
        }

        try {
            mainViewController.setLocaleRows(currentBook.getName(), "name");
            mainViewController.setLocaleRows(currentBook.getAuthor(), "author");
            mainViewController.setLocaleRows(currentBook.getDesc(), "desc");
            mainViewController.setLocaleRows(currentBook.getEdition(), "edition");

            mainViewController.setTextInField("year", currentBook.getYearOfEdition());
            mainViewController.setTextInField("pages", currentBook.getPages().toString());
            mainViewController.setTextInField("price", currentBook.getPrice().toString());
        } catch (NullPointerException ignored) {
            //ignored
        }

        try {
            setAvatarImage(mainViewController, SwingFXUtils
                    .toFXImage(ImageIO.read(new ByteArrayInputStream(currentBook.getAvatar())), null));
        } catch (IOException | NullPointerException e) {
            mainViewController.showError(e);
            e.printStackTrace();
        }
        mainViewController.clearListView("additional");
        addImagesToListView(mainViewController);
    }

    @Override
    public void selectSearchDirectory(ViewController viewController, ActionEvent ae) {
        File file = viewController.getDirectoryFromDirectoryChooser(ae);
        if (file == null) return;
        viewController.setTextInField("search", file.getAbsolutePath());
    }

    @Override
    public void searchHibFilesFromPath(MainViewController viewController, ActionEvent ae) {
        viewController.clearListView("preview");
        String stringPath = viewController.getTextFromField("search");
        if (stringPath.equals("")) {
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("Warning");
            alert.setHeaderText("Select a directory before searching");
            alert.showAndWait();
        }

        List<File> previewFiles = new ArrayList<>();

        try (Stream<Path> paths = Files.walk(Paths.get(stringPath), 1)) {
            paths
                    .filter(e -> e.toString().endsWith(".hib"))
                    .forEach(path -> previewFiles.add(path.toFile()));
        } catch (IOException e) {
            e.printStackTrace();
        }

        for (File file : previewFiles) {
            BookDTO bookDTO = bookService.getBookDTO(file);
            preview.add(bookDTO);
            Text text = new Text();
            text.setText(" " + bookDTO.getAuthor().getEn() + " - " + bookDTO.getName().getEn());
            ImageView imageView = null;
            try {
                if (bookDTO.getAvatar() != null) imageView = new ImageView(SwingFXUtils
                        .toFXImage((ImageIO.read(new ByteArrayInputStream(bookDTO.getAvatar()))), null));
                else imageView = new ImageView(new Image("images/noImage.png"));
            } catch (IOException e) {
                e.printStackTrace();
            }
            imageView.setFitWidth(100);
            imageView.setFitHeight(150);
            HBox hBox = new HBox(imageView, text);
            viewController.addPreviews(hBox);
        }
    }

    @Override
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
        byte[] newPhoto = new byte[0];
        try {
            newPhoto = getPhotoFromWebCam(event);
        } catch (IOException e) {
            viewController.showError(e);
            e.printStackTrace();
        }
        if (newPhoto.length == 0) return;
        currentBook.setAvatar(newPhoto);
        try {
            setAvatarImage(viewController, SwingFXUtils
                    .toFXImage(ImageIO.read(new ByteArrayInputStream(currentBook.getAvatar())), null));
        } catch (IOException e) {
            viewController.showError(e);
            e.printStackTrace();
        }
    }

    @Override
    public void deleteAvatar(ViewController viewController, ActionEvent event) {
        currentBook.setAvatar(null);
        viewController.deleteImageInView("avatar");
    }

    @Override
    public void addAdditionalFromWebCam(MainViewController mainViewController, ActionEvent event) {
        try {
            byte[] newPhoto = getPhotoFromWebCam(event);
            if (newPhoto.length == 0) return;
            currentBook.getAdditionalPhotos().add(newPhoto);
            mainViewController.addAdditionalImages(new ArrayList<>(Collections.singletonList(SwingFXUtils
                    .toFXImage(ImageIO
                            .read(new ByteArrayInputStream(currentBook
                                    .getAdditionalPhotos()
                                    .get(currentBook
                                            .getAdditionalPhotos()
                                            .size() - 1))), null))));
        } catch (IOException e) {
            mainViewController.showError(e);
            e.printStackTrace();
        }
    }

    @Override
    public void deleteAdditional(ViewController viewController, ActionEvent event) {
        currentBook.setAdditionalPhotos(new ArrayList<>());
        viewController.clearListView("additional");
    }

    @Override
    public void addAdditionalFromDisk(MainViewController viewController, ActionEvent ae) {
        try {
            currentBook.getAdditionalPhotos().addAll(fileSystemService.getFilesBytes(viewController.getFilesFromFileChooser(ae)));
            addImagesToListView(viewController);
        } catch (IOException e) {
            viewController.showError(e);
            e.printStackTrace();
        }
    }

    private void setAvatarImage(ViewController viewController, Image image) {
        viewController.setImage("avatar", image);
    }

    private void addImagesToListView(MainViewController mainViewController) {
        mainViewController.addAdditionalImages(currentBook.getAdditionalPhotos().stream().map(e -> {
            try {
                return SwingFXUtils.toFXImage(ImageIO.read(new ByteArrayInputStream(e)), null);
            } catch (IOException ex) {
                mainViewController.showError(ex);
                ex.printStackTrace();
                return null;
            }
        }).collect(Collectors.toList()));
    }

    private byte[] getPhotoFromWebCam(ActionEvent ae) throws IOException {
        WebCamPreviewViewController controller = HibSerializerApplication.startWebCamModal();
        controller.stopCamera(ae);
        BufferedImage photo = controller.getPhoto();
        try (ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream()) {
            if (photo != null) {
                ImageIO.write(photo, "jpg", byteArrayOutputStream);
            }
            return byteArrayOutputStream.toByteArray();
        }
    }
}
