package com.example.apppkluxury.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.apppkluxury.R;
import com.example.apppkluxury.model.Item;

import java.util.List;

public class DetailAdapter extends RecyclerView.Adapter<DetailAdapter.MyViewHolder> {
    List<Item> itemList;
    Context context;

    public DetailAdapter(List<Item> itemList, Context context) {
        this.itemList = itemList;
        this.context = context;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_detail, parent, false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        Item item = itemList.get(position);
        holder.txtName.setText(item.getProd_name()+ "");
        holder.txtAmount.setText("Số lượng: "+item.getAmount()+ "");
        Glide.with(context).load(item.getImgProd()).into(holder.imgDetail);
    }

    @Override
    public int getItemCount() {
        return itemList.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder{
        ImageView imgDetail;
        TextView txtName, txtAmount;
        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
             imgDetail = itemView.findViewById(R.id.item_imgdetail);
             txtName = itemView.findViewById(R.id.item_nameDetail);
             txtAmount = itemView.findViewById(R.id.item_amountDetail);
        }
    }
}
