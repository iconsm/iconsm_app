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
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.example.alaiapp.Model.Acto;
import com.example.alaiapp.Model.Barra;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.UUID;

public class Add_Acts extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener, AdapterView.OnItemSelectedListener{

    Spinner add_acts_fiestas;
    EditText editText_add_name_act;
    String text_add_acts_name;

    Acto a = new Acto();
    boolean entrance_from_add_act = true;

    FirebaseDatabase firebaseDatabase;
    DatabaseReference databaseReference;

    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_act);
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

        initializeFirebase();

        //Spinners
        add_acts_fiestas = (Spinner) findViewById(R.id.spinner_add_act);

        ArrayAdapter<CharSequence> adapter_add_acts = ArrayAdapter.createFromResource(Add_Acts.this, R.array.actos_de_fiestas, android.R.layout.simple_spinner_item);
        adapter_add_acts.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        add_acts_fiestas.setAdapter(adapter_add_acts);
        add_acts_fiestas.setOnItemSelectedListener(this);

        //Add to database
        editText_add_name_act = findViewById(R.id.editText_add_name_act);

        //Add act button
        Button buttonAddAct = (Button) findViewById(R.id.button_add_act);
        buttonAddAct.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                add_act_to_database();
            }
        });
    }

    //Initializes Firebase
    private void initializeFirebase(){
        firebaseDatabase = FirebaseDatabase.getInstance();
        databaseReference = firebaseDatabase.getReference();
    }

    //Add act button
    private void add_act_to_database(){
        entrance_from_add_act = true;

        String act_member_name = editText_add_name_act.getText().toString();
        if (act_member_name.equals("")) {
            Toast.makeText(this, "Hay que rellenar todos los datos!", Toast.LENGTH_LONG).show();
        }
        else {

            a.setName_acto(text_add_acts_name);
            a.setName_participante(act_member_name);
            a.setUid_acto(UUID.randomUUID().toString());

            databaseReference.child("Actos").child(a.getUid_acto()).setValue(a);
            Toast.makeText(Add_Acts.this, "Añadido!", Toast.LENGTH_SHORT).show();
            clean_editText();
        }
    }
    private void clean_editText(){
        editText_add_name_act.setText("");
    }
    //Add act button END

    //Spinner
    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        text_add_acts_name = parent.getItemAtPosition(position).toString();

    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }
    //Spinner_END

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
                Intent mail = new Intent(Add_Acts.this, Send_email.class);
                startActivity(mail);
                break;
            case R.id.nav_poll:
                Intent poll = new Intent(Add_Acts.this, Poll.class);
                startActivity(poll);
                break;
            case R.id.nav_bar:
                Intent bar = new Intent(Add_Acts.this, Bar.class);
                startActivity(bar);
                break;
            case R.id.nav_list:
                Intent list = new Intent(Add_Acts.this, Acts.class);
                startActivity(list);
                break;
            case R.id.nav_internal_schedule:
                Intent internal_schedule = new Intent(Add_Acts.this, Internal_schedule.class);
                startActivity(internal_schedule);
                break;
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
}
