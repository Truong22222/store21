package com.example.apppkluxury.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.apppkluxury.Interface.ItemClickListener;
import com.example.apppkluxury.R;
import com.example.apppkluxury.activity.DetailActivity;
import com.example.apppkluxury.model.Product;

import java.text.DecimalFormat;
import java.util.List;

public class DongHoAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    Context context;
    List<Product> array;

    private static final int VIEW_TYPE_DATA = 0;
    private static final int VIEW_TYPE_LOADING = 1;

    public DongHoAdapter(Context context, List<Product> array) {
        this.context = context;
        this.array = array;
    }


    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if(viewType == VIEW_TYPE_DATA ){
            View item = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_dongho, parent, false);
            return new MyViewHolder(item);
        }else {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_loading, parent, false);
            return new LoadingViewHolder(view);
        }
    }

    @Override
    public int getItemViewType(int position) {
        return array.get(position) == null ? VIEW_TYPE_LOADING:VIEW_TYPE_DATA;

    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        if(holder instanceof MyViewHolder) {
            MyViewHolder myViewHolder = (MyViewHolder) holder;
            Product product = array.get(position);
            myViewHolder.txtName.setText(product.getProd_name());
            DecimalFormat decFormat = new DecimalFormat("###,###,###");
            myViewHolder.txtPrice.setText("Giá : "+decFormat.format(Double.parseDouble(String.valueOf(product.getProd_price())))+"vnd");
            myViewHolder.txtDetail.setText(product.getProd_detail());
            myViewHolder.txtId.setText(product.getId()+"");
            Glide.with(context).load(product.getProd_img()).into(myViewHolder.imgProd);

            myViewHolder.setItemClickListener(new ItemClickListener() {
                @Override
                public void onClick(View view, int pos, boolean isLongClick) {
                    if(!isLongClick){
                        //click
                        Intent intent = new Intent(context, DetailActivity.class);
                        intent.putExtra("detail", product);
                        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        context.startActivity(intent);
                    }
                }
            });

        }else {
            LoadingViewHolder loading = (LoadingViewHolder) holder;
            loading.progressBar.setIndeterminate(true);
        }


    }

    @Override
    public int getItemCount() {
        return array.size();
    }

    public class LoadingViewHolder extends RecyclerView.ViewHolder{
        ProgressBar progressBar;
        public LoadingViewHolder(@NonNull View itemView) {
            super(itemView);
            progressBar = itemView.findViewById(R.id.progressbar);
        }
    }

    public class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        TextView txtPrice, txtName, txtDetail, txtId;
        ImageView imgProd;
        private ItemClickListener itemClickListener;

        public void setItemClickListener(ItemClickListener itemClickListener) {
            this.itemClickListener = itemClickListener;
        }

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            txtPrice = itemView.findViewById(R.id.itemdh_price);
            txtName = itemView.findViewById(R.id.itemdh_name);
            txtDetail = itemView.findViewById(R.id.itemdh_details);
            txtId = itemView.findViewById(R.id.itemdh_id);
            imgProd = itemView.findViewById(R.id.itemdh_img);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
             itemClickListener.onClick(view, getAdapterPosition(), false);
        }
    }
}
