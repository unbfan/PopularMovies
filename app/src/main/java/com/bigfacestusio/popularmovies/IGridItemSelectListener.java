package com.bigfacestusio.popularmovies;


import android.view.View;
import android.widget.AdapterView;

public interface IGridItemSelectListener {
    public void onGridItemSelected(AdapterView<?> parent, View view, int position);
}
