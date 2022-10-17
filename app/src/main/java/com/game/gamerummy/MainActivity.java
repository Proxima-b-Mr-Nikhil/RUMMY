package com.game.gamerummy;

import android.content.DialogInterface;
import android.content.Intent;

import android.content.SharedPreferences;
import android.graphics.BlurMaskFilter;
import android.os.Build;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.auth.api.signin.GoogleSignInResult;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.Random;

public class MainActivity extends AppCompatActivity {
    TextView code;
    EditText code1;
    LinearLayout lcode;
    ImageButton arrow,arrow1,nav;
    String b;
    DatabaseReference reference= FirebaseDatabase.getInstance().getReference("rummy");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        nav=findViewById(R.id.nav);
        nav.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, ProfileActivity.class);
                startActivity(intent);
            }
        });




        Button but = (Button) findViewById(R.id.host);
        if (but != null) {
            but.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                        final BottomSheetDialog dialog = new BottomSheetDialog(MainActivity.this);
                        dialog.setContentView(R.layout.bottomsheet);
                    code=dialog.findViewById(R.id.code);
                    lcode=dialog.findViewById(R.id.lcode);
                    arrow=dialog.findViewById(R.id.arrow);

                    float radius = code.getTextSize();
                    BlurMaskFilter filter = new BlurMaskFilter(radius, BlurMaskFilter.Blur.NORMAL);
                    code.getPaint().setMaskFilter(filter);
                    lcode.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {

                            code.getPaint().setMaskFilter(null);
                            int a=  new Random().nextInt(999999);
                            b=String.format("%06d", a);
                            code.setText(String.valueOf(b));
                            lcode.setClickable(false);
                            arrow.setVisibility(View.VISIBLE);

                        }
                    });
                    arrow.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            Intent intent = new Intent(MainActivity.this, Lobby.class);
                            intent.putExtra("card",b);
                            intent.putExtra("player","1");
                            startActivity(intent);
                        }
                    });


                        dialog.show();

                }
            });
        }
        Button bu = (Button) findViewById(R.id.client);
        if (bu != null) {
            bu.setOnClickListener(v -> {
                SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
                String a = preferences.getString("id","");

                final BottomSheetDialog dialog = new BottomSheetDialog(MainActivity.this);
                dialog.setContentView(R.layout.bottomsheet1);
                code1=dialog.findViewById(R.id.code1);
                arrow1=dialog.findViewById(R.id.arrow1);
                arrow1.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        String cd=code1.getText().toString();
                       if (!cd.equals("")){
                        reference.child(cd).addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot snapshot) {
                                if (snapshot.exists()){
                                    String id= String.valueOf(snapshot.child("host").getValue());
                                    if (id.equals(a)){
                                        Intent intent = new Intent(MainActivity.this, Lobby.class);
                                        intent.putExtra("card",b);
                                        intent.putExtra("player","1");
                                        startActivity(intent);
                                    }else {
                                    Intent inten = new Intent(MainActivity.this, Lobby.class);
                                    inten.putExtra("card",cd);
                                    inten.putExtra("player","2");
                                    startActivity(inten);
                                    }
                                }else {
                                    Toast.makeText(MainActivity.this,"No room found",Toast.LENGTH_LONG).show();
                                }
                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError error) {

                            }
                        });

                    }else {
                           Toast.makeText(MainActivity.this,"Invalid",Toast.LENGTH_LONG).show();

                       }
                    }
                });

                dialog.show();
            });

        }


    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        this.finishAffinity();
    }
}
