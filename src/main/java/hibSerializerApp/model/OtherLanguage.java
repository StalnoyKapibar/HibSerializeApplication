package hibSerializerApp.model;

import lombok.Data;

import java.io.Serializable;

@Data
public class OtherLanguage implements Serializable, Cloneable {
    private String translitNameBook;

    private String translitAuthor;

    private String otherLangNameBook;

    private String otherLangAuthor;

    @Override
    protected OtherLanguage clone() throws CloneNotSupportedException {
        return (OtherLanguage) super.clone();
    }
}
