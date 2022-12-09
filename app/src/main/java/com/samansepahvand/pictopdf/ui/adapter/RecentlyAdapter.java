package com.samansepahvand.pictopdf.ui.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextClock;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.samansepahvand.pictopdf.R;
import com.samansepahvand.pictopdf.bussines.metaModel.RecentlyMetaModel;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class RecentlyAdapter  extends RecyclerView.Adapter<RecentlyAdapter.MyViewHolder> {


    private Context mContext;

    private List<RecentlyMetaModel> mData=new ArrayList<>();

private  IGetRecentlyMetaModel _iGetRecentlyMetaModel;

    public RecentlyAdapter(Context mContext, List<RecentlyMetaModel> listData,IGetRecentlyMetaModel iGetRecentlyMetaModel) {
        this.mContext = mContext;
        this.mData=listData;
        this._iGetRecentlyMetaModel=iGetRecentlyMetaModel;

    }

    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(mContext).inflate(R.layout.recently_item,parent,false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {


        holder.txtTitle.setText(mData.get(position).getFileTitle());

        holder.txtDate.setText(mData.get(position).getFileDate());

        holder.txtSize.setText(mData.get(position).getFileSize());


    }

    @Override
    public int getItemCount() {
        return mData.size();
    }

    public class MyViewHolder extends  RecyclerView.ViewHolder implements View.OnClickListener {


        private TextView txtTitle,txtDate,txtSize;

        private ImageView imgMore;


        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            initView();

        }

        private void initView(){

            txtTitle=itemView.findViewById(R.id.item_file_title);
            txtDate=itemView.findViewById(R.id.item_file_date);
            txtSize=itemView.findViewById(R.id.item_file_size);

            imgMore=itemView.findViewById(R.id.img_more);

            imgMore.setOnClickListener(this);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    _iGetRecentlyMetaModel.GetRecentlyMetaModel(mData.get(getAdapterPosition()));
                }
            });
        }

        @Override
        public void onClick(View view) {

            switch (view.getId()){
                case R.id.img_more:
                    Toast.makeText(mContext, ""+mData.get(getAdapterPosition()).getFilePath(), Toast.LENGTH_SHORT).show();
                    break;

            }
        }
    }

    public  interface IGetRecentlyMetaModel{

        void   GetRecentlyMetaModel (RecentlyMetaModel infoMetaModel);

    }
}
