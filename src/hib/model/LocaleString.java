package hib.model;

import java.io.Serializable;
import java.util.Map;

public class LocaleString implements Serializable {
    private String ru;
    private String en;
    private String fr;
    private String it;
    private String de;
    private String cs;
    private String gr;

    public LocaleString(String ru, String en, String fr, String it, String de, String cs, String gr) {
        this.ru = ru;
        this.en = en;
        this.fr = fr;
        this.it = it;
        this.de = de;
        this.cs = cs;
        this.gr = gr;
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

    public String getRu() {
        return ru;
    }

    public void setRu(String ru) {
        this.ru = ru;
    }

    public String getEn() {
        return en;
    }

    public void setEn(String en) {
        this.en = en;
    }

    public String getFr() {
        return fr;
    }

    public void setFr(String fr) {
        this.fr = fr;
    }

    public String getIt() {
        return it;
    }

    public void setIt(String it) {
        this.it = it;
    }

    public String getDe() {
        return de;
    }

    public void setDe(String de) {
        this.de = de;
    }

    public String getCs() {
        return cs;
    }

    public void setCs(String cs) {
        this.cs = cs;
    }

    public String getGr() {
        return gr;
    }

    public void setGr(String gr) {
        this.gr = gr;
    }
}
