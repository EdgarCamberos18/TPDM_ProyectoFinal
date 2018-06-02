package mx.edu.ittepic.tpdm_proyectofinal;

import android.content.Intent;
import android.media.MediaPlayer;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class LoginActivity extends AppCompatActivity implements View.OnClickListener {

    private Button registrar,login,provando;
    private FirebaseAuth auth;
    private FirebaseAuth.AuthStateListener authStateListener;
    private EditText password, email;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        provando = findViewById(R.id.provando);
        provando.setOnClickListener(this);

        password = findViewById(R.id.password);
        email = findViewById(R.id.email);

        registrar=findViewById(R.id.registrar);
        registrar.setOnClickListener(this);

        login=findViewById(R.id.login);
        login.setOnClickListener(this);

        auth = FirebaseAuth.getInstance();
        authStateListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser user = firebaseAuth.getCurrentUser();
                if(user !=null){
                    Toast.makeText(getApplicationContext(), "Inicio Sesion", Toast.LENGTH_SHORT).show();
                }
            }
        };


    }



    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.registrar:
                startActivity(new Intent(this, SignUpActivity.class));
                break;

            case R.id.login:
                ingresar();
                break;

            case R.id.provando:
                Connection a = new Connection();
                a.addNewBabySister("12","a","a","a","a",12,"as",1,"as");
                break;
        }
    }

    private void ingresar() {
        String email = this.email.getText().toString().trim();
        String password = this.password.getText().toString().trim();

        if(email.isEmpty()){
            this.email.setError("E-mail is required");
            this.email.requestFocus();
            return;
        }
        if(password.isEmpty()){
            this.password.setError("Password is required");
            this.password.requestFocus();
            return;
        }

        if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            this.email.setError("Please enter a valid email");
            this.email.requestFocus();
            return;
        }

        auth.signInWithEmailAndPassword(email,password).addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if(!task.isSuccessful()){
                    Toast.makeText(getApplicationContext(), task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                }
            }
        });
    }


    @Override
    protected void onStop() {
        super.onStop();
        if(authStateListener!=null){
            auth.addAuthStateListener(authStateListener);
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
        auth.addAuthStateListener(authStateListener);
    }



}
