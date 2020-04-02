package hibSerializerApp.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.util.Objects;

@JsonIgnoreProperties(ignoreUnknown = true)
public class LocaleOnlyEnglishDTO {
    private String en;

    public LocaleOnlyEnglishDTO() {
    }

    public String getEn() {
        return en;
    }

    public void setEn(String en) {
        this.en = en;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        LocaleOnlyEnglishDTO that = (LocaleOnlyEnglishDTO) o;
        return Objects.equals(en, that.en);
    }

    @Override
    public int hashCode() {
        return Objects.hash(en);
    }

    @Override
    public String toString() {
        return "LocaleOnlyEnglishDTO{" +
                "en='" + en + '\'' +
                '}';
    }
}
