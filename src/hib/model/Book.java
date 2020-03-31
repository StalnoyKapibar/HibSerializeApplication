package hib.model;

import java.io.Serializable;
import java.util.List;

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
}
