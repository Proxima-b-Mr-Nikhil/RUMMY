package com.game.gamerummy;

import android.app.Activity;
import android.content.ClipData;
import android.content.Context;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Canvas;
import android.graphics.Rect;
import android.preference.PreferenceManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.databinding.ObservableArrayList;
import androidx.databinding.ObservableList;
import androidx.databinding.ViewDataBinding;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.RecyclerView;

import com.game.gamerummy.databinding.ChooseExerciseItemBinding;
import com.game.gamerummy.databinding.SelectedExerciseItemBinding;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;


public class Adapter extends RecyclerView.Adapter<Adapter.ProjectHolder> {

    private ObservableList<CardData> exerciseObservableList = new ObservableArrayList<>();
    private Context context;
    private RecyclerView recyclerExercise;
    private int layoutId;
    DatabaseReference reference;
    List<String> open=new ArrayList<>();
    boolean turn;
    SharedPreferences preferences;


    public Adapter(RecyclerView recyclerExercise, ObservableArrayList<CardData> exerciseObservableList, int layoutId) {
        this.exerciseObservableList = exerciseObservableList;
        this.recyclerExercise = recyclerExercise;
        this.layoutId = layoutId;
    }

    @Override
    public ProjectHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(layoutId, parent, false);
        context = parent.getContext();
        return new ProjectHolder(v);
    }

    @Override
    public void onBindViewHolder(ProjectHolder holder, int position) {
         preferences= PreferenceManager.getDefaultSharedPreferences(context);
        final CardData cardData = exerciseObservableList.get(position);
        Intent intent = ((Activity) context).getIntent();
        String player= intent.getStringExtra("player");
        String roomno= intent.getStringExtra("card");
        reference=FirebaseDatabase.getInstance().getReference("rummy").child(roomno).child("turn").child(player);
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()){
                    turn = (Boolean) snapshot.getValue();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        if (layoutId == R.layout.layout_choose_exercise_item) {
            ChooseExerciseItemBinding chooseExerciseItemBinding = (ChooseExerciseItemBinding) holder.chooseExerciseItemBinding;

            String s = String.valueOf(cardData.name.charAt(0));

            String str = cardData.name.substring(1);
            StringBuffer suit = new StringBuffer(),
                    valu = new StringBuffer();
            for (int i = 0; i < str.length(); i++) {
                if (Character.isDigit(str.charAt(i)))
                    valu.append(str.charAt(i));
                else if (Character.isAlphabetic(str.charAt(i)))
                    suit.append(str.charAt(i));
            }
            String suite = suit.toString();
            String value = String.valueOf(valu);
            cards(suite, Integer.parseInt(value), chooseExerciseItemBinding.imgg);
            chooseExerciseItemBinding.rlExerciseType.setOnLongClickListener(view -> {
                if (!s.equals("j")&&!s.equals("5")){

                    ClipData data = ClipData.newPlainText("",s);
                    View.DragShadowBuilder shadowBuilder = new View.DragShadowBuilder(view);
                    view.startDrag(data, shadowBuilder, view, 0);
                    view.setVisibility(View.INVISIBLE);

                }
                if (s.equals("5")&&turn){
                    ClipData data = ClipData.newPlainText("",s);
                    View.DragShadowBuilder shadowBuilder = new View.DragShadowBuilder(view);
                    view.startDrag(data, shadowBuilder, view, 0);
                    view.setVisibility(View.INVISIBLE);

                }

                return true;
            });


            chooseExerciseItemBinding.setChooseExerciseListAdapter(this);

        } else {
            SelectedExerciseItemBinding selectedExerciseItemBinding = (SelectedExerciseItemBinding) holder.chooseExerciseItemBinding;
            String s = String.valueOf(cardData.name.charAt(0));

            String str = cardData.name.substring(1);
            StringBuffer suit = new StringBuffer(),
                    valu = new StringBuffer();
            for (int i = 0; i < str.length(); i++) {
                if (Character.isDigit(str.charAt(i)))
                    valu.append(str.charAt(i));
                else if (Character.isAlphabetic(str.charAt(i)))
                    suit.append(str.charAt(i));
            }
            String suite = suit.toString();
            String value = String.valueOf(valu);
            cards(suite, Integer.parseInt(value), selectedExerciseItemBinding.imgg);

            selectedExerciseItemBinding.rlExerciseType.setOnLongClickListener(view -> {
                if (!s.equals("j")&&!s.equals("5")){
                    ClipData data = ClipData.newPlainText("",s);
                    View.DragShadowBuilder shadowBuilder = new View.DragShadowBuilder(view);
                    view.startDrag(data, shadowBuilder, view, 0);
                    view.setVisibility(View.INVISIBLE);

                }

                if (s.equals("5")&&turn){

                    ClipData data = ClipData.newPlainText("",s);
                    View.DragShadowBuilder shadowBuilder = new View.DragShadowBuilder(view);
                    view.startDrag(data, shadowBuilder, view, 0);
                    view.setVisibility(View.INVISIBLE);

                }
                return true;
            });
            selectedExerciseItemBinding.setChooseExerciseListAdapter(this);
        }

        holder.chooseExerciseItemBinding.executePendingBindings();
    }

    @Override
    public int getItemCount() {
        if (exerciseObservableList == null) {
            return 0;
        }
        return exerciseObservableList.size();
    }


    public class ProjectHolder extends RecyclerView.ViewHolder  {
        public ViewDataBinding chooseExerciseItemBinding;
        public ProjectHolder(View itemView) {
            super(itemView);

            chooseExerciseItemBinding = DataBindingUtil.bind(itemView);
        }

    }



    private void cards(String suite, int value, ImageView img) {

        if (suite.equals("hearts")) {
            switch (value) {
                case 1:
                    img.setImageResource(R.drawable.ace_of_hearts);
                    break;
                case 2:
                    img.setImageResource(R.drawable.f2_of_hearts);
                    break;
                case 3:
                    img.setImageResource(R.drawable.f3_of_hearts);
                    break;
                case 4:
                    img.setImageResource(R.drawable.f4_of_hearts);
                    break;
                case 5:
                    img.setImageResource(R.drawable.f5_of_hearts);
                    break;
                case 6:
                    img.setImageResource(R.drawable.f6_of_hearts);
                    break;
                case 7:
                    img.setImageResource(R.drawable.f7_of_hearts);
                    break;
                case 8:
                    img.setImageResource(R.drawable.f8_of_hearts);
                    break;
                case 9:
                    img.setImageResource(R.drawable.f9_of_hearts);
                    break;
                case 10:
                    img.setImageResource(R.drawable.f10_of_hearts);
                    break;
                case 11:
                    img.setImageResource(R.drawable.jack_of_hearts);
                    break;
                case 12:
                    img.setImageResource(R.drawable.queen_of_hearts);
                    break;
                case 13:
                    img.setImageResource(R.drawable.king_of_hearts);
                    break;
            }
        }
        if (suite.equals("clubs")) {
            switch (value) {

                case 1:
                    img.setImageResource(R.drawable.ace_of_clubs);
                    break;
                case 2:
                    img.setImageResource(R.drawable.f2_of_clubs);
                    break;
                case 3:
                    img.setImageResource(R.drawable.f3_of_clubs);
                    break;
                case 4:
                    img.setImageResource(R.drawable.f4_of_clubs);
                    break;
                case 5:
                    img.setImageResource(R.drawable.f5_of_clubs);
                    break;
                case 6:
                    img.setImageResource(R.drawable.f6_of_clubs);
                    break;
                case 7:
                    img.setImageResource(R.drawable.f7_of_clubs);
                    break;
                case 8:
                    img.setImageResource(R.drawable.f8_of_clubs);
                    break;
                case 9:
                    img.setImageResource(R.drawable.f9_of_clubs);
                    break;
                case 10:
                    img.setImageResource(R.drawable.f10_of_clubs);
                    break;
                case 11:
                    img.setImageResource(R.drawable.jack_of_clubs);
                    break;
                case 12:
                    img.setImageResource(R.drawable.queen_of_clubs);
                    break;
                case 13:
                    img.setImageResource(R.drawable.king_of_clubs);
                    break;
            }
        }
        if (suite.equals("spades")) {
            switch (value) {

                case 1:
                    img.setImageResource(R.drawable.ace_of_spades);
                    break;
                case 2:
                    img.setImageResource(R.drawable.f2_of_spades);
                    break;
                case 3:
                    img.setImageResource(R.drawable.f3_of_spades);
                    break;
                case 4:
                    img.setImageResource(R.drawable.f4_of_spades);
                    break;
                case 5:
                    img.setImageResource(R.drawable.f5_of_spades);
                    break;
                case 6:
                    img.setImageResource(R.drawable.f6_of_spades);
                    break;
                case 7:
                    img.setImageResource(R.drawable.f7_of_spades);
                    break;
                case 8:
                    img.setImageResource(R.drawable.f8_of_spades);
                    break;
                case 9:
                    img.setImageResource(R.drawable.f9_of_spades);
                    break;
                case 10:
                    img.setImageResource(R.drawable.f10_of_spades);
                    break;
                case 11:
                    img.setImageResource(R.drawable.jack_of_spades);
                    break;
                case 12:
                    img.setImageResource(R.drawable.queen_of_spades);
                    break;
                case 13:
                    img.setImageResource(R.drawable.king_of_spades);
                    break;
            }
        }
        if (suite.equals("diamonds")) {
            switch (value) {
                case 1:
                    img.setImageResource(R.drawable.ace_of_diamonds);
                    break;
                case 2:
                    img.setImageResource(R.drawable.f2_of_diamonds);
                    break;
                case 3:
                    img.setImageResource(R.drawable.f3_of_diamonds);
                    break;
                case 4:
                    img.setImageResource(R.drawable.f4_of_diamonds);
                    break;
                case 5:
                    img.setImageResource(R.drawable.f5_of_diamonds);
                    break;
                case 6:
                    img.setImageResource(R.drawable.f6_of_diamonds);
                    break;
                case 7:
                    img.setImageResource(R.drawable.f7_of_diamonds);
                    break;
                case 8:
                    img.setImageResource(R.drawable.f8_of_diamonds);
                    break;
                case 9:
                    img.setImageResource(R.drawable.f9_of_diamonds);
                    break;
                case 10:
                    img.setImageResource(R.drawable.f10_of_diamonds);
                    break;
                case 11:
                    img.setImageResource(R.drawable.jack_of_diamonds);
                    break;
                case 12:
                    img.setImageResource(R.drawable.queen_of_diamonds);
                    break;
                case 13:
                    img.setImageResource(R.drawable.king_of_diamonds);
                    break;
            }

        }
    }

}
