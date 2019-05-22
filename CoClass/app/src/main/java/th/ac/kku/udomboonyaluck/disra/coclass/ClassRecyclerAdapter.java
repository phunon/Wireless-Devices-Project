package th.ac.kku.udomboonyaluck.disra.coclass;

import android.app.Activity;
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
import android.widget.AdapterView;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.List;

public class ClassRecyclerAdapter extends RecyclerView.Adapter<ClassRecyclerAdapter.Holder> {

    private List<Course> mData;
    private List<Course> data;
    static Context context;

    public ClassRecyclerAdapter(Context context, List<Course> mData) {
        this.mData = mData;
        this.context = context;
        data = new ArrayList<>(mData);
    }

    @NonNull
    @Override
    public Holder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.classes_list,viewGroup,false);
        final Holder holder = new Holder(view);
        final Vibrator vibe = (Vibrator) context.getSystemService(Context.VIBRATOR_SERVICE);

        holder.classes_list.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context,ClassActivity.class);
                intent.putExtra("courseName",mData.get(holder.getAdapterPosition()).getCoursename());
                intent.putExtra("teacher",mData.get(holder.getAdapterPosition()).getTeacher());
                intent.putExtra("code",mData.get(holder.getAdapterPosition()).getCode());
                context.startActivity(intent);
            }
        });

        holder.classes_list.setOnLongClickListener(new View.OnLongClickListener() {
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
        holder.className.setText(mData.get(position).getCoursename());
    }

    @Override
    public int getItemCount() {
        return mData.size();
    }

    public  void getItemSelected(MenuItem item) {
        if(item.getTitle().equals("Edit2")) {
            Toast.makeText(context,"Edited",Toast.LENGTH_LONG).show();
        } else if(item.getTitle().equals("Delete2")) {
            Toast.makeText(context,"Deleted",Toast.LENGTH_LONG).show();
        }
    }

    public void filterList(ArrayList<Course> filterList) {
        mData = filterList;
        notifyDataSetChanged();
    }

    static class Holder extends RecyclerView.ViewHolder {

        private LinearLayout classes_list;
        private TextView className;

        public Holder(View itemView) {
            super(itemView);

            classes_list = itemView.findViewById(R.id.classes_list);
            className = itemView.findViewById(R.id.className);
        }
    }
}
