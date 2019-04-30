package th.ac.kku.udomboonyaluck.disra.coclass;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.facebook.CallbackManager;
import com.google.firebase.auth.GoogleAuthProvider;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class LoginActivity extends AppCompatActivity {

    private FirebaseAuth auth = FirebaseAuth.getInstance();
    FirebaseUser FireUser = auth.getCurrentUser();
    private GoogleSignInOptions gso;
    private GoogleSignInClient mGoogleSignInClient;
    private SignInButton glogin;
    private EditText email;
    private EditText password;
    private Button login,create;
    private static int RC_SIGN_IN = 9001;
    private CallbackManager callbackManager;
    private Dialog dialog;
    private FirebaseDatabase database;
    private DatabaseReference dbRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (FireUser == null) {
            setContentView(R.layout.activity_login);
            init();
            mGoogleSignInClient = GoogleSignIn.getClient(this, gso);
            setListener();
        } else {
            finish();
            startActivity(new Intent(LoginActivity.this, MainActivity.class));
        }
    }

    private void init() {
        auth = FirebaseAuth.getInstance();
        email = findViewById(R.id.email);
        password = findViewById(R.id.password);
        login = findViewById(R.id.login);
        create = findViewById(R.id.createUser);
        callbackManager = CallbackManager.Factory.create();
        glogin = findViewById(R.id.sign_in_button);
        glogin.setSize(SignInButton.SIZE_WIDE);
        gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.googleIdToken))
                .requestEmail()
                .build();

        dialog = new Dialog(this);
        SignInButton signInGoogle = findViewById(R.id.sign_in_button);
        setGooglePlusButtonText(signInGoogle,"Sign in");

        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                firebaseLogin(email.getText().toString(),password.getText().toString());
            }
        });
        database = FirebaseDatabase.getInstance();
        dbRef = database.getReference("");
    }

    protected void setGooglePlusButtonText(SignInButton signInButton, String buttonText) {
        // Find the TextView that is inside of the SignInButton and set its text
        for (int i = 0; i < signInButton.getChildCount(); i++) {
            View v = signInButton.getChildAt(i);

            if (v instanceof TextView) {
                TextView tv = (TextView) v;
                tv.setText(buttonText);
                return;
            }
        }
    }

    private void setListener(){
        glogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent signInIntent = mGoogleSignInClient.getSignInIntent();
                startActivityForResult(signInIntent, RC_SIGN_IN);
            }
        });

        create.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.setContentView(R.layout.create_pop);
                dialog.show();

                final EditText username,email,pass,rePass,id;
                Button submit;

                username = dialog.findViewById(R.id.username);
                email = dialog.findViewById(R.id.email);
                pass = dialog.findViewById(R.id.password);
                rePass = dialog.findViewById(R.id.rePass);
                submit = dialog.findViewById(R.id.submit);
                id = dialog.findViewById(R.id.yourID);

                submit.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if(pass.getText().toString().equals(rePass.getText().toString())){
                            createAccount(email.getText().toString().trim(),pass.getText().toString().trim(),username.getText().toString().trim(),id.getText().toString());
                        } else {
                            Toast.makeText(LoginActivity.this,"Your Re-Password is not correct.",Toast.LENGTH_SHORT).show();
                        }
                    }
                });

            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        callbackManager.onActivityResult(requestCode, resultCode, data);
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == RC_SIGN_IN) {
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            try {
                // Google Sign In was successful, authenticate with Firebase
                GoogleSignInAccount account = task.getResult(ApiException.class);
                firebaseAuthWithGoogle(Objects.requireNonNull(account));
            } catch (ApiException e) {
                // Google Sign In failed, update UI appropriately
                Toast.makeText(LoginActivity.this, "Authentication failed.", Toast.LENGTH_SHORT).show();
                Log.w("Google ", "Google sign in failed", e);
            }
        }
    }
    private void firebaseAuthWithGoogle(GoogleSignInAccount acct) {
        AuthCredential credential = GoogleAuthProvider.getCredential(acct.getIdToken(), null);
        auth.signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            FireUser = auth.getCurrentUser();
                            final Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                            assert FireUser != null;

                            dbRef.child("users").orderByChild("email").equalTo(FireUser.getEmail()).addValueEventListener(new ValueEventListener() {
                                @Override
                                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                    if(dataSnapshot.exists()){
                                        dialog.dismiss();
                                        finish();
                                        startActivity(intent);
                                    } else {
                                        Button yes,no;
                                        TextView showName;
                                        final EditText username,id;
                                        dialog.setContentView(R.layout.popup);
                                        dialog.show();

                                        yes = dialog.findViewById(R.id.yes);
                                        no = dialog.findViewById(R.id.no);
                                        showName = dialog.findViewById(R.id.showName);
                                        username = dialog.findViewById(R.id.username);
                                        id = dialog.findViewById(R.id.yourID);

                                        showName.setText(FireUser.getDisplayName());

                                        yes.setOnClickListener(new View.OnClickListener() {
                                            @Override
                                            public void onClick(View v) {

                                                if(username.getText().toString().equals("")){
                                                    writeNewUser(FireUser.getUid(),FireUser.getDisplayName(),FireUser.getEmail(),id.getText().toString());
                                                } else {
                                                    writeNewUser(FireUser.getUid(),username.getText().toString(),FireUser.getEmail(),id.getText().toString());
                                                }

                                                dialog.dismiss();
                                                finish();
                                                startActivity(intent);
                                            }
                                        });

                                        no.setOnClickListener(new View.OnClickListener() {
                                            @Override
                                            public void onClick(View v) {
                                                username.setVisibility(View.VISIBLE);
                                            }
                                        });
                                    }
                                }

                                @Override
                                public void onCancelled(@NonNull DatabaseError databaseError) {

                                }
                            });
                        } else {
                            // If sign in fails, display a message to the user.
                            Toast.makeText(LoginActivity.this, "Authentication failed.", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }

    private void firebaseLogin(String email,String password){
        auth.signInWithEmailAndPassword(email, password).addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()) {
                    // Sign in success, update UI with the signed-in user's information
                    Toast.makeText(LoginActivity.this, "Log in success.", Toast.LENGTH_SHORT).show();
                    finish();
                    startActivity(new Intent(LoginActivity.this, MainActivity.class));
                } else {
                    // If sign in fails, display a message to the user.
                    Toast.makeText(LoginActivity.this, "Authentication failed.", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void writeNewUser(String userID, String name, String email, String id) {
        String key = dbRef.child("users").push().getKey();
        User user = new User(name,email,id);
        Map<String, Object> userValues = user.toMap();

        Map<String, Object> childUpdates = new HashMap<>();
        childUpdates.put("/users/" + userID, userValues);
        dbRef.updateChildren(childUpdates);
    }

    private void createAccount(final String email, final String password, final String username, final String id) {
        auth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            Toast.makeText(LoginActivity.this,"Create Success!",Toast.LENGTH_SHORT).show();
                            dialog.dismiss();
                            FireUser = auth.getCurrentUser();
                            assert FireUser != null;
                            writeNewUser(FireUser.getUid(),username,FireUser.getEmail(),id);
                            firebaseLogin(email,password);
                        } else {
                            Toast.makeText(LoginActivity.this,"Create Fail!",Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }

}
