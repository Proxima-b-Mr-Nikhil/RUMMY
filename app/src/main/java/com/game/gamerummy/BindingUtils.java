package com.game.gamerummy;

import androidx.databinding.BindingAdapter;
import androidx.databinding.ObservableArrayList;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;


public class BindingUtils {

    @BindingAdapter({"bind:exerciseChooseItems", "bind:layoutId"})
    public static void bindExerciseList(RecyclerView view, ObservableArrayList<CardData> list, int layoutId) {
        GridLayoutManager layoutManager = new GridLayoutManager(view.getContext(), 14);
        view.setLayoutManager(layoutManager);
        view.setAdapter(new Adapter(view, list, layoutId));
    }


    @BindingAdapter({"bind:exerciseHorizontalItems", "bind:layoutId"})
    public static void bindHorizontalList(RecyclerView view, ObservableArrayList<CardData> list, int layoutId) {
        LinearLayoutManager layoutManager = new LinearLayoutManager(view.getContext(), LinearLayoutManager.HORIZONTAL, false);
        view.setLayoutManager(layoutManager);
        view.setAdapter(new Adapter(view, list, layoutId));
    }


}
