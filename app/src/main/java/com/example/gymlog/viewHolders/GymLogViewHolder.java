/**
 * Title: GymLogViewHolder.java
 * @author Noah deFer
 * Date: 4/6/2025
 * Description:
 */
package com.example.gymlog.viewHolders;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.example.gymlog.R;

public class GymLogViewHolder extends RecyclerView.ViewHolder{
    // FIELDS
    private final TextView gymLogViewItem;

    // CONSTRUCTORS
    private GymLogViewHolder(View gymLogView) {
        super(gymLogView);
        gymLogViewItem = gymLogView.findViewById(R.id.recyclerItemTextView);
    }

    // METHODS

    public void bind(String text) {
        gymLogViewItem.setText(text);
    }

    static GymLogViewHolder create(ViewGroup parent) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.gymlog_recycler_item, parent, false);
        return new GymLogViewHolder(view);
    }
}
