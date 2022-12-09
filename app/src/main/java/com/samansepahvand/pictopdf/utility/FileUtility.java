package com.samansepahvand.pictopdf.utility;

import android.os.Build;
import android.os.Environment;
import android.os.Handler;
import android.view.View;

import com.samansepahvand.pictopdf.MainApplication;
import com.samansepahvand.pictopdf.bussines.domain.Constants;

import java.io.File;

public class FileUtility {


    public static final String FOLDER_NAME="pdfConverter";

    public static File createFolder(){

        File baseDir;
        if (Build.VERSION.SDK_INT<8){
            baseDir= Environment.getExternalStorageDirectory();

        }else{
            baseDir=Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOCUMENTS);

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


    public static File genEditFile(String fileName){
        return FileUtility.getEmptyFile(fileName+".pdf");

    }

    private static File getEmptyFile(String name) {

        File folder=FileUtility.createFolder();
        if (folder!=null){
            if (folder.exists()){
                File file=new File(folder,name);
                return file;
            }
        }
        return null;
    }




    public static void  animOpenCloseDialog(View view) {
        view.startAnimation(MainApplication.SetAnimation("Rotate"));
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {

            }
        }, Constants.DelayTimeDialogAnimation);
    }


}
