package mx.edu.ittepic.tpdm_proyectofinal;

import android.content.Intent;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
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
import java.util.Iterator;
import java.util.Map;
import java.util.Objects;

import static android.R.layout.simple_list_item_1;
public class ViewNinerasActivity extends AppCompatActivity {

	private FirebaseAuth auth;
	private FirebaseAuth.AuthStateListener authStateListener;
	private final DatabaseReference DATABASE = FirebaseDatabase.getInstance().getReference();
	String babysister[];
	ArrayList<String> list = null;
	ListView listview;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_view_nineras);
		auth = FirebaseAuth.getInstance();

		authStateListener = new FirebaseAuth.AuthStateListener() {
			@Override
			public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
				FirebaseUser user = firebaseAuth.getCurrentUser();

				if(user!=null) {
					if (user.isEmailVerified()) {
						Toast.makeText(getApplicationContext(),"Niñeras",Toast.LENGTH_SHORT).show();
					}
					else {
						Toast.makeText(getApplicationContext(),"CORREO NO VERIFICADO",Toast.LENGTH_SHORT).show();

					}
				}
			}
		};

		Button cancelar = findViewById(R.id.cancelar);
		cancelar.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				ViewNinerasActivity.this.finish();
			}
		});
	}

	@Override
	protected void onStart() {
		super.onStart();

		auth.addAuthStateListener(this.authStateListener);
		this.listview = findViewById(R.id.listview_ninera);

		DATABASE.child("BabySister").addValueEventListener(new ValueEventListener() {
			@RequiresApi(api = Build.VERSION_CODES.KITKAT)
			@Override
			public void onDataChange(final DataSnapshot dataSnapshot) {
				Map<String,String> data = (Map<String, String>) dataSnapshot.getValue();
				if(data!=null){
					StringBuilder usr = new StringBuilder();

					for (Object o : data.entrySet()) {
						Map.Entry e = (Map.Entry) o;
						usr.append(e.getKey().toString()).append(",");

						babysister = usr.toString().split(",");
					}

					String[] array = new String[babysister.length];

					for (int i = 0; i < babysister.length; i++) {
						array[i] =  Objects.requireNonNull(dataSnapshot.child(babysister[i]).child("DatosGenerales").child("Nombre").getValue()).toString() + " " +
									Objects.requireNonNull(dataSnapshot.child(babysister[i]).child("DatosGenerales").child("ApellidoPaterno").getValue()).toString() + " " +
									Objects.requireNonNull(dataSnapshot.child(babysister[i]).child("DatosGenerales").child("ApellidoMaterno").getValue()).toString();
					}

					ViewNinerasActivity.this.list       = new ArrayList<>(Arrays.asList(array));
					final ArrayAdapter<String> adapter  = new ArrayAdapter<>(ViewNinerasActivity.this, simple_list_item_1, list);

					ViewNinerasActivity.this.listview.setAdapter(adapter);
					ViewNinerasActivity.this.listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {

						@RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
						@Override
						public void onItemClick(AdapterView<?> parent, final View view, final int position, long id) {
							final String item = (String) parent.getItemAtPosition(position);
							view.animate().setDuration(2000).alpha(0)
									.withEndAction(new Runnable() {
										@Override
										public void run() {
											Intent intent = new Intent(ViewNinerasActivity.this, InfoNineraActivity.class);
											intent.putExtra("Nombre", item);
											intent.putExtra("ID", ViewNinerasActivity.this.babysister[position]);
											intent.putExtra("Domicilio", dataSnapshot.child(ViewNinerasActivity.this.babysister[position]).child("DatosGenerales").child("Domicilio").getValue().toString());
											intent.putExtra("FechaNacimiento", dataSnapshot.child(ViewNinerasActivity.this.babysister[position]).child("DatosGenerales").child("FechaNacimiento").getValue().toString());
											intent.putExtra("NumeroCelular", dataSnapshot.child(ViewNinerasActivity.this.babysister[position]).child("DatosGenerales").child("NumeroCelular").getValue().toString());
											intent.putExtra("Sexo", dataSnapshot.child(ViewNinerasActivity.this.babysister[position]).child("DatosGenerales").child("Sexo").getValue().toString());
											startActivityForResult(intent, 0);
										}
									});
						}

					});
				}
			}

			@Override
			public void onCancelled(DatabaseError databaseError) {
				Toast.makeText(ViewNinerasActivity.this, databaseError.getMessage(), Toast.LENGTH_SHORT).show();
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
