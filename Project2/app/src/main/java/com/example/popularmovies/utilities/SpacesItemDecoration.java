package com.example.popularmovies.utilities;

import android.graphics.Rect;
import android.view.View;

import androidx.recyclerview.widget.RecyclerView;

//I found how to do this in order to space evenly the items in a recycler view.
public class SpacesItemDecoration extends RecyclerView.ItemDecoration {

    private int space;

    public SpacesItemDecoration(int space, boolean firstTime) {

        if (firstTime){
            this.space = space;
        }
    }

    @Override
    public void getItemOffsets(Rect outRect, View view,
                               RecyclerView parent, RecyclerView.State state) {
        outRect.left = space;
        outRect.right = space;
        outRect.bottom = space;

        /*This if must to take into account how many columns I'm using in the recycler view.
        It's hardcoded, but I think I can do it better passing the number of columns as a parameter.
         */
        if (parent.getChildLayoutPosition(view) == 0 || parent.getChildLayoutPosition(view) == 1) {
            outRect.top = space;
        } else {
            outRect.top = 0;
        }
    }
}
