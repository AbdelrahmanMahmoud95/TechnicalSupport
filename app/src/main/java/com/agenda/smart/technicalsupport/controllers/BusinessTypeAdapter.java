package com.agenda.smart.technicalsupport.controllers;

import android.content.Context;
import android.content.SharedPreferences;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.agenda.smart.technicalsupport.ChooseDialog;
import com.agenda.smart.technicalsupport.R;
import com.agenda.smart.technicalsupport.models.BusinessType;
import com.agenda.smart.technicalsupport.models.City;

import java.util.List;

import static android.preference.PreferenceManager.getDefaultSharedPreferences;

/**
 * Created by Abdelrahman on 9/5/2018.
 */

public class BusinessTypeAdapter extends ArrayAdapter {
    private Context mContext;
    private int resourceID;
    private List<BusinessType> types;
    private SharedPreferences dataSaver;
    private ChooseDialog holder;

    public BusinessTypeAdapter(@NonNull Context context, int resource, @NonNull List<BusinessType> types, ChooseDialog holder) {
        super(context, resource);
        this.mContext = context;
        this.resourceID = resource;
        this.types = types;
        this.holder = holder;
    }

    @Override
    public int getCount() {
        return types.size();
    }

    @Override
    public BusinessType getItem(int position) {
        return types.get(position);
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        View row = convertView;
        LayoutInflater inflater = LayoutInflater.from(mContext);
        row = inflater.inflate(resourceID, parent, false);
        TextView name = (TextView) row.findViewById(R.id.name);
        name.setText(types.get(position).getName());
        name.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Context context = mContext.getApplicationContext();
                dataSaver = getDefaultSharedPreferences(context);

                dataSaver.edit()
                        .putString(AppKeys.BUSINESS_TYPE_NAME, types.get(position).getName())
                        .commit();
                dataSaver.edit()
                        .putInt(AppKeys.BUSINESS_TYPE_ID, types.get(position).getId())
                        .commit();

                holder.dismiss();
            }
        });
        return row;
    }
}