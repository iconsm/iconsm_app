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

import com.example.alaiapp.Model.Barra;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.UUID;

public class Add_Bar extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener, AdapterView.OnItemSelectedListener{

    Spinner week_days_fiestas, bar_start_time, bar_end_time;
    EditText editText_add_name;
    String text_week_day_fiestas, text_bar_start_time, text_bar_end_time;
    String Uid_barra = "";

    Barra b = new Barra();
    Barra check_bar = new Barra();
    boolean flag_check_bar = false;
    boolean entrance_from_add_bar = true;

    FirebaseDatabase firebaseDatabase;
    DatabaseReference databaseReference;

    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add__bar);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        initializeFirebase();

        //Spinners
        week_days_fiestas = (Spinner) findViewById(R.id.spinner_add_bar_day);

        ArrayAdapter<CharSequence> adapter_week = ArrayAdapter.createFromResource(this, R.array.week_day_fiestas, android.R.layout.simple_spinner_item);
        adapter_week.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        week_days_fiestas.setAdapter(adapter_week);
        week_days_fiestas.setOnItemSelectedListener(this);

        bar_start_time = (Spinner) findViewById(R.id.spinner_add_bar_start_time);

        ArrayAdapter<CharSequence> adapter_start_time = ArrayAdapter.createFromResource(this, R.array.bar_time, android.R.layout.simple_spinner_item);
        adapter_start_time.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        bar_start_time.setAdapter(adapter_start_time);
        bar_start_time.setOnItemSelectedListener(this);

        bar_end_time = (Spinner) findViewById(R.id.spinner_add_bar_end_time);

        ArrayAdapter<CharSequence> adapter_end_time = ArrayAdapter.createFromResource(this, R.array.bar_time, android.R.layout.simple_spinner_item);
        adapter_end_time.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        bar_end_time.setAdapter(adapter_end_time);
        bar_end_time.setOnItemSelectedListener(this);

        //Add to database
        editText_add_name = findViewById(R.id.editText_add_name);

        //Add bar button
        Button buttonAddBar = (Button) findViewById(R.id.button_add_bar);
        buttonAddBar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                add_bar_to_database();
            }
        });
    }

    //Initializes Firebase
    private void initializeFirebase(){
        //FirebaseApp.initializeApp(this);
        firebaseDatabase = FirebaseDatabase.getInstance();
        databaseReference = firebaseDatabase.getReference();
    }

    //Add bar button
    private void add_bar_to_database(){
        entrance_from_add_bar = true;

        String bar_name = editText_add_name.getText().toString();
        if (bar_name.equals("")) {
            Toast.makeText(this, "Hay que rellenar todos los datos!", Toast.LENGTH_LONG).show();
        }
        else if(text_bar_start_time.equals(text_bar_end_time)){
            Toast.makeText(this, "El horario de inicio y fin de la barra no pueden ser iguales!", Toast.LENGTH_LONG).show();
        }
        else {

            b.setDay_barra(text_week_day_fiestas);
            b.setName_barra(bar_name);
            b.setStart_time_barra(text_bar_start_time);
            b.setEnd_time_barra(text_bar_end_time);
            //Codifying bar
            switch (b.getDay_barra()){
                case "Viernes, 11 octubre":
                    Uid_barra = Uid_barra.concat("0");
                    break;
                case "Sábado, 12 octubre":
                    Uid_barra = Uid_barra.concat("1");
                    break;
                case "Domingo, 13 octubre":
                    Uid_barra = Uid_barra.concat("2");
                    break;
                case "Lunes, 14 octubre":
                    Uid_barra = Uid_barra.concat("3");
                    break;
                case "Martes, 15 octubre":
                    Uid_barra = Uid_barra.concat("4");
                    break;
                case "Miércoles, 16 octubre":
                    Uid_barra = Uid_barra.concat("5");
                    break;
                case "Jueves, 17 octubre":
                    Uid_barra = Uid_barra.concat("6");
                    break;
                case "Viernes, 18 octubre":
                    Uid_barra = Uid_barra.concat("7");
                    break;
                case "Sábado, 19 octubre":
                    Uid_barra = Uid_barra.concat("8");
                    break;
                case "Domingo, 20 octubre":
                    Uid_barra = Uid_barra.concat("9");
                    break;
            }
            switch (b.getStart_time_barra()){
                case "10:00":
                    Uid_barra = Uid_barra.concat("10");
                    break;
                case "10:30":
                    Uid_barra = Uid_barra.concat("11");
                    break;
                case "11:00":
                    Uid_barra = Uid_barra.concat("12");
                    break;
                case "11:30":
                    Uid_barra = Uid_barra.concat("13");
                    break;
                case "12:00":
                    Uid_barra = Uid_barra.concat("14");
                    break;
                case "12:30":
                    Uid_barra = Uid_barra.concat("15");
                    break;
                case "13:00":
                    Uid_barra = Uid_barra.concat("16");
                    break;
                case "13:30":
                    Uid_barra = Uid_barra.concat("17");
                    break;
                case "14:00":
                    Uid_barra = Uid_barra.concat("18");
                    break;
                case "14:30":
                    Uid_barra = Uid_barra.concat("19");
                    break;
                case "15:00":
                    Uid_barra = Uid_barra.concat("20");
                    break;
                case "15:30":
                    Uid_barra = Uid_barra.concat("21");
                    break;
                case "16:00":
                    Uid_barra = Uid_barra.concat("22");
                    break;
                case "16:30":
                    Uid_barra = Uid_barra.concat("23");
                    break;
                case "17:00":
                    Uid_barra = Uid_barra.concat("24");
                    break;
                case "17:30":
                    Uid_barra = Uid_barra.concat("25");
                    break;
                case "18:00":
                    Uid_barra = Uid_barra.concat("26");
                    break;
                case "18:30":
                    Uid_barra = Uid_barra.concat("27");
                    break;
                case "19:00":
                    Uid_barra = Uid_barra.concat("28");
                    break;
                case "19:30":
                    Uid_barra = Uid_barra.concat("29");
                    break;
                case "20:00":
                    Uid_barra = Uid_barra.concat("30");
                    break;
                case "20:30":
                    Uid_barra = Uid_barra.concat("31");
                    break;
                case "21:00":
                    Uid_barra = Uid_barra.concat("32");
                    break;
                case "21:30":
                    Uid_barra = Uid_barra.concat("33");
                    break;
                case "22:00":
                    Uid_barra = Uid_barra.concat("34");
                    break;
                case "22:30":
                    Uid_barra = Uid_barra.concat("35");
                    break;
                case "23:00":
                    Uid_barra = Uid_barra.concat("36");
                    break;
                case "23:30":
                    Uid_barra = Uid_barra.concat("37");
                    break;
                case "00:00":
                    Uid_barra = Uid_barra.concat("38");
                    break;
                case "00:30":
                    Uid_barra = Uid_barra.concat("39");
                    break;
                case "01:00":
                    Uid_barra = Uid_barra.concat("40");
                    break;
                case "01:30":
                    Uid_barra = Uid_barra.concat("41");
                    break;
                case "02:00":
                    Uid_barra = Uid_barra.concat("42");
                    break;
                case "02:30":
                    Uid_barra = Uid_barra.concat("43");
                    break;
                case "03:00":
                    Uid_barra = Uid_barra.concat("44");
                    break;
                case "03:30":
                    Uid_barra = Uid_barra.concat("45");
                    break;
                case "04:00":
                    Uid_barra = Uid_barra.concat("46");
                    break;
                case "04:30":
                    Uid_barra = Uid_barra.concat("47");
                    break;
                case "05:00":
                    Uid_barra = Uid_barra.concat("48");
                    break;
                case "05:30":
                    Uid_barra = Uid_barra.concat("49");
                    break;
                case "06:00":
                    Uid_barra = Uid_barra.concat("50");
                    break;
                case "06:30":
                    Uid_barra = Uid_barra.concat("51");
                    break;
                case "07:00":
                    Uid_barra = Uid_barra.concat("52");
                    break;
                case "07:30":
                    Uid_barra = Uid_barra.concat("53");
                    break;
                case "08:00":
                    Uid_barra = Uid_barra.concat("54");
                    break;
                case "08:30":
                    Uid_barra = Uid_barra.concat("55");
                    break;
                case "09:00":
                    Uid_barra = Uid_barra.concat("56");
                    break;
                case "09:30":
                    Uid_barra = Uid_barra.concat("57");
                    break;
            }
            b.setUid_barra(Uid_barra);

            //Check if Uid_barra is already used
            databaseReference.child("Barras").addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    if (entrance_from_add_bar) {
                        //Do for loop to check if Uid is already used
                        for (DataSnapshot objSnapshot : dataSnapshot.getChildren()) {
                            check_bar = objSnapshot.getValue(Barra.class);
                            if (check_bar.getUid_barra().compareTo(b.getUid_barra()) == 0) {
                                AlertDialog.Builder builder = new AlertDialog.Builder(Add_Bar.this);
                                String txt_message = "Se va a sobreescribir la barra de ";
                                txt_message = txt_message.concat(check_bar.getName_barra());
                                txt_message = txt_message.concat(" que comienza a las ");
                                txt_message = txt_message.concat(check_bar.getStart_time_barra());
                                txt_message = txt_message.concat(". ¿Quieres seguir?");

                                builder.setMessage(txt_message)
                                        .setPositiveButton("Sí", new DialogInterface.OnClickListener() {
                                            @Override
                                            public void onClick(DialogInterface dialog, int which) {
                                                databaseReference.child("Barras").child(b.getUid_barra()).setValue(b);
                                                Toast.makeText(Add_Bar.this, "Añadida!", Toast.LENGTH_SHORT).show();
                                                clean_editText();
                                            }
                                        }).setNegativeButton("Cancelar", null);
                                AlertDialog alert = builder.create();
                                alert.show();
                                entrance_from_add_bar = false;
                                return;
                            }
                        }
                        databaseReference.child("Barras").child(b.getUid_barra()).setValue(b);
                        Toast.makeText(Add_Bar.this, "Añadida!", Toast.LENGTH_SHORT).show();
                        clean_editText();
                        entrance_from_add_bar = false;
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });
            
            Uid_barra = "";
        }
    }
    private void clean_editText(){
        editText_add_name.setText("");
    }
    //Add bar button END

    //Spinner
    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        switch (parent.getId()){
            case R.id.spinner_add_bar_day:
                text_week_day_fiestas = parent.getItemAtPosition(position).toString();
                break;
            case R.id.spinner_add_bar_start_time:
                text_bar_start_time = parent.getItemAtPosition(position).toString();
                break;
            case R.id.spinner_add_bar_end_time:
                text_bar_end_time = parent.getItemAtPosition(position).toString();
                break;
        }

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
                Intent mail = new Intent(Add_Bar.this, Send_email.class);
                startActivity(mail);
                break;
            case R.id.nav_poll:
                Intent poll = new Intent(Add_Bar.this, Poll.class);
                startActivity(poll);
                break;
            case R.id.nav_bar:
                Intent bar = new Intent(Add_Bar.this, Bar.class);
                startActivity(bar);
                break;
            case R.id.nav_list:
                Intent list = new Intent(Add_Bar.this, Acts.class);
                startActivity(list);
                break;
            case R.id.nav_internal_schedule:
                Intent internal_schedule = new Intent(Add_Bar.this, Internal_schedule.class);
                startActivity(internal_schedule);
                break;
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
}
