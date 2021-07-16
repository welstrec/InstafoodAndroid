package com.instafood.welstrec.instafood.ViewHolder;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.instafood.welstrec.instafood.Interface.ItemClickListener;
import com.instafood.welstrec.instafood.R;

public class FoodViewHolder  extends RecyclerView.ViewHolder implements View.OnClickListener {

    public TextView food_name;
    public ImageView food_image;

    private ItemClickListener itemClickListener;

    public void setItemClickListener(ItemClickListener itemClickListener) {
        this.itemClickListener = itemClickListener;
    }

    public FoodViewHolder(View itemView) {
        super(itemView);

        food_name= (TextView) itemView.findViewById(R.id.food_name);
        food_image=(ImageView) itemView.findViewById(R.id.food_image);
        itemView.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        itemClickListener.OnClick(view,getAdapterPosition(),false);
    }
}