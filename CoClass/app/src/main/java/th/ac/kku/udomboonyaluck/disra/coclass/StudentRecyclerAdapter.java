package th.ac.kku.udomboonyaluck.disra.coclass;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

public class StudentRecyclerAdapter extends RecyclerView.Adapter<StudentRecyclerAdapter.Holder>{

    private List<Student> mData;
    ArrayList<String> lstUrl;
    Context context;
    private String code;
    private FirebaseDatabase database;
    private DatabaseReference dbRef;

    public StudentRecyclerAdapter(Context context, List<Student> mData, String code, ArrayList<String> lstUrl) {
        this.mData = mData;
        this.context = context;
        this.code = code;
        this.lstUrl = lstUrl;
    }

    @NonNull
    @Override
    public StudentRecyclerAdapter.Holder onCreateViewHolder(@NonNull ViewGroup viewGroup, final int position) {

        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.student_list,viewGroup,false);
        final StudentRecyclerAdapter.Holder holder = new StudentRecyclerAdapter.Holder(view);

        return holder;
    }


    @Override
    public void onBindViewHolder(@NonNull final StudentRecyclerAdapter.Holder holder, final int position) {
        holder.score_tv.setText("" + mData.get(position).getScore());
        holder.studentName.setText(mData.get(position).getUsername());
        if(lstUrl.get(position).equals("")){
            holder.student_img.setImageResource(R.drawable.user_image_default);
        }else {
            try {
                Picasso.get().load(lstUrl.get(position)).into(holder.student_img);
            }catch (Exception e){
                holder.student_img.setImageResource(R.drawable.user_image_default);
            }
        }

        database = FirebaseDatabase.getInstance();
        final int[] score = {mData.get(position).getScore()};
        holder.addScore.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dbRef = database.getReference("/courses/" + code + "/students/student" + position + "/score/");
                score[0]++;
                if(score[0] <= 2){
                    dbRef.setValue(score[0]);
                    holder.score_tv.setText(String.valueOf(score[0]));
                }else {
                    score[0] = 2;
                    dbRef.setValue(score[0]);
                    holder.score_tv.setText(String.valueOf(score[0]));
                }
                dbRef = database.getReference("/courses/" + code + "/queues/");
                dbRef.child(mData.get(position).getUsername()).removeValue();
            }
        });
        holder.subScore.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dbRef = database.getReference("/courses/" + code + "/students/student" + position + "/score/");
                score[0]--;
                if(score[0] >= 0){
                    dbRef.setValue(score[0]);
                    holder.score_tv.setText(String.valueOf(score[0]));
                } else {
                    score[0] = 0;
                    dbRef.setValue(score[0]);
                    holder.score_tv.setText(String.valueOf(score[0]));
                }
                dbRef = database.getReference("/courses/" + code + "/queues/");
                dbRef.child(mData.get(position).getUsername()).removeValue();
            }
        });

        dbRef = database.getReference("/courses/" + code + "/queues/");
        dbRef.child(mData.get(position).getUsername()).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if(dataSnapshot.exists()){
                    holder.wow.setVisibility(View.VISIBLE);
                    Animation animation = AnimationUtils.loadAnimation(context,R.anim.bounce);
                    animation.setRepeatMode(1);
                    holder.wow.startAnimation(animation);
                } else if (!dataSnapshot.exists()){
                    holder.wow.clearAnimation();
                    holder.wow.setVisibility(View.GONE);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    @Override
    public int getItemCount() {
        return  mData.size();
    }

    static class Holder extends RecyclerView.ViewHolder {

        private Button addScore;
        private Button subScore;
        private TextView studentName;
        private TextView score_tv;
        private ImageView student_img,wow;

        public Holder(@NonNull View itemView) {
            super(itemView);
            addScore = itemView.findViewById(R.id.add);
            subScore = itemView.findViewById(R.id.subtract);
            studentName = itemView.findViewById(R.id.studentName);
            score_tv = itemView.findViewById(R.id.score_tv);
            student_img = itemView.findViewById(R.id.student_img);
            wow = itemView.findViewById(R.id.wow);

        }
    }
}
