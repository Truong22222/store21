package com.example.apppkluxury.adapter;

import android.content.Context;
import android.content.DialogInterface;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.apppkluxury.Interface.ImageClickListenner;
import com.example.apppkluxury.R;
import com.example.apppkluxury.model.Cart;
import com.example.apppkluxury.model.EventBus.toTalEvent;
import com.example.apppkluxury.utils.utils;

import org.greenrobot.eventbus.EventBus;

import java.text.DecimalFormat;
import java.util.List;

public class ShoppingCartAdapter extends RecyclerView.Adapter<ShoppingCartAdapter.MyViewHolder> {
    Context context;
    List<Cart> listCart;

    public ShoppingCartAdapter(Context context, List<Cart> listCart) {
        this.context = context;
        this.listCart = listCart;
    }


    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View  view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_shopping_cart,parent,false);

        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        Cart cart = listCart.get(position);
        holder.item_cart_nameprod.setText(cart.getNameProd());
        holder.item_cart_amount.setText(cart.getAmount()+"");
        Glide.with(context).load(cart.getImgProd()).into(holder.item_cart_img);
        DecimalFormat decFormat = new DecimalFormat("###,###,###");
        holder.item_cart_price1.setText(decFormat.format(cart.getPrice()));
        long price = cart.getAmount() * cart.getPrice();
        holder.item_cart_price2.setText(decFormat.format(price)+"Đ");
        holder.checkBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if(b){
                    utils.arraybyCart.add(cart);
                    EventBus.getDefault().postSticky(new toTalEvent());
                }else{
                    for (int i=0 ; i<utils.arrayCart.size(); i++){
                        if(utils.arraybyCart.get(i).getIdProd() == cart.getIdProd()){
                            utils.arraybyCart.remove(i);
                            EventBus.getDefault().postSticky(new toTalEvent());
                        }
                    }
                }
            }
        });
        holder.setListenner(new ImageClickListenner() {
            @Override
            public void onImageClick(View view, int pos, int value) {
                Log.d("TAG", "onImageClick: "+pos+"..."+value);
                if (value == 1){
                    if (listCart.get(pos).getAmount() > 1){
                        int amountnew = listCart.get(pos).getAmount()-1;
                        listCart.get(pos).setAmount(amountnew);
                    }
                    else if (listCart.get(pos).getAmount() == 1) {
                        AlertDialog.Builder builder = new AlertDialog.Builder(view.getRootView().getContext());
                        builder.setTitle("Thông Báo");
                        builder.setMessage("Bạn có muốn xóa sản phẩm khỏi giỏ hàng không?");
                        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                utils.arrayCart.remove(pos);
                                notifyDataSetChanged();
                                EventBus.getDefault().postSticky(new toTalEvent());
                            }
                        });
                        builder.setNegativeButton("Hủy", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                dialogInterface.dismiss();
                            }
                        });
                        builder.show();
                    }
                }else if( value == 2) {
                    if(listCart.get(pos).getAmount() < 11){
                        int amountnew = listCart.get(pos).getAmount() + 1;
                        listCart.get(pos).setAmount(amountnew);
                    }
                }
                holder.item_cart_amount.setText(listCart.get(pos).getAmount()+"");
                long price = listCart.get(pos).getAmount() * listCart.get(pos).getPrice();
                holder.item_cart_price2.setText(decFormat.format(price)+"Đ");
                EventBus.getDefault().postSticky(new toTalEvent());
            }
        });
    }

    @Override
    public int getItemCount() {
        return listCart.size();
    }

    public class MyViewHolder  extends RecyclerView.ViewHolder implements View.OnClickListener {
        ImageView item_cart_img, img_remove, img_add;
        TextView item_cart_nameprod, item_cart_price1, item_cart_amount,  item_cart_price2;
        ImageClickListenner listenner;
        CheckBox checkBox;
        public void setListenner(ImageClickListenner listenner) {
            this.listenner = listenner;
        }

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            item_cart_img = itemView.findViewById(R.id.item_cart_img);
            item_cart_nameprod = itemView.findViewById(R.id.item_cart_nameprod);
            item_cart_price1 = itemView.findViewById(R.id.item_cart_price1);
            item_cart_amount = itemView.findViewById(R.id.item_cart_amount);
            item_cart_price2 = itemView.findViewById(R.id.item_cart_price2);

            img_remove = itemView.findViewById(R.id.item_cart_remove);
            img_add = itemView.findViewById(R.id.item_cart_add);
            checkBox = itemView.findViewById(R.id.item_checkbx_cart);
            //event click
            img_add.setOnClickListener(this);
            img_remove.setOnClickListener(this);

        }
        @Override
        public void onClick(View view) {
            if(view == img_remove) {
                listenner.onImageClick(view, getAdapterPosition(), 1);
            }else if ( view == img_add){
                listenner.onImageClick(view, getAdapterPosition(), 2 );
            }
        }

    }

}
