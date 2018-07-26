package com.example.lenovo.safetyhelper;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class Menu extends AppCompatActivity {
    private Button mbuttonRoute;
    private Button mbuttonProtection;
    private Button mbuttonPerson;
    private Button mbuttonRecord;
    private Button mbuttonPolice;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu);
        mbuttonRoute = (Button) findViewById(R.id.buttonRoute);
        mbuttonProtection = (Button) findViewById(R.id.buttonProtection);
        mbuttonPerson = (Button) findViewById(R.id.buttonPerson);
        mbuttonRecord = (Button) findViewById(R.id.buttonRecord);
        mbuttonPolice = (Button) findViewById(R.id.buttonPolice);
        mbuttonRoute.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //attemptLogin();
                Intent intent = new Intent(Menu.this, Route.class);
                startActivity(intent);
            }
        });
        mbuttonProtection.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //attemptLogin();
                Intent intent = new Intent(Menu.this, Protection.class);
                startActivity(intent);
            }
        });
        mbuttonPerson.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //attemptLogin();
                Intent intent = new Intent(Menu.this, Person.class);
                startActivity(intent);
            }
        });
        mbuttonPolice.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //attemptLogin();
                Intent intent = new Intent(Menu.this, Police.class);
                startActivity(intent);
            }
        });
        mbuttonRecord.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //attemptLogin();
                Intent intent = new Intent(Menu.this, Record.class);
                startActivity(intent);
            }
        });
    }
}
