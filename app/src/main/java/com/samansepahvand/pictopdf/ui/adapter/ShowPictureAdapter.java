package com.samansepahvand.pictopdf.ui.adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.samansepahvand.pictopdf.MainApplication;
import com.samansepahvand.pictopdf.R;
import com.samansepahvand.pictopdf.bussines.domain.Constants;
import com.samansepahvand.pictopdf.utility.FileUtility;

import java.util.ArrayList;
import java.util.List;

public class ShowPictureAdapter extends RecyclerView.Adapter<ShowPictureAdapter.MyViewHolder> {


    private Context mContext;
    private List<Bitmap> bitmapList = new ArrayList<>();


    public ShowPictureAdapter(Context mContext, List<Bitmap> bitmapLists) {
        this.mContext = mContext;
        this.bitmapList = bitmapLists;
    }

    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.show_picture_item, parent, false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        Bitmap bitmap = bitmapList.get(position);

        holder.imgPic.setImageBitmap(bitmap);
        holder.txtFileTitle.setText("Image"+position);

    }

    @Override
    public int getItemCount() {
        return bitmapList.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {


        private ImageView imgClose,imgPic;
        private TextView txtFileTitle;


        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            initView();

        }

        private void initView() {

            imgClose = itemView.findViewById(R.id.img_dialog_close2);
            txtFileTitle = itemView.findViewById(R.id.item_txt_title);
            imgPic = itemView.findViewById(R.id.item_img_pic);


            imgClose.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    //remove Item
                    animOpenCloseDialog(imgClose);


                }
            });
        }

        public void animOpenCloseDialog(View view) {
            view.startAnimation(MainApplication.SetAnimation("Rotate"));
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {


                        bitmapList.remove(getAdapterPosition());
                        notifyItemRemoved(getAdapterPosition());

                }
            }, Constants.DelayTimeDialogAnimation);
        }

    }

}
