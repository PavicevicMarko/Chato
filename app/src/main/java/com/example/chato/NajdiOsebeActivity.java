package com.example.chato;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.database.Cursor;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.telephony.TelephonyManager;

import com.example.chato.User.SeznamUpoAdapter;
import com.example.chato.User.Uporabnik;
import com.example.chato.Utils.KlicnaConverter;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class NajdiOsebeActivity extends AppCompatActivity {

    private RecyclerView nListOseb;
    private RecyclerView.Adapter nAdapter;
    private RecyclerView.LayoutManager nListLayoutManager;

    ArrayList<Uporabnik> kontakti;   //bere v telefonu kontakte
    ArrayList<Uporabnik> uporabniki; //updata recycle view


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_najdi_osebo);

        kontakti = new ArrayList<>();
        uporabniki = new ArrayList<>();

        initializeRecyclerView();
        getSeznamOseb();


    }

    private void getSeznamOseb(){
        String klicna = getKlicna();
        Cursor phones = getContentResolver().query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI, null, null, null,null);
        while(phones.moveToNext()){
            String ime = phones.getString(phones.getColumnIndex(ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME));
            String fonska = phones.getString(phones.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER));

            fonska = fonska.replace(" ", "");       // polepšanje številk
            fonska = fonska.replace("-", "");
            fonska = fonska.replace("(", "");
            fonska = fonska.replace(")", "");

            if(!String.valueOf(fonska.charAt(0)).equals("+")){
                fonska = klicna + fonska;
            }

            Uporabnik kontakt = new Uporabnik("", ime,fonska);
            kontakti.add(kontakt);
            getUserDetails(kontakt);

        }
    }

    boolean checkExistence(Uporabnik uporabnik){
        boolean test= true;
        for(int i = 0 ; i<uporabniki.size();i++)
        {
            if(uporabniki.get(i).getFonska().equals(uporabnik.getFonska()))
            {
                test = false;
                break;
            }
        }
        return test;
    }

    private void getUserDetails(Uporabnik kontakt) {
        DatabaseReference userDB = FirebaseDatabase.getInstance().getReference().child("user");
        Query query;
        query = userDB.orderByChild("phone").equalTo(kontakt.getFonska());
        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot.exists()){
                    String fonska = "";
                    String ime = "";


                    for(DataSnapshot childSnapshot : snapshot.getChildren()){
                        if(childSnapshot.child("phone").getValue()!=null){
                            fonska = childSnapshot.child("phone").getValue().toString();
                        }
                        if(childSnapshot.child("name").getValue()!=null){
                            ime = childSnapshot.child("name").getValue().toString();
                        }

                        Uporabnik uporabnik = new Uporabnik(childSnapshot.getKey(), ime, fonska);

                        if (ime.equals(fonska)){
                            for (Uporabnik iterator : kontakti) {
                                if(iterator.getFonska().equals(uporabnik.getFonska())){
                                    uporabnik.setIme(iterator.getIme());
                                }
                            }
                        }

                        if(checkExistence(uporabnik)){
                            uporabniki.add(uporabnik);
                            nAdapter.notifyDataSetChanged();
                        }
                    }
                }

            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }
//    public ArrayList<Uporabnik> popraviSeznam (ArrayList<Uporabnik> uporabniki){
//
//        LinkedHashSet<Uporabnik> set = new LinkedHashSet<>(uporabniki);
//
//        Set<Uporabnik> tempList = new HashSet<>();
//        Set<Uporabnik> finalSet = new HashSet<>();
//
//        for (Uporabnik uporabnik : uporabniki){
//            if(tempList.contains(uporabnik)){
//                finalSet.add(uporabnik);
//            }
//            else {
//                tempList.add(uporabnik);
//            }
//        }
//        ArrayList<Uporabnik> finalList = new ArrayList<Uporabnik>();
//        finalList.addAll(tempList);
//        return finalList;
//        return new ArrayList<>(set);
//    }



    private String getKlicna(){
        String iso = "";
        TelephonyManager telephonyManager = (TelephonyManager) getApplicationContext().getSystemService(getApplicationContext().TELEPHONY_SERVICE);
        if(telephonyManager.getNetworkCountryIso()!=null){
            if(!telephonyManager.getNetworkCountryIso().equals("")){
                iso = telephonyManager.getNetworkCountryIso();
            }
        }

        return KlicnaConverter.getPhone(iso);
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