package com.tubes.kouveepetshop.RecyclerAdapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.tubes.kouveepetshop.Activity.DetailCustomerActivity;
import com.tubes.kouveepetshop.Model.CustomerDAO;
import com.tubes.kouveepetshop.R;

import java.util.ArrayList;
import java.util.List;

public class CustomerRecyclerAdapter extends RecyclerView.Adapter<CustomerRecyclerAdapter.RoomViewHolder> implements Filterable {
    private String nama, tglLahir, id, alamat, noTelp, icon;
    private List<CustomerDAO> dataList;
    private List<CustomerDAO> filteredDataList;
    private Context context;

    public CustomerRecyclerAdapter(Context context, List<CustomerDAO> dataList) {
        this.context=context;
        this.dataList = dataList;
        this.filteredDataList = dataList;
    }

    @NonNull
    @Override
    public RoomViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View view = layoutInflater.inflate(R.layout.recycle_adapter_customer, parent, false);
        return new RoomViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CustomerRecyclerAdapter.RoomViewHolder holder, int position) {
        final CustomerDAO brg = filteredDataList.get(position);
        holder.mIcon.setText(brg.getNAMA().substring(0, 1));
        holder.mNama.setText(brg.getNAMA());
        holder.mTglLahir.setText(brg.getTGL_LAHIR());
        holder.mAlamat.setText(brg.getALAMAT());
        holder.mNoTelp.setText(brg.getNO_TELP());

        holder.mParent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view){
                id = brg.getID_CUSTOMER();

                Intent i = new Intent(context, DetailCustomerActivity.class);
                i.putExtra("id",id);
                context.startActivity(i);
            }
        });
    }

    @Override
    public int getItemCount() {
        return filteredDataList.size();
    }

    public class RoomViewHolder extends RecyclerView.ViewHolder{
        private TextView mNama, mTglLahir, mAlamat, mNoTelp, mIcon;
        private LinearLayout mParent;

        public RoomViewHolder(@NonNull View itemView) {
            super(itemView);
            mIcon = itemView.findViewById(R.id.twIcon);
            mNama = itemView.findViewById(R.id.twName);
            mTglLahir = itemView.findViewById(R.id.twTglLahir);
            mAlamat = itemView.findViewById(R.id.twAlamat);
            mNoTelp = itemView.findViewById(R.id.twNoTelp);
            mParent = itemView.findViewById(R.id.linearLayout);
        }
    }

    @Override
    public Filter getFilter() {
        return new Filter() {
            @Override
            protected FilterResults performFiltering(CharSequence constraint) {
                String charSequenceString = constraint.toString();
                if (charSequenceString.isEmpty()) {
                    filteredDataList = dataList;
                } else {
                    List<CustomerDAO> filteredList = new ArrayList<>();
                    for (CustomerDAO CustomerDAO : dataList) {
                        if (CustomerDAO.getNAMA().toLowerCase().contains(charSequenceString.toLowerCase())) {
                            filteredList.add(CustomerDAO);
                        }
                        filteredDataList = filteredList;
                    }

                }
                FilterResults results = new FilterResults();
                results.values = filteredDataList;
                return results;
            }

            @Override
            protected void publishResults(CharSequence constraint, FilterResults results) {
                filteredDataList = (List<CustomerDAO>) results.values;
                notifyDataSetChanged();
            }
        };
    }
}
