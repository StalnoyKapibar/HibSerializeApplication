package hibSerializerApp.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.util.Arrays;
import java.util.Objects;

@JsonIgnoreProperties(ignoreUnknown = true)
public class BookDTO {
    private LocaleOnlyEnglishDTO name;
    private LocaleOnlyEnglishDTO author;
    private String location;
    private byte[] avatar;

    public BookDTO() {
    }

    public LocaleOnlyEnglishDTO getName() {
        return name;
    }

    public void setName(LocaleOnlyEnglishDTO name) {
        this.name = name;
    }

    public LocaleOnlyEnglishDTO getAuthor() {
        return author;
    }

    public void setAuthor(LocaleOnlyEnglishDTO author) {
        this.author = author;
    }

    public byte[] getAvatar() {
        return avatar;
    }

    public void setAvatar(byte[] avatar) {
        this.avatar = avatar;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        BookDTO bookDTO = (BookDTO) o;
        return Objects.equals(name, bookDTO.name) &&
                Objects.equals(author, bookDTO.author) &&
                Arrays.equals(avatar, bookDTO.avatar) &&
                Objects.equals(location, bookDTO.location);
    }

    @Override
    public int hashCode() {
        int result = Objects.hash(name, author, location);
        result = 31 * result + Arrays.hashCode(avatar);
        return result;
    }

    @Override
    public String toString() {
        return "BookDTO{" +
                "name=" + name +
                ", author=" + author +
                ", avatar=" + Arrays.toString(avatar) +
                ", location='" + location + '\'' +
                '}';
    }
}
