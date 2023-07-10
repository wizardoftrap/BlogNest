package com.shivprakash.objekto;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import java.util.ArrayList;

public class CustomAdaptor extends RecyclerView.Adapter<CustomAdaptor.MyViewHolder>{
    private Context context;
    private ArrayList title, Author,shortdscrpsn, longdscrpsn,date;
    CustomAdaptor(Context context, ArrayList title, ArrayList Author, ArrayList shortdscrpsn, ArrayList longdscrpsn, ArrayList date){
        this.context=context;
        this.Author=Author;
        this.title=title;
        this.longdscrpsn=longdscrpsn;
        this.shortdscrpsn=shortdscrpsn;
        this.date=date;
    }
    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View view=inflater.inflate(R.layout.result_rv_item,parent,false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CustomAdaptor.MyViewHolder holder, int position) {
        holder.titleTextView.setText(title.get(position).toString());
        holder.authorTextView.setText(Author.get(position).toString());
        holder.dateTextView.setText(date.get(position).toString());
        holder.contentTextView.setText(shortdscrpsn.get(position).toString());
        holder.descriptionTextView.setText(longdscrpsn.get(position).toString());
    }

    @Override
    public int getItemCount() {

        return Author.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        TextView titleTextView, authorTextView, dateTextView, contentTextView, descriptionTextView;
        Button readMoreButton, contactButton;
        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            titleTextView = itemView.findViewById(R.id.titleTextView);
            authorTextView = itemView.findViewById(R.id.authorTextView);
            dateTextView = itemView.findViewById(R.id.dateTextView);
            contentTextView = itemView.findViewById(R.id.contentTextView);
            descriptionTextView = itemView.findViewById(R.id.descriptionTextView);
             readMoreButton = itemView.findViewById(R.id.readMoreButton);
           contactButton = itemView.findViewById(R.id.contactButton);
           readMoreButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    toggleDescriptionVisibility();
                }

                private void toggleDescriptionVisibility() {
                    if (descriptionTextView.getVisibility() == View.GONE) {
                        descriptionTextView.setVisibility(View.VISIBLE);
                    } else {
                        descriptionTextView.setVisibility(View.GONE);
                    }
                }
            });
            contactButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    shareContent();
                }
                private void shareContent() {
                    String textToShare = titleTextView.getText().toString()+":\n"+contentTextView.getText().toString()+"\n"+"Team BlogNest";
                    Intent intent = new Intent(Intent.ACTION_SEND);
                    intent.setType("text/plain");
                    intent.putExtra(Intent.EXTRA_TEXT, textToShare);
                    context.startActivity(Intent.createChooser(intent, "Share via"));
                }
            });

        }
    }

}
