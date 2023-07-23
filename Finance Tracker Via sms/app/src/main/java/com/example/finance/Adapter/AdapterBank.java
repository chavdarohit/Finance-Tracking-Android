package com.example.finance.Adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.finance.Models.ModelBank;
import com.example.finance.R;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;

public class AdapterBank extends FirebaseRecyclerAdapter<ModelBank, AdapterBank.myviewHolder> {


    /**
     * Initialize a {@link RecyclerView.Adapter} that listens to a Firebase query. See
     * {@link FirebaseRecyclerOptions} for configuration options.
     *  @param options
     *
     */
    public AdapterBank(@NonNull FirebaseRecyclerOptions<ModelBank> options) {
        super(options);
    }

    @Override
    protected void onBindViewHolder(@NonNull myviewHolder holder, int position, @NonNull ModelBank model) {
        holder.accno.setText(model.getAccno());
        holder.mbal.setText(model.getMbal());
        holder.bname.setText(model.getBname());
        //Picasso.get().load(model.getBanklogo()).into(holder.BankImage);
    }

    @NonNull
    @Override
    public myviewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.bankacc, parent, false);
        return new myviewHolder(view);

    }

    class myviewHolder extends RecyclerView.ViewHolder {
        TextView accno,mbal,bname;
        ImageView BankImage;

        public myviewHolder(@NonNull View itemView) {
            super(itemView);

            accno = itemView.findViewById(R.id.accno);
            mbal = itemView.findViewById(R.id.mbal);
            bname = itemView.findViewById(R.id.bname);
          //  BankImage=itemView.findViewById(R.id.BankImage);
        }
    }




}