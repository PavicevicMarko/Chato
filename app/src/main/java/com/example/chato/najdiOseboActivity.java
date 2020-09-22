package com.example.chato;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.widget.LinearLayout;

import java.util.ArrayList;

public class najdiOseboActivity extends AppCompatActivity {

    private RecyclerView nListOseb;
    private RecyclerView.Adapter nAdapter;
    private RecyclerView.LayoutManager nListLayoutManager;

    ArrayList<Uporabnik> uporabniki;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_najdi_osebo);

        initializeRecyclerView();
    }

    private void initializeRecyclerView() {

    nListOseb = findViewById(R.id.najdiOsebo);
    nListOseb.setNestedScrollingEnabled(false);
    nListOseb.setHasFixedSize(false);
    nListLayoutManager = new LinearLayoutManager(getApplicationContext(), RecyclerView.VERTICAL, false);
    nListOseb.setLayoutManager(nListLayoutManager);
    nAdapter = new SeznamUpoAdapter(uporabniki);
    nListOseb.setAdapter(nAdapter);
    }
}