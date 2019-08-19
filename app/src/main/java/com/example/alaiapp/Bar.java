package com.example.alaiapp;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.Spinner;
import android.widget.Toast;

import com.example.alaiapp.Global_Variables.GlobalClass;
import com.example.alaiapp.Model.Barra;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class Bar extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener, AdapterView.OnItemSelectedListener {

    Spinner week_days_fiestas;
    String text_week_day_fiestas;

    private List<Barra> listBarra = new ArrayList<Barra>();
    ArrayAdapter<Barra> arrayAdapterBarra;
    FirebaseDatabase firebaseDatabase;
    DatabaseReference databaseReference;
    ListView listView_barras;

    Barra barraSelected;
    int barraPositionSelected = 99;

    String text_item = "Horario: ";
    String text_subitem = "Persona encargada: ";

    LinkedHashMap<String, String> nameAddresses = new LinkedHashMap<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bar);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);


        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        //START FROM HERE

        //Add bar button
        ImageButton buttonAddBar = (ImageButton) findViewById(R.id.imageButton);
        buttonAddBar.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    gotoactivity();
                }
            });

        //Spinner
        week_days_fiestas = (Spinner) findViewById(R.id.spinner_bar);

        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this, R.array.week_day_fiestas, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        week_days_fiestas.setAdapter(adapter);
        week_days_fiestas.setOnItemSelectedListener(this);

        //Database
        initializeFirebase();
        listView_barras = findViewById(R.id.listview_show_bars);

        //Selecting barra for erasing functionality
        listView_barras.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                barraPositionSelected= position;
            }
        });

        //Delete bar button
        ImageButton buttonDeleteBar = (ImageButton) findViewById(R.id.imageButton2);
        buttonDeleteBar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                deleteBar();
            }
        });
    }

    //Initializes Firebase
    private void initializeFirebase(){
        //FirebaseApp.initializeApp(this);
        firebaseDatabase = FirebaseDatabase.getInstance();
        databaseReference = firebaseDatabase.getReference();
    }

    //Shows Database
    private void showDatabase(){
        //Searching just in "Barras" child

        databaseReference.child("Barras").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                listBarra.clear();
                nameAddresses.clear();
                for (DataSnapshot objSnapshot : dataSnapshot.getChildren()){
                    Barra b = objSnapshot.getValue(Barra.class);
                    //Set condition for listing database value
                    if (b.getDay_barra().compareTo(text_week_day_fiestas) == 0) {
                        text_item = text_item.concat(b.getStart_time_barra());
                        text_item = text_item.concat(" - ");
                        text_item = text_item.concat(b.getEnd_time_barra());

                        text_subitem = text_subitem.concat(b.getName_barra());
                        nameAddresses.put(text_item, text_subitem);

                        text_item = "Horario: ";
                        text_subitem = "Persona encargada: ";

                        listBarra.add(b);
                        arrayAdapterBarra = new ArrayAdapter<Barra>(Bar.this, android.R.layout.simple_list_item_1, listBarra);
                    }
                    else{
                        //arrayAdapterBarra = new ArrayAdapter<Barra>(Bar.this, android.R.layout.simple_list_item_1, listBarra);
                        /*listView_barras.setAdapter(arrayAdapterBarra);*/
                    }
                }

                List<LinkedHashMap<String, String>> listItemAndSubitem = new ArrayList<>();
                SimpleAdapter adapter_item_subitem = new SimpleAdapter(Bar.this, listItemAndSubitem, R.layout.list_item,
                        new String[]{"First Line", "Second Line"},
                        new int[]{R.id.text1,R.id.text2});

                Iterator it = nameAddresses.entrySet().iterator();
                while (it.hasNext()){
                    LinkedHashMap<String, String> resultMap = new LinkedHashMap<>();
                    Map.Entry pair = (Map.Entry)it.next();
                    resultMap.put("First Line", pair.getKey().toString());
                    resultMap.put("Second Line", pair.getValue().toString());
                    listItemAndSubitem.add(resultMap);
                }
                listView_barras.setAdapter(adapter_item_subitem);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    //Add bar button
    private void gotoactivity(){
        GlobalClass globalClass = (GlobalClass) getApplicationContext();
        if (globalClass.getGlobal_user().equals("ikercondeperez@gmail.com")) {
            Intent add_bar = new Intent(Bar.this, Add_Bar.class);
            startActivity(add_bar);
        }
        else {
            Toast.makeText(this, "No tienes permiso para agregar barras", Toast.LENGTH_SHORT).show();
        }
    }
    //Add bar button END

    //Delete bar button
    private void deleteBar(){
        GlobalClass globalClass = (GlobalClass) getApplicationContext();
        if (globalClass.getGlobal_user().equals("ikercondeperez@gmail.com")) {
            //Show warning message
            if (barraPositionSelected != 99){
                barraSelected = arrayAdapterBarra.getItem(barraPositionSelected);
                AlertDialog.Builder builder = new AlertDialog.Builder(this);
                String txt_message = "Se va a borrar la barra de ";
                txt_message = txt_message.concat(barraSelected.getName_barra());
                txt_message = txt_message.concat(" que comienza a las ");
                txt_message = txt_message.concat(barraSelected.getStart_time_barra());
                txt_message = txt_message.concat(". ¿Quieres seguir?");

                builder.setMessage(txt_message)
                        .setPositiveButton("Sí", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                Barra b = new Barra();
                                b.setUid_barra(barraSelected.getUid_barra());
                                databaseReference.child("Barras").child(b.getUid_barra()).removeValue();
                                Toast.makeText(Bar.this, "Eliminado", Toast.LENGTH_SHORT).show();
                                barraSelected = null;
                            }
                        }).setNegativeButton("Cancelar", null);
                AlertDialog alert = builder.create();
                alert.show();
            }
        }
        else {
            Toast.makeText(this, "No tienes permiso para eliminar barras", Toast.LENGTH_SHORT).show();
        }
    }
    //Delete bar button END

    //Spinner
    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        text_week_day_fiestas = parent.getItemAtPosition(position).toString();
        showDatabase();
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }
    //Spinner END

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.home, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id=item.getItemId();
        switch (id) {

            case R.id.nav_mail:
                Intent mail = new Intent(Bar.this, Send_email.class);
                startActivity(mail);
                break;
            case R.id.nav_poll:
                Intent poll = new Intent(Bar.this, Poll.class);
                startActivity(poll);
                break;
            case R.id.nav_bar:
                Intent bar = new Intent(Bar.this, Bar.class);
                startActivity(bar);
                break;
            case R.id.nav_list:
                Intent list = new Intent(Bar.this, Acts.class);
                startActivity(list);
                break;
            case R.id.nav_internal_schedule:
                Intent internal_schedule = new Intent(Bar.this, Internal_schedule.class);
                startActivity(internal_schedule);
                break;
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
}
