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
import hibSerializerApp.view.MainViewControllerImpl;
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
import javafx.scene.paint.Color;
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
    private File currentFile;
    private Integer currentWebCam;

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
        if (currentFile == null) {
            currentFile = viewController.getPathForSaveHibFile(ae);
        }
        currentBook.setName(new LocaleString(viewController.getLocaleRows("name")));
        currentBook.setAuthor(new LocaleString(viewController.getLocaleRows("author")));
        currentBook.setDesc(new LocaleString(viewController.getLocaleRows("desc")));
        currentBook.setEdition(new LocaleString(viewController.getLocaleRows("edition")));
        currentBook.setYearOfEdition(viewController.getTextFromField("year"));
        try {
            currentBook.setPages(Long.parseLong(viewController.getTextFromField("pages")));
            currentBook.setPrice(Long.parseLong(viewController.getTextFromField("price")));
        } catch (NumberFormatException ignore) {
            //ignored
        }

        currentBook.setOriginalLanguage(viewController.getLanguageFromChoiceBox());

        try {
            bookService.saveBook(currentBook, currentFile);
        } catch (IOException e) {
            viewController.showError(e);
            e.printStackTrace();
        }
        if (!viewController.getTextFromField("search").equals("")) {
            searchHibFilesFromPath(viewController, ae);
        }
    }

    @Override
    public void cancel(ViewController viewController, ActionEvent event) {
        viewController.clearAllRows();
        currentFile = null;
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
        File file = preview.get(index).getLocation();
        deserializeFromDisk(mainViewController, file);
    }

    @Override
    public void createNewFile(MainViewControllerImpl mainViewController, ActionEvent event) {
        currentFile = mainViewController.getPathForSaveHibFile(event);
        cancel(mainViewController, event);
    }

    @Override
    public void delete(MainViewControllerImpl mainViewController, ActionEvent event) {
        try {
            Files.delete(currentFile.toPath());
        } catch (IOException e) {
            mainViewController.showError(e);
            e.printStackTrace();
        }
        if (!mainViewController.getTextFromField("search").equals("")) {
            searchHibFilesFromPath(mainViewController, event);
        }
        cancel(mainViewController, event);
    }

    private void deserializeFromDisk(MainViewController mainViewController, File file) {
        currentFile = file;
        mainViewController.clearAllRows();
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
            mainViewController.setLanguageInChoiceBox(currentBook.getOriginalLanguage());
        } catch (NullPointerException ignored) {
            //ignored
        }

        try {
            setAvatarImage(mainViewController, SwingFXUtils
                    .toFXImage(ImageIO.read(new ByteArrayInputStream(currentBook.getAvatar())), null));
        } catch (IOException e) {
            mainViewController.showError(e);
            e.printStackTrace();
        } catch (NullPointerException ignore) {
            //ignored
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
        preview.clear();
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
            String title = getOriginalLanguageTitle(viewController, bookDTO);
            if (title != null) {
                text.setText(title);
            } else {
                text.setFill(Color.RED);
                text.setText("Original language not set" + "\n\n\n\n\n\n\n\n " + bookDTO.getLocation().getName());
            }

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

    private String getOriginalLanguageTitle(ViewController viewController, BookDTO book) {
        try {
            LocaleString name = book.getName();
            LocaleString author = book.getAuthor();
            String result;
            if (book.getOriginalLanguage() == null) return null;
            switch (book.getOriginalLanguage()) {
                case RU:
                    result = " " + author.getRu()
                            + " - " + name.getRu() + "\n\n\n\n\n\n\n\n " + book.getLocation().getName();
                    break;
                case EN:
                    result = " " + author.getEn()
                            + " - " + name.getEn() + "\n\n\n\n\n\n\n\n " + book.getLocation().getName();
                    break;
                case FR:
                    result = " " + author.getFr()
                            + " - " + name.getFr() + "\n\n\n\n\n\n\n\n " + book.getLocation().getName();
                    break;
                case IT:
                    result = " " + author.getIt()
                            + " - " + name.getIt() + "\n\n\n\n\n\n\n\n " + book.getLocation().getName();
                    break;
                case DE:
                    result = " " + author.getDe()
                            + " - " + name.getDe() + "\n\n\n\n\n\n\n\n " + book.getLocation().getName();
                    break;
                case CS:
                    result = " " + author.getCs()
                            + " - " + name.getCs() + "\n\n\n\n\n\n\n\n " + book.getLocation().getName();
                    break;
                case GR:
                    result = " " + author.getGr()
                            + " - " + name.getGr() + "\n\n\n\n\n\n\n\n " + book.getLocation().getName();
                    break;
                default:
                    throw new IllegalArgumentException(book.getOriginalLanguage().name() + " Not Found");
            }
            return result;
        } catch (IllegalStateException e) {
            viewController.showError(e);
            return null;
        }
    }

    public Integer getCurrentWebCam() {
        return currentWebCam;
    }

    public void setCurrentWebCam(Integer currentWebCam) {
        this.currentWebCam = currentWebCam;
    }
}
