package th.ac.kku.koysawat.phunon;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;
import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.auth.api.signin.GoogleSignInResult;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FacebookAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;

public class LoginActivity extends AppCompatActivity {

   /* private ProgressDialog progressDialog;
    private FirebaseAuth auth = FirebaseAuth.getInstance();
    private FirebaseUser FireUser = auth.getCurrentUser();
    private CallbackManager callbackManager;
    private GoogleSignInOptions gso;
    private GoogleSignInClient mGoogleSignInClient;
    private SignInButton glogin;
    private LoginButton fblogin;
    private TextView signup;
    private EditText user;
    private EditText password;
    private Button login;
    private static int RC_SIGN_IN = 9001;*/

    private Button logIn;

    private SignInButton mGoogleBtn;
    private GoogleApiClient mGoogleSignInClient;
    private static final int RC_SIGN_IN = 1;
    private static final String TAG = "0987654321";
    String user;

    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthListener;
    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        mAuth = FirebaseAuth.getInstance();
        mAuthListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                if (firebaseAuth.getCurrentUser() != null){
                    Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                    startActivity(intent);
                }
            }
        };

        mGoogleBtn = (SignInButton) findViewById(R.id.sign_in_gmail_button);

        // Configure Google Sign In
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build();

        mGoogleSignInClient = new GoogleApiClient.Builder(getApplicationContext())
                .enableAutoManage(this, new GoogleApiClient.OnConnectionFailedListener() {
                    @Override
                    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
                        Toast.makeText(LoginActivity.this, "You got Error.", Toast.LENGTH_LONG).show();
                    }
                })
                .addApi(Auth.GOOGLE_SIGN_IN_API, gso)
                .build();

        mGoogleBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                signIn();
            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();
        mAuth.addAuthStateListener(mAuthListener);
    }

    private void signIn() {
        Intent signInIntent = Auth.GoogleSignInApi.getSignInIntent(mGoogleSignInClient);
        startActivityForResult(signInIntent, RC_SIGN_IN);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        // Result returned from launching the Intent from GoogleSignInApi.getSignInIntent(...);
        if (requestCode == RC_SIGN_IN) {
            GoogleSignInResult result = Auth.GoogleSignInApi.getSignInResultFromIntent(data);
            if (result.isSuccess()){
                GoogleSignInAccount account = result.getSignInAccount();
                user = account.getDisplayName();

                firebaseAuthWithGoogle(account);
            } else {

            }
        }
    }

    private void firebaseAuthWithGoogle(GoogleSignInAccount account) {

        AuthCredential credential = GoogleAuthProvider.getCredential(account.getIdToken(), null);
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            Log.d(TAG, "signInWithCredential:success");
                            FirebaseUser user = mAuth.getCurrentUser();
//                            updateUI(user);
                        } else {
                            // If sign in fails, display a message to the user.
                            Log.w(TAG, "signInWithCredential:failure", task.getException());
                            Toast.makeText(LoginActivity.this, "Authentication failed.",
                                    Toast.LENGTH_SHORT).show();
//                            updateUI(null);
                        }

                        // ...
                    }
                });

    }
        /*if (FireUser == null) {
            setProgress();
            setContentView(R.layout.activity_login);
            init();
            mGoogleSignInClient = GoogleSignIn.getClient(this, gso);
            setListener();
        } else {
            finish();
            startActivity(new Intent(LoginActivity.this, MainActivity.class));
        }
    }*/

    /*
    //save username and password
    @Override
    protected void  onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putString("user",user.getText().toString());
        outState.putString("password",password.getText().toString());
    }
    @Override
    protected void onRestoreInstanceState(Bundle saveInstanceState){
        user.setText(saveInstanceState.getString("user"));
        password.setText(saveInstanceState.getString("password"));
    }

    // Initialization
    private void init() {
        signup = findViewById(R.id.register);
        user = findViewById(R.id.user);
        password = findViewById(R.id.password);
        login = findViewById(R.id.login);
        callbackManager = CallbackManager.Factory.create();
        fblogin = findViewById(R.id.login_button);
        fblogin.setReadPermissions("email", "public_profile");
        glogin = findViewById(R.id.sign_in_button);
        glogin.setSize(SignInButton.SIZE_WIDE);
        gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.googleIdToken))
                .requestEmail()
                .build();

        SignInButton signInGoogle = findViewById(R.id.sign_in_button);
        setGooglePlusButtonText(signInGoogle,"Sign in");
    }
    // ActionListener
    private void setListener(){
        login.setOnClickListener(this);
        fblogin.setOnClickListener(this);
        glogin.setOnClickListener(this);
        signup.setOnClickListener(this);
    }

    // Progress
    private void setProgress() {
        progressDialog = new ProgressDialog(this);
        progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        progressDialog.setMessage("Loading...please wait");
    }

    // Email sign in
    private void signIn(String email, String password) {
        if (!validateForm()) {
            return;
        }
        progressDialog.show();
        auth.signInWithEmailAndPassword(email, password).addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()) {
                    // Sign in success, update UI with the signed-in user's information
                    Toast.makeText(LoginActivity.this, "Log in success.", Toast.LENGTH_SHORT).show();
                    progressDialog.dismiss();
                    finish();
                    startActivity(new Intent(LoginActivity.this, MainActivity.class));
                } else {
                    // If sign in fails, display a message to the user.
                    Toast.makeText(LoginActivity.this, "Authentication failed.", Toast.LENGTH_SHORT).show();
                    progressDialog.dismiss();
                }
            }
        });
    }

    // Facebook Log in
    private void fbLogin() {
        LoginManager.getInstance().registerCallback(callbackManager, new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(LoginResult loginResult) {
                handleFacebookAccessToken(loginResult.getAccessToken());
                progressDialog.show();
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
                            progressDialog.dismiss();
                            finish();
                            startActivity(new Intent(LoginActivity.this, MainActivity.class));
                        } else {
                            // If sign in fails, display a message to the user.
                            Toast.makeText(LoginActivity.this, "Authentication failed.", Toast.LENGTH_SHORT).show();
                            progressDialog.dismiss();
                        }
                    }
                });
    }

    // Gmail Log in
    private void firebaseAuthWithGoogle(GoogleSignInAccount acct) {
        progressDialog.show();
        AuthCredential credential = GoogleAuthProvider.getCredential(acct.getIdToken(), null);
        auth.signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            progressDialog.dismiss();
                            finish();
                            startActivity(new Intent(LoginActivity.this, MainActivity.class));
                        } else {
                            // If sign in fails, display a message to the user.
                            Toast.makeText(LoginActivity.this, "Authentication failed.", Toast.LENGTH_SHORT).show();
                            progressDialog.dismiss();
                        }
                    }
                });
    }

    // Result
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        callbackManager.onActivityResult(requestCode, resultCode, data);
        super.onActivityResult(requestCode, resultCode, data);

            if (requestCode == RC_SIGN_IN) {
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            try {
                // Google Sign In was successful, authenticate with Firebase
                GoogleSignInAccount account = task.getResult(ApiException.class);
                firebaseAuthWithGoogle(account);
            } catch (ApiException e) {
                // Google Sign In failed, update UI appropriately
                Toast.makeText(LoginActivity.this, "Authentication failed.", Toast.LENGTH_SHORT).show();
                Log.w("Google ", "Google sign in failed", e);
            }
        }
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
            signIn(user.getText().toString(), password.getText().toString());
        } else if (i == R.id.login_button) {
            fbLogin();
        } else if (i == R.id.register) {
            startActivity(new Intent(LoginActivity.this,RegisterActivity.class));
        } else if (i == R.id.sign_in_button) {
            Intent signInIntent = mGoogleSignInClient.getSignInIntent();
            startActivityForResult(signInIntent, RC_SIGN_IN);
        }
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
    }*/
}