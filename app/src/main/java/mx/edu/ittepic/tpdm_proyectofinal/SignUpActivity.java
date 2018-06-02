package mx.edu.ittepic.tpdm_proyectofinal;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthUserCollisionException;
import com.google.firebase.auth.FirebaseUser;

public class SignUpActivity extends AppCompatActivity implements View.OnClickListener {

    private Button cancelar,signUp;
    private EditText password,passwordV,email;
    private FirebaseAuth mAuth;
    private RadioButton radiob1,radiob2;
    private Connection conexion;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        cancelar = findViewById(R.id.cancelar);
        signUp =findViewById(R.id.signUp);
        email =findViewById(R.id.email);
        passwordV = findViewById(R.id.passwordV);
        password = findViewById(R.id.password);
        radiob1 = findViewById(R.id.radiob1);
        radiob2 = findViewById(R.id.radiob2);
        conexion = new Connection();



        cancelar.setOnClickListener(this);
        signUp.setOnClickListener(this);


        mAuth = FirebaseAuth.getInstance();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.cancelar:
                startActivity(new Intent(this, LoginActivity.class));
                break;
        }

        switch (v.getId()) {
            case R.id.signUp:
                RegistrarUsuario();
                break;
        }


    }
    String correo;
    private void RegistrarUsuario() {

        correo = email.getText().toString().trim();
        String contrasena = password.getText().toString().trim();
        String contrasenaV = passwordV.getText().toString().trim();

        if(correo.isEmpty()){
            email.setError("E-mail is required");
            email.requestFocus();
            return;
        }
        if(contrasena.isEmpty()){
            password.setError("Password is required");
            password.requestFocus();
            return;
        }
        if(contrasenaV.isEmpty()){
            passwordV.setError("Verify your password is required");
            passwordV.requestFocus();
            return;
        }

        if (!Patterns.EMAIL_ADDRESS.matcher(correo).matches()) {
            email.setError("Please enter a valid email");
            email.requestFocus();
            return;
        }



        if(!radiob1.isChecked() && !radiob2.isChecked()){
            Toast.makeText(SignUpActivity.this, "Select a user Type", Toast.LENGTH_SHORT).show();
            return;
        }


        if(contrasena.equals(contrasenaV))
            mAuth.createUserWithEmailAndPassword(correo, contrasena).addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {
                    if(task.isSuccessful()){
                        Toast.makeText(getApplicationContext(),"User Register Successful",Toast.LENGTH_LONG).show();
                        FirebaseUser user = mAuth.getCurrentUser();
                        String usertType= radiob1.isChecked()?"BabySister":"Cliente";
                        conexion.addNewUser(correo,usertType);
                        user.sendEmailVerification();
                        startActivity(new Intent(getApplicationContext(), LoginActivity.class));
                    }
                    else {

                        if (task.getException() instanceof FirebaseAuthUserCollisionException) {
                            Toast.makeText(SignUpActivity.this, "You are already registered", Toast.LENGTH_SHORT).show();

                        } else {
                            Toast.makeText(getApplicationContext(), task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                        }

                    }
                }
            });
        else {
            passwordV.setError("Your password it does not match");
            passwordV.requestFocus();
        }


    }
}
