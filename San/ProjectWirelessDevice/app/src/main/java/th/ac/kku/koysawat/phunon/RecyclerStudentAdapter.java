package th.ac.kku.koysawat.phunon;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;

public class RecyclerStudentAdapter extends RecyclerView.Adapter<RecyclerStudentAdapter.ViewHolder> {


    Context context;
    ArrayList<Student> students;


    public RecyclerStudentAdapter(Context context, ArrayList<Student> arrayList) {
        this.context = context;
        this.students = arrayList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        return new ViewHolder(LayoutInflater.from(context).inflate(R.layout.card_student_layout, viewGroup, false));
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, int i) {
        viewHolder.itemTitle.setText(students.get(i).getStudentName());
        viewHolder.scoretv.setText(students.get(i).getScore());
    }

    @Override
    public int getItemCount() {
        return students.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder{

        public ImageView itemImage;
        public TextView itemTitle;
        public TextView itemDetail;
        public TextView scoretv;
        public ImageButton plus, minus;
        FirebaseDatabase database ;
        DatabaseReference classData ;
        public ViewHolder(View itemView) {
            super(itemView);
            itemImage = (ImageView)itemView.findViewById(R.id.item_image);
            itemTitle = (TextView)itemView.findViewById(R.id.item_title);
            itemDetail = (TextView)itemView.findViewById(R.id.item_detail);
            scoretv = itemView.findViewById(R.id.score);
            plus = itemView.findViewById(R.id.plus);
            minus = itemView.findViewById(R.id.minus);
            database = FirebaseDatabase.getInstance();
            classData = database.getReference("Class");


            plus.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int position = getAdapterPosition();
                    String score = students.get(position).getScore();
                    String classname = students.get(position).getClassname();
                    String name = students.get(position).getStudentName();
                    int i = Integer.parseInt(score);
                    i = i+1;
                    String tostring = ""+i;
                    classData.child(classname).child("Student").child(name).child("Score").setValue(tostring);
                }
            });

            minus.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int position = getAdapterPosition();
                    String score = students.get(position).getScore();
                    String classname = students.get(position).getClassname();
                    String name = students.get(position).getStudentName();
                    int i = Integer.parseInt(score);
                    i = i-1;
                    String tostring = ""+i;
                    classData.child(classname).child("Student").child(name).child("Score").setValue(tostring);
                }
            });

                itemView.setOnClickListener(new View.OnClickListener() {
                @Override public void onClick(View v) {
                    int position = getAdapterPosition();
                    Snackbar.make(v, "Click detected on item " + position, Snackbar.LENGTH_LONG).setAction("Action", null).show();

                }
            });
        }
    }
}
