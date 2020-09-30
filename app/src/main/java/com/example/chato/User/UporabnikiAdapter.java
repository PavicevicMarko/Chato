package com.example.chato.User;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.chato.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;

public class UporabnikiAdapter extends RecyclerView.Adapter<UporabnikiAdapter.UpoViewHolder> {

    ArrayList<Uporabnik> uporabniki;
    public UporabnikiAdapter(ArrayList<Uporabnik> uporabniki){
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
    public void onBindViewHolder(@NonNull final UpoViewHolder holder, final int position) {
        holder.ime.setText(uporabniki.get(position).getIme());
        holder.fonska.setText(uporabniki.get(position).getFonska());

        holder.add.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                 uporabniki.get(holder.getAdapterPosition()).setSelected(isChecked);
            }
        });


        holder.mLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String key = FirebaseDatabase.getInstance().getReference().child("chat").push().getKey();   //vrne unique ID od pogovora

                FirebaseDatabase.getInstance().getReference().child("user").child(FirebaseAuth.getInstance().getUid()).child("chat").child(key).setValue(true);
                FirebaseDatabase.getInstance().getReference().child("user").child(uporabniki.get(position).getUid()).child("chat").child(key).setValue(true);

            }
        });
     }

    @Override
    public int getItemCount() {
        return uporabniki.size();
    }


    public class UpoViewHolder extends RecyclerView.ViewHolder{
        public TextView ime, fonska;
        public LinearLayout mLayout;
        CheckBox add;
        public UpoViewHolder(View view){
            super(view);
            ime = view.findViewById(R.id.ime);
            fonska = view.findViewById(R.id.fonska);
            mLayout = view.findViewById(R.id.layout);
            add= view.findViewById(R.id.add);
        }
    }
}
