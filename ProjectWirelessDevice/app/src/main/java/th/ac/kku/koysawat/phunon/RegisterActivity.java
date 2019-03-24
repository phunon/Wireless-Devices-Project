package th.ac.kku.koysawat.phunon;

import android.os.Bundle;
//>>>>>>> 96f594f061424c534590ad5f0ed98ef28c0c395e
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.UserProfileChangeRequest;

public class RegisterActivity extends AppCompatActivity implements View.OnClickListener {


    private FirebaseAuth auth;
    EditText user,email,password,conPass;
    Button signup;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        auth = FirebaseAuth.getInstance();
        user = findViewById(R.id.userNameEdt);
        email = findViewById(R.id.Email);
        password = findViewById(R.id.Pass);
        conPass = findViewById(R.id.ConPass);
        signup = findViewById(R.id.signUp);
        signup.setOnClickListener(this);
    }

    private void createAccount(String email, String password) {
        if (!validateForm()) {
            return;
        }
        auth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            UserProfileChangeRequest profileUpdates = new UserProfileChangeRequest.Builder()
                                    .setDisplayName(user.getText().toString())
                                    .build();
                            Toast.makeText(RegisterActivity.this, "Register success.",Toast.LENGTH_SHORT).show();
                            finish();
                        } else {
                            // If sign in fails, display a message to the user.
                            Toast.makeText(RegisterActivity.this, "Register failed.",Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }

    private boolean validateForm() {
        boolean valid = true;
        String username = user.getText().toString();
        if (TextUtils.isEmpty(username)) {
            user.setError("Required.");
            valid = false;
        } else {
            user.setError(null);
        }
        String em = email.getText().toString();
        if (TextUtils.isEmpty(em)) {
            email.setError("Required.");
            valid = false;
        } else {
            email.setError(null);
        }
        String pass = password.getText().toString();
        if (TextUtils.isEmpty(pass)) {
            password.setError("Required.");
            valid = false;
        } else {
            if (pass.length() < 6) {
                Toast.makeText(RegisterActivity.this, "Password must be at least 6 characters.",Toast.LENGTH_LONG).show();
            } else {
                password.setError(null);
            }
        }
        String conpass = conPass.getText().toString();
        if (TextUtils.isEmpty(conpass)) {
            conPass.setError("Required.");
            valid = false;
        } else {
            if (!conpass.equals(pass)) {
                Toast.makeText(RegisterActivity.this, "Password not matched." + conpass + " " + pass,Toast.LENGTH_LONG).show();
            } else {
                conPass.setError(null);
            }
        }
        return valid;
    }

    @Override
    public void onClick(View v) {
        int i = v.getId();
        if (i == R.id.signUp) {
            createAccount(email.getText().toString(), password.getText().toString());
        }
    }

}
