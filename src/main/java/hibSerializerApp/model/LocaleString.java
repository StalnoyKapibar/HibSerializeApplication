package hibSerializerApp.model;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.io.Serializable;
import java.util.Map;

@Data
@AllArgsConstructor
public class LocaleString implements Serializable {
    private String ru;
    private String en;
    private String fr;
    private String it;
    private String de;
    private String cs;
    private String gr;

    public LocaleString() {
    }

    public LocaleString(Map<String, String> languages) {
        this.ru = languages.get("ru");
        this.en = languages.get("en");
        this.fr = languages.get("fr");
        this.it = languages.get("it");
        this.de = languages.get("de");
        this.cs = languages.get("cs");
        this.gr = languages.get("gr");
    }
}
