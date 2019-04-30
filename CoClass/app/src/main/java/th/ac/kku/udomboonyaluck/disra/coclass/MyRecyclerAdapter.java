package th.ac.kku.udomboonyaluck.disra.coclass;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

public class MyRecyclerAdapter extends RecyclerView.Adapter<MyRecyclerAdapter.Holder>{

    private String[] mDataset;
    Context context;

    public MyRecyclerAdapter(Context context, String[] dataSet){
        this.context = context;
        this.mDataset = dataSet;
    }

    public MyRecyclerAdapter(String courses) {
    }

    @NonNull
    @Override
    public Holder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.course_list,viewGroup,false);
        final Holder holder = new Holder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull Holder holder, int i) {
        holder.setItem(i);
    }

    @Override
    public int getItemCount() {
        return mDataset.length;
    }

    class Holder extends RecyclerView.ViewHolder{

        public TextView coursename;

        public Holder(View itemView){
            super(itemView);
            coursename = itemView.findViewById(R.id.courseName);
        }

        public void setItem(int position){
            coursename.setText(mDataset[position]);
        }
    }
}
