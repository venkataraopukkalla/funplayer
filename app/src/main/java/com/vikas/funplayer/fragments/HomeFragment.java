package com.vikas.funplayer.fragments;

import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Parcelable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextClock;
import android.widget.TextView;
import android.widget.Toast;

import com.vikas.funplayer.Adpaters.CategorieModel;
import com.vikas.funplayer.Adpaters.CategoriesAdpater;
import com.vikas.funplayer.MainActivity;
import com.vikas.funplayer.R;
import com.vikas.funplayer.model.SongsDetails;

import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link HomeFragment#} factory method to
 * create an instance of this fragment.
 */
public class HomeFragment extends Fragment {
    private RecyclerView categoriesRecycleView;
    private TextView justChecking;
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view= inflater.inflate(R.layout.fragment_home, container, false);
        categoriesRecycleView=view.findViewById(R.id.categories_recycleview);
        justChecking=view.findViewById(R.id.videos_viewall_txt);

        categoriesRecycleView.setLayoutManager(new LinearLayoutManager(getContext(),LinearLayoutManager.HORIZONTAL,false));
        ArrayList<CategorieModel> arrayList=new ArrayList<>();
        arrayList.add(new CategorieModel(R.drawable.movie_categorie,"Videos"));
        arrayList.add(new CategorieModel(R.drawable.music_logo,"Music"));
        arrayList.add(new CategorieModel(R.drawable.tool_logo,"Tools"));
        arrayList.add(new CategorieModel(R.drawable.fungames_logo,"Games"));
        arrayList.add(new CategorieModel(R.drawable.notebook_logo,"NootBook"));
        categoriesRecycleView.setAdapter(new CategoriesAdpater(arrayList,getContext()));

       // ArrayList<SongsDetails> parcelableArrayList = getArguments().getParcelableArrayList(MainActivity.KEY_VALUE);
        //  String string = getArguments().getString(MainActivity.KEY_VALUE);
       // Toast.makeText(getContext(),string,Toast.LENGTH_SHORT).show();
       // justChecking.setText(string);


        return view;
    }
}