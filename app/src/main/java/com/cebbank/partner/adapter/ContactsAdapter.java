package com.cebbank.partner.adapter;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.cebbank.partner.R;
import com.cebbank.partner.bean.Contact;
import com.cebbank.partner.ui.CityActivity;
import com.cebbank.partner.utils.LogUtils;

import java.util.List;

public class ContactsAdapter extends RecyclerView.Adapter<ContactsAdapter.ContactsViewHolder>{

    private List<Contact> contacts;
    private int layoutId;
    private Context context;

    public ContactsAdapter(List<Contact> contacts, int layoutId,Context context) {
        this.contacts = contacts;
        this.layoutId = layoutId;
        this.context = context;
    }

    @Override
    public ContactsViewHolder onCreateViewHolder(final ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View view = inflater.inflate(layoutId, null);
        final ContactsViewHolder viewHolder = new ContactsViewHolder(view);
        viewHolder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int position = viewHolder.getAdapterPosition();
                Intent intent = new Intent();
                intent.putExtra("name",contacts.get(position).getName());
                ((CityActivity)context).setResult(Activity.RESULT_OK,intent);
                ((CityActivity)context).finish();
            }
        });
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(final ContactsViewHolder holder, final int position) {
        final Contact contact = contacts.get(position);
        if (position == 0 || !contacts.get(position-1).getIndex().equals(contact.getIndex())) {
            holder.tvIndex.setVisibility(View.VISIBLE);
            holder.tvIndex.setText(contact.getIndex());
        } else {
            holder.tvIndex.setVisibility(View.GONE);
        }
        holder.tvName.setText(contact.getName());
    }

    @Override
    public int getItemCount() {
        return contacts.size();
    }

    class ContactsViewHolder extends RecyclerView.ViewHolder {
        public TextView tvIndex;
        public ImageView ivAvatar;
        public TextView tvName;

        public ContactsViewHolder(View itemView) {
            super(itemView);
            tvIndex = (TextView) itemView.findViewById(R.id.tv_index);
//            ivAvatar = (ImageView) itemView.findViewById(R.id.iv_avatar);
            tvName = (TextView) itemView.findViewById(R.id.tv_name);
        }
    }

}
