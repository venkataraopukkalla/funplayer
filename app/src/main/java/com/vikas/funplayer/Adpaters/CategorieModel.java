package com.vikas.funplayer.Adpaters;

public class CategorieModel {
    private int categoriesLogo;
    private  String categoriesTitle;
    // to generate  constructor

    public CategorieModel(int categoriesLogo, String categoriesTitle) {
        this.categoriesLogo = categoriesLogo;
        this.categoriesTitle = categoriesTitle;
    }

    // to genearte getters
    public int getCategoriesLogo() {
        return categoriesLogo;
    }

    public String getCategoriesTitle() {
        return categoriesTitle;
    }


}
