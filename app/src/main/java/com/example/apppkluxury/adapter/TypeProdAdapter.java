package com.example.apppkluxury.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.apppkluxury.R;
import com.example.apppkluxury.model.Type_Pro;

import java.util.List;

public class TypeProdAdapter extends BaseAdapter {
    Context context;
    List<Type_Pro> array;

    public TypeProdAdapter(Context context, List<Type_Pro> array) {
        this.context = context;
        this.array = array;
    }

    @Override
    public int getCount() {
        return array.size();
    }

    @Override
    public Object getItem(int i) {
        return null;
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }
    public class ViewHolder{
        TextView textTypeName;
        ImageView img;
    }
    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        ViewHolder viewHolder = null;
        if(view == null){
            viewHolder = new ViewHolder();
            LayoutInflater layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            view = layoutInflater.inflate(R.layout.item_type_product, null);

            viewHolder.textTypeName = view.findViewById(R.id.item_type_name);
            viewHolder.img = view.findViewById(R.id.item_img);
            view.setTag(viewHolder);
        }else{
            viewHolder = (ViewHolder) view.getTag();
        }
        viewHolder.textTypeName.setText(array.get(i).getType_name());
        Glide.with(context).load(array.get(i).getType_img()).into(viewHolder.img);
        return view;
    }
}
