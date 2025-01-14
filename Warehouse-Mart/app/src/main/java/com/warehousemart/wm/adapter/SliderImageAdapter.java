package com.warehousemart.wm.adapter;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.smarteist.autoimageslider.SliderViewAdapter;
import com.warehousemart.wm.R;

public class SliderImageAdapter extends SliderViewAdapter<SliderImageAdapter.SliderAdapterVH> {
    private Context context;
    private int mCount;

    public SliderImageAdapter(Context context) {
        this.context = context;
    }

    public void setCount(int count) {
        this.mCount = count;
    }

    @Override
    public SliderAdapterVH onCreateViewHolder(ViewGroup parent) {
        View inflate = LayoutInflater.from(parent.getContext()).inflate(R.layout.image_slider_myshop, null);
        return new SliderAdapterVH(inflate);
    }

    @Override
    public void onBindViewHolder(SliderAdapterVH viewHolder, final int position) {

        viewHolder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                switch (position) {
                    case 0:
                        Intent browserIntent_1 = new Intent(
                                Intent.ACTION_VIEW,
                                Uri.parse("https://github.com/NovaAndriana"));
                        context.startActivity(browserIntent_1);
                        break;
                    case 1:
                        Intent browserIntent_2 = new Intent(
                                Intent.ACTION_VIEW,
                                Uri.parse("https://github.com/NovaAndriana"));
                        context.startActivity(browserIntent_2);
                        break;
                    case 2:
                        Intent browserIntent_3 = new Intent(
                                Intent.ACTION_VIEW,
                                Uri.parse("https://github.com/NovaAndriana"));
                        context.startActivity(browserIntent_3);
                        break;
                    case 3:
                        Intent browserIntent_4 = new Intent(
                                Intent.ACTION_VIEW,
                                Uri.parse("https://github.com/NovaAndriana"));
                        context.startActivity(browserIntent_4);
                        break;
                }
            }
        });

        switch (position) {
            case 0:
                Glide.with(viewHolder.itemView)
                        .load(R.drawable.slider1)
                        .fitCenter()
                        .into(viewHolder.imageViewBackground);
                break;
            case 1:
                Glide.with(viewHolder.itemView)
                        .load(R.drawable.slider2)
                        .fitCenter()
                        .into(viewHolder.imageViewBackground);
                break;
            case 2:
                Glide.with(viewHolder.itemView)
                        .load(R.drawable.slider3)
                        .fitCenter()
                        .into(viewHolder.imageViewBackground);
                break;
//            case 3:
//                Glide.with(viewHolder.itemView)
//                        .load(R.drawable.slider_4)
//                        .fitCenter()
//                        .into(viewHolder.imageViewBackground);
//                break;
        }
    }

    public int getCount() {
        //slider view count could be dynamic size
        return mCount;
    }

    class SliderAdapterVH extends SliderViewAdapter.ViewHolder {

        View itemView;
        ImageView imageViewBackground;
        ImageView imageGifContainer;
        TextView textViewDescription;

        public SliderAdapterVH(View itemView) {
            super(itemView);
            imageViewBackground = itemView.findViewById(R.id.iv_auto_image_slider);
            imageGifContainer = itemView.findViewById(R.id.iv_gif_container);
            textViewDescription = itemView.findViewById(R.id.tv_auto_image_slider);
            this.itemView = itemView;
        }
    }
}
