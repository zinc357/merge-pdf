package org.example;

public class PDFFile {
    /**
     * 文件流
     */
    private String base64;

    /**
     * 书签名
     */
    private String bookmark;


    public String getBase64() {
        return base64;
    }

    public void setBase64(String base64) {
        this.base64 = base64;
    }

    public String getBookmark() {
        return bookmark;
    }

    public void setBookmark(String bookmark) {
        this.bookmark = bookmark;
    }
}
