package com.example.finance.Adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.finance.Models.ModelTran;
import com.example.finance.R;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;

public class AdapterTran extends FirebaseRecyclerAdapter<ModelTran, AdapterTran.myviewHolder> {


    /**
     * Initialize a {@link RecyclerView.Adapter} that listens to a Firebase query. See
     * {@link FirebaseRecyclerOptions} for configuration options.
     *
     * @param options
     */
    public AdapterTran(@NonNull FirebaseRecyclerOptions<ModelTran> options) {
        super(options);
    }

    @Override
    protected void onBindViewHolder(@NonNull myviewHolder holder, int position, @NonNull ModelTran model) {
        holder.type.setText(model.getType());
        holder.date.setText(model.getDate());
        holder.amount.setText(model.getAmt());
        //Picasso.get().load(model.getBankImage()).into(holder.Bankimage);
    }

    @NonNull
    @Override
    public myviewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.transaction, parent, false);
        return new myviewHolder(view);

    }

    class myviewHolder extends RecyclerView.ViewHolder {
        TextView date,amount,type;
        ImageView Bankimage;

        public myviewHolder(@NonNull View itemView) {
            super(itemView);

            date = itemView.findViewById(R.id.tvDate);
            type = itemView.findViewById(R.id.tvName);
            amount = itemView.findViewById(R.id.tvAmount);
          //  Bankimage=(ImageView) itemView.findViewById(R.id.ivBankImage);
        }
    }

}