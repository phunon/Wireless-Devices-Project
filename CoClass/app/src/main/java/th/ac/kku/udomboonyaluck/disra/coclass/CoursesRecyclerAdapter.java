package th.ac.kku.udomboonyaluck.disra.coclass;

import android.content.Context;
import android.content.Intent;
import android.os.Vibrator;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
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

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.List;

public class CoursesRecyclerAdapter extends RecyclerView.Adapter<CoursesRecyclerAdapter.Holder> {

    private List<Course> mData;
    Context context;
    private FirebaseDatabase database;
    private DatabaseReference dbRef;

    public CoursesRecyclerAdapter(Context context, List<Course> mData) {
        this.mData = mData;
        this.context = context;
    }

    @NonNull
    @Override
    public Holder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
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

    public  void getItemSelected(MenuItem item) {
        if(item.getTitle().equals("Edit")) {
            Toast.makeText(context,"Edited",Toast.LENGTH_LONG).show();
        } else if(item.getTitle().equals("Delete")) {
            Toast.makeText(context,"Deleted",Toast.LENGTH_LONG).show();
        }
    }

    public void accessData() {
        database = FirebaseDatabase.getInstance();
        /*
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
        });*/
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
            menu.setHeaderTitle("Select The Action");
            menu.add(0, v.getId(), 0, "Edit");
            menu.add(0, v.getId(), 0, "Delete");
        }

    }
}
