package th.ac.kku.koysawat.phunon;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FacebookAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class LoginActivity extends AppCompatActivity implements View.OnClickListener {

    ProgressDialog progressDialog;
    private FirebaseAuth auth;
    FirebaseUser FireUser;
    private CallbackManager callbackManager;
    LoginButton fblogin;
    TextView signup;
    EditText user;
    EditText password;
    Button login;
    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        if (FireUser == null) {
            progressDialog = new ProgressDialog(this);
            setContentView(R.layout.activity_login);
            auth = FirebaseAuth.getInstance();
            signup = findViewById(R.id.register);
            signup.setOnClickListener(this);
            user = findViewById(R.id.user);
            password = findViewById(R.id.password);
            login = findViewById(R.id.login);
            FireUser = auth.getCurrentUser();
            // Email Log in
            login.setOnClickListener(this);
            // Facebook log in
            callbackManager = CallbackManager.Factory.create();
            fblogin = findViewById(R.id.login_button);
            fblogin.setReadPermissions("email", "public_profile");
            fblogin.setOnClickListener(this);
        } else {
            startActivity(new Intent(LoginActivity.this, MainActivity.class));
        }
    }

    // Facebook Result
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        callbackManager.onActivityResult(requestCode, resultCode, data);
        super.onActivityResult(requestCode, resultCode, data);
    }

    // Email sign in
    private void signIn(String email, String password) {
        if (!validateForm()) {
            return;
        }
        auth.signInWithEmailAndPassword(email, password).addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()) {
                    // Sign in success, update UI with the signed-in user's information
                    Toast.makeText(LoginActivity.this, "Log in success.", Toast.LENGTH_SHORT).show();
                    progressDialog.dismiss();
                    startActivity(new Intent(LoginActivity.this, MainActivity.class));
                } else {
                    // If sign in fails, display a message to the user.
                    Toast.makeText(LoginActivity.this, "Authentication failed.", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void fbLogin() {
        LoginManager.getInstance().registerCallback(callbackManager, new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(LoginResult loginResult) {
                handleFacebookAccessToken(loginResult.getAccessToken());

            }

            @Override
            public void onCancel() {
                Toast.makeText(LoginActivity.this, "Cancel.", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onError(FacebookException error) {
                Toast.makeText(LoginActivity.this, "Error.", Toast.LENGTH_SHORT).show();
                Log.i("Error:",error.toString());
            }
        });
    }

    private void handleFacebookAccessToken(AccessToken token) {
        AuthCredential credential = FacebookAuthProvider.getCredential(token.getToken());
        auth.signInWithCredential(credential).addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            startActivity(new Intent(LoginActivity.this, MainActivity.class));
                        } else {
                            // If sign in fails, display a message to the user.
                            Toast.makeText(LoginActivity.this, "Authentication failed.", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }

    // Check email/password form
    private boolean validateForm() {
        boolean valid = true;
        String email = user.getText().toString();
        if (TextUtils.isEmpty(email)) {
            user.setError("Required.");
            valid = false;
        } else {
            user.setError(null);
        }
        String pass = password.getText().toString();
        if (TextUtils.isEmpty(pass)) {
            password.setError("Required.");
            valid = false;
        } else {
            password.setError(null);
        }
        return valid;
    }

    @Override
    public void onClick(View v) {
        int i = v.getId();
        if (i == R.id.login) {
            progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
            progressDialog.setMessage("Loading...please wait");
            progressDialog.show();
            signIn(user.getText().toString(), password.getText().toString());
        } else if (i == R.id.login_button) {
            fbLogin();
        } else if (i == R.id.register) {
            startActivity(new Intent(LoginActivity.this,RegisterActivity.class));
        }
    }
}
