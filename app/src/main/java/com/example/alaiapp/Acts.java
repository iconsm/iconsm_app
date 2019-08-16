package com.example.alaiapp;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
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
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.Toast;

import com.example.alaiapp.Global_Variables.GlobalClass;
import com.example.alaiapp.Model.Acto;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class Acts extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener, AdapterView.OnItemSelectedListener {

    Spinner acts_fiestas;
    String text_acts_fiestas;

    ListView listView_actos;
    FirebaseDatabase firebaseDatabase;
    DatabaseReference databaseReference;

    private List<Acto> listActo = new ArrayList<Acto>();
    ArrayAdapter<Acto> arrayAdapterActo;

    Acto actoSelected;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_acts);
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

        //Add act button
        ImageButton buttonAddAct = (ImageButton) findViewById(R.id.imageButton_act);
        buttonAddAct.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                gotoactivity();
            }
        });

        //Spinner
        acts_fiestas = (Spinner) findViewById(R.id.spinner_act);

        ArrayAdapter<CharSequence> adapter_acts = ArrayAdapter.createFromResource(Acts.this, R.array.actos_de_fiestas, android.R.layout.simple_spinner_item);
        adapter_acts.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        acts_fiestas.setAdapter(adapter_acts);
        acts_fiestas.setOnItemSelectedListener(this);

        //Database
        initializeFirebase();
        listView_actos = findViewById(R.id.listview_show_acts);

        //Selecting member for erasing functionality
        listView_actos.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                actoSelected = (Acto) parent.getItemAtPosition(position);
            }
        });

        //Delete member button
        ImageButton buttonDeleteAct = (ImageButton) findViewById(R.id.imageButton_act_delete);
        buttonDeleteAct.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                deleteActMember();
            }
        });
    }

    //Add act button
    private void gotoactivity(){
        GlobalClass globalClass = (GlobalClass) getApplicationContext();
        if (globalClass.getGlobal_user().equals("ikercondeperez@gmail.com")) {
            Intent add_act = new Intent(Acts.this, Add_Acts.class);
            startActivity(add_act);
        }
        else {
            Toast.makeText(this, "No tienes permiso para agregar participantes", Toast.LENGTH_SHORT).show();
        }
    }
    //Add act button END

    //Initializes Firebase
    private void initializeFirebase(){
        firebaseDatabase = FirebaseDatabase.getInstance();
        databaseReference = firebaseDatabase.getReference();
    }

    //Shows Database
    private void showDatabase(){
        //Searching just in "Actos" child
        databaseReference.child("Actos").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                listActo.clear();
                for (DataSnapshot objSnapshot : dataSnapshot.getChildren()){
                    Acto a = objSnapshot.getValue(Acto.class);
                    //Set condition for listing database value
                    if (a.getName_acto().compareTo(text_acts_fiestas) == 0) {
                        listActo.add(a);

                        arrayAdapterActo = new ArrayAdapter<Acto>(Acts.this, android.R.layout.simple_list_item_1, listActo);
                        listView_actos.setAdapter(arrayAdapterActo);
                    }
                    else{
                        arrayAdapterActo = new ArrayAdapter<Acto>(Acts.this, android.R.layout.simple_list_item_1, listActo);
                        listView_actos.setAdapter(arrayAdapterActo);
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    //Delete member button
    private void deleteActMember(){
        GlobalClass globalClass = (GlobalClass) getApplicationContext();
        if (globalClass.getGlobal_user().equals("ikercondeperez@gmail.com")) {
            //Show warning message
            if (actoSelected != null){
                AlertDialog.Builder builder = new AlertDialog.Builder(this);
                String txt_message = "Se va a borrar al participante ";
                txt_message = txt_message.concat(actoSelected.getName_participante());
                txt_message = txt_message.concat(" del acto ");
                txt_message = txt_message.concat(actoSelected.getName_acto());
                txt_message = txt_message.concat(". ¿Quieres seguir?");

                builder.setMessage(txt_message)
                        .setPositiveButton("Sí", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                Acto a = new Acto();
                                a.setUid_acto(actoSelected.getUid_acto());
                                databaseReference.child("Actos").child(a.getUid_acto()).removeValue();
                                Toast.makeText(Acts.this, "Eliminado", Toast.LENGTH_SHORT).show();
                                actoSelected = null;
                            }
                        }).setNegativeButton("Cancelar", null);
                AlertDialog alert = builder.create();
                alert.show();
            }
        }
        else {
            Toast.makeText(this, "No tienes permiso para eliminar participantes", Toast.LENGTH_SHORT).show();
        }
    }
    //Delete member button END

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
                Intent mail = new Intent(Acts.this, Send_email.class);
                startActivity(mail);
                break;
            case R.id.nav_poll:
                Intent poll = new Intent(Acts.this, Poll.class);
                startActivity(poll);
                break;
            case R.id.nav_bar:
                Intent bar = new Intent(Acts.this, Bar.class);
                startActivity(bar);
                break;
            case R.id.nav_list:
                Intent list = new Intent(Acts.this, Acts.class);
                startActivity(list);
                break;
            case R.id.nav_internal_schedule:
                Intent internal_schedule = new Intent(Acts.this, Internal_schedule.class);
                startActivity(internal_schedule);
                break;
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    //Spinner
    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        text_acts_fiestas = parent.getItemAtPosition(position).toString();
        showDatabase();
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }
    //ENDs spinner
}
