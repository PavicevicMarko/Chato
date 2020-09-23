package com.example.chato;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.database.Cursor;
import android.os.Bundle;
import android.provider.ContactsContract;
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
        getSeznamOseb();
    }

    private void getSeznamOseb(){
        Cursor phones = getContentResolver().query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI, null, null, null,null);
        assert phones != null;
        while(phones.moveToNext()){
            String ime = phones.getString(phones.getColumnIndex(ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME));
            String fonska = phones.getString(phones.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER));

            Uporabnik kontakt = new Uporabnik(ime,fonska);
            uporabniki.add(kontakt);
            nAdapter.notifyDataSetChanged();
        }
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