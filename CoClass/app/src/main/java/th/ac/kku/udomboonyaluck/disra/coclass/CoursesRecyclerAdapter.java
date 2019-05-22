package th.ac.kku.udomboonyaluck.disra.coclass;

import android.content.Context;
import android.content.Intent;
import android.os.Vibrator;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class CoursesRecyclerAdapter extends RecyclerView.Adapter<CoursesRecyclerAdapter.Holder> {

    private FirebaseAuth auth = FirebaseAuth.getInstance();
    private List<Course> mData;
    Context context;
    String code,cname;
    private FirebaseUser FireUser = auth.getCurrentUser();
    private FirebaseDatabase database;
    private DatabaseReference dbRef;

    public CoursesRecyclerAdapter(Context context, List<Course> mData) {
        this.mData = mData;
        this.context = context;
    }

    @NonNull
    @Override
    public Holder onCreateViewHolder(@NonNull ViewGroup viewGroup, final int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.course_list,viewGroup,false);
        final Holder holder = new Holder(view);
        final Vibrator vibe = (Vibrator) context.getSystemService(Context.VIBRATOR_SERVICE);

        holder.courses_list.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context,CourseActivity.class);
                intent.putExtra("courseName",mData.get(holder.getAdapterPosition()).getCoursename());
                intent.putExtra("teacher",mData.get(holder.getAdapterPosition()).getTeacher());
                intent.putExtra("code",mData.get(holder.getAdapterPosition()).getCode());
                context.startActivity(intent);
            }
        });
        holder.courses_list.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                code = mData.get(holder.getAdapterPosition()).getCode();
                cname = mData.get(holder.getAdapterPosition()).getCoursename();
                vibe.vibrate(80);
                return false;
            }
        });

        return holder;
    }


    @Override
    public void onBindViewHolder(@NonNull Holder holder, int position) {
        holder.courseName.setText(mData.get(position).getCoursename());
    }

    @Override
    public int getItemCount() {
        return mData.size();
    }

    public void getItemSelected(MenuItem item) {
        if(item.getTitle().equals("Delete")) {
            accessData();
        }
    }

    public void accessData() {
        database = FirebaseDatabase.getInstance();

        dbRef = database.getReference("/courses/");
        dbRef.child(code).removeValue()
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {
                            Log.i("Delete", "Course deleted.");
                        } else {
                            Log.i("Delete", "No Data.");
                        }
                    }
                });
        dbRef = database.getReference("/users/");
        dbRef.child(FireUser.getUid()).child("owned").child(cname).removeValue()
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {
                            Log.i("Delete", "Course owner deleted.");
                        } else {
                            Log.i("Delete", "No Data.");
                        }
                    }
                });
        dbRef = database.getReference("/users/");
        dbRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot data : dataSnapshot.getChildren()) {
                    data.child("classes").child(code).getRef().removeValue();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
        Intent intent = new Intent(context, MainActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        context.startActivity(intent);
    }

    public void filterList(ArrayList<Course> filteredList) {
        mData = filteredList;
        notifyDataSetChanged();
    }

    static class Holder extends RecyclerView.ViewHolder implements View.OnCreateContextMenuListener {

        private LinearLayout courses_list;
        private TextView courseName;

        public Holder(View itemView){
            super(itemView);

            itemView.setOnCreateContextMenuListener(this);
            courses_list = itemView.findViewById(R.id.course_list);
            courseName = itemView.findViewById(R.id.courseName);
        }

        @Override
        public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
            menu.add(0, v.getId(), 0, "Delete");
        }

    }
}
