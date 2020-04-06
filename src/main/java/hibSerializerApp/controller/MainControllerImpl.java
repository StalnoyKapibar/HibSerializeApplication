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
import hibSerializerApp.view.SaveModalViewController;
import hibSerializerApp.view.WebCamPreviewViewController;
import hibSerializerApp.view.abstraction.MainViewController;
import hibSerializerApp.view.abstraction.ViewController;
import javafx.embed.swing.SwingFXUtils;
import javafx.event.ActionEvent;
import javafx.scene.Node;
import javafx.scene.control.Alert;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import jfxtras.styles.jmetro.Style;

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
    private Book originalBook;
    private Book currentBook;
    private List<BookDTO> preview;
    private File currentFile;
    private Integer currentWebCam;
    private Style currentStyle = Style.LIGHT;

    private MainControllerImpl() {
        currentBook = new Book();
        originalBook = new Book();
        currentBook.setAdditionalPhotos(new ArrayList<>());
        preview = new ArrayList<>();
        fileSystemService = new FileSystemServiceImpl();
        bookService = new BookServiceImpl();
    }

    public static MainControllerImpl getInstance() {
        return instance;
    }

    public void serialize(MainViewController viewController, ActionEvent ae) {
        collectCurrentBook(viewController, ae);
        if (currentFile == null) {
            currentFile = viewController.getPathForSaveHibFile(ae,
                    getOriginalLanguageFileName());
            if (currentFile == null) return;
            viewController.setTextInField("currentDir", currentFile.getAbsolutePath());
        }
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
    public void cancel(MainViewController mainViewController, ActionEvent event) {
        startSaveModal(e -> e.clear(mainViewController, event), mainViewController, event);
    }

    @Override
    public void createNew(MainViewController mainViewController, ActionEvent event) {
        clear(mainViewController, event);
        currentFile = mainViewController.getPathForSaveHibFile(event);
        if (currentFile == null) return;
        mainViewController.setTextInField("currentDir", currentFile.getAbsolutePath());
    }

    @Override
    public void openImage(MainViewControllerImpl mainViewController, MouseEvent mouseEvent) {
        Integer index = mainViewController.getSelectedItemIndex("additional");
        if (index < 0) return;
        HibSerializerApplication.openImage(currentStyle, currentBook.getAdditionalPhotos().get(index));
    }

    private void collectCurrentBook(MainViewController mainViewController, ActionEvent event) {
        currentBook.setName(new LocaleString(mainViewController.getLocaleRows("name")));
        currentBook.setAuthor(new LocaleString(mainViewController.getLocaleRows("author")));
        currentBook.setDesc(new LocaleString(mainViewController.getLocaleRows("desc")));
        currentBook.setEdition(new LocaleString(mainViewController.getLocaleRows("edition")));
        currentBook.setYearOfEdition(mainViewController.getTextFromField("year"));
        try {
            currentBook.setPages(Long.parseLong(mainViewController.getTextFromField("pages")));
            currentBook.setPrice(Long.parseLong(mainViewController.getTextFromField("price")));
        } catch (NumberFormatException ignore) {
            //ignored
        }

        currentBook.setOriginalLanguage(mainViewController.getLanguageFromChoiceBox());
    }

    @Override
    public void enableNightMode(MainViewController mainViewController, ActionEvent event) {
        currentStyle = Style.DARK;
        Node node = (Node) event.getSource();
        Stage stage = (Stage) node.getScene().getWindow();
        stage.close();
        HibSerializerApplication.startWithDarkMode(stage);
    }

    @Override
    public void enableLightMode(MainViewController mainViewController, ActionEvent event) {
        currentStyle = Style.LIGHT;
        Node node = (Node) event.getSource();
        Stage stage = (Stage) node.getScene().getWindow();
        stage.close();
        HibSerializerApplication.startWithLightMode(stage);
    }

    @Override
    public void clear(ViewController viewController, ActionEvent event) {
        viewController.clearAllRows();
        currentFile = null;
        viewController.setTextInField("currentDir", "");
        currentBook = new Book();
        originalBook = new Book();
        currentBook.setAdditionalPhotos(new ArrayList<>());
    }

    @Override
    public void deserialize(MainViewController mainViewController, ActionEvent event) {
        File file = mainViewController.getHibFileFromDisk(event);
        startSaveModal(e -> e.deserializeFromDisk(mainViewController, file), mainViewController, event);
        deserializeFromDisk(mainViewController, file);
    }

    @Override
    public void selectPreviewItem(MainViewController mainViewController, MouseEvent me) {
        selectPreviewItemAction(mainViewController, me);
    }

    @Override
    public void selectPreviewItemAction(MainViewController mainViewController, MouseEvent mouseEvent) {
        Integer index = mainViewController.getSelectedItemIndex("preview");
        if (index < 0) return;
        File file = preview.get(index).getLocation();
        deserializeFromDisk(mainViewController, file);
    }

    @Override
    public void createNewFile(MainViewController mainViewController, ActionEvent event) {
        startSaveModal(e -> e.createNew(mainViewController, event), mainViewController, event);
    }


    @Override
    public void delete(MainViewController mainViewController, ActionEvent event) {
        try {
            if (currentFile == null) return;
            Files.delete(currentFile.toPath());
        } catch (IOException e) {
            mainViewController.showError(e);
            e.printStackTrace();
        }
        if (!mainViewController.getTextFromField("search").equals("")) {
            searchHibFilesFromPath(mainViewController, event);
        }
        clear(mainViewController, event);
    }

    @Override
    public void deserializeFromDisk(MainViewController mainViewController, File file) {
        currentFile = file;
        if (file == null) return;
        mainViewController.clearAllRows();
        mainViewController.setTextInField("currentDir", currentFile.getAbsolutePath());
        try {
            currentBook = bookService.getBook(file);
            try {
                originalBook = currentBook.clone();
            } catch (CloneNotSupportedException e) {
                e.printStackTrace();
            }
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
            File file = viewController.getImageFromFileChooser(ae);
            if (file == null) return;
            currentBook.setAvatar(fileSystemService.getFileBytes(file));
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
        WebCamPreviewViewController controller = HibSerializerApplication.startWebCamModal(currentStyle);
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

    private String getOriginalLanguageFileName() {
        LocaleString name = currentBook.getName();
        LocaleString author = currentBook.getAuthor();
        String result;
        if (currentBook.getOriginalLanguage() == null) return null;
        switch (currentBook.getOriginalLanguage()) {
            case RU:
                result = currentBook.getOriginalLanguage().name() + "_"
                        + currentBook.getAuthor().getRu() + "_" + currentBook.getName().getRu();
                break;
            case EN:
                result = currentBook.getOriginalLanguage().name()
                        + "_" + currentBook.getAuthor().getEn() + "_" + currentBook.getName().getEn();
                break;
            case FR:
                result = currentBook.getOriginalLanguage().name()
                        + "_" + currentBook.getAuthor().getFr() + "_" + currentBook.getName().getFr();
                break;
            case IT:
                result = currentBook.getOriginalLanguage().name()
                        + "_" + currentBook.getAuthor().getIt() + "_" + currentBook.getName().getIt();
                break;
            case DE:
                result = currentBook.getOriginalLanguage().name()
                        + "_" + currentBook.getAuthor().getDe() + "_" + currentBook.getName().getDe();
                break;
            case CS:
                result = currentBook.getOriginalLanguage().name()
                        + "_" + currentBook.getAuthor().getCs() + "_" + currentBook.getName().getCs();
                break;
            case GR:
                result = currentBook.getOriginalLanguage().name()
                        + "_" + currentBook.getAuthor().getGr() + "_" + currentBook.getName().getGr();
                break;
            default:
                throw new IllegalArgumentException(currentBook.getOriginalLanguage().name() + " Not Found");
        }
        return result;
    }

    private void startSaveModal(Action actionDontSave,
                                MainViewController mainViewController,
                                ActionEvent actionEvent) {
        collectCurrentBook(mainViewController, actionEvent);
        if (!currentBook.equals(originalBook) && currentFile != null) {
            SaveModalViewController saveModalViewController = HibSerializerApplication
                    .startSaveModal(currentStyle);
            if (saveModalViewController.getSave() != null) {
                if (saveModalViewController.getSave()) {
                    this.serialize(mainViewController, actionEvent);
                } else {
                    actionDontSave.doAction(this);
                }
            }
        } else {
            actionDontSave.doAction(this);
        }
    }

    public Integer getCurrentWebCam() {
        return currentWebCam;
    }

    public void setCurrentWebCam(Integer currentWebCam) {
        this.currentWebCam = currentWebCam;
    }
}
