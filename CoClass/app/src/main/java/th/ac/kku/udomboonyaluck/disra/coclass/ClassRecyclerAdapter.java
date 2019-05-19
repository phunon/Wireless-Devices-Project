package th.ac.kku.udomboonyaluck.disra.coclass;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.List;

public class ClassRecyclerAdapter extends RecyclerView.Adapter<ClassRecyclerAdapter.Holder> {

    private List<Course> mData;
    Context context;

    public ClassRecyclerAdapter(Context context, List<Course> mData) {
        this.mData = mData;
        this.context = context;
    }

    @NonNull
    @Override
    public Holder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.classes_list,viewGroup,false);
        final Holder holder = new Holder(view);

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

    static class Holder extends RecyclerView.ViewHolder{

        private LinearLayout classes_list;
        private TextView className;

        public Holder(View itemView){
            super(itemView);

            classes_list = itemView.findViewById(R.id.classes_list);
            className = itemView.findViewById(R.id.className);
        }
    }
}
