package hibSerializerApp.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

import java.io.File;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class BookDTO {
    private LocaleString name;
    private LocaleString author;
    private File location;
    private byte[] avatar;
    private Language originalLanguage;

    public BookDTO() {
    }
}
