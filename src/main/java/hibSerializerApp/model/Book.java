package hibSerializerApp.model;

import lombok.Data;

import java.io.Serializable;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

@Data
public class Book implements Serializable, Cloneable {
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
    private String category;
    private OtherLanguage otherLanguageOfBook = new OtherLanguage();

    public Book(LocaleString name, LocaleString author, LocaleString desc, LocaleString edition) {
        this.name = name;
        this.author = author;
        this.desc = desc;
        this.edition = edition;
    }

    public Book() {
    }

    @Override
    public Book clone() throws CloneNotSupportedException {
        Book newBook = (Book) super.clone();
        newBook.name = name.clone();
        newBook.author = author.clone();
        newBook.desc = desc.clone();
        newBook.edition = edition.clone();
        newBook.otherLanguageOfBook = otherLanguageOfBook.clone();
        return newBook;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Book book = (Book) o;
        return Objects.equals(name, book.name) &&
                Objects.equals(author, book.author) &&
                Objects.equals(desc, book.desc) &&
                Objects.equals(edition, book.edition) &&
                Objects.equals(yearOfEdition, book.yearOfEdition) &&
                Objects.equals(pages, book.pages) &&
                Objects.equals(price, book.price) &&
                Arrays.equals(avatar, book.avatar) &&
                Objects.equals(additionalPhotos, book.additionalPhotos) &&
                originalLanguage == book.originalLanguage;
    }

    @Override
    public int hashCode() {
        int result = Objects.hash(name, author, desc, edition, yearOfEdition, pages, price, additionalPhotos, originalLanguage);
        result = 31 * result + Arrays.hashCode(avatar);
        return result;
    }
}
