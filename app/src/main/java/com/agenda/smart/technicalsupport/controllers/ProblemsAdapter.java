package com.agenda.smart.technicalsupport.controllers;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.agenda.smart.technicalsupport.R;
import com.agenda.smart.technicalsupport.models.Datum;
import com.agenda.smart.technicalsupport.models.Datum2;
import com.agenda.smart.technicalsupport.models.Problems;
import com.agenda.smart.technicalsupport.views.ProblemDetailsActivity;

import java.util.List;

/**
 * Created by Abdelrahman on 8/9/2018.
 */

public class ProblemsAdapter extends RecyclerView.Adapter<ProblemsAdapter.ProblemsViewHolder> {
    Context context;
    Problems problems;

    public ProblemsAdapter(Context context, Problems problems) {
        this.context = context;
        this.problems = problems;
    }

    @Override
    public ProblemsViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.problems, parent, false);
        ProblemsViewHolder problemsViewHolder = new ProblemsViewHolder(view);
        return problemsViewHolder;
    }

    @Override
    public void onBindViewHolder(ProblemsViewHolder holder, final int position) {
        holder.itemName.setText(problems.getProblems().getData().get(position).getTitle());
        holder.layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int problem_id = problems.getProblems().getData().get(position).getId();
                Intent intent = new Intent(context, ProblemDetailsActivity.class);
                intent.putExtra("problem_id", problem_id);
                context.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return problems.getProblems().getData().size();
    }

    public class ProblemsViewHolder extends RecyclerView.ViewHolder {
        RelativeLayout layout;
        TextView itemName;

        public ProblemsViewHolder(View itemView) {
            super(itemView);

            itemName = itemView.findViewById(R.id.item_name);
            layout = itemView.findViewById(R.id.problems_layout);

        }
    }
    public void addInRecycle(List<Datum> datum) {
        for (Datum dt : datum) {
            problems.getProblems().getData().add(dt);
        }
        notifyDataSetChanged();
    }
}
