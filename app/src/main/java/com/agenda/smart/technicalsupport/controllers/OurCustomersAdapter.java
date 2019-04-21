package com.agenda.smart.technicalsupport.controllers;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v4.view.PagerAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;


import com.agenda.smart.technicalsupport.R;
import com.agenda.smart.technicalsupport.models.Slider;
import com.squareup.picasso.Picasso;

import java.util.List;

/**
 * Created by Abdelrahman on 10/4/2018.
 */

public class OurCustomersAdapter extends PagerAdapter{
    private List<Slider> images;
    private LayoutInflater inflater;
    private Context context;

    public OurCustomersAdapter(Context context, List<Slider> images) {
        this.context = context;
        this.images=images;
        inflater = LayoutInflater.from(context);
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        container.removeView((View) object);
    }

    @Override
    public int getCount() {
        return images.size();
    }

    @Override
    public Object instantiateItem(ViewGroup view, int position) {
        View view1 = inflater.inflate(R.layout.slide, view, false);
        ImageView myImage = (ImageView) view1.findViewById(R.id.slider_image);
        Picasso.with(context).load(images.get(position).getImage()).into(myImage);
        view.addView(view1, 0);
        return view1;
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view.equals(object);
    }
}