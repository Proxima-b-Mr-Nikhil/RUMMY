package com.game.gamerummy;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.ObservableArrayList;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
public class MainActivity2 extends AppCompatActivity {

    Button host,client;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);

        host=findViewById(R.id.button);
        client=findViewById(R.id.button2);
        host.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent inten = new Intent(MainActivity2.this, Host.class);
                inten.putExtra("card","123456");
                inten.putExtra("player","1");
                startActivity(inten);
            }
        });
        client.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent inten = new Intent(MainActivity2.this, Client.class);
                inten.putExtra("card","123456");
                inten.putExtra("player","2");
                startActivity(inten);
            }
        });
    }
}