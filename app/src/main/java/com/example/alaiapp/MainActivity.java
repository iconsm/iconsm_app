package com.example.alaiapp;

import android.content.Intent;
import android.content.SharedPreferences;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Toast;

import com.example.alaiapp.Global_Variables.GlobalClass;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class MainActivity extends AppCompatActivity {

    private EditText name;
    private EditText password;
    private Button button;
    private FirebaseAuth firebaseAuth;

    private CheckBox mCheckBoxRememberMe;
    private SharedPreferences mPrefs;
    private static final String PREFS_NAME = "PrefsFile";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        name = (EditText)findViewById(R.id.etName);
        password = (EditText)findViewById(R.id.etPassword);
        button = (Button) findViewById(R.id.btnSignIn);

        mCheckBoxRememberMe = (CheckBox) findViewById(R.id.checkBoxRememberMe);
        mPrefs = getSharedPreferences(PREFS_NAME, MODE_PRIVATE);

        getPreferencesData();


        firebaseAuth = FirebaseAuth.getInstance();
       /* FirebaseUser user = firebaseAuth.getCurrentUser();

        if (user != null){
            finish();
            startActivity(new Intent(MainActivity.this,MainActivity.class));
        }else{
            Toast.makeText(this,"Erabiltzaile edo pasahitz okerra!",Toast.LENGTH_SHORT).show();
        }*/
        GlobalClass globalClass = (GlobalClass) getApplicationContext();
        globalClass.setGlobal_user(name.getText().toString());

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                saveRememberMeData();
                validate(name.getText().toString(),password.getText().toString());
            }
        });
    }

    private void validate (String userName, String userPassword){

        firebaseAuth.signInWithEmailAndPassword(userName,userPassword).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if(task.isSuccessful()){
                    Toast.makeText(MainActivity.this,"Login exitoso!",Toast.LENGTH_SHORT).show();
                    //finish();
                    startActivity(new Intent(MainActivity.this,Home.class));

                }else{
                    Toast.makeText(MainActivity.this,"Login fallido!",Toast.LENGTH_SHORT).show();
                }
            }
        });

        /*
        if((userName.equals("admin")) && (userPassword.equals("123"))){
            Intent intent = new Intent (MainActivity.this,MenuActivity.class);
            startActivity(intent);

        }else{
            Toast.makeText(this,"Erabiltzaile edo pasahitz okerra!",Toast.LENGTH_SHORT).show();
        }*/
    }

    private void getPreferencesData(){
        SharedPreferences sp = getSharedPreferences(PREFS_NAME, MODE_PRIVATE);
        if(sp.contains("pref_name")){
            String u = sp.getString("pref_name","not found");
            name.setText(u.toString());
        }
        if(sp.contains("pref_password")){
            String p = sp.getString("pref_password","not found");
            password.setText(p.toString());
        }
        if(sp.contains("pref_check")){
            Boolean ch = sp.getBoolean("pref_check",false);
            mCheckBoxRememberMe.setChecked(ch);
        }
    }

    private void saveRememberMeData(){
        if (mCheckBoxRememberMe.isChecked()){
            Boolean boolIsChecked = mCheckBoxRememberMe.isChecked();
            SharedPreferences.Editor editor = mPrefs.edit();
            editor.putString("pref_name", name.getText().toString());
            editor.putString("pref_password", password.getText().toString());
            editor.putBoolean("pref_check", boolIsChecked);
            editor.apply();
        } else {
            mPrefs.edit().clear().apply();
        }
    }
}
