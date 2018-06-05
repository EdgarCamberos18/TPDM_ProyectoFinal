package mx.edu.ittepic.tpdm_proyectofinal;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.Map;

public class InfoUsuarioActivity extends AppCompatActivity {

	TextView viewNombre, viewDireccion, viewFechaNacimiento, viewNumCelular, viewSexo;
	Button cancel;
	String phoneNumber;

	private FirebaseAuth auth;
	private FirebaseAuth.AuthStateListener authStateListener;
	private final DatabaseReference DATABASE = FirebaseDatabase.getInstance().getReference();

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_info_usuario);

		initComponents();

		auth = FirebaseAuth.getInstance();

		authStateListener = new FirebaseAuth.AuthStateListener() {
			@Override
			public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
				FirebaseUser user = firebaseAuth.getCurrentUser();

				if(user!=null) {
					if (user.isEmailVerified()) {
						return;
					}
					else {
						Toast.makeText(getApplicationContext(),"CORREO NO VERIFICADO",Toast.LENGTH_SHORT).show();

					}
				}
			}
		};
	}

	private void initComponents() {
		cancel = findViewById(R.id.cancel);
		cancel.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				InfoUsuarioActivity.this.finish();
			}
		});
	}

	@Override
	protected void onStart() {
		super.onStart();
		auth.addAuthStateListener(this.authStateListener);

		viewNombre = findViewById(R.id.nombre);
		viewDireccion = findViewById(R.id.direccion);
		viewFechaNacimiento = findViewById(R.id.fecha_nacimiento);
		viewNumCelular = findViewById(R.id.numero_celular);
		viewSexo = findViewById(R.id.sexo);

		phoneNumber = getIntent().getStringExtra("NumeroCelular");

		viewNombre.setText(getIntent().getStringExtra("Nombre"));
		viewDireccion.setText(getIntent().getStringExtra("Domicilio"));
		viewFechaNacimiento.setText(getIntent().getStringExtra("FechaNacimiento"));
		viewNumCelular.setText(getIntent().getStringExtra("NumeroCelular"));
		viewSexo.setText(getIntent().getStringExtra("Sexo"));

		viewNumCelular.setOnClickListener(new View.OnClickListener() {
			@SuppressLint("MissingPermission")
			@Override
			public void onClick(View v) {
				Intent callIntent = new Intent(Intent.ACTION_CALL);
				callIntent.setData(Uri.parse("tel:" + InfoUsuarioActivity.this.phoneNumber));
				startActivity(callIntent);
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
	protected void onRestart() {
		super.onRestart();
		auth.addAuthStateListener(authStateListener);
	}

	@Override
	protected void onStop() {
		super.onStop();
		if(authStateListener!=null){
			auth.removeAuthStateListener(authStateListener);
		}
	}
	
}
