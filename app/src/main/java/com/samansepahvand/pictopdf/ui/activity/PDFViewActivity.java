package com.samansepahvand.pictopdf.ui.activity;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.github.barteksc.pdfviewer.PDFView;
import com.github.barteksc.pdfviewer.listener.OnLoadCompleteListener;
import com.github.barteksc.pdfviewer.listener.OnPageChangeListener;
import com.github.barteksc.pdfviewer.scroll.DefaultScrollHandle;
import com.samansepahvand.pictopdf.R;
import com.samansepahvand.pictopdf.bussines.metaModel.RecentlyMetaModel;
import com.samansepahvand.pictopdf.utility.FileUtility;
import com.shockwave.pdfium.PdfDocument;

import java.io.File;
import java.util.List;

public class PDFViewActivity extends AppCompatActivity implements OnPageChangeListener, OnLoadCompleteListener, View.OnClickListener {
   private PDFView pdfView;
    private Integer pageNumber = 0;
    private  String pdfFileName;
    private  String TAG = "PDFViewActivity";
    private  int position = -1;
    private ImageView imgBack;

    private TextView txtFileName,txtFileSize,txtFilePath;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_preview);
        init();
    }

    private void init() {
        pdfView = (PDFView) findViewById(R.id.pdfview);
        imgBack=findViewById(R.id.img_back);

        txtFileName=findViewById(R.id.txt_file_name);
        txtFileSize=findViewById(R.id.txt_file_size);
        txtFilePath=findViewById(R.id.txt_file_path);


        RecentlyMetaModel model=  (RecentlyMetaModel) getIntent().getSerializableExtra("RecentlyMetaModel");
        position = getIntent().getIntExtra("position", -1);
        ShowPdfInfo(model);
        imgBack.setOnClickListener(this);
    }

    private void ShowPdfInfo(RecentlyMetaModel model) {

        txtFileName.setText(model.getFileTitle());
        txtFileSize.setText(model.getFileSize());
        txtFilePath.setText(model.getFilePath());

        File file = FileUtility.genEditFile(model.getFileTitle());

        pdfView.fromFile(file)
                .defaultPage(pageNumber)
                .enableSwipe(true)
                .swipeHorizontal(false)
                .onPageChange(this)
                .enableAnnotationRendering(true)
                .onLoad(this)
                .scrollHandle(new DefaultScrollHandle(this))
                .load();
    }

    @Override
    public void onPageChanged(int page, int pageCount) {
        pageNumber = page;
        setTitle(String.format("%s %s / %s", pdfFileName, page + 1, pageCount));
    }

    @Override
    public void loadComplete(int nbPages) {
        PdfDocument.Meta meta = pdfView.getDocumentMeta();
        printBookmarksTree(pdfView.getTableOfContents(), "-");

    }

    public void printBookmarksTree(List<PdfDocument.Bookmark> tree, String sep) {
        for ( PdfDocument.Bookmark b : tree) {

            Log.e(TAG, String.format("%s %s, p %d", sep, b.getTitle(), b.getPageIdx()));

            if (b.hasChildren()) {
                printBookmarksTree(b.getChildren(), sep + "-");
            }
        }
    }

    @Override
    public void onClick(View view) {

        switch (view.getId()){

            case R.id.img_back:
                onBackPressed();
                break;
        }
    }
}
