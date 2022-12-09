package com.samansepahvand.pictopdf.ui.customDialog;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.CountDownTimer;
import android.os.Handler;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.samansepahvand.pictopdf.MainApplication;
import com.samansepahvand.pictopdf.R;
import com.samansepahvand.pictopdf.bussines.domain.Constants;
import com.samansepahvand.pictopdf.bussines.metaModel.RecentlyMetaModel;
import com.samansepahvand.pictopdf.ui.activity.MainActivity;
import com.samansepahvand.pictopdf.ui.activity.PDFViewActivity;
import com.samansepahvand.pictopdf.ui.adapter.ShowPictureAdapter;

import java.text.DecimalFormat;
import java.text.NumberFormat;


public class DialogProgress extends Dialog implements View.OnClickListener {

    private Context mContext;

    private ImageView imgCloseDialog;
    private OnAcceptInterface acceptListener;
    private OnCancelInterface cancelListener;


    private int CurrentProgress = 0;
    private ProgressBar progressBar;
    private TextView txtShowPercentage,txtHeader;

    private ConstraintLayout headerContent;



    public DialogProgress(@NonNull Context context ,RecentlyMetaModel metaModel) {
        super(context);
        this.mContext = context;
        setContentView(R.layout.dialog_progress);
        this.getWindow().getAttributes().windowAnimations = R.style.AlertDialogAnimation;
        getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        initView(metaModel);
        // initValue(alertDialogModel);

    }


    private void initView(RecentlyMetaModel metaModel) {

        headerContent=findViewById(R.id.header);

        imgCloseDialog = findViewById(R.id.img_dialog_close);
        txtHeader = findViewById(R.id.txt_header);
        txtShowPercentage = findViewById(R.id.txt_pregress_precentage);
        progressBar = findViewById(R.id.progressBar);

        progressBar.setVisibility(View.GONE);
        txtShowPercentage.setVisibility(View.GONE);
        txtHeader.setVisibility(View.GONE);


        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                progressBar.startAnimation(MainApplication.SetAnimation("FadeIn"));
                txtShowPercentage.startAnimation(MainApplication.SetAnimation("FadeIn"));
                txtHeader.startAnimation(MainApplication.SetAnimation("FadeIn"));


                progressBar.setVisibility(View.VISIBLE);
                txtShowPercentage.setVisibility(View.VISIBLE);
                txtHeader.setVisibility(View.VISIBLE);


            }
        }, Constants.DelayMoreFast);

        imgCloseDialog.startAnimation(MainApplication.SetAnimation("Rotate"));


        imgCloseDialog.setOnClickListener(this);

        new CountDownTimer(2480, 16) {

            public void onTick(long millisUntilFinished) {
                // Used for formatting digit to be in 2 digits only
//                NumberFormat f = new DecimalFormat("00");
//                long hour = (millisUntilFinished / 3600000) % 24;
//                long min = (millisUntilFinished / 60000) % 60;
//                long sec = (millisUntilFinished / 1000) % 60;

                //     txtShowPercentage.setText(f.format(hour) + ":" + f.format(min) + ":"  + f.format(sec));

                CurrentProgress = CurrentProgress + 1;

                if (CurrentProgress<=100) {
                    txtShowPercentage.setText("%" + CurrentProgress);
                    txtHeader.setText("Processing ...." + "%" + CurrentProgress);
                }
                progressBar.setProgress(CurrentProgress);
                progressBar.setMax(100);

            }

            // When the task is over it will print 00:00:00 there

            public void onFinish() {
                // txtShowPercentage.setText("00:00:00");
                if (metaModel.isSuccess()) {
                    txtShowPercentage.setText("Done !");

                    Intent intent1 = new Intent(mContext, PDFViewActivity.class);
                    intent1.putExtra("RecentlyMetaModel",metaModel);
                    mContext.startActivity(intent1);


                }
                else{
                    headerContent.setBackgroundResource(R.drawable.corrner_background_red);
                    txtShowPercentage.setText("Failed !");
                }
                /// open activity result pdf


            }

        }.start();


    }


    public Dialog setAcceptButton(OnAcceptInterface acceptListener) {
        this.acceptListener = acceptListener;
        //btnCancel.setVisibility(View.VISIBLE);
        return this;
    }


    @Override
    public void onClick(View view) {

        switch (view.getId()) {
            case R.id.img_dialog_close:
                animOpenCloseDialog();

                break;

            case R.id.btn_dialog_confirm:

                //open detail
//                final CountDownTimer countDownTimer = new CountDownTimer(11*1000,1000) {
//                    @Override
//                    public void onTick(long millisUntilFinished) {
//
//                        CurrentProgress = CurrentProgress + 10;
//                        progressBar.setProgress(CurrentProgress);
//                        progressBar.setMax(100);
//
//                    }
//
//                    @Override
//                    public void onFinish() {
//
//                    }
//                };

                dismiss();
                break;
        }
        // dismiss();
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


    public interface OnAcceptInterface {
        void accept();
    }

    public interface OnCancelInterface {
        void cancel();
    }
}
