package com.shuvo.ttit.terrainshop.productDetails.adapters;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.shuvo.ttit.terrainshop.R;
import com.shuvo.ttit.terrainshop.productDetails.lists.ImageList;
import com.smarteist.autoimageslider.SliderViewAdapter;

import java.util.ArrayList;

public class ImageAdapter extends  SliderViewAdapter<ImageAdapter.ImageSliderAdapterVH>{

    private Context context;
    private ArrayList<ImageList> mSliderItems = new ArrayList<>();

    public ImageAdapter(Context context, ArrayList<ImageList> sliderItems) {
        this.context = context;
        this.mSliderItems = sliderItems;
    }

    public void renewItems(ArrayList<ImageList> sliderItems) {
        this.mSliderItems = sliderItems;
        notifyDataSetChanged();
    }

    public void deleteItem(int position) {
        this.mSliderItems.remove(position);
        notifyDataSetChanged();
    }

    public void addItem(ImageList sliderItem) {
        this.mSliderItems.add(sliderItem);
        notifyDataSetChanged();
    }

    @Override
    public ImageSliderAdapterVH onCreateViewHolder(ViewGroup parent) {
        View inflate = LayoutInflater.from(parent.getContext()).inflate(R.layout.image_slider_layout_item, parent,false);
        return new ImageSliderAdapterVH(inflate);
    }

    @Override
    public void onBindViewHolder(ImageSliderAdapterVH viewHolder, final int position) {

        ImageList sliderItem = mSliderItems.get(position);

        viewHolder.textViewDescription.setText(sliderItem.getDescription());
        viewHolder.textViewDescription.setTextSize(16);
        viewHolder.textViewDescription.setTextColor(Color.parseColor("#40739e"));
        Glide.with(viewHolder.itemView)
                .load(sliderItem.getImageUrl())
                .fitCenter()
                .into(viewHolder.imageViewBackground);

        viewHolder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Toast.makeText(context, "This is item in position " + position, Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public int getCount() {
        //slider view count could be dynamic size
        return mSliderItems.size();
    }

    static class ImageSliderAdapterVH extends SliderViewAdapter.ViewHolder {

        View itemView;
        ImageView imageViewBackground;
        ImageView imageGifContainer;
        TextView textViewDescription;

        public ImageSliderAdapterVH(View itemView) {
            super(itemView);
            imageViewBackground = itemView.findViewById(R.id.iv_auto_image_slider);
            imageGifContainer = itemView.findViewById(R.id.iv_gif_container);
            textViewDescription = itemView.findViewById(R.id.tv_auto_image_slider);
            this.itemView = itemView;
        }
    }
}
