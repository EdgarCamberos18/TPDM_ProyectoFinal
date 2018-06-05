package mx.edu.ittepic.tpdm_proyectofinal;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.annotation.RequiresApi;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
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

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Map;
import java.util.Objects;

import static android.R.layout.simple_list_item_1;

public class InfoNineraActivity extends AppCompatActivity {

	TextView viewNombre, viewDireccion, viewFechaNacimiento, viewNumCelular, viewSexo;
	Switch status;
	Button save, delete, cancel;
	String phoneNumber;

	private FirebaseAuth auth;
	private FirebaseAuth.AuthStateListener authStateListener;
	private final DatabaseReference DATABASE = FirebaseDatabase.getInstance().getReference();

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_info_ninera);

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
		this.status = findViewById(R.id.status);
		save = findViewById(R.id.save);
		cancel = findViewById(R.id.cancel);

		cancel.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				InfoNineraActivity.this.finish();
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

		delete = findViewById(R.id.delete);

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
				callIntent.setData(Uri.parse("tel:" + InfoNineraActivity.this.phoneNumber));
				startActivity(callIntent);
			}
		});


		DATABASE.child("BabySister").addValueEventListener(new ValueEventListener() {
			@RequiresApi(api = Build.VERSION_CODES.KITKAT)
			@Override
			public void onDataChange(final DataSnapshot dataSnapshot) {
				Map<String,String> data = (Map<String, String>) dataSnapshot.getValue();
				if(data != null){
					if (dataSnapshot.child(getIntent().getStringExtra("ID")).child("DatosGenerales").child("Estatus").getValue().toString().equals("Activo"))
						InfoNineraActivity.this.status.setChecked(true);
					else
						InfoNineraActivity.this.status.setChecked(false);
				}
			}

			@Override
			public void onCancelled(DatabaseError databaseError) {
				Toast.makeText(InfoNineraActivity.this, databaseError.getMessage(), Toast.LENGTH_SHORT).show();
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
