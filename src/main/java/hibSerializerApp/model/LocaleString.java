package hibSerializerApp.model;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.io.Serializable;
import java.util.Map;
import java.util.Objects;

@Data
@AllArgsConstructor
public class LocaleString implements Serializable, Cloneable {
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

    @Override
    protected LocaleString clone() throws CloneNotSupportedException {
        return (LocaleString) super.clone();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        LocaleString that = (LocaleString) o;
        return Objects.equals(ru, that.ru) &&
                Objects.equals(en, that.en) &&
                Objects.equals(fr, that.fr) &&
                Objects.equals(it, that.it) &&
                Objects.equals(de, that.de) &&
                Objects.equals(cs, that.cs) &&
                Objects.equals(gr, that.gr);
    }

    @Override
    public int hashCode() {
        return Objects.hash(ru, en, fr, it, de, cs, gr);
    }
}
