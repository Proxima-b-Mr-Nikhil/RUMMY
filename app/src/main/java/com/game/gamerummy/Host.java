package com.game.gamerummy;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.databinding.ObservableArrayList;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Dialog;
import android.content.ClipData;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.preference.PreferenceManager;
import android.view.DragEvent;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;
import com.game.gamerummy.databinding.HostBinding;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.database.annotations.NotNull;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.Random;
import java.util.concurrent.TimeUnit;

public class Host extends AppCompatActivity implements View.OnDragListener{
    List<String> starray=new ArrayList<>();
    public RecyclerView handRecyclerView,jokerRecyclerView,openRecyclerView,finishRecyclerView,set1,set2,set3,set4;
    String suite="",id="",idd="";
    List<String> handcards=new ArrayList<>();
    List<String> jokercards=new ArrayList<>();
    List<String> opencards=new ArrayList<>();
    List<String> rearrangecards=new ArrayList<>();
    List<String> openc=new ArrayList<>();
    DatabaseReference reference;
    ImageView backofcard;
    List<String> remainingcards=new ArrayList<>();
    List<String> player1=new ArrayList<>();
    List<String> open=new ArrayList<>();
    Button sort,allopencards;
    private final List<Integer> handcardsrank=new ArrayList<>();

    private HostBinding mainActivityBindin;
    public ObservableArrayList<CardData> handcardList,opencardlist,jokercardlist;
    public ObservableArrayList<CardData> exerciseSelectedList1 = new ObservableArrayList<>();
    public ObservableArrayList<CardData> exerciseSelectedList2 = new ObservableArrayList<>();
    public ObservableArrayList<CardData> exerciseSelectedList3 = new ObservableArrayList<>();
    public ObservableArrayList<CardData> exerciseSelectedList4 = new ObservableArrayList<>();
    public ObservableArrayList<CardData> finishlist = new ObservableArrayList<>();
    public CardData handcardToMove;

    private int newContactPosition =-1 ;
    private int currentPosition = -1;
    long last;
    String roomno,player;
    private ProgressBar progressBarCircle,progressBarCircle1;

    private CountDownTimer countDownTimer,countDownTimer1,fcountDownTimer;
    LinearLayout pro,pro1;
    boolean turn;
    String a;
    int count=0;
    int cnt=0;
    String str;
    int fcount=0;
    int joker,sum;
    ImageView pp,pp1;
    boolean time=false;
    boolean fshow=false;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);
        this.getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_HIDE_NAVIGATION);

        mainActivityBindin = DataBindingUtil.setContentView(this, R.layout.activity_host);
        progressBarCircle = (ProgressBar) findViewById(R.id.progressBarCircle);
        progressBarCircle1 = (ProgressBar) findViewById(R.id.progressBarCircle1);
        pro=findViewById(R.id.lpro);
        pro1=findViewById(R.id.lpro1);
        pp=findViewById(R.id.dp);
        pp1=findViewById(R.id.dp1);
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(Host.this);
       String a= preferences.getString("img","");
        Intent intent=getIntent();
        roomno= intent.getStringExtra("card");
        player= intent.getStringExtra("player");
        System.out.println(a+"hii");

       try{
            Picasso.with(this)

                    .load(a)
                    .placeholder(R.drawable.defaultpic)
                    .noFade()
                    .into(pp1);
        }catch (NullPointerException e){
            Toast.makeText(getApplicationContext(),"image not found",Toast.LENGTH_LONG).show();
        }
        reference=FirebaseDatabase.getInstance().getReference("rummy").child(roomno);

        handcardList = new ObservableArrayList<>();
        opencardlist = new ObservableArrayList<>();
        jokercardlist=new ObservableArrayList<>();
        for (int i = 1; i < 14; i++) {
            for (int j = 1; j < 5; j++) {
                switch (j) {
                    case 1:
                        suite = "spades";
                        starray.add(suite + i);
                        break;
                    case 2:
                        suite = "hearts";
                        starray.add(suite + i);
                        break;
                    case 3:
                        suite = "clubs";
                        starray.add(suite + i);
                        break;
                    case 4:
                        suite = "diamonds";
                        starray.add(suite + i);
                        break;
                }


            }

        }
        starray.addAll(starray);


        for (int h = 0; h < 3; h++) {

            if (h == 0) {
                for (int i=0;i<13;i++){
                    int r = new Random().nextInt(starray.size());
                    String s = String.valueOf(starray.get(r));

                    StringBuilder alph = new StringBuilder(),
                            nu = new StringBuilder();

                    for (int j = 0; j < s.length(); j++) {
                        if (Character.isDigit(s.charAt(j)))
                            nu.append(s.charAt(j));
                        else if (Character.isAlphabetic(s.charAt(j)))
                            alph.append(s.charAt(j));

                    }
                    player1.add(alph.toString()+nu);
                    starray.remove(r);
                }

                reference.child("player1").setValue(player1);

            }
            if (h == 1) {
                int rr = new Random().nextInt(starray.size());
                String st = String.valueOf(starray.get(rr));

                StringBuilder alpha = new StringBuilder(),
                        num = new StringBuilder();

                for (int i = 0; i < st.length(); i++) {
                    if (Character.isDigit(st.charAt(i)))
                        num.append(st.charAt(i));
                    else if (Character.isAlphabetic(st.charAt(i)))
                        alpha.append(st.charAt(i));
                }

                reference.child("joker").setValue(starray.get(rr));
                starray.remove(rr);

            }
            if (h == 2) {
                int rr = new Random().nextInt(starray.size());
                String st = String.valueOf(starray.get(rr));

                StringBuilder alpha = new StringBuilder(),
                        num = new StringBuilder();

                for (int i = 0; i < st.length(); i++) {
                    if (Character.isDigit(st.charAt(i)))
                        num.append(st.charAt(i));
                    else if (Character.isAlphabetic(st.charAt(i)))
                        alpha.append(st.charAt(i));
                }

                open.add(alpha.toString()+num);
                starray.remove(rr);
                reference.child("opencard").setValue(open);

            }

        }
        reference.child("remaining").setValue(starray).addOnCompleteListener(task -> reference.child("start").setValue(true));

        load();
        mainActivityBindin.setHost(this);
        mainActivityBindin.handcard0.setOnDragListener(this);
        mainActivityBindin.set1.setOnDragListener(this);
        mainActivityBindin.set2.setOnDragListener(this);
        mainActivityBindin.set3.setOnDragListener(this);
        mainActivityBindin.set4.setOnDragListener(this);
        mainActivityBindin.opencard5.setOnDragListener(this);
        mainActivityBindin.finish6.setOnDragListener(this);
        mainActivityBindin.joker.setOnDragListener(this);


        backofcard=findViewById(R.id.backofcard);
        sort=findViewById(R.id.sort);
        allopencards=findViewById(R.id.allopencards);

        handRecyclerView = findViewById(R.id.handcard0);
        openRecyclerView = findViewById(R.id.opencard5);
        jokerRecyclerView= findViewById(R.id.joker);
        finishRecyclerView=findViewById(R.id.finish6);

        set1=findViewById(R.id.set1);
        set2=findViewById(R.id.set2);
        set3=findViewById(R.id.set3);
        set4=findViewById(R.id.set4);


        handRecyclerView.setHasFixedSize(true);
        openRecyclerView.setHasFixedSize(true);
        jokerRecyclerView.setHasFixedSize(true);

        LinearLayoutManager mLayoutManager = new LinearLayoutManager(this,LinearLayoutManager.HORIZONTAL,false);
        LinearLayoutManager mLayoutManage = new LinearLayoutManager(this,LinearLayoutManager.HORIZONTAL,false);
        LinearLayoutManager mLayoutManag = new LinearLayoutManager(this,LinearLayoutManager.HORIZONTAL,false);
        LinearLayoutManager mLayoutMana = new LinearLayoutManager(this,LinearLayoutManager.HORIZONTAL,false);
        handRecyclerView.setLayoutManager(mLayoutManager);
        jokerRecyclerView.setLayoutManager(mLayoutManage);
        openRecyclerView.setLayoutManager(mLayoutManag);
        finishRecyclerView.setLayoutManager(mLayoutMana);

        reference=FirebaseDatabase.getInstance().getReference("rummy").child(roomno);

        ItemTouchHelper itemTouchHelper=new ItemTouchHelper(simpleCallback);
        itemTouchHelper.attachToRecyclerView(handRecyclerView);
        itemTouchHelper.attachToRecyclerView(set1);
        itemTouchHelper.attachToRecyclerView(set2);
        itemTouchHelper.attachToRecyclerView(set3);
        itemTouchHelper.attachToRecyclerView(set4);

        sort.setOnClickListener(view -> {
            sort.setVisibility(View.GONE);
            handcardsrank.clear();
            for (int position=0;position<handcards.size();position++) {
                String str = String.valueOf(handcards.get(position));
                StringBuffer suit = new StringBuffer(),
                        valu = new StringBuffer();
                for (int i = 0; i < str.length(); i++) {
                    if (Character.isDigit(str.charAt(i)))
                        valu.append(str.charAt(i));
                    else if (Character.isAlphabetic(str.charAt(i)))
                        suit.append(str.charAt(i));
                }
                String suite = suit.toString();
                String value= String.valueOf(valu);
                int count=0;
                for (int i=0;i<value.length();i++){
                    count++;
                }
                if (count==1){
                    value="0"+value;
                }
                if (suite.equals("spades")){
                    handcardsrank.add(Integer.valueOf("1"+value));
                }
                if (suite.equals("hearts")){
                    handcardsrank.add(Integer.valueOf("2"+value));
                }
                if (suite.equals("diamonds")){
                    handcardsrank.add(Integer.valueOf("3"+value));
                }
                if (suite.equals("clubs")){
                    handcardsrank.add(Integer.valueOf("4"+value));
                }
            }

            Collections.sort(handcardsrank);
            System.out.println(handcardsrank);
            handcardList.clear();
            for (int j=0;j<handcardsrank.size();j++){
                int firstDigit = Integer.parseInt(Integer.toString(handcardsrank.get(j)).substring(0, 1));
                int lastDigits = Integer.parseInt(Integer.toString(handcardsrank.get(j)).substring(1, 3));

                switch (firstDigit) {

                    case 1:
                        handcardList.add(new CardData(j,"0"+"spades"+lastDigits));
                        break;
                    case 2:
                        handcardList.add(new CardData(j,"0"+"hearts"+lastDigits));
                        break;
                    case 3:
                        handcardList.add(new CardData(j,"0"+"diamonds"+lastDigits));
                        break;
                    case 4:
                        handcardList.add(new CardData(j,"0"+"clubs"+lastDigits));
                        break;
                }
                System.out.println(handcards);

            }
        });
        reference.child("turn").child(player).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()){
                     turn = (Boolean) snapshot.getValue();
                     if(turn){
                         startCountDownTimer();
                   }else {
                         opponent();
                     }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        backofcard.setOnClickListener(view -> {

            sort.setVisibility(View.GONE);
            if (turn) {
                reference.child("remaining").addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull @NotNull DataSnapshot snapshot) {

                        remainingcards.clear();

                        for (DataSnapshot snap : snapshot.getChildren()) {
                            remainingcards.add(String.valueOf(snap.getValue()));
                        }
                        if (remainingcards.size() > 0) {

                            int r = new Random().nextInt(remainingcards.size());
                            Query query = reference.child("player1");
                            query.addListenerForSingleValueEvent(new ValueEventListener() {
                                @Override
                                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                                    System.out.println(dataSnapshot.getChildrenCount());
                                    last = dataSnapshot.getChildrenCount();
                                    if (last == 13) {
                                        reference.child("player1").child(String.valueOf(last)).setValue(remainingcards.get(r));
                                        handcardList.add(new CardData(13, "0" + remainingcards.get(r)));
                                        remainingcards.remove(r);
                                        reference.child("remaining").removeValue().addOnCompleteListener(task -> reference.child("remaining").setValue(remainingcards));
                                    }
                                }

                                @Override
                                public void onCancelled(@NonNull DatabaseError databaseError) {
                                    throw databaseError.toException();
                                }
                            });

                        }

                    }

                    @Override
                    public void onCancelled(@NonNull @NotNull DatabaseError error) {

                    }
                });

            }else {
                Toast.makeText(Host.this,"It's not your turn",Toast.LENGTH_SHORT).show();
            }
        });
        allopencards.setOnClickListener(view -> {
            if (opencardlist.size()>0){


            AlertDialog.Builder builder = new AlertDialog.Builder(Host.this);
            ViewGroup viewGroup = findViewById(android.R.id.content);
            View dialogView = LayoutInflater.from(view.getContext()).inflate(R.layout.allopencards, viewGroup, false);
            RecyclerView recyclerView = (RecyclerView) dialogView.findViewById (R.id.rec);

            reference.child("opencard").addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    if (snapshot.exists()){
                        openc.clear();

                        for (DataSnapshot snap : snapshot.getChildren()) {
                            openc.add(String.valueOf(snap.getValue()));
                        }

                        if (openc.size() > 0){

                            GridLayoutManager mlayout = new GridLayoutManager(Host.this, 3, GridLayoutManager.VERTICAL, true);
                           recyclerView.setLayoutManager(mlayout);

                            opencardsAdapter  mopenAdapter = new opencardsAdapter(getApplicationContext(), openc);
                            recyclerView.setAdapter(mopenAdapter);


                        }

                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {
                    Toast.makeText(Host.this, databaseError.getMessage(), Toast.LENGTH_SHORT).show();

                }
            });

            builder.setView(dialogView);
            AlertDialog alertDialog = builder.create();

            alertDialog.show();
                WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
                lp.copyFrom(alertDialog.getWindow().getAttributes());
                lp.width = 500;
                lp.height = 600;
                lp.x=1000;

                alertDialog.getWindow().setAttributes(lp);

            }else {
                Toast.makeText(Host.this,"No open cards!",Toast.LENGTH_LONG).show();
            }

        });

    }
    private void startCountDownTimer() {

        if (time){
            countDownTimer.cancel();
        }
        final long timeCountInMilliSeconds = 15000;

        pro.setVisibility(View.INVISIBLE);pro1.setVisibility(View.VISIBLE);

        count=0;
        cnt=0;
        countDownTimer1 = new CountDownTimer(timeCountInMilliSeconds, 1000) {

            @Override
            public void onTick(long millisUntilFinished) {
                long seconds = TimeUnit.MILLISECONDS.toSeconds(millisUntilFinished);
                progressBarCircle1.setProgress((int) (millisUntilFinished / 1000));
            }

            @Override
            public void onFinish() {
               f1();
            }

        }.start();
        countDownTimer1.start();

    }

    private void f1(){
        reference.child("turn/1").setValue(false);
        reference.child("turn/2").setValue(true);
        sum=handcardList.size()+exerciseSelectedList1.size()+ exerciseSelectedList2.size()+ exerciseSelectedList3.size()
                + exerciseSelectedList4.size();
        if (sum == 14) {
            removeextra();
        }
    }

    private void opponent() {
        final long timeCountInMilliSeconds1 = 15000;
        pro.setVisibility(View.VISIBLE);pro1.setVisibility(View.INVISIBLE);
        countDownTimer = new CountDownTimer(timeCountInMilliSeconds1, 1000) {
            @Override
            public void onTick(long millisUntilFinished1) {
                progressBarCircle.setProgress((int) (millisUntilFinished1 / 1000));
            }

            @Override
            public void onFinish() {
               // reference.child("turn/1").setValue(true);
                //reference.child("turn/2").setValue(false);
            }

        }.start();
        countDownTimer.start();
        countDownTimer1.cancel();
        time=true;
    }

    @Override
    public boolean onDrag(View view, DragEvent dragEvent) {
        View selectedView = (View) dragEvent.getLocalState();
        RecyclerView rcvSelected = (RecyclerView) view;

        switch (dragEvent.getAction()) {
            case DragEvent.ACTION_DRAG_LOCATION:

                break;
            case DragEvent.ACTION_DRAG_ENTERED:

                break;
            case DragEvent.ACTION_DRAG_EXITED:
                break;
            case DragEvent.ACTION_DROP:

                ClipData.Item item = dragEvent.getClipData().getItemAt(0);
                idd = item.getText().toString();
                switch (Integer.parseInt(idd)){
                    case 0:
                        currentPosition = mainActivityBindin.handcard0.getChildAdapterPosition(selectedView);
                        if (currentPosition != -1) {
                            handcardToMove = handcardList.get(currentPosition);
                        }
                        break;
                    case 1:
                        currentPosition = mainActivityBindin.set1.getChildAdapterPosition(selectedView);

                        if (currentPosition != -1) {
                            handcardToMove = exerciseSelectedList1.get(currentPosition);

                        }
                        break;
                    case 2:
                        currentPosition = mainActivityBindin.set2.getChildAdapterPosition(selectedView);

                        if (currentPosition != -1) {
                            handcardToMove = exerciseSelectedList2.get(currentPosition);

                        }
                        break;
                    case 3:
                        currentPosition = mainActivityBindin.set3.getChildAdapterPosition(selectedView);

                        if (currentPosition != -1) {
                            handcardToMove = exerciseSelectedList3.get(currentPosition);

                        }
                        break;
                    case 4:
                        currentPosition = mainActivityBindin.set4.getChildAdapterPosition(selectedView);

                        if (currentPosition != -1) {
                            handcardToMove = exerciseSelectedList4.get(currentPosition);

                        }
                        break;
                    case 5:
                        currentPosition = mainActivityBindin.opencard5.getChildAdapterPosition(selectedView);
                        if (currentPosition != -1) {
                            handcardToMove = opencardlist.get(currentPosition);

                        }
                        break;
                }

                RecyclerView rcvSelecte = (RecyclerView) view;
                View onTopOf1 = rcvSelecte.findChildViewUnder(dragEvent.getX(), dragEvent.getY());
                newContactPosition = rcvSelecte.getChildAdapterPosition(onTopOf1);
                String str= rcvSelecte.toString();
                int b=str.length();
                id= String.valueOf(str.charAt(b-2));


                dragAndDrop();

                break;

            case DragEvent.ACTION_DRAG_ENDED:

                if (dragEvent.getResult()) {
                  sort.setVisibility(View.GONE);
                }
                selectedView.setVisibility(View.VISIBLE);
                if (newContactPosition != -1) {
                    rcvSelected.scrollToPosition(newContactPosition);
                    newContactPosition = -1;
                } else {
                    rcvSelected.scrollToPosition(0);
                }
            default:
                break;
        }
        return true;
    }

    private void dragAndDrop() {
        if (handcardToMove!=null){
        String s = handcardToMove.name;
        char[] ch = s.toCharArray();
        sum=handcardList.size()+exerciseSelectedList1.size()+ exerciseSelectedList2.size()+ exerciseSelectedList3.size()
                + exerciseSelectedList4.size();
        switch (Integer.parseInt(idd)){
            case 0:
                switch (Integer.parseInt(id)){
                    case 1:
                        ch[0] = '1';
                        handcardToMove.name= String.valueOf(ch);
                        exerciseSelectedList1.add(handcardToMove);
                        handcardList.remove(handcardToMove);
                        Objects.requireNonNull(mainActivityBindin.handcard0.getAdapter()).notifyItemRemoved(currentPosition);
                        break;
                    case 2:
                        ch[0] = '2';
                        handcardToMove.name= String.valueOf(ch);
                        exerciseSelectedList2.add(handcardToMove);
                        handcardList.remove(handcardToMove);
                        Objects.requireNonNull(mainActivityBindin.handcard0.getAdapter()).notifyItemRemoved(currentPosition);
                        break;
                    case 3:
                        ch[0] = '3';
                        handcardToMove.name= String.valueOf(ch);
                        exerciseSelectedList3.add(handcardToMove);
                        handcardList.remove(handcardToMove);
                        Objects.requireNonNull(mainActivityBindin.handcard0.getAdapter()).notifyItemRemoved(currentPosition);
                        break;
                    case 4:
                        ch[0] = '4';
                        handcardToMove.name= String.valueOf(ch);
                        exerciseSelectedList4.add(handcardToMove);
                        handcardList.remove(handcardToMove);
                        Objects.requireNonNull(mainActivityBindin.handcard0.getAdapter()).notifyItemRemoved(currentPosition);
                        break;
                    case 5:
                        addopen();
                        break;
                    case 6:
                        if (sum == 14) {
                            countDownTimer1.cancel();
                            finishlist.clear();
                            ch[0] = '6';
                            handcardToMove.name = String.valueOf(ch);
                            finishlist.add(handcardToMove);
                            handcardList.remove(handcardToMove);
                            Objects.requireNonNull(mainActivityBindin.handcard0.getAdapter()).notifyItemRemoved(currentPosition);
                            finishGame(handcardToMove);

                        }
                        break;
                }
                break;
            case 1:
                switch (Integer.parseInt(id)){
                    case 0:
                        ch[0] = '0';
                        handcardToMove.name= String.valueOf(ch);
                        handcardList.add(handcardToMove);
                        exerciseSelectedList1.remove(handcardToMove);
                        Objects.requireNonNull(mainActivityBindin.set1.getAdapter()).notifyItemRemoved(currentPosition);
                        break;
                    case 2:
                        ch[0] = '2';
                        handcardToMove.name= String.valueOf(ch);
                        exerciseSelectedList2.add(handcardToMove);
                        exerciseSelectedList1.remove(handcardToMove);
                        Objects.requireNonNull(mainActivityBindin.set1.getAdapter()).notifyItemRemoved(currentPosition);
                        break;
                    case 3:
                        ch[0] = '3';
                        handcardToMove.name= String.valueOf(ch);
                        exerciseSelectedList3.add(handcardToMove);
                        exerciseSelectedList1.remove(handcardToMove);
                        Objects.requireNonNull(mainActivityBindin.set1.getAdapter()).notifyItemRemoved(currentPosition);
                        break;
                    case 4:
                        ch[0] = '4';
                        handcardToMove.name= String.valueOf(ch);
                        exerciseSelectedList4.add(handcardToMove);
                        exerciseSelectedList1.remove(handcardToMove);
                        Objects.requireNonNull(mainActivityBindin.set1.getAdapter()).notifyItemRemoved(currentPosition);
                        break;
                    case 5:
                        addopen();
                        break;
                    case 6:

                        if (sum == 14) {
                            finishlist.clear();
                            ch[0] = '6';
                            handcardToMove.name= String.valueOf(ch);
                            finishlist.add(handcardToMove);
                            exerciseSelectedList1.remove(handcardToMove);
                            Objects.requireNonNull(mainActivityBindin.set1.getAdapter()).notifyItemRemoved(currentPosition);
                            finishGame(handcardToMove);
                        }
                        break;
                }
                break;
            case 2:
                switch (Integer.parseInt(id)){
                    case 0:
                        ch[0] = '0';
                        handcardToMove.name= String.valueOf(ch);
                        handcardList.add(handcardToMove);
                        exerciseSelectedList2.remove(handcardToMove);
                        Objects.requireNonNull(mainActivityBindin.set2.getAdapter()).notifyItemRemoved(currentPosition);
                        break;
                    case 1:
                        ch[0] = '1';
                        handcardToMove.name= String.valueOf(ch);
                        exerciseSelectedList1.add(handcardToMove);
                        exerciseSelectedList2.remove(handcardToMove);
                        Objects.requireNonNull(mainActivityBindin.set2.getAdapter()).notifyItemRemoved(currentPosition);
                        break;
                    case 3:
                        ch[0] = '3';
                        handcardToMove.name= String.valueOf(ch);
                        exerciseSelectedList3.add(handcardToMove);
                        exerciseSelectedList2.remove(handcardToMove);
                        Objects.requireNonNull(mainActivityBindin.set2.getAdapter()).notifyItemRemoved(currentPosition);
                        break;
                    case 4:
                        ch[0] = '4';
                        handcardToMove.name= String.valueOf(ch);
                        exerciseSelectedList4.add(handcardToMove);
                        exerciseSelectedList2.remove(handcardToMove);
                        Objects.requireNonNull(mainActivityBindin.set2.getAdapter()).notifyItemRemoved(currentPosition);
                        break;
                    case 5:
                        addopen();
                        break;
                    case 6:

                        if (sum == 14) {
                            finishlist.clear();
                            ch[0] = '6';
                            handcardToMove.name= String.valueOf(ch);
                            finishlist.add(handcardToMove);
                            exerciseSelectedList2.remove(handcardToMove);
                            Objects.requireNonNull(mainActivityBindin.set2.getAdapter()).notifyItemRemoved(currentPosition);
                            finishGame(handcardToMove);
                        }
                        break;
                }
        break;
            case 3:
                switch (Integer.parseInt(id)){
                    case 0:
                        ch[0] = '0';
                        handcardToMove.name= String.valueOf(ch);
                        handcardList.add(handcardToMove);
                        exerciseSelectedList3.remove(handcardToMove);
                        Objects.requireNonNull(mainActivityBindin.set3.getAdapter()).notifyItemRemoved(currentPosition);
                        break;
                    case 1:
                        ch[0] = '1';
                        handcardToMove.name= String.valueOf(ch);
                        exerciseSelectedList1.add(handcardToMove);
                        exerciseSelectedList3.remove(handcardToMove);
                        Objects.requireNonNull(mainActivityBindin.set3.getAdapter()).notifyItemRemoved(currentPosition);
                        break;
                    case 2:
                        ch[0] = '2';
                        handcardToMove.name= String.valueOf(ch);
                        exerciseSelectedList2.add(handcardToMove);
                        exerciseSelectedList3.remove(handcardToMove);
                        Objects.requireNonNull(mainActivityBindin.set3.getAdapter()).notifyItemRemoved(currentPosition);
                        break;
                    case 4:
                        ch[0] = '4';
                        handcardToMove.name= String.valueOf(ch);
                        exerciseSelectedList4.add(handcardToMove);
                        exerciseSelectedList3.remove(handcardToMove);
                        Objects.requireNonNull(mainActivityBindin.set3.getAdapter()).notifyItemRemoved(currentPosition);
                        break;
                    case 5:
                        addopen();
                        break;
                    case 6:

                        if (sum == 14) {
                            finishlist.clear();
                            ch[0] = '6';
                            handcardToMove.name= String.valueOf(ch);
                            finishlist.add(handcardToMove);
                            exerciseSelectedList3.remove(handcardToMove);
                            Objects.requireNonNull(mainActivityBindin.set3.getAdapter()).notifyItemRemoved(currentPosition);
                            finishGame(handcardToMove);
                        }
                        break;
                }
        break;
            case 4:
                switch (Integer.parseInt(id)){
                    case 0:
                        ch[0] = '0';
                        handcardToMove.name= String.valueOf(ch);
                        handcardList.add(handcardToMove);
                        exerciseSelectedList4.remove(handcardToMove);
                        Objects.requireNonNull(mainActivityBindin.set4.getAdapter()).notifyItemRemoved(currentPosition);
                        break;
                    case 1:
                        ch[0] = '1';
                        handcardToMove.name= String.valueOf(ch);
                        exerciseSelectedList1.add(handcardToMove);
                        exerciseSelectedList4.remove(handcardToMove);
                        Objects.requireNonNull(mainActivityBindin.set4.getAdapter()).notifyItemRemoved(currentPosition);
                        break;
                    case 2:
                        ch[0] = '2';
                        handcardToMove.name= String.valueOf(ch);
                        exerciseSelectedList2.add(handcardToMove);
                        exerciseSelectedList4.remove(handcardToMove);
                        Objects.requireNonNull(mainActivityBindin.set4.getAdapter()).notifyItemRemoved(currentPosition);
                        break;
                    case 3:
                        ch[0] = '3';
                        handcardToMove.name= String.valueOf(ch);
                        exerciseSelectedList3.add(handcardToMove);
                        exerciseSelectedList4.remove(handcardToMove);
                        Objects.requireNonNull(mainActivityBindin.set4.getAdapter()).notifyItemRemoved(currentPosition);
                        break;
                    case 5:
                        addopen();
                        break;
                    case 6:
                        if (sum == 14) {
                            finishlist.clear();
                            ch[0] = '6';
                            handcardToMove.name = String.valueOf(ch);
                            finishlist.add(handcardToMove);
                            exerciseSelectedList4.remove(handcardToMove);
                            Objects.requireNonNull(mainActivityBindin.set4.getAdapter()).notifyItemRemoved(currentPosition);

                            finishGame(handcardToMove);
                        }
                        break;
                }
                break;
            case 5:
                if (sum==13){
                   switch (Integer.parseInt(id)){
                        case 0:
                            ch[0] = '0';
                            handcardToMove.name= String.valueOf(ch);
                            handcardList.add(handcardToMove);
                            opencardlist.remove(handcardToMove);
                            Objects.requireNonNull(mainActivityBindin.opencard5.getAdapter()).notifyItemRemoved(currentPosition);
                            removeopen(handcardToMove.position);
                            break;
                        case 1:
                            ch[0] = '1';
                            handcardToMove.name= String.valueOf(ch);
                            exerciseSelectedList1.add(handcardToMove);
                            opencardlist.remove(handcardToMove);
                            Objects.requireNonNull(mainActivityBindin.opencard5.getAdapter()).notifyItemRemoved(currentPosition);
                            removeopen(handcardToMove.position);
                            break;
                        case 2:
                            ch[0] = '2';
                            handcardToMove.name= String.valueOf(ch);
                            exerciseSelectedList2.add(handcardToMove);
                            opencardlist.remove(handcardToMove);
                            Objects.requireNonNull(mainActivityBindin.opencard5.getAdapter()).notifyItemRemoved(currentPosition);
                            removeopen(handcardToMove.position);
                            break;
                        case 3:
                            ch[0] = '3';
                            handcardToMove.name= String.valueOf(ch);
                            exerciseSelectedList3.add(handcardToMove);
                            opencardlist.remove(handcardToMove);
                            Objects.requireNonNull(mainActivityBindin.opencard5.getAdapter()).notifyItemRemoved(currentPosition);
                            removeopen(handcardToMove.position);
                            break;
                        case 4:
                            ch[0] = '4';
                            handcardToMove.name= String.valueOf(ch);
                            exerciseSelectedList4.add(handcardToMove);
                            opencardlist.remove(handcardToMove);
                            Objects.requireNonNull(mainActivityBindin.opencard5.getAdapter()).notifyItemRemoved(currentPosition);
                            removeopen(handcardToMove.position);
                            break;
                        case 6:
                                finishlist.clear();
                                ch[0] = '6';
                                handcardToMove.name = String.valueOf(ch);
                                finishlist.add(handcardToMove);
                                exerciseSelectedList4.remove(handcardToMove);
                                Objects.requireNonNull(mainActivityBindin.set4.getAdapter()).notifyItemRemoved(currentPosition);

                                finishGame(handcardToMove);

                            break;
                    }

                }
        break;


        }
        mainActivityBindin.executePendingBindings();
    }
    }

    private void finishGame(CardData movecard) {
        AlertDialog.Builder builder = new AlertDialog.Builder(Host.this);
        ViewGroup viewGroup = findViewById(android.R.id.content);
        View dialogView = LayoutInflater.from(getApplicationContext()).inflate(R.layout.yesorno, viewGroup, false);
        builder.setView(dialogView);
        AlertDialog alertDialog = builder.create();
        alertDialog.setCancelable(false);

        Button yes=(Button)dialogView. findViewById(R.id.finishyes);
        Button no=(Button)dialogView. findViewById(R.id.finishno);
       ProgressBar progressBarCircl = (ProgressBar)dialogView. findViewById(R.id.progressBarCircle);
       TextView textViewTim = (TextView)dialogView. findViewById(R.id.textViewTime);
            final long timeCountInMilliSeconds1 = 15000;
            fcountDownTimer = new CountDownTimer(timeCountInMilliSeconds1, 1000) {
                @Override
                public void onTick(long millisUntilFinished1) {
                    progressBarCircl.setProgress((int) (millisUntilFinished1 / 1000));
                    long seconds = TimeUnit.MILLISECONDS.toSeconds(millisUntilFinished1);
                    textViewTim.setText(String.valueOf(seconds));
                }

                @Override
                public void onFinish() {
                   alertDialog.dismiss();
                    addopen();
                    finishlist.remove(movecard);
                    String s0 = movecard.name;
                    char[] ch0 = s0.toCharArray();
                    ch0[0] = '0';
                    movecard.name= String.valueOf(ch0);
                    handcardList.add(movecard);
                    handcardList.remove(movecard);
                    countDownTimer1.start();
                }

            }.start();
            fcountDownTimer.start();

        yes.setOnClickListener(view -> {
            reference.child("finishcard").setValue(movecard.name.substring(1)).addOnCompleteListener(task -> {
                finishlist.clear();
                finishlist.add(new CardData(0,movecard.name));
                validatecards();
                if (exerciseSelectedList1.size()>0&&exerciseSelectedList2.size()>0&&
                        exerciseSelectedList3.size()==0&&exerciseSelectedList4.size()==0){
                    if (fcount>=2){
                        fshow=true;
                        show();
                        System.out.println("valid showhii");
                    }else {
                        fshow=false;
                        show();
                        System.out.println("invalid showhii");
                    }
                }

                if (exerciseSelectedList1.size()>0&&exerciseSelectedList2.size()>0&&
                        exerciseSelectedList3.size()>0&&exerciseSelectedList4.size()==0){
                    if (fcount==3){
                        fshow=true;
                        show();
                        System.out.println("valid showhii");
                    }else {
                        fshow=false;
                        show();
                        System.out.println("invalid showhii");
                    }
                }

                if (exerciseSelectedList1.size()>0&&exerciseSelectedList2.size()>0&&
                        exerciseSelectedList3.size()>0&&exerciseSelectedList4.size()>0){
                    if (fcount==4){
                        fshow=true;
                        show();
                        System.out.println("valid showhii");
                    }else {
                        fshow=false;
                        show();
                        System.out.println("invalid showhii");
                    }
                }

                alertDialog.dismiss();
            });
        });
        no.setOnClickListener(view -> {
            countDownTimer1.start();
            NoMethod(movecard,alertDialog);

        });
        alertDialog.show();

    }

    private void show() {
        Intent inten = new Intent(Host.this, FinishPage.class);
        inten.putExtra("card",roomno);
        inten.putExtra("player",player);
        inten.putExtra("valid",fshow);
        startActivity(inten);
    }

    private void NoMethod(CardData movecard, AlertDialog alertDialog) {
        alertDialog.dismiss();
        int a= Integer.parseInt(idd);
        switch (a) {
            case 0:
                finishlist.remove(movecard);
                String s0 = movecard.name;
                char[] ch0 = s0.toCharArray();
                ch0[0] = '0';
                movecard.name= String.valueOf(ch0);
                handcardList.add(movecard);
                break;
            case 1:
                finishlist.remove(movecard);
                String s1 = movecard.name;
                char[] ch1 = s1.toCharArray();
                ch1[0] = '0';
                movecard.name= String.valueOf(ch1);
                exerciseSelectedList1.add(movecard);
                break;
            case 2:
                finishlist.remove(movecard);
                String s2 = movecard.name;
                char[] ch2 = s2.toCharArray();
                ch2[0] = '0';
                movecard.name= String.valueOf(ch2);
                exerciseSelectedList2.add(movecard);
                break;
            case 3:
                finishlist.remove(movecard);
                String s3 = movecard.name;
                char[] ch3 = s3.toCharArray();
                ch3[0] = '0';
                movecard.name= String.valueOf(ch3);
                exerciseSelectedList3.add(movecard);
                break;
            case 4:
                finishlist.remove(movecard);
                String s4 = movecard.name;
                char[] ch4 = s4.toCharArray();
                ch4[0] = '0';
                movecard.name= String.valueOf(ch4);
                exerciseSelectedList4.add(movecard);
                break;
        }
    }


    private void removeopen(int pos) {
        Query query=reference.child("opencard").child(String.valueOf(pos));
        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapsho) {

                Query query = reference.child("player1").orderByKey().limitToLast(1);
                query.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        for (DataSnapshot childSnapshot : dataSnapshot.getChildren()) {
                            System.out.println(childSnapshot.getKey());
                            int last = Integer.parseInt(Objects.requireNonNull(childSnapshot.getKey())) + 1;
                            reference.child("player1").child(String.valueOf(last)).setValue(Objects.requireNonNull(dataSnapsho.getValue()).toString()).addOnCompleteListener(task -> dataSnapsho.getRef().removeValue());
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {
                        throw databaseError.toException();
                    }
                });

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    private void addopen() {

        int fsum=finishlist.size();
        int sum=handcardList.size()+exerciseSelectedList1.size()+ exerciseSelectedList2.size()+ exerciseSelectedList3.size()
                + exerciseSelectedList4.size();
        if (sum == 14||fsum==1) {
            String s = handcardToMove.name;
            char[] ch = s.toCharArray();
            ch[0] = '5';
            handcardToMove.name= String.valueOf(ch);
            opencardlist.add(handcardToMove);
            Query query = reference.child("opencard").orderByKey().limitToLast(1);
            query.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    if (dataSnapshot.exists()){
                    for (DataSnapshot childSnapshot : dataSnapshot.getChildren()) {
                        System.out.println(childSnapshot.getKey());
                        int last = Integer.parseInt(Objects.requireNonNull(childSnapshot.getKey())) + 1;
                        reference.child("opencard").child(String.valueOf(last)).setValue(handcardToMove.name.substring(1));
                    }
                    }else {
                        reference.child("opencard").child("0").setValue(handcardToMove.name.substring(1));
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });
            reference.child("player1").child(String.valueOf(handcardToMove.position)).removeValue();
            rearrangedata();
            switch (Integer.parseInt(idd)) {
                case 0:
                    handcardList.remove(handcardToMove);
                    Objects.requireNonNull(mainActivityBindin.handcard0.getAdapter()).notifyItemRemoved(currentPosition);
                    break;
                case 1:
                    exerciseSelectedList1.remove(handcardToMove);
                    Objects.requireNonNull(mainActivityBindin.set1.getAdapter()).notifyItemRemoved(currentPosition);
                    break;
                case 2:
                    exerciseSelectedList2.remove(handcardToMove);
                    Objects.requireNonNull(mainActivityBindin.set2.getAdapter()).notifyItemRemoved(currentPosition);
                    break;
                case 3:
                    exerciseSelectedList3.remove(handcardToMove);
                    Objects.requireNonNull(mainActivityBindin.set3.getAdapter()).notifyItemRemoved(currentPosition);
                    break;
                case 4:
                    exerciseSelectedList4.remove(handcardToMove);
                    Objects.requireNonNull(mainActivityBindin.set4.getAdapter()).notifyItemRemoved(currentPosition);
                    break;}
            f1();
            opponent();

        }
    }
    private void rearrangedata() {
        reference.child("player1").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()){

                    rearrangecards.clear();
                    for (DataSnapshot snap : snapshot.getChildren()) {
                        rearrangecards.add(String.valueOf(snap.getValue()));
                    }
                    reference.child("player1").setValue(rearrangecards);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
            }
        });

    }
    private void load() {
        reference.child("joker").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()){
                    jokercards.clear();
                    jokercards.add(Objects.requireNonNull(snapshot.getValue()).toString());
                    if (jokercards.size() > 0){
                        jokercardlist.add(new CardData(0,"j"+jokercards.get(0)));
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
        reference.child("opencard").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()){
                    opencards.clear();

                    for (DataSnapshot snap : snapshot.getChildren()) {
                        opencards.add(String.valueOf(snap.getValue()));
                    }
                    int a= (int) snapshot.getChildrenCount();
                    opencardlist.clear();
                    if (opencards.size() > 0){
                        opencardlist.add(new CardData(a-1,"5"+opencards.get(opencards.size()-1)));
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
            }
        });
        reference.child("player1").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()){

                    handcards.clear();
                    for (DataSnapshot snap : snapshot.getChildren()) {
                        handcards.add(String.valueOf(snap.getValue()));
                    }

                    handcardList.clear();
                    if (handcards.size() > 0){
                        for (int i = 0; i <handcards.size(); i++) {
                           handcardList.add(new CardData(i,"0"+handcards.get(i)));

                        }
                    }

                    reference.child("turn/1").setValue(true);
                    reference.child("turn/2").setValue(false);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
            }
        });

    }

    private void removeextra() {
        reference.child("player1").child("13").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    a = String.valueOf(snapshot.getValue());
                    if (cnt==0&&handcardList.size()>0){
                        for (int i = handcardList.size() - 1; i >= 0; i--) {
                            handcardToMove = handcardList.get(i);
                            if (count == 0) {
                                if (handcardToMove.name.substring(1).equals(a)) {
                                    String s1 = handcardToMove.name;
                                    char[] ch1 = s1.toCharArray();
                                    ch1[0] = '5';
                                    String str = String.valueOf(ch1).substring(1);
                                    int b = opencardlist.size();
                                    opencardlist.add(new CardData(b - 1, str));
                                    handcardList.remove(i);
                                    Objects.requireNonNull(mainActivityBindin.handcard0.getAdapter()).notifyItemRemoved(i);
                                    count++;
                                    cnt++;
                                    handcardToMove=null;
                                    remdb(str);
                                }
                            }
                        }
                    }
                    if (cnt==0&&exerciseSelectedList1.size()>0){
                        for (int i = exerciseSelectedList1.size() - 1; i >= 0; i--) {
                            handcardToMove = exerciseSelectedList1.get(i);
                            if (count == 0) {
                                if (handcardToMove.name.substring(1).equals(a)) {
                                    String s1 = handcardToMove.name;
                                    char[] ch1 = s1.toCharArray();
                                    ch1[0] = '5';
                                    String str = String.valueOf(ch1).substring(1);
                                    int b = opencardlist.size();
                                    opencardlist.add(new CardData(b - 1, str));
                                    exerciseSelectedList1.remove(i);
                                    Objects.requireNonNull(mainActivityBindin.set1.getAdapter()).notifyItemRemoved(i);
                                    count++;
                                    cnt++;
                                    handcardToMove=null;
                                    remdb(str);
                                }
                            }
                        }
                    }
                    if (cnt==0&&exerciseSelectedList2.size()>0){
                        for (int i = exerciseSelectedList2.size() - 1; i >= 0; i--) {
                            handcardToMove = exerciseSelectedList2.get(i);
                            if (count == 0) {
                                if (handcardToMove.name.substring(1).equals(a)) {
                                    String s1 = handcardToMove.name;
                                    char[] ch1 = s1.toCharArray();
                                    ch1[0] = '5';
                                    String str = String.valueOf(ch1).substring(1);
                                    int b = opencardlist.size();
                                    opencardlist.add(new CardData(b - 1, str));
                                    exerciseSelectedList2.remove(i);
                                    Objects.requireNonNull(mainActivityBindin.set2.getAdapter()).notifyItemRemoved(i);
                                    count++;
                                    cnt++;
                                    handcardToMove=null;
                                    remdb(str);
                                }
                            }
                        }
                    }

                    if (cnt==0&&exerciseSelectedList3.size()>0){
                        for (int i = exerciseSelectedList3.size() - 1; i >= 0; i--) {
                            handcardToMove = exerciseSelectedList3.get(i);
                            if (count == 0) {
                                if (handcardToMove.name.substring(1).equals(a)) {
                                    String s1 = handcardToMove.name;
                                    char[] ch1 = s1.toCharArray();
                                    ch1[0] = '5';
                                    String str = String.valueOf(ch1).substring(1);
                                    int b = opencardlist.size();
                                    opencardlist.add(new CardData(b - 1, str));
                                    exerciseSelectedList3.remove(i);
                                    Objects.requireNonNull(mainActivityBindin.set3.getAdapter()).notifyItemRemoved(i);
                                    count++;
                                    cnt++;
                                    handcardToMove=null;
                                    remdb(str);

                                }
                            }
                        }
                        if (cnt==0&&exerciseSelectedList4.size()>0){
                            for (int i = exerciseSelectedList4.size() - 1; i >= 0; i--) {
                                handcardToMove = exerciseSelectedList4.get(i);
                                if (count == 0) {
                                    if (handcardToMove.name.substring(1).equals(a)) {
                                        String s1 = handcardToMove.name;
                                        char[] ch1 = s1.toCharArray();
                                        ch1[0] = '5';
                                        String str = String.valueOf(ch1).substring(1);
                                        int b = opencardlist.size();
                                        opencardlist.add(new CardData(b - 1, str));
                                        exerciseSelectedList4.remove(i);
                                        Objects.requireNonNull(mainActivityBindin.set4.getAdapter()).notifyItemRemoved(i);
                                        count++;
                                        cnt++;
                                        handcardToMove=null;
                                        remdb(str);

                                    }
                                }
                            }
                        }
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void remdb(String str) {
        Query query = reference.child("opencard").orderByKey().limitToLast(1);
        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()){
                    for (DataSnapshot childSnapshot : dataSnapshot.getChildren()) {
                        System.out.println(childSnapshot.getKey());
                        int last = Integer.parseInt(Objects.requireNonNull(childSnapshot.getKey())) + 1;
                        reference.child("opencard").child(String.valueOf(last)).setValue(str);
                        reference.child("player1/13").setValue(null);

                    }
                }else {
                    reference.child("opencard").child("0").setValue(str);
                    reference.child("player1/13").setValue(null);

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }
    private void validatecards() {

        jokervalue();

        List<String> s1array=new ArrayList<>();
        List<Integer> i1array=new ArrayList<>();

        if (exerciseSelectedList1.size()>=3){
            for(int i=0;i<exerciseSelectedList1.size();i++){
                handcardToMove=exerciseSelectedList1.get(i);
                String str =handcardToMove.name.substring(1);
                StringBuilder suit = new StringBuilder(),
                        valu = new StringBuilder();
                for (int j = 0; j < str.length(); j++) {
                    if (Character.isDigit(str.charAt(j))) {
                        valu.append(str.charAt(j));
                    }
                    else if(Character.isAlphabetic(str.charAt(j))){
                        suit.append(str.charAt(j));
                    }
                }
                s1array.add(String.valueOf(suit));
                i1array.add(Integer.parseInt(String.valueOf(valu)));

            }

            int ct=0;
            String a=s1array.get(0);
            for (int k=0;k<s1array.size();k++){
                if (s1array.get(k).equals(a)){
                    ct++;
                }
            }
            if (ct==s1array.size()) {
                Collections.sort(i1array);
                int n = i1array.size();
                int first_term;
                int last_term;
                first_term=i1array.get(0);
                last_term=i1array.get(n-1);
                if (first_term==1&&last_term>7){
                    i1array.remove(0);
                    i1array.add(i1array.size()-1,14);
                    Collections.sort(i1array);
                    first_term=i1array.get(0);

                }
                int ap_sum = (n * (2 * first_term +
                        (n - 1))) / 2;
                int arr_sum = 0;
                for (int i = 0; i < n; i++) {
                    arr_sum += i1array.get(i);
                }
                if (ap_sum == arr_sum) {
                    fcount++;
                    System.out.println("pure squencehii");
                    set2();
                } else {
                    System.out.println("Not a sequencehii");
                }
            }else {
                System.out.println("invalid suitehii");
            }
        }else {
            System.out.println("invalid sequencehii");
        }
    }



    private void set2() {

        int k=0;

        List<Integer> i2array=new ArrayList<>();
        if (exerciseSelectedList2.size()>=3) {
            for (int i = 0; i < exerciseSelectedList2.size(); i++) {
                handcardToMove = exerciseSelectedList2.get(i);
                String st = handcardToMove.name.substring(1);

                if (st.contains("spades")) {
                    str = st.replaceAll("spades", "1");
                }
                if (st.contains("hearts")) {
                    str = st.replaceAll("hearts", "2");
                }
                if (st.contains("clubs")) {
                    str = st.replaceAll("clubs", "3");
                }
                if (st.contains("diamonds")) {
                    str = st.replaceAll("diamonds", "4");
                }

                String value= String.valueOf(str);
                int count=0;
                for (int l=0;l<value.length();l++){
                    count++;
                }
                if (count==2){
                    value = new StringBuilder(value).insert(1, "0").toString();
                }
                i2array.add(Integer.parseInt(value));
                Collections.sort(i2array);

            }
            for (int m=0;m<i2array.size();m++){
                if (i2array.get(m)==joker){
                    k++;
                }
            }
            i2array.removeAll(Arrays.asList(joker));
            int N = i2array.size();
            int diff = i2array.get(0);
            int ls=i2array.get(N-1);

            if (diff%100==1&&ls%100>7){
                char a= String.valueOf(Math.abs((long)ls)).charAt(0);
                String b=a+"14";
                int c = Integer.parseInt(b);
                i2array.remove(0);
                i2array.add(i2array.size()-1,c);
                Collections.sort(i2array);
                diff = i2array.get(0);

            }
            int c=0;
            for(int i = 0; i < N; i++)
            {
                if (i2array.get(i) - i != diff)
                {

                    while (diff < i2array.get(i) - i)
                    {
                        int a=i+diff;
                        c++;
                        diff++;
                    }
                }
            }
            for (int i = 0; i < i2array.size(); i++) {
                for (int j = i + 1 ; j < i2array.size(); j++) {
                    if (i2array.get(i).equals(i2array.get(j))) {
                        c=100;
                    }
                }
            }
            if (k>=c){
                fcount++;
                System.out.println("impure sequence/pure sequence hii");
                if (exerciseSelectedList3.size()>0){
                    set3();
                } else {
                    if (exerciseSelectedList4.size()>0){
                        set4();
                    }
                }
            }else{
                System.out.println("not a sequence hii"); }
        }
    }

    private void set3() {
        int k=0;

        List<Integer> i3array=new ArrayList<>();
        if (exerciseSelectedList3.size()>=3) {
            for (int i = 0; i < exerciseSelectedList3.size(); i++) {
                handcardToMove = exerciseSelectedList3.get(i);
                String st = handcardToMove.name.substring(1);

                if (st.contains("spades")) {
                    str = st.replaceAll("spades", "1");
                }
                if (st.contains("hearts")) {
                    str = st.replaceAll("hearts", "2");
                }
                if (st.contains("clubs")) {
                    str = st.replaceAll("clubs", "3");
                }
                if (st.contains("diamonds")) {
                    str = st.replaceAll("diamonds", "4");
                }

                String value = String.valueOf(str);
                int count = 0;
                for (int l = 0; l < value.length(); l++) {
                    count++;
                }
                if (count == 2) {
                    value = new StringBuilder(value).insert(1, "0").toString();
                }
                i3array.add(Integer.parseInt(value));
                Collections.sort(i3array,Collections.reverseOrder());
            }
            i3array.removeAll(Arrays.asList(joker));

            int p = 0;
            if (i3array.size()>1){
                for (int i = 0; i < i3array.size()-1; i++) {
                    int a=i3array.get(i);
                    int b=i3array.get(i+1);
                    if (a - b == 100||a - b == 200||a -b == 300){
                    }else {
                        p++;
                    }
                }
                if (p==0){
                    fcount++;
                    System.out.println("set3 hii");
                    if (exerciseSelectedList4.size()>0){
                        set4();
                    }
                }else {
                    System.out.println("not a set CHECKING SEQUENCE SET3 hii");
                    checkseq3();
                }
            }else {
                fcount++;
                System.out.println("set3 hii");
                if (exerciseSelectedList4.size()>0){
                    set4();
                }
            }
        }else {
            System.out.println("not a set3 hii");
        }
    }

    private void checkseq3() {


        int k=0;

        List<Integer> i3array=new ArrayList<>();
        if (exerciseSelectedList3.size()>=3) {
            for (int i = 0; i < exerciseSelectedList3.size(); i++) {
                handcardToMove = exerciseSelectedList3.get(i);
                String st = handcardToMove.name.substring(1);

                if (st.contains("spades")) {
                    str = st.replaceAll("spades", "1");
                }
                if (st.contains("hearts")) {
                    str = st.replaceAll("hearts", "2");
                }
                if (st.contains("clubs")) {
                    str = st.replaceAll("clubs", "3");
                }
                if (st.contains("diamonds")) {
                    str = st.replaceAll("diamonds", "4");
                }

                String value= String.valueOf(str);
                int count=0;
                for (int l=0;l<value.length();l++){
                    count++;
                }
                if (count==2){
                    value = new StringBuilder(value).insert(1, "0").toString();
                }
                i3array.add(Integer.parseInt(value));
                Collections.sort(i3array);

            }
            for (int m=0;m<i3array.size();m++){
                if (i3array.get(m)==joker){
                    k++;
                }
            }
            i3array.removeAll(Arrays.asList(joker));
            int N = i3array.size();
            int diff = i3array.get(0);

            int ls=i3array.get(N-1);

            if (diff%100==1&&ls%100>7){
                char a= String.valueOf(Math.abs((long)ls)).charAt(0);
                String b=a+"14";
                int c = Integer.parseInt(b);
                i3array.remove(0);
                i3array.add(i3array.size()-1,c);
                Collections.sort(i3array);
                diff = i3array.get(0);

            }

            int c=0;
            for(int i = 0; i < N; i++)
            {
                if (i3array.get(i) - i != diff)
                {

                    while (diff < i3array.get(i) - i)
                    {
                        int a=i+diff;
                        c++;
                        diff++;
                    }
                }
            }
            for (int i = 0; i < i3array.size(); i++) {
                for (int j = i + 1 ; j < i3array.size(); j++) {
                    if (i3array.get(i).equals(i3array.get(j))) {
                        c=100;
                    }
                }
            }
            if (k>=c){
                fcount++;
                System.out.println("impure sequence/pure sequence SET3 hii");
                if (exerciseSelectedList4.size()>0){
                    set4();
                }
            }else{
                System.out.println("not a sequence3 hii");
            }
        }else {
            System.out.println("not a sequence3 hii");

        }
    }

    private void set4() {

        int k=0;
        List<Integer> i4array=new ArrayList<>();
        if (exerciseSelectedList4.size()>=3) {
            for (int i = 0; i < exerciseSelectedList4.size(); i++) {
                handcardToMove = exerciseSelectedList4.get(i);
                String st = handcardToMove.name.substring(1);

                if (st.contains("spades")) {
                    str = st.replaceAll("spades", "1");
                }
                if (st.contains("hearts")) {
                    str = st.replaceAll("hearts", "2");
                }
                if (st.contains("clubs")) {
                    str = st.replaceAll("clubs", "3");
                }
                if (st.contains("diamonds")) {
                    str = st.replaceAll("diamonds", "4");
                }

                String value = String.valueOf(str);
                int count = 0;
                for (int l = 0; l < value.length(); l++) {
                    count++;
                }
                if (count == 2) {
                    value = new StringBuilder(value).insert(1, "0").toString();
                }
                i4array.add(Integer.parseInt(value));
                Collections.sort(i4array,Collections.reverseOrder());
            }
            i4array.removeAll(Arrays.asList(joker));

            int p = 0;
            if (i4array.size()>1){
                for (int i = 0; i < i4array.size()-1; i++) {
                    int a=i4array.get(i);
                    int b=i4array.get(i+1);
                    if (a - b == 100||a - b == 200||a -b == 300){
                    }else {
                        p++;
                    }
                }
                if (p==0){
                    fcount++;
                    System.out.println("set4 hii");
                }else {
                    System.out.println("not a set4 CHECKING SEQUENCE SET4 hii");
                    checkseq4();
                }
            }else {
                fcount++;
                System.out.println("set4 hii");
            }
        }else {
            System.out.println("not a set4 hii");
        }
    }

    private void checkseq4() {


        int k=0;

        List<Integer> i4array=new ArrayList<>();
        if (exerciseSelectedList4.size()>=3) {
            for (int i = 0; i < exerciseSelectedList4.size(); i++) {
                handcardToMove = exerciseSelectedList4.get(i);
                String st = handcardToMove.name.substring(1);

                if (st.contains("spades")) {
                    str = st.replaceAll("spades", "1");
                }
                if (st.contains("hearts")) {
                    str = st.replaceAll("hearts", "2");
                }
                if (st.contains("clubs")) {
                    str = st.replaceAll("clubs", "3");
                }
                if (st.contains("diamonds")) {
                    str = st.replaceAll("diamonds", "4");
                }
               /* StringBuilder valu = new StringBuilder();
                for (int j = 0; j < str.length(); j++) {
                    if (Character.isDigit(str.charAt(j))) {
                        valu.append(str.charAt(j));
                    }
                }

                */
                String value= str;
                int count=0;
                for (int l=0;l<value.length();l++){
                    count++;
                }
                if (count==2){
                    value = new StringBuilder(value).insert(1, "0").toString();
                }
                i4array.add(Integer.parseInt(value));
                Collections.sort(i4array);

            }
            for (int m=0;m<i4array.size();m++){
                if (i4array.get(m)==joker){
                    k++;
                }
            }
            i4array.removeAll(Arrays.asList(joker));
            int N = i4array.size();
            int diff = i4array.get(0);

            int ls=i4array.get(N-1);

            if (diff%100==1&&ls%100>7){
                char a= String.valueOf(Math.abs((long)ls)).charAt(0);
                String b=a+"14";
                int c = Integer.parseInt(b);
                i4array.remove(0);
                i4array.add(i4array.size()-1,c);
                Collections.sort(i4array);
                diff = i4array.get(0);

            }
            int c=0;
            for(int i = 0; i < N; i++)
            {
                if (i4array.get(i) - i != diff)
                {

                    while (diff < i4array.get(i) - i)
                    {
                        int a=i+diff;
                        c++;
                        diff++;
                    }
                }
            }
            for (int i = 0; i < i4array.size(); i++) {
                for (int j = i + 1 ; j < i4array.size(); j++) {
                    if (i4array.get(i).equals(i4array.get(j))) {
                        c=100;
                    }
                }
            }
            if (k>=c){
                fcount++;
                System.out.println("impure sequence/pure sequence SET4 hii");

            }else{
                System.out.println("not a sequence4 hii");
            }
        }else {
            System.out.println("not a sequence4 hii");

        }
    }
    public void jokervalue(){
        List<Integer> array=new ArrayList<>();
        if (jokercardlist.size()>0) {
            handcardToMove = jokercardlist.get(0);
            String st = handcardToMove.name.substring(1);

            if (st.contains("spades")) {
                str = st.replaceAll("spades", "1");
            }
            if (st.contains("hearts")) {
                str = st.replaceAll("hearts", "2");
            }
            if (st.contains("clubs")) {
                str = st.replaceAll("clubs", "3");
            }
            if (st.contains("diamonds")) {
                str = st.replaceAll("diamonds", "4");
            }
            String value= str;
            int count=0;
            for (int l=0;l<value.length();l++){
                count++;
            }
            if (count==2){
                value = new StringBuilder(value).insert(1, "0").toString();
            }
            joker=Integer.parseInt(value);

        }
    }

    ItemTouchHelper.SimpleCallback simpleCallback= new ItemTouchHelper.SimpleCallback(ItemTouchHelper.LEFT |ItemTouchHelper.RIGHT|ItemTouchHelper.START|ItemTouchHelper.END,0) {
        @Override
        public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target) {
            int fromposition=viewHolder.getAdapterPosition();
            int topoition=target.getAdapterPosition();
            System.out.println(fromposition+"hii"+topoition);
            Collections.swap(handcardList,fromposition,topoition);
            Objects.requireNonNull(recyclerView.getAdapter()).notifyItemMoved(fromposition,topoition);

            return false;
        }

        @Override
        public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {
        }
    };


}