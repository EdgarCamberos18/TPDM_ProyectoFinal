package mx.edu.ittepic.tpdm_proyectofinal;

import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.util.Iterator;
import java.util.Map;

public class BabySisterActivity extends AppCompatActivity implements View.OnClickListener {

    private StorageReference mStorage;
    private ImageView foto;
    private static final int GALERY_INTENT = 1;
    private Connection conexion;
    private String usuario;
    private final DatabaseReference DATABASE = FirebaseDatabase.getInstance().getReference();
    private Button datosGenerales,proximosEvent,historial;
    private TextView nombreN;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_baby_sister);

        usuario = getIntent().getStringExtra("usuario");
        conexion = new Connection();
        getDatos();
        mStorage = FirebaseStorage.getInstance().getReference();
        foto = findViewById(R.id.foto);
        datosGenerales = findViewById(R.id.datosGenerales);
        nombreN = findViewById(R.id.nombreN);
        historial = findViewById(R.id.historial);
        proximosEvent = findViewById(R.id.programarEventos);


        foto.setOnClickListener(this);
        datosGenerales.setOnClickListener(this);
        historial.setOnClickListener(this);
        proximosEvent.setOnClickListener(this);

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.foto:
                Intent intent = new Intent(Intent.ACTION_PICK);
                intent.setType("image/*");
                startActivityForResult(intent,GALERY_INTENT);
                break;
            case R.id.datosGenerales:
                Intent i = new Intent (getApplicationContext(), DatosGeneralesBS.class);
                datosgenerales[7]=usuario;
                i.putExtra("datos", datosgenerales);
                startActivity(i);
                break;
            case R.id.programarEventos:
                Intent it = new Intent (getApplicationContext(), ServiciosActivity.class);
                it.putExtra("usuario", usuario);
                startActivity(it);
                break;
            case R.id.historial:
                /*Intent intent3 = new Intent(Intent.ACTION_PICK);
                intent3.setType("image/*");
                startActivityForResult(intent,GALERY_INTENT);*/
                break;
        }



    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == GALERY_INTENT && resultCode == RESULT_OK){
            Uri uri = data.getData();
            StorageReference filePath = mStorage.child("Fotos").child(usuario).child(uri.getLastPathSegment());
            filePath.putFile(uri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                    Uri descargarFoto = taskSnapshot.getDownloadUrl();
                    conexion.addPhoto(usuario,descargarFoto.toString(),"BabySister");
                    Toast.makeText(getApplicationContext(),"Foto cargada con exito",Toast.LENGTH_SHORT).show();
                }
            });
        }
        else{
            Toast.makeText(getApplicationContext(),"Error al cargara foto",Toast.LENGTH_SHORT).show();
        }
    }


    String datosgenerales[] = new String[8];
    private void getDatos(){
        DATABASE.child("BabySister").child(usuario).child("Photo").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if(dataSnapshot.getValue()!=null) {
                    Uri fotoUri = Uri.parse(dataSnapshot.getValue().toString());
                    Glide.with(BabySisterActivity.this).load(fotoUri).apply((new RequestOptions().fitCenter().centerCrop())).into(foto);
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });


        DATABASE.child("BabySister").child(usuario).child("DatosGenerales").child("Nombre").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                datosgenerales[0]=dataSnapshot.getValue().toString();
                            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        DATABASE.child("BabySister").child(usuario).child("DatosGenerales").child("ApellidoPaterno").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                datosgenerales[1]=dataSnapshot.getValue().toString();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        DATABASE.child("BabySister").child(usuario).child("DatosGenerales").child("ApellidoMaterno").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                datosgenerales[2]=dataSnapshot.getValue().toString();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        DATABASE.child("BabySister").child(usuario).child("DatosGenerales").child("FechaNacimiento").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                datosgenerales[3]=dataSnapshot.getValue().toString();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        DATABASE.child("BabySister").child(usuario).child("DatosGenerales").child("Domicilio").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                datosgenerales[4]=dataSnapshot.getValue().toString();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        DATABASE.child("BabySister").child(usuario).child("DatosGenerales").child("Sexo").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                datosgenerales[5]=dataSnapshot.getValue().toString();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        DATABASE.child("BabySister").child(usuario).child("DatosGenerales").child("NumeroCelular").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                datosgenerales[6]=dataSnapshot.getValue().toString();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        DATABASE.child("BabySister").child(usuario).child("DatosGenerales").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                nombreN.setText(dataSnapshot.child("Nombre").getValue().toString()+" "+dataSnapshot.child("ApellidoPaterno").getValue().toString()+" "+dataSnapshot.child("ApellidoMaterno").getValue().toString());
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });



    }


    private void getServicio(){

    }
}
