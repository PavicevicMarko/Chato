package com.example.chato;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class SeznamUpoAdapter extends RecyclerView.Adapter<SeznamUpoAdapter.UpoViewHolder> {

    ArrayList<Uporabnik> uporabniki;
    public SeznamUpoAdapter(ArrayList<Uporabnik> uporabniki){
        this.uporabniki = uporabniki;
    }

    @NonNull
    @Override
    public UpoViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) { //
        View layoutView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_uporabnik,null,false);
        RecyclerView.LayoutParams lp = new RecyclerView.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        layoutView.setLayoutParams(lp);

        UpoViewHolder xyz = new UpoViewHolder(layoutView);
        return xyz;
    }

    @Override
    public void onBindViewHolder(@NonNull UpoViewHolder holder, int position) {
        holder.ime.setText(uporabniki.get(position).getIme());
        holder.fonska.setText(uporabniki.get(position).getFonska());

     }

    @Override
    public int getItemCount() {
        return uporabniki.size();
    }


    public class UpoViewHolder extends RecyclerView.ViewHolder{
        public TextView ime, fonska;
        public UpoViewHolder(View view){
            super(view);
            ime = view.findViewById(R.id.ime);
            fonska = view.findViewById(R.id.fonska);
        }
    }
}
