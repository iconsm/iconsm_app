package com.example.alaiapp;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        name = (EditText)findViewById(R.id.etName);
        password = (EditText)findViewById(R.id.etPassword);
        button = (Button) findViewById(R.id.btnSignIn);

        firebaseAuth = FirebaseAuth.getInstance();
       /* FirebaseUser user = firebaseAuth.getCurrentUser();

        if (user != null){
            finish();
            startActivity(new Intent(MainActivity.this,MainActivity.class));
        }else{
            Toast.makeText(this,"Erabiltzaile edo pasahitz okerra!",Toast.LENGTH_SHORT).show();
        }*/

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
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
                    //startActivity(new Intent(MainActivity.this,MenuActivity.class));

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
}
