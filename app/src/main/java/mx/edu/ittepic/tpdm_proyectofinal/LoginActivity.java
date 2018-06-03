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
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.Iterator;
import java.util.Map;

public class LoginActivity extends AppCompatActivity implements View.OnClickListener {

    private Button registrar,login,provando;
    private FirebaseAuth auth;
    private FirebaseAuth.AuthStateListener authStateListener;
    private EditText password, email;
    String usuario;
    boolean emailV = false;
    private final DatabaseReference DATABASE = FirebaseDatabase.getInstance().getReference();;



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

                if(user!=null) {
                    if (user.isEmailVerified() && user.getUid() != null) {
                        emailV=user.isEmailVerified();
                        usuario = user.getUid();
                        if (usuariosCliente != null)
                            for (String usr : usuariosCliente) {
                                if (usr.equals(usuario)) {
                                    startActivity(new Intent(getApplicationContext(), ClienteActivity.class));
                                    return;
                                }
                            }
                    }
                    else {
                        Toast.makeText(getApplicationContext(),"CORREO NO VERIFICADO",Toast.LENGTH_SHORT);
                    }
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

                boolean s= a.verificarUsuariosPendientes(usuario);
                int k=0;
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
                else {
                    if (emailV) {
                        if (usuariosCliente != null) {
                            for (String usr : usuariosCliente) {
                                if (usr.equals(usuario)) {
                                    startActivity(new Intent(getApplicationContext(), ClienteActivity.class));
                                    return;
                                }
                            }
                        }
                        if (babysister != null) {
                            for (String usr : babysister) {
                                if (usr.equals(usuario)) {
                                    startActivity(new Intent(getApplicationContext(), BabySisterActivity.class));
                                    return;
                                }
                            }
                        }

                        startActivity(new Intent(getApplicationContext(), AdminActivity.class));
                    }
                    else
                        Toast.makeText(getApplicationContext(),"El email no ha sido verificado aun",Toast.LENGTH_SHORT).show();

                }
            }
        });
    }


    @Override
    protected void onPause() {
        super.onPause();
        auth.addAuthStateListener(authStateListener);
    }

    @Override
    protected void onResume() {
        super.onResume();
        auth.addAuthStateListener(authStateListener);
    }

    @Override
    protected void onStop() {
        super.onStop();
        if(authStateListener!=null){
            auth.removeAuthStateListener(authStateListener);
        }
    }

    int tipoCliente =  0;
    String usuariosCliente [];
    String babysister[];

    @Override
    protected void onStart() {
        super.onStart();
        auth.addAuthStateListener(authStateListener);
        DATABASE.child("Cliente").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Map<String,String> data = (Map<String, String>) dataSnapshot.getValue();
                if(data!=null){
                    String usr="";
                    Iterator it = data.entrySet().iterator();
                    while (it.hasNext()) {
                        Map.Entry e = (Map.Entry) it.next();
                        usr+=e.getKey().toString()+",";

                        usuariosCliente = usr.split(",");
                    }
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        DATABASE.child("BabySister").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Map<String,String> data = (Map<String, String>) dataSnapshot.getValue();
                if(data!=null){
                    String usr="";
                    Iterator it = data.entrySet().iterator();
                    while (it.hasNext()) {
                        Map.Entry e = (Map.Entry) it.next();
                        usr+=e.getKey().toString()+",";

                        babysister = usr.split(",");
                    }
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }



}
