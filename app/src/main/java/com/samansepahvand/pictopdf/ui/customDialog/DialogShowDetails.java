package com.samansepahvand.pictopdf.ui.customDialog;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.drawable.ColorDrawable;
import android.graphics.pdf.PdfDocument;
import android.net.Uri;
import android.os.Environment;
import android.os.Handler;
import android.provider.MediaStore;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Display;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.samansepahvand.pictopdf.MainApplication;
import com.samansepahvand.pictopdf.R;
import com.samansepahvand.pictopdf.bussines.domain.Constants;
import com.samansepahvand.pictopdf.bussines.metaModel.RecentlyMetaModel;
import com.samansepahvand.pictopdf.ui.activity.MainActivity;
import com.samansepahvand.pictopdf.ui.adapter.ShowPictureAdapter;
import com.samansepahvand.pictopdf.utility.FileUtility;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.DateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;


public class DialogShowDetails extends Dialog implements View.OnClickListener, RadioGroup.OnCheckedChangeListener {

    private Context mContext;
    private Button btnConfirm;
    private ImageView imgCloseDialog;
    private OnAcceptInterface acceptListener;
    private OnCancelInterface cancelListener;
    private RecyclerView showPictureRecyclerView;


    private boolean booleanSave=false;
    private ArrayList<Uri> mArrayUris = new ArrayList();

    private EditText edtFileName;
    private RadioGroup radioGroup;

    private String qualityTypes="medium";


    public DialogShowDetails(@NonNull Context context,ArrayList<Uri> mArrayUri ) {
        super(context);
        this.mContext = context;
        setContentView(R.layout.dialog_show_details);
        this.getWindow().getAttributes().windowAnimations = R.style.AlertDialogAnimation;
        getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        this.mArrayUris=mArrayUri;
        initView();


    }


    private void initView() {

        radioGroup=findViewById(R.id.radioGroup);
        edtFileName=findViewById(R.id.editTextTextPersonName);
        btnConfirm = findViewById(R.id.btn_dialog_confirm);
        imgCloseDialog = findViewById(R.id.img_dialog_close);

        showPictureRecyclerView=findViewById(R.id.show_pictuers_recyclerView);
        initRecyclerView(mArrayUris);

        btnConfirm.setVisibility(View.GONE);
        showPictureRecyclerView.setVisibility(View.GONE);
        radioGroup.setVisibility(View.GONE);
        edtFileName.setVisibility(View.GONE);


        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                btnConfirm.startAnimation(MainApplication.SetAnimation("FadeIn"));
                showPictureRecyclerView.startAnimation(MainApplication.SetAnimation("FadeIn"));
                btnConfirm.startAnimation(MainApplication.SetAnimation("FadeIn"));

                radioGroup.startAnimation(MainApplication.SetAnimation("FadeIn"));
                edtFileName.startAnimation(MainApplication.SetAnimation("FadeIn"));

                showPictureRecyclerView.setVisibility(View.VISIBLE);
                btnConfirm.setVisibility(View.VISIBLE);
                radioGroup.setVisibility(View.VISIBLE);
                edtFileName.setVisibility(View.VISIBLE);
            }
        }, Constants.DelayMoreFast);

        imgCloseDialog.startAnimation(MainApplication.SetAnimation("Rotate"));

        btnConfirm.setOnClickListener(this);
        imgCloseDialog.setOnClickListener(this);
        radioGroup.setOnCheckedChangeListener(this);

    }


    public Dialog setAcceptButton(OnAcceptInterface acceptListener) {
        this.acceptListener = acceptListener;
        //btnCancel.setVisibility(View.VISIBLE);
        return this;
    }

    private  void initRecyclerView(ArrayList<Uri> mArrayUri){

        showPictureRecyclerView.setHasFixedSize(true);
        showPictureRecyclerView.setLayoutManager(new GridLayoutManager(mContext,3));
        showPictureRecyclerView.setAdapter(new ShowPictureAdapter(mContext,bitmapList(mArrayUri)));
    }

    private List<Bitmap> bitmapList(ArrayList<Uri> mArrayUri) {
        try {
            List<Bitmap> result=new ArrayList<>();

            for (Uri imageUri : mArrayUri) {
                Bitmap bitmap = MediaStore.Images.Media.getBitmap(mContext.getContentResolver(), imageUri);
                result.add(bitmap);
            }
            return  result;
        }catch ( Exception e){
            return  null;

        }
    }


    private RecentlyMetaModel createPdf(String filePdfName, ArrayList<Uri> mArrayUri, String qualityType) {

        WindowManager wm = (WindowManager) mContext.getSystemService(Context.WINDOW_SERVICE);
        Display display = wm.getDefaultDisplay();
        DisplayMetrics displaymetrics = new DisplayMetrics();
       // (Activity)this.getWindowManager().getDefaultDisplay().getMetrics(displaymetrics);
        float hight = displaymetrics.heightPixels;
        float width = displaymetrics.widthPixels;

        int convertHighet = (int) hight, convertWidth = (int) width;

//        Resources mResources = getResources();
//        Bitmap bitmap = BitmapFactory.decodeResource(mResources, R.drawable.screenshot);
        PdfDocument document = new PdfDocument();
        try {

            for (Uri imageUri : mArrayUri) {

                Bitmap bitmap = MediaStore.Images.Media.getBitmap(mContext.getContentResolver(), imageUri);

                PdfDocument.PageInfo pageInfo = new PdfDocument.PageInfo.Builder(bitmap.getWidth(), bitmap.getHeight(), 1).create();
                PdfDocument.Page page = document.startPage(pageInfo);

                Canvas canvas = page.getCanvas();
                Paint paint = new Paint();
                paint.setColor(Color.parseColor("#ffffff"));
                canvas.drawPaint(paint);
                bitmap = Bitmap.createScaledBitmap(bitmap, bitmap.getWidth(), bitmap.getHeight(), true);
                paint.setColor(Color.BLUE);
                canvas.drawBitmap(bitmap, 0, 0, null);
                document.finishPage(page);

            }

            File file = FileUtility.genEditFile(filePdfName);

            document.writeTo(new FileOutputStream(file));
            booleanSave = true;
            document.close();

            Date date=new Date();

            return new RecentlyMetaModel(filePdfName,file.getAbsolutePath(),"0", DateFormat.getDateInstance(DateFormat.LONG).format(date)+"",true);

        } catch (IOException e) {

            e.printStackTrace();
            Toast.makeText(mContext, "Something wrong: " + e.toString(), Toast.LENGTH_LONG).show();

        }
        document.close();
        return new RecentlyMetaModel(filePdfName,"file.getAbsolutePath()","0", "",false);

    }



    @Override
    public void onClick(View view) {

        switch (view.getId()) {
            case R.id.img_dialog_close:
                animOpenCloseDialog();
                break;

            case R.id.btn_dialog_confirm:

             String filePdfName=   edtFileName.getText().toString();
             String qualityType=qualityTypes;
             if (filePdfName.equals(""))filePdfName=System.currentTimeMillis()+"";
             if (mArrayUris==null|| mArrayUris.size()==0){
                 Toast.makeText(mContext, "No File Selected !", Toast.LENGTH_SHORT).show();
             }else {
                RecentlyMetaModel result= createPdf(filePdfName, mArrayUris,qualityType);

                OpenDialogProgress(result);

             }

             dismiss();
                break;
        }
        // dismiss();
    }




    private void OpenDialogProgress(RecentlyMetaModel result){
        DialogProgress dialogProgress=new DialogProgress(mContext,result);
        dialogProgress.setCancelable(false);
        dialogProgress.show();


    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }


    private void animOpenCloseDialog() {
        imgCloseDialog.startAnimation(MainApplication.SetAnimation("Rotate"));
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                dismiss();
            }
        }, Constants.DelayTimeDialogAnimation);
    }

    @Override
    public void onCheckedChanged(RadioGroup radioGroup, int checkedId) {
        // on below line we are getting radio button from our group.
        RadioButton radioButton = findViewById(checkedId);
        qualityTypes=radioButton.getText().toString();

        // on below line we are displaying a toast message.
      //  Toast.makeText(mContext, "Selected Radio Button is : " + radioButton.getText(), Toast.LENGTH_SHORT).show();

    }


    public interface OnAcceptInterface {
        void accept();
    }

    public interface OnCancelInterface {
        void cancel();
    }
}
