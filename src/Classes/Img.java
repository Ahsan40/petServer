package Classes;

import java.io.Serializable;

public class Img implements Serializable {
    private String path;
    private String extension;
    private String base64;

    public Img(String path, String extension, String base64) {
        this.path = path;
        this.extension = extension;
        this.base64 = base64;
    }

    public Img(String extension, String base64) {
        this.extension = extension;
        this.base64 = base64;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public String getExtension() {
        return extension;
    }

    public void setExtension(String extension) {
        this.extension = extension;
    }

    public String getBase64() {
        return base64;
    }

    public void setBase64(String base64) {
        this.base64 = base64;
    }
}
