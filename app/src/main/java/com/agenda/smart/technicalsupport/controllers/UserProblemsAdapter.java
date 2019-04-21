package com.agenda.smart.technicalsupport.controllers;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.agenda.smart.technicalsupport.R;
import com.agenda.smart.technicalsupport.models.UserProblems;
import com.agenda.smart.technicalsupport.views.ReplyActivity;

/**
 * Created by Abdelrahman on 9/4/2018.
 */

public class UserProblemsAdapter extends RecyclerView.Adapter<UserProblemsAdapter.UserProblemViewHolder> {
    Context context;
    UserProblems problems;

    public UserProblemsAdapter(Context context, UserProblems problems) {
        this.context = context;
        this.problems = problems;
    }

    @NonNull
    @Override
    public UserProblemsAdapter.UserProblemViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.my_problems, parent, false);
        UserProblemsAdapter.UserProblemViewHolder userProblemViewHolder = new UserProblemsAdapter.UserProblemViewHolder(view);
        return userProblemViewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull UserProblemViewHolder holder, final int position) {
        holder.problemDate.setText(problems.getIssues().get(position).getCreatedAt());
        holder.problemStatus.setText(problems.getIssues().get(position).getStatusName());
        holder.problemNumber.setText(problems.getIssues().get(position).getIssueNumber());
        String statusName = problems.getIssues().get(position).getStatusName();
        if (statusName.equals("تم حل المشكلة")) {
            holder.showReply.setVisibility(View.VISIBLE);
        }
        if (problems.getIssues().get(position).getReply() != null) {
            holder.showReply.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    int problem_id = problems.getIssues().get(position).getId();
                    Intent intent = new Intent(context, ReplyActivity.class);
                    intent.putExtra("problem_id", problem_id);
                    context.startActivity(intent);
                }
            });

            holder.showReplyLayout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    int problem_id = problems.getIssues().get(position).getId();
                    Intent intent = new Intent(context, ReplyActivity.class);
                    intent.putExtra("problem_id", problem_id);
                    Log.e("TAG", "problem_id " + problem_id);
                    context.startActivity(intent);
                }
            });
        }
    }

    @Override
    public int getItemCount() {
        return problems.getIssues().size();
    }

    public class UserProblemViewHolder extends RecyclerView.ViewHolder {
        TextView problemName, problemDate, problemStatus, problemNumber;
        ImageView showReply;
        RelativeLayout showReplyLayout;

        public UserProblemViewHolder(View itemView) {
            super(itemView);
            problemDate = itemView.findViewById(R.id.problem_date);
            problemName = itemView.findViewById(R.id.problem_name);
            problemStatus = itemView.findViewById(R.id.problem_status);
            problemNumber = itemView.findViewById(R.id.problem_number);
            showReply = itemView.findViewById(R.id.show_reply);
            showReplyLayout = itemView.findViewById(R.id.show_reply_layout);

        }
    }
}

