package th.ac.kku.udomboonyaluck.disra.coclass;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.List;

public class StudentRecyclerAdapter extends RecyclerView.Adapter<StudentRecyclerAdapter.Holder>{

    private List<Student> mData;
    Context context;
    private String code;
    private FirebaseDatabase database;
    private DatabaseReference dbRef;

    public StudentRecyclerAdapter(Context context, List<Student> mData,String code) {
        this.mData = mData;
        this.context = context;
        this.code = code;
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
        holder.studentName.setText(mData.get(position).getUsername());

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
                    score[0] = 0;
                    dbRef.setValue(score[0]);
                    holder.score_tv.setText(String.valueOf(score[0]));
                }
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
                    score[0] = 2;
                    dbRef.setValue(score[0]);
                    holder.score_tv.setText(String.valueOf(score[0]));
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return  mData.size();
    }

    static class Holder extends RecyclerView.ViewHolder{

        private Button addScore;
        private Button subScore;
        private TextView studentName;
        private TextView score_tv;
        private ImageView student_img;

        public Holder(@NonNull View itemView) {
            super(itemView);

            addScore = itemView.findViewById(R.id.add);
            subScore = itemView.findViewById(R.id.subtract);
            studentName = itemView.findViewById(R.id.studentName);
            score_tv = itemView.findViewById(R.id.score_tv);
            student_img = itemView.findViewById(R.id.student_img);

        }
    }
}
