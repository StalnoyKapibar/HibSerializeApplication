package hib.controller;

import hib.model.Book;
import hib.model.LocaleString;
import hib.service.BookService;
import javafx.embed.swing.SwingFXUtils;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.control.TextInputControl;
import javafx.scene.image.ImageView;
import javafx.stage.FileChooser;

import javax.imageio.ImageIO;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MainController {
    private final FileChooser fileChooser;
    private final FileChooser hibFileChooser;
    private final BookService bookService;

    private File avatar;
    private List<File> additional;
    private Map<String, String> nameLocale;
    private Map<String, String> authorLocale;
    private Map<String, String> descLocale;
    private File hib;

    @FXML
    private ImageView avatarImage;

    @FXML
    private TextField nameRu;
    @FXML
    private TextField authorRu;
    @FXML
    private TextArea descRu;

    @FXML
    private TextField nameEn;
    @FXML
    private TextField authorEn;
    @FXML
    private TextArea descEn;

    @FXML
    private TextField nameFr;
    @FXML
    private TextField authorFr;
    @FXML
    private TextArea descFr;

    @FXML
    private TextField nameIt;
    @FXML
    private TextField authorIt;
    @FXML
    private TextArea descIt;

    @FXML
    private TextField nameDe;
    @FXML
    private TextField authorDe;
    @FXML
    private TextArea descDe;

    @FXML
    private TextField nameCs;
    @FXML
    private TextField authorCs;
    @FXML
    private TextArea descCs;

    @FXML
    private TextField nameGr;
    @FXML
    private TextField authorGr;
    @FXML
    private TextArea descGr;


    public MainController() {
        fileChooser = new FileChooser();
        fileChooser.getExtensionFilters().add(
                new FileChooser.ExtensionFilter("JPEG Files", "*.jpg")
        );

        hibFileChooser = new FileChooser();
        hibFileChooser.getExtensionFilters().add(
                new FileChooser.ExtensionFilter(".hib Files", "*.hib")
        );

        nameLocale = new HashMap<>();
        authorLocale = new HashMap<>();
        descLocale = new HashMap<>();
        bookService = new BookService();
    }

    @FXML
    private void chooseAvatar(ActionEvent ae) {
        Node source = (Node) ae.getSource();
        avatar = fileChooser.showOpenDialog(source.getScene().getWindow());
        try {
            avatarImage.setImage(SwingFXUtils.toFXImage(ImageIO.read(avatar), null));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void chooseAdditional(ActionEvent ae) {
        Node source = (Node) ae.getSource();
        additional = fileChooser.showOpenMultipleDialog(source.getScene().getWindow());
    }

    @FXML
    private void deserialize(ActionEvent ae) {
        Node source = (Node) ae.getSource();
        hib = hibFileChooser.showOpenDialog(source.getScene().getWindow());
        Book book = null;
        try {
            book = bookService.getBook(hib);
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }
        if (book == null) return;

        setLocaleText(book.getName(), nameRu, nameEn, nameFr, nameIt, nameDe, nameCs, nameGr);
        setLocaleText(book.getAuthor(), authorRu, authorEn, authorFr, authorIt, authorDe, authorCs, authorGr);
        setLocaleText(book.getDesc(), descRu, descEn, descFr, descIt, descDe, descCs, descGr);

        if (book.getAvatar() != null) {
            try {
                avatarImage.setImage(SwingFXUtils.toFXImage(ImageIO.read(new ByteArrayInputStream(book.getAvatar())), null));
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    @FXML
    private void serialize(ActionEvent ae) {
        Node source = (Node) ae.getSource();
        File target = hibFileChooser.showSaveDialog(source.getScene().getWindow());

        if (target == null) return;

        getLocaleText(nameLocale, nameRu, nameEn, nameFr, nameIt, nameDe, nameCs, nameGr);
        getLocaleText(authorLocale, authorRu, authorEn, authorFr, authorIt, authorDe, authorCs, authorGr);
        getLocaleText(descLocale, descRu, descEn, descFr, descIt, descDe, descCs, descGr);


        Book book = new Book(new LocaleString(nameLocale),
                new LocaleString(authorLocale), new LocaleString(descLocale));


        try {
            List<byte[]> additionalBytes = new ArrayList<>();
            if (additional != null) {
                for (File file : additional) {
                    additionalBytes.add(convertImage(file));
                }
            }
            book.setAdditionalPhotos(additionalBytes);
            if (avatar != null) book.setAvatar(convertImage(avatar));
        } catch (IOException e) {
            e.printStackTrace();
        }

        try {
            bookService.saveBook(book, target);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private byte[] convertImage(File file) throws IOException {
        return Files.readAllBytes(file.toPath());
    }

    private void setLocaleText(LocaleString localeString, TextInputControl ru,
                               TextInputControl en, TextInputControl fr,
                               TextInputControl it, TextInputControl de,
                               TextInputControl cs, TextInputControl gr) {
        ru.setText(localeString.getRu());
        en.setText(localeString.getEn());
        fr.setText(localeString.getFr());
        it.setText(localeString.getIt());
        de.setText(localeString.getDe());
        cs.setText(localeString.getCs());
        gr.setText(localeString.getGr());
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
}
