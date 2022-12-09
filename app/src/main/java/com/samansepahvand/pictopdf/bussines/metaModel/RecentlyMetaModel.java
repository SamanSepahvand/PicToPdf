package com.samansepahvand.pictopdf.bussines.metaModel;

import java.io.Serializable;

public class RecentlyMetaModel     implements Serializable {


   private String fileTitle ;
    private String filePath ;
    private String fileSize ;
    private String fileDate ;

    private boolean isSuccess;


    public RecentlyMetaModel(String fileTitle, String filePath, String fileSize, String fileDate,boolean isSuccess) {
        this.fileTitle = fileTitle;
        this.filePath = filePath;
        this.fileSize = fileSize;
        this.fileDate = fileDate;
        this.isSuccess=isSuccess;
    }


    public String getFileTitle() {
        return fileTitle;
    }

    public String getFilePath() {
        return filePath;
    }

    public String getFileSize() {
        return fileSize;
    }

    public String getFileDate() {
        return fileDate;
    }

    public boolean isSuccess() {
        return isSuccess;
    }
}
