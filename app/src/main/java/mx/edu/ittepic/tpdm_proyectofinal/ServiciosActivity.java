package mx.edu.ittepic.tpdm_proyectofinal;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.Map;

public class ServiciosActivity extends AppCompatActivity {

    private final DatabaseReference DATABASE = FirebaseDatabase.getInstance().getReference();
    private String usuario;
    private ListView lista;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_servicios);

        usuario = getIntent().getStringExtra("usuario");
        lista = findViewById(R.id.lista);


    }


    String idServicio[];
    String datosServicio[];
    private void getElementsForList(){
        DATABASE.child("BabySister").child(usuario).child("servicio").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Map<String,String> data = (Map<String, String>) dataSnapshot.getValue();
                if(data!=null){
                    String usr="";
                    Iterator it = data.entrySet().iterator();
                    while (it.hasNext()) {
                        Map.Entry e = (Map.Entry) it.next();
                        usr+=e.getKey().toString()+",";
                    }
                    idServicio = usr.split(",");
                }
                datosServicio = new String[idServicio.length];

                for(int i=0;i< idServicio.length;i++){
                    datosServicio[i]=(dataSnapshot.child(idServicio[i]).child("Usuario").getValue().toString()+"\n"+dataSnapshot.child(idServicio[i]).child("Fecha").getValue().toString()+"\n"+dataSnapshot.child(idServicio[i]).child("HoraInicio").getValue().toString()+"\n"+dataSnapshot.child(idServicio[i]).child("Domicilio").getValue().toString()+"\n");
                }


            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }


}
