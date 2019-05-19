package com.example.alaiapp;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.remoteconfig.FirebaseRemoteConfig;

public class Poll extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    FirebaseRemoteConfig mFirebaseRemoteConfig;
    TextView textView_poll_subject;
    TextView textView_option1;
    TextView textView_option2;
    TextView textView_option3;
    TextView textView_option4;

    RadioGroup radioGroup;
    RadioButton radioButton;
    String receiverList = "alaiaktaldeaapp@gmail.com";
    String[] receiver = {receiverList};
    String message = "Añade aquí algún comentario extra sobre el tema encuestado.";
    String subject;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_poll);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        /* Function code starts here */
        mFirebaseRemoteConfig = FirebaseRemoteConfig.getInstance();
        textView_poll_subject = (TextView) findViewById(R.id.textViewPollSubject);
        textView_option1 = (TextView) findViewById(R.id.textViewPoll_Option1);
        textView_option2 = (TextView) findViewById(R.id.textViewPoll_Option2);
        textView_option3 = (TextView) findViewById(R.id.textViewPoll_Option3);
        textView_option4 = (TextView) findViewById(R.id.textViewPoll_Option4);

        long cacheExpiration = 0;
        mFirebaseRemoteConfig.fetch(cacheExpiration)
                .addOnCompleteListener(this, new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {
                            Toast.makeText(Poll.this, "Encuesta actualizada",
                                    Toast.LENGTH_SHORT).show();

                            // After config data is successfully fetched, it must be activated before newly fetched
                            // values are returned.
                            mFirebaseRemoteConfig.activateFetched();
                        } else {
                            Toast.makeText(Poll.this, "Actualización de encuesta fallida",
                                    Toast.LENGTH_SHORT).show();
                        }
                        displayPollMessages();
                    }
                });

        radioGroup = findViewById(R.id.radio_group);
        Button buttonSendPoll = findViewById(R.id.button_send_poll);
        buttonSendPoll.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int radioId = radioGroup.getCheckedRadioButtonId();
                radioButton = findViewById(radioId);
                subject = radioButton.getText().toString();
                sendMail();
            }
        });
    }

    private void displayPollMessages() {
        textView_poll_subject.setText(mFirebaseRemoteConfig.getString("poll_subject"));
        textView_option1.setText(mFirebaseRemoteConfig.getString("poll_option1"));
        textView_option2.setText(mFirebaseRemoteConfig.getString("poll_option2"));
        textView_option3.setText(mFirebaseRemoteConfig.getString("poll_option3"));
        textView_option4.setText(mFirebaseRemoteConfig.getString("poll_option4"));

    }

    private void sendMail(){

        Intent intent = new Intent(Intent.ACTION_SEND);
        intent.putExtra(Intent.EXTRA_EMAIL,receiver);
        intent.putExtra(Intent.EXTRA_SUBJECT, subject);
        intent.putExtra(Intent.EXTRA_TEXT, message);

        /* This will take care of only opening email clients (Gmail, Hotmail...) */
        intent.setType("message/rfc822");

        startActivity(Intent.createChooser(intent, "Elige cómo mandar el correo"));
    }

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
                Intent mail = new Intent(Poll.this, Send_email.class);
                startActivity(mail);
                break;
            case R.id.nav_poll:
                Intent poll = new Intent(Poll.this, Poll.class);
                startActivity(poll);
                break;
            case R.id.nav_bar:
                Intent bar = new Intent(Poll.this, Bar.class);
                startActivity(bar);
                break;
            case R.id.nav_list:
                Intent list = new Intent(Poll.this, List.class);
                startActivity(list);
                break;
            case R.id.nav_internal_schedule:
                Intent internal_schedule = new Intent(Poll.this, Internal_schedule.class);
                startActivity(internal_schedule);
                break;
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
}
