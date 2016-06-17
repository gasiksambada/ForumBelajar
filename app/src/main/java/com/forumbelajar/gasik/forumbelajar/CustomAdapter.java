package com.forumbelajar.gasik.forumbelajar;

import android.animation.Animator;
import android.content.Context;
import android.graphics.Bitmap;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;


public class CustomAdapter extends BaseAdapter{
    String [] result,result2;
    Context context;
    Bitmap [] imageId,imageId2;
    int right_answer_pos;
    ImageView expandedImageView;

    private static LayoutInflater inflater = null;
    public CustomAdapter(DetailQuestionActivity mainActivity,int pos_id, String[] prgmNameFrom, String[] prgmNameList, Bitmap[] prgmImages, Bitmap[] prgmImages2) {
        right_answer_pos = pos_id;
        result = prgmNameList;
        result2 = prgmNameFrom;
        context = mainActivity;
        imageId = prgmImages;
        imageId2 = prgmImages2;
        inflater = ( LayoutInflater )context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }
    @Override
    public int getCount() {
        return result.length;
    }

    @Override
    public Object getItem(int position) {
        return position;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    public class Holder
    {
        TextView tv,tv2;
        ImageView img,img2,expandedImageView,expandedImageView2;
        RelativeLayout checkmark;
    }
    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        final Holder holder = new Holder();
        final View rowView;
        rowView = inflater.inflate(R.layout.answer_list, null);
        holder.tv = (TextView) rowView.findViewById(R.id.answer);
        holder.tv2 = (TextView) rowView.findViewById(R.id.answer_from);
        holder.img = (ImageView) rowView.findViewById(R.id.showanswerphoto1);
        holder.img2 = (ImageView) rowView.findViewById(R.id.showanswerphoto2);
        holder.expandedImageView = (ImageView) rowView.findViewById(R.id.expanded_image_answer);
        holder.expandedImageView2 = (ImageView) rowView.findViewById(R.id.expanded_image_answer2);
        holder.checkmark = (RelativeLayout) rowView.findViewById(R.id.right_answer_box);
        if(right_answer_pos == position){
            holder.checkmark.setVisibility(View.VISIBLE);
        }
        holder.tv.setText(result[position]);
        holder.tv2.setText(result2[position]);

        if(imageId[position] != null){
            holder.img.setImageBitmap(imageId[position]);
            holder.img.setVisibility(View.VISIBLE);
            holder.expandedImageView.setImageBitmap(imageId[position]);

            holder.img.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    holder.expandedImageView.setVisibility(View.VISIBLE);
                    holder.img.setVisibility(View.GONE);
                    if(imageId2[position] != null) {
                        holder.img2.setVisibility(View.GONE);
                    }
                }
            });
        }

        if(imageId2[position] != null) {
            holder.img2.setImageBitmap(imageId2[position]);
            holder.img2.setVisibility(View.VISIBLE);
            holder.expandedImageView2.setImageBitmap(imageId2[position]);

            holder.img2.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    holder.expandedImageView2.setVisibility(View.VISIBLE);
                    holder.img2.setVisibility(View.GONE);
                    if(imageId[position] != null) {
                        holder.img.setVisibility(View.GONE);
                    }
                }
            });
        }

        holder.expandedImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                holder.expandedImageView.setVisibility(View.GONE);
                holder.img.setVisibility(View.VISIBLE);
                if(imageId2[position] != null) {
                    holder.img2.setVisibility(View.VISIBLE);
                }
            }
        });

        holder.expandedImageView2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                holder.expandedImageView2.setVisibility(View.GONE);
                holder.img2.setVisibility(View.VISIBLE);
                if(imageId[position] != null) {
                    holder.img.setVisibility(View.VISIBLE);
                }
            }
        });

        return rowView;
    }
}
