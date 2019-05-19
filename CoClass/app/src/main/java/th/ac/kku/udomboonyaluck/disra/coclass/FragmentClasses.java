package th.ac.kku.udomboonyaluck.disra.coclass;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
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

public class FragmentClasses extends Fragment {

    View v;
    private RecyclerView myRecyclerView;
    private List<Course> lstClasses;
    FirebaseAuth auth = FirebaseAuth.getInstance();
    FirebaseDatabase database;
    DatabaseReference dbRef;
    FirebaseUser FireUser = auth.getCurrentUser();
    String name = "",sid = "";

    public FragmentClasses(){

    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        v = inflater.inflate(R.layout.classes_fragment,container,false);
        myRecyclerView = v.findViewById(R.id.classes_recyclerView);
        myRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        return v;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        database = FirebaseDatabase.getInstance();

        lstClasses = new ArrayList<>();
        dbRef = database.getReference("/users/" + FireUser.getUid() + "/classes/");
        dbRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                lstClasses.clear();
                for(DataSnapshot snapshot:dataSnapshot.getChildren()){
                    Course course = snapshot.getValue(Course.class);
                    assert course != null;
                    lstClasses.add(course);
                    ClassRecyclerAdapter recyclerViewAdapter = new ClassRecyclerAdapter(getContext(),lstClasses);
                    myRecyclerView.setAdapter(recyclerViewAdapter);
                    registerForContextMenu(myRecyclerView);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }

    }
