package th.ac.kku.udomboonyaluck.disra.coclass;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class FragmentCourses extends Fragment {

    View v;
    private RecyclerView myRecyclerView;
    private List<Course> lstCourses;
    FirebaseAuth auth = FirebaseAuth.getInstance();
    FirebaseDatabase database;
    DatabaseReference dbRef;
    FirebaseUser FireUser = auth.getCurrentUser();
    String name = "",sid = "";
    CoursesRecyclerAdapter recyclerViewAdapter;

    public FragmentCourses(){
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        v = inflater.inflate(R.layout.courses_fragment,container,false);
        myRecyclerView = v.findViewById(R.id.courses_recyclerView);
        myRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        return v;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        database = FirebaseDatabase.getInstance();

        dbRef = database.getReference("/users/");
        dbRef.child(FireUser.getUid()).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                User user = dataSnapshot.getValue(User.class);
                assert user != null;
                name = (String) user.toMap().get("username");
                sid = (String) user.toMap().get("id");
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
        lstCourses = new ArrayList<>();
        dbRef = database.getReference("/users/" + FireUser.getUid() + "/owned/");
        dbRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                lstCourses.clear();
                for(DataSnapshot snapshot:dataSnapshot.getChildren()){
                    Course course = snapshot.getValue(Course.class);
                    assert course != null;
                    lstCourses.add(course);
                    recyclerViewAdapter = new CoursesRecyclerAdapter(getContext(),lstCourses);
                    myRecyclerView.setAdapter(recyclerViewAdapter);
                    registerForContextMenu(myRecyclerView);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
        recyclerViewAdapter = new CoursesRecyclerAdapter(getContext(),lstCourses);
    }

    @Override
    public boolean onContextItemSelected(MenuItem item) {
        recyclerViewAdapter.getItemSelected(item);
        return super.onContextItemSelected(item);

    }
}
