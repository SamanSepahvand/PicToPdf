package com.samansepahvand.pictopdf;

import android.app.Application;
import android.content.Context;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;


public class MainApplication extends Application {

    private static Context context;
    private static Animation animation;
    private Context _context;


    public MainApplication() {
    }

    @Override
    public void onCreate() {
        super.onCreate();
        MainApplication.context = getApplicationContext();



     //   fontAssign();

    }

//    public void fontAssign(){
//        CalligraphyConfig.initDefault(new CalligraphyConfig.Builder()
//        .setDefaultFontPath("fonts/iran_sans.ttf")
//                .setFontAttrId(R.attr.fontPath)
//                .disableCustomViewInflation()
//                .build() );
//
//    }

    public MainApplication(Context context) {
        this._context=context;
    }



    public static Context getAppMainContext() {
        return MainApplication.context;
    }

//    public   void CopyContent(Context context, String content, String type){
//        new CustomToast(context,type+" با موفقیت کپی شد ! ").show();
//        ClipboardManager clipboard = (ClipboardManager) context.getSystemService(CLIPBOARD_SERVICE);
//        ClipData clip = ClipData.newPlainText("label",content );
//        clipboard.setPrimaryClip(clip);
//    }


    public  static Animation SetAnimation(String typeAnimation) {

        switch (typeAnimation) {
            case "Rotate":
                animation = AnimationUtils.loadAnimation(context, R.anim.anim_rotate_menu);
                break;
            case "RotateSlow":
                animation = AnimationUtils.loadAnimation(context, R.anim.rotate_anim);
                break;
            case "FadeIn":
                animation = AnimationUtils.loadAnimation(context, R.anim.fade_anim_dialog);
                break;
            case "FadeInLong":
                animation = AnimationUtils.loadAnimation(context, R.anim.fade_anim);
                break;
            case "SlideUp":
                animation = AnimationUtils.loadAnimation(context, R.anim.slide_up);
                break;
            case "SlideUpSlow":
                animation = AnimationUtils.loadAnimation(context, R.anim.slide_up_slow);
                break;
            case "FadeOut":
                animation = AnimationUtils.loadAnimation(context, R.anim.fade_out);
                break;

        }
        return animation;

    }



}

