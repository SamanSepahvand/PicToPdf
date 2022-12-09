package com.samansepahvand.pictopdf.ui.activity;

import static android.Manifest.permission.MANAGE_EXTERNAL_STORAGE;
import static android.Manifest.permission.READ_EXTERNAL_STORAGE;
import static android.Manifest.permission.WRITE_EXTERNAL_STORAGE;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.AlertDialog;
import android.content.ClipData;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.webkit.MimeTypeMap;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.samansepahvand.pictopdf.MainApplication;
import com.samansepahvand.pictopdf.R;
import com.samansepahvand.pictopdf.bussines.metaModel.RecentlyMetaModel;
import com.samansepahvand.pictopdf.ui.adapter.RecentlyAdapter;
import com.samansepahvand.pictopdf.ui.customDialog.DialogShowDetails;
import com.samansepahvand.pictopdf.utility.FileUtility;

import java.io.File;
import java.text.DateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Random;

public class MainActivity extends AppCompatActivity implements View.OnClickListener, RecentlyAdapter.IGetRecentlyMetaModel {

    private static final String TAG = "MainActivity";


    public static final int GALLERY_PICTURE = 1;
    private static final int PERMISSION_REQUEST_CODE = 303;
    private static final int PICK_IMAGE_MULTIPLE = 1;


    private Bitmap bitmap;

    private String imageEncoded;
    private List<String> imagesEncodedList;
    private ArrayList<Uri> mArrayUri = new ArrayList();


    private RecyclerView recentlyRecyclerView;
    private ImageView imgAdd;
    private TextView txtTimeHeader;
    private ConstraintLayout clNoFileList;



    RecentlyAdapter.IGetRecentlyMetaModel iGetRecentlyMetaModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initView();
        initHeaderData();
        if (!checkPermission()) {
            requestPermission();
        }

    }

    private void initView() {
        txtTimeHeader = findViewById(R.id.txt_time_header);
        clNoFileList = findViewById(R.id.no_file_constraint);
        recentlyRecyclerView = findViewById(R.id.recently_recycler_view);
        initRecyclerView();
        imgAdd = findViewById(R.id.img_add);
        imgAdd.setOnClickListener(this);
    }

    private void initRecyclerView() {
        iGetRecentlyMetaModel=(MainActivity)this;

        List<RecentlyMetaModel> result = getAllPdfInDirectory();

        if (result != null && result.size() != 0) {
            recentlyRecyclerView.setVisibility(View.VISIBLE);
            clNoFileList.setVisibility(View.GONE);
            recentlyRecyclerView.setHasFixedSize(true);
            recentlyRecyclerView.setLayoutManager(new LinearLayoutManager(this));
            recentlyRecyclerView.setAdapter(new RecentlyAdapter(MainActivity.this, result,iGetRecentlyMetaModel));
        } else {
            recentlyRecyclerView.setVisibility(View.GONE);
            clNoFileList.setVisibility(View.VISIBLE);

        }

    }


    /* this action find all file pdf in phone
    protected ArrayList<String> getPdfList() {
        ArrayList<String> pdfList = new ArrayList<>();
        Uri collection;

        final String[] projection = new String[]{
                MediaStore.Files.FileColumns.DISPLAY_NAME,
                MediaStore.Files.FileColumns.DATE_ADDED,
                MediaStore.Files.FileColumns.DATA,
                MediaStore.Files.FileColumns.MIME_TYPE,
        };

        final String sortOrder = MediaStore.Files.FileColumns.DATE_ADDED + " DESC";

        final String selection = MediaStore.Files.FileColumns.MIME_TYPE + " = ?";

        final String mimeType = MimeTypeMap.getSingleton().getMimeTypeFromExtension("pdf");
        final String[] selectionArgs = new String[]{mimeType};

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            collection = MediaStore.Files.getContentUri(MediaStore.VOLUME_EXTERNAL);
        }else{
            collection = MediaStore.Files.getContentUri("external");
        }


        try (Cursor cursor = getContentResolver().query(collection, projection, selection, selectionArgs, sortOrder)) {
            assert cursor != null;

            if (cursor.moveToFirst()) {
                int columnData = cursor.getColumnIndex(MediaStore.Files.FileColumns.DATA);
                int columnName = cursor.getColumnIndex(MediaStore.Files.FileColumns.DISPLAY_NAME);
                do {
                    pdfList.add((cursor.getString(columnData)));
                    Log.d(TAG, "getPdf: " + cursor.getString(columnData));
                    //you can get your pdf files
                } while (cursor.moveToNext());
            }
        }
        return pdfList;
    }
*/

    private List<RecentlyMetaModel> getAllPdfInDirectory() {
        File baseDir = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOCUMENTS);
        File folders = new File(baseDir, FileUtility.FOLDER_NAME);

        List<RecentlyMetaModel> recentlyMetaModels = new ArrayList<>();


        if (folders.exists()) {
            List<String> arrayListPath = new ArrayList<>();
            String[] filePathImageUrl = folders.list();
            for (String path : filePathImageUrl) {
                arrayListPath.add(folders.getAbsolutePath() + "/" + path);
                Date date = new Date();
                Random rand = new Random();

                recentlyMetaModels.add(
                        new RecentlyMetaModel(path.split("\\.")[0],
                                folders.getAbsolutePath(),
                                rand.nextInt(50) + "",
                                 DateFormat.getDateInstance(DateFormat.LONG).format(date)+"",true));

            }
            return recentlyMetaModels;

        }
        return null;


    }

    private void initHeaderData() {
        Date date = new Date();
        txtTimeHeader.setText("Last Update in " + DateFormat.getDateInstance(DateFormat.LONG).format(date));
    }

    private boolean checkPermission() {
        int result = ContextCompat.checkSelfPermission(getApplicationContext(), READ_EXTERNAL_STORAGE);
        int result2 = ContextCompat.checkSelfPermission(getApplicationContext(), WRITE_EXTERNAL_STORAGE);
        int result3 = ContextCompat.checkSelfPermission(getApplicationContext(), MANAGE_EXTERNAL_STORAGE);

        return (result == PackageManager.PERMISSION_GRANTED) && result2 == PackageManager.PERMISSION_GRANTED && result3 == PackageManager.PERMISSION_GRANTED;

    }

    private void requestPermission() {

        ActivityCompat.requestPermissions(this, new String[]{READ_EXTERNAL_STORAGE, WRITE_EXTERNAL_STORAGE, MANAGE_EXTERNAL_STORAGE}, PERMISSION_REQUEST_CODE);


    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case PERMISSION_REQUEST_CODE:
                if (grantResults.length > 0) {
                    boolean cameraAccepted = grantResults[0] == PackageManager.PERMISSION_GRANTED;
                    if (cameraAccepted) {
                    } else {

                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                            if (shouldShowRequestPermissionRationale(READ_EXTERNAL_STORAGE)

                            ) {
                                showMessageOKCancel("دسترسی تایید نشد! شما اجازه استفاده از سایر قسمت های سیستم را نداری  ",
                                        new DialogInterface.OnClickListener() {
                                            @Override
                                            public void onClick(DialogInterface dialog, int which) {
                                                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                                                    requestPermissions(new String[]{READ_EXTERNAL_STORAGE, WRITE_EXTERNAL_STORAGE, MANAGE_EXTERNAL_STORAGE},
                                                            PERMISSION_REQUEST_CODE);
                                                }
                                            }
                                        });
                                return;
                            }
                        }

                    }
                }


                break;
        }
    }

    private void showMessageOKCancel(String message, DialogInterface.OnClickListener okListener) {
        Typeface face = Typeface.createFromAsset(getAssets(), "fonts/iran_sans.ttf");
        TextView content = new TextView(this);
        content.setText("برای استفاده از سایر امکانات  برنامه به دسترسی ها نیاز داریم!");
        content.setTypeface(face);
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
        alertDialogBuilder.setTitle("درخواست دسترسی");
        alertDialogBuilder.setIcon(R.mipmap.ic_launcher);
        alertDialogBuilder.setView(content);
        alertDialogBuilder.setCancelable(false);
        alertDialogBuilder.setPositiveButton("تایید", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface arg0, int arg1) {
                requestPermission();
            }
        });
        alertDialogBuilder.setNeutralButton("انصراف", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

                dialog.dismiss();

            }
        });

        AlertDialog alertDialog = alertDialogBuilder.create();
        alertDialog.show();

    }


    private void OpenDialog() {
        DialogShowDetails dialogChooseDate = new DialogShowDetails(MainActivity.this, mArrayUri);
        dialogChooseDate.setCancelable(false);
        dialogChooseDate.setAcceptButton(new DialogShowDetails.OnAcceptInterface() {
            @Override
            public void accept() {
                // ClearData();
            }
        });
        dialogChooseDate.show();


    }

    private void ClearData() {

        if (mArrayUri.size() > 0) mArrayUri.clear();
    }

    @Override
    public void onClick(View view) {

        switch (view.getId()) {
            case R.id.img_add:

                Intent intent = new Intent();
                intent.setType("image/*");
                intent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true);
                intent.setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(Intent.createChooser(intent, "Select Picture"), PICK_IMAGE_MULTIPLE);

                //  OpenDialog();
                break;
        }

    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        try {
            // When an Image is picked
            if (requestCode == GALLERY_PICTURE && resultCode == RESULT_OK
                    && null != data) {
                // Get the Image from data

                String[] filePathColumn = {MediaStore.Images.Media.DATA};
                imagesEncodedList = new ArrayList<String>();
                if (data.getData() != null) {

                    Uri mImageUri = data.getData();

                    // Get the cursor
                    Cursor cursor = getContentResolver().query(mImageUri,
                            filePathColumn, null, null, null);
                    // Move to first row
                    cursor.moveToFirst();

                    int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
                    imageEncoded = cursor.getString(columnIndex);
                    cursor.close();

                    bitmap = BitmapFactory.decodeFile(imageEncoded);

                    //bitmap have image Url

                    mArrayUri.add(mImageUri);

                } else {
                    if (data.getClipData() != null) {
                        ClipData mClipData = data.getClipData();

                        for (int i = 0; i < mClipData.getItemCount(); i++) {

                            ClipData.Item item = mClipData.getItemAt(i);
                            Uri uri = item.getUri();
                            mArrayUri.add(uri);
                            // Get the cursor
                            Cursor cursor = getContentResolver().query(uri, filePathColumn, null, null, null);
                            // Move to first row
                            cursor.moveToFirst();

                            int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
                            imageEncoded = cursor.getString(columnIndex);
                            imagesEncodedList.add(imageEncoded);
                            cursor.close();

                        }
                    }
                }

                OpenDialog();
            } else {
                Toast.makeText(this, "You haven't picked Image",
                        Toast.LENGTH_LONG).show();
            }
        } catch (Exception e) {
            Toast.makeText(this, "Something went wrong", Toast.LENGTH_LONG)
                    .show();
        }


    }


    @Override
    public void GetRecentlyMetaModel(RecentlyMetaModel infoMetaModel) {

        if (infoMetaModel!=null){

            Intent intent1 = new Intent(MainActivity.this, PDFViewActivity.class);
            intent1.putExtra("RecentlyMetaModel",infoMetaModel);
            startActivity(intent1);

        }
    }
}