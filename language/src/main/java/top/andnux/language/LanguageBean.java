package top.andnux.language;

import androidx.annotation.Keep;

import java.io.Serializable;
import java.util.Locale;

@Keep
public class LanguageBean implements Serializable {

    private String code;
    private String value;
    private Locale locale;
    private boolean select;

    public LanguageBean() {

    }

    public LanguageBean(String code, String value, Locale locale) {
        this.code = code;
        this.value = value;
        this.locale = locale;
    }

    public boolean isSelect() {
        return select;
    }

    public void setSelect(boolean select) {
        this.select = select;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public Locale getLocale() {
        return locale;
    }

    public void setLocale(Locale locale) {
        this.locale = locale;
    }

    @Override
    public String toString() {
        return "LanguageBean{" +
                "code='" + code + '\'' +
                ", value='" + value + '\'' +
                ", locale=" + locale +
                ", select=" + select +
                '}';
    }
}
