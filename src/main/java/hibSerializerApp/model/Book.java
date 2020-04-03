package hibSerializerApp.model;

import lombok.Data;

import java.io.Serializable;
import java.util.List;

@Data
public class Book implements Serializable {
    private LocaleString name;
    private LocaleString author;
    private LocaleString desc;
    private LocaleString edition;
    private String yearOfEdition;
    private Long pages;
    private Long price;
    private byte[] avatar;
    private List<byte[]> additionalPhotos;
    private Language originalLanguage;

    public Book(LocaleString name, LocaleString author, LocaleString desc, LocaleString edition) {
        this.name = name;
        this.author = author;
        this.desc = desc;
        this.edition = edition;
    }

    public Book() {
    }
}
