package com.samansepahvand.pictopdf.utility;

import android.os.Build;
import android.os.Environment;

import java.io.File;

public class FileUtility {


    public static final String FOLDER_NAME="pdfConverter";

    public static File createFolder(){

        File baseDir;
        if (Build.VERSION.SDK_INT<8){
            baseDir= Environment.getExternalStorageDirectory();

        }else{
            baseDir=Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES);

        }
        if (baseDir==null){
            return Environment.getExternalStorageDirectory();
        }
        File aviaryFolder=new File(baseDir,FOLDER_NAME);
        if (aviaryFolder.exists()){
            return aviaryFolder;
        }
        if (aviaryFolder.isFile()){
            aviaryFolder.delete();
        }
        if (aviaryFolder.mkdirs()){
            return aviaryFolder;
        }
        return Environment.getExternalStorageDirectory();

    }









}
