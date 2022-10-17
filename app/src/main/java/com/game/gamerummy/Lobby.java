package com.game.gamerummy;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.admin.SystemUpdateInfo;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class Lobby extends AppCompatActivity {

    String status,id,name,roomno,player;
    TextView cardno,name1,name2,status1,status2,msg;
    ImageView share;
    Button strt;
    DatabaseReference reference= FirebaseDatabase.getInstance().getReference("rummy");
    DatabaseReference ref;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lobby);

        name1=findViewById(R.id.name1);
        name2=findViewById(R.id.name2);
        status1=findViewById(R.id.splayer1);
        status2=findViewById(R.id.splayer2);
        cardno=findViewById(R.id.cardno);
        share=findViewById(R.id.share);
        strt=findViewById(R.id.startbtn);
        msg=findViewById(R.id.msg);
        Intent intent=getIntent();
        roomno= intent.getStringExtra("card");
        player= intent.getStringExtra("player");
        if (player.equals("2")){
            msg.setVisibility(View.VISIBLE);
            strt.setVisibility(View.GONE);
        }else {
            msg.setVisibility(View.GONE);
            strt.setVisibility(View.VISIBLE);
        }

       cardno.setText(roomno);

       status="online";

        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
         id = preferences.getString("id","");
        name = preferences.getString("name","");
        if (id!=null&&roomno!=null) {
            if (player.equals("1")) {
                ref = reference.child(roomno);
                ref.child("host").setValue(id);
                ref.child("nameplayer1").setValue(name);
                ref.child("idplayer1").setValue(id);
                ref.child("statusplayer1").setValue(status);
            }
            if (player.equals("2")) {
                ref = reference.child(roomno);
                ref.child("nameplayer2").setValue(name);
                ref.child("idplayer2").setValue(id);
                ref.child("statusplayer2").setValue(status);
            }
        }
        ref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()){
                name1.setText((CharSequence) snapshot.child("nameplayer1").getValue());
                status1.setText((CharSequence) snapshot.child("statusplayer1").getValue());

                name2.setText((CharSequence) snapshot.child("nameplayer2").getValue());
                status2.setText((CharSequence) snapshot.child("statusplayer2").getValue());

                if (name2.getText().toString().equals("")){
                    name2.setText("player-2");
                    status2.setText("offline");
                }
            }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
        ref.child("start").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
               if (snapshot.exists()){
                   if (player.equals("2")){
                   Intent inten = new Intent(Lobby.this, Client.class);
                   inten.putExtra("card",roomno);
                   inten.putExtra("player",player);
                   startActivity(inten);
                   }
               }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
       share.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View view) {
               Intent intent = new Intent(android.content.Intent.ACTION_SEND);
               String shareBody = "Hey! Let's Play Rummy together join game using :"+" "+roomno;
               intent.setType("text/plain");
               intent.putExtra(android.content.Intent.EXTRA_TEXT, shareBody);
               startActivity(Intent.createChooser(intent,"Share via"));
           }
       });

       strt.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View view) {
               String a=status1.getText().toString();
               String b=status2.getText().toString();
              if (a.equals("online")&&b.equals("online")){
                   Intent inten = new Intent(Lobby.this, Host.class);
                   inten.putExtra("card",roomno);
                  inten.putExtra("player",player);
                   startActivity(inten);
               }else {
                   Toast.makeText(Lobby.this,"player is offline",Toast.LENGTH_LONG).show();
              }
           }
       });
    }

    @Override
    protected void onResume() {
        super.onResume();
        status="online";
        if (player.equals("1")) {
        ref.child("statusplayer1").setValue(status);
        }
        if (player.equals("2")) {
            ref.child("statusplayer2").setValue(status);
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        status="offline";
        if (player.equals("1")) {
            ref.child("statusplayer1").setValue(status);
        }
        if (player.equals("2")) {
            ref.child("statusplayer2").setValue(status);
        }
    }
}