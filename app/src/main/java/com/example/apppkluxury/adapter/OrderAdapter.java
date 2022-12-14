package com.example.apppkluxury.adapter;


import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.apppkluxury.R;
import com.example.apppkluxury.model.Order;

import java.util.List;

public class OrderAdapter extends RecyclerView.Adapter<OrderAdapter.MyViewHolder> {
    private RecyclerView.RecycledViewPool viewPool = new RecyclerView.RecycledViewPool();
    List<Order> listOrder;
    Context context;
    public OrderAdapter(List<Order> listOrder, Context context) {
        this.listOrder = listOrder;
        this.context = context;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_order, parent, false);

        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        Order order = listOrder.get(position);
        holder.txtorder.setText("Đơn hàng: " + order.getId());
        LinearLayoutManager layoutManager = new LinearLayoutManager(
                holder.recyclerDetail.getContext(),
                LinearLayoutManager.VERTICAL,
                false

        );
        layoutManager.setInitialPrefetchItemCount(order.getItem().size());
        // adapter detail
        DetailAdapter detailAdapter =  new DetailAdapter(order.getItem(), context);
        holder.recyclerDetail.setLayoutManager(layoutManager);
        holder.recyclerDetail.setAdapter(detailAdapter);
        holder.recyclerDetail.setRecycledViewPool(viewPool);
    }

    @Override
    public int getItemCount() {
        return listOrder.size();
    }

    public class MyViewHolder  extends RecyclerView.ViewHolder {
        TextView txtorder;
        RecyclerView recyclerDetail;
        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            txtorder = itemView.findViewById(R.id.order_id);
            recyclerDetail = itemView.findViewById(R.id.recycleview_detail);

        }
    }
}
