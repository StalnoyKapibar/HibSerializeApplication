package hib.model;

import java.io.Serializable;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

public class Book implements Serializable {
    private LocaleString name;
    private LocaleString author;
    private LocaleString desc;
    private byte[] avatar;
    private List<byte[]> additionalPhotos;

    public Book(LocaleString name, LocaleString author, LocaleString desc) {
        this.name = name;
        this.author = author;
        this.desc = desc;
    }

    public Book() {
    }

    public LocaleString getName() {
        return name;
    }

    public void setName(LocaleString name) {
        this.name = name;
    }

    public LocaleString getAuthor() {
        return author;
    }

    public void setAuthor(LocaleString author) {
        this.author = author;
    }

    public LocaleString getDesc() {
        return desc;
    }

    public void setDesc(LocaleString desc) {
        this.desc = desc;
    }

    public byte[] getAvatar() {
        return avatar;
    }

    public void setAvatar(byte[] avatar) {
        this.avatar = avatar;
    }

    public List<byte[]> getAdditionalPhotos() {
        return additionalPhotos;
    }

    public void setAdditionalPhotos(List<byte[]> additionalPhotos) {
        this.additionalPhotos = additionalPhotos;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Book book = (Book) o;
        return Objects.equals(name, book.name) &&
                Objects.equals(author, book.author) &&
                Objects.equals(desc, book.desc) &&
                Arrays.equals(avatar, book.avatar) &&
                Objects.equals(additionalPhotos, book.additionalPhotos);
    }

    @Override
    public int hashCode() {
        int result = Objects.hash(name, author, desc, additionalPhotos);
        result = 31 * result + Arrays.hashCode(avatar);
        return result;
    }

    @Override
    public String toString() {
        return "Book{" +
                "name=" + name +
                ", author=" + author +
                ", desc=" + desc +
                ", avatar=" + Arrays.toString(avatar) +
                ", additionalPhotos=" + additionalPhotos +
                '}';
    }
}
