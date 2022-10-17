package com.game.gamerummy;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.preference.PreferenceManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.databinding.ObservableArrayList;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;


class opencardsAdapter extends RecyclerView.Adapter<opencardsAdapter.ImageViewHolder> {
    private List<String> handcards;
    Context contex;
    boolean flag;


    public opencardsAdapter(Context context, List<String> ocards) {
        handcards = ocards;
        contex = context;
    }



    @NonNull
    @Override
    public ImageViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int i) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.card, parent, false);

        return new ImageViewHolder(v);
    }

    @Override
    public void onViewRecycled(@NonNull ImageViewHolder holder) {
        holder.img.setImageBitmap(null);
    }

    @Override
    public void onBindViewHolder(@NonNull final ImageViewHolder holder, int position) {

        if (position==0){
            holder.img.setElevation(50f);
        }
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
        String value = String.valueOf(valu);
        cards(suite, Integer.parseInt(value), holder.img);

    }

    @Override
    public int getItemCount() {
        return handcards.size();
    }


    public class ImageViewHolder extends RecyclerView.ViewHolder {

        public ImageView img;

        public ImageViewHolder(@NonNull View itemView) {
            super(itemView);


            img = itemView.findViewById(R.id.cardI);


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