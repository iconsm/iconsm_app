package com.example.alaiapp;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

public class NotMember extends AppCompatActivity {

    private EditText mEditTextMessage_notMember;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_not_member);

        /* Function code starts here */
        mEditTextMessage_notMember = findViewById(R.id.edit_text_message_not_member);

        Button buttonSend_notMember = findViewById(R.id.button_send_not_member);
        buttonSend_notMember.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sendMail_notMember();
            }
        });
    }

    private void sendMail_notMember() {
        String receiverList = "alaiaktaldeaapp@gmail.com";
        String[] receiver = {receiverList};
        String subject = "Solicitud de información";
        String message = mEditTextMessage_notMember.getText().toString();

        Intent intent = new Intent(Intent.ACTION_SEND);
        intent.putExtra(Intent.EXTRA_EMAIL, receiver);
        intent.putExtra(Intent.EXTRA_SUBJECT, subject);
        intent.putExtra(Intent.EXTRA_TEXT, message);

        /* This will take care of only opening email clients (Gmail, Hotmail...) */
        intent.setType("message/rfc822");

        startActivity(Intent.createChooser(intent, "Elige cómo mandar el correo"));
    }
}