package com.example.popularmovies.utilities;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.popularmovies.MovieDetail;
import com.example.popularmovies.R;
import com.squareup.picasso.Picasso;

import org.json.JSONException;
import org.json.JSONObject;


public class recyclerAdapter extends RecyclerView.Adapter<recyclerAdapter.ViewHolder>{

    ItemClickListener mClickListener;
    Context context;
    String[] data;
    String imageUrl = "https://image.tmdb.org/t/p/w185";
    String movie_poster;
    JSONObject json;

    public recyclerAdapter(Context context, String[] data) {
        this.context = context;
        this.data = data;
    }

    @Override
    public ViewHolder onCreateViewHolder(@NonNull  ViewGroup viewGroup, int viewType){
        View view = LayoutInflater.from(context).inflate(R.layout.movie, viewGroup, false);
        ViewHolder viewHolder = new ViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull  ViewHolder holder, final int position) {
        try {
            json = new JSONObject(data[position]);
            movie_poster = json.getString("poster_path");
            Picasso.get().load(imageUrl+movie_poster).into(holder.imageView);
            holder.itemView.setOnClickListener(new View.OnClickListener(){
                @Override
                public void onClick(View view) {
                    /*Frankly, this is the only way I make the onClick method work.
                    Also, I have to pass the data directly from the array rather than the JSON itself.
                    When I tried to pass the data using json instead of data[position], I only
                    saw the details from one movie.*/
                    Intent intent = new Intent(context, MovieDetail.class);
                    intent.putExtra("json",data[position]);
                    context.startActivity(intent);
                }
            });
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public int getItemCount() {
        return data.length;
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        ImageView imageView;

        public ViewHolder(View itemView) {
            super(itemView);

            imageView =  itemView.findViewById(R.id.poster_view);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
           if(mClickListener != null) mClickListener.onItemClick(view, getAdapterPosition());
        }
    }

    public interface ItemClickListener {
        void onItemClick(View view, int position);
    }
}