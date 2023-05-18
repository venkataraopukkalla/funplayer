package com.vikas.funplayer.Adpaters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.vikas.funplayer.R;

import java.util.ArrayList;

public class CategoriesAdpater extends RecyclerView.Adapter<CategoriesAdpater.CategoriresViewHolde> {

    ArrayList<CategorieModel> categorieModelArrayList=new ArrayList<>();
    Context context;

    public CategoriesAdpater(ArrayList<CategorieModel> categorieModelArrayList, Context context) {
        this.categorieModelArrayList = categorieModelArrayList;
        this.context = context;
    }

    @Override
    public CategoriresViewHolde onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.categories,parent,false);
        return new CategoriresViewHolde(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CategoriresViewHolde holder, int position) {
        // set dyanamic images
        //logic part
        holder.categorieLogo.setImageResource(categorieModelArrayList.get(position).getCategoriesLogo());
        holder.categorieTitle.setText(categorieModelArrayList.get(position).getCategoriesTitle());


    }

    @Override
    public int getItemCount() {
        return categorieModelArrayList.size();
    }

    public  class CategoriresViewHolde extends RecyclerView.ViewHolder{

        // categorie logo
          private   ImageView categorieLogo;
          private    TextView categorieTitle;
        public CategoriresViewHolde(@NonNull View itemView) {
            super(itemView);
            categorieLogo=itemView.findViewById(R.id.setcategerie_imgview);
            categorieTitle=itemView.findViewById(R.id.setcategeriestitle_txt);
        }
    }
}
