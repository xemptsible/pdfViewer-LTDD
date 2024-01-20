package com.mt.pdfviewer;

import java.util.Date;


// Cái này chắc không cần thiết vì mình có một class để xử lý file rồi
public class Pdf {
    String fileName, filePath;
    long fileSize;
    Date fileDateAdded;

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public String getFilePath() {
        return filePath;
    }

    public void setFilePath(String filePath) {
        this.filePath = filePath;
    }

    public Date getFileDateAdded() {
        return fileDateAdded;
    }

    public void setFileDateAdded(Date fileDateAdded) {
        this.fileDateAdded = fileDateAdded;
    }

    public long getFileSize() {
        return fileSize;
    }

    public void setFileSize(long fileSize) {
        this.fileSize = fileSize;
    }
}
