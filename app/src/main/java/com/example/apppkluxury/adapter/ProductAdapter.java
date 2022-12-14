package com.example.apppkluxury.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.apppkluxury.Interface.ItemClickListener;
import com.example.apppkluxury.R;
import com.example.apppkluxury.activity.DetailActivity;
import com.example.apppkluxury.model.Product;
import com.example.apppkluxury.model.Type_Pro;

import org.w3c.dom.Text;

import java.text.DecimalFormat;
import java.util.List;

public class ProductAdapter extends RecyclerView.Adapter<ProductAdapter.MyViewHolder> {
    List<Product> array;
    Context context;


    public ProductAdapter(Context context, List<Product> array) {
        this.array = array;
        this.context = context;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View item = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_product, parent, false);

        return new MyViewHolder(item);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        Product product = array.get(position);
        holder.txtName.setText(product.getProd_name());
        DecimalFormat decFormat = new DecimalFormat("###,###,###");
        holder.txtPrice.setText("Price: "+decFormat.format(Double.parseDouble(String.valueOf(product.getProd_price())))+"vnd");
        Glide.with(context).load(product.getProd_img()).into(holder.imgProd);
        holder.setItemClickListener(new ItemClickListener() {
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
    }

    @Override
    public int getItemCount() {
        return array.size();
    }


    public class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        TextView txtPrice, txtName;
        ImageView imgProd;
        private ItemClickListener itemClickListener;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            txtPrice = itemView.findViewById(R.id.itemprod_price);
            txtName = itemView.findViewById(R.id.itemprod_name);
            imgProd = itemView.findViewById(R.id.itemprod_img);
            itemView.setOnClickListener(this);
        }

        public void setItemClickListener(ItemClickListener itemClickListener) {
            this.itemClickListener = itemClickListener;
        }

        @Override
        public void onClick(View view) {
            itemClickListener.onClick(view, getAdapterPosition(), false);
        }
    }
}
