package th.ac.kku.koysawat.phunon;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.facebook.Profile;
import com.facebook.login.LoginManager;
import com.facebook.login.widget.ProfilePictureView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private FirebaseAuth auth;
    TextView userTxt;
    Button signout;
    ProfilePictureView profile;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        signout = findViewById(R.id.signout);
        signout.setOnClickListener(this);
        userTxt = findViewById(R.id.userShow);
        userTxt.setText("User: " + user.getEmail() + "\nUID: " + user.getUid());
        Profile.getCurrentProfile();
    }

    public void signOut() {
        auth.signOut();
        LoginManager.getInstance().logOut();
        finish();
    }

    @Override
    public void onClick(View v) {
        int i = v.getId();
        if (i == R.id.signout) {
            signOut();
            Toast.makeText(MainActivity.this, "Sign out.", Toast.LENGTH_SHORT).show();
        }
    }
}
