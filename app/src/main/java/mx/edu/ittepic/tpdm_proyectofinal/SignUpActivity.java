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

import java.util.regex.Pattern;

public class SignUpActivity extends AppCompatActivity implements View.OnClickListener {

    private Button cancelar,signUp, registrar;
    private EditText password,passwordV,email;
    private FirebaseAuth mAuth;
    private RadioButton radiob1,radiob2,radiob3, radiob4;
    private Connection conexion;
    private EditText    nombre, apellidoP, apellidoM, direccion, edad, celular;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        cancelar    = findViewById(R.id.cancelar);
        signUp      = findViewById(R.id.signUp);
        email       = findViewById(R.id.email);
        passwordV   = findViewById(R.id.passwordV);
        password    = findViewById(R.id.password);
        radiob1     = findViewById(R.id.radiob1);
        radiob2     = findViewById(R.id.radiob2);
        radiob3     = findViewById(R.id.radiob3);
        radiob4     = findViewById(R.id.radiob4);
        nombre      = findViewById(R.id.nombre);
        apellidoP   = findViewById(R.id.apellidoP);
        apellidoM   = findViewById(R.id.apellidoM);
        direccion   = findViewById(R.id.direccion);
        edad        = findViewById(R.id.edad);
        celular     = findViewById(R.id.celular);
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

        if(direccion.getText().toString().isEmpty()){
            direccion.setError("Please enter a valid email");
            direccion.requestFocus();
            return;
        }

        if(nombre.getText().toString().isEmpty()){
            nombre.setError("Please enter a valid email");
            nombre.requestFocus();
            return;
        }

        if(apellidoM.getText().toString().isEmpty()){
            apellidoM.setError("Please enter a valid email");
            apellidoM.requestFocus();
            return;
        }

        if(apellidoP.getText().toString().isEmpty()){
            apellidoP.setError("Please enter a valid email");
            apellidoP.requestFocus();
            return;
        }

        if(celular.getText().toString().isEmpty()){
            celular.setError("Please enter your cell");
            celular.requestFocus();
            return;
        }


        if(edad.getText().toString().isEmpty()){
            edad.setError("Please enter your birthdate");
            edad.requestFocus();
            return;
        }

        if(!radiob1.isChecked() && !radiob2.isChecked()){
            Toast.makeText(SignUpActivity.this, "Select a user Type", Toast.LENGTH_SHORT).show();
            return;
        }

        if(!radiob3.isChecked() && !radiob4.isChecked()){
            Toast.makeText(SignUpActivity.this, "Select your gender", Toast.LENGTH_SHORT).show();
            return;
        }

        if(radiob1.isChecked() && radiob3.isChecked()){
            Toast.makeText(SignUpActivity.this, "Por el momento solo se buscan mujeres para ser niñeras", Toast.LENGTH_SHORT).show();
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
                        String sexo = radiob3.isChecked()?"Masculino":"Femenino";
                        conexion.addNewUser(usertType,celular.getText().toString(),nombre.getText().toString(),apellidoP.getText().toString(),apellidoM.getText().toString(),direccion.getText().toString(),edad.getText().toString(),sexo,user.getUid());
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
