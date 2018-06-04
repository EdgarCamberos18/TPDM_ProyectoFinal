package mx.edu.ittepic.tpdm_proyectofinal;


import android.os.CountDownTimer;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.Iterator;
import java.util.Map;
import java.util.Set;


public class Connection {
    private final DatabaseReference DATABASE = FirebaseDatabase.getInstance().getReference();



    protected void addNewUser(String type,String numeroCelular,String nombre,String appellidoPaterno,
                                  String apellidoMaterno,String domicilio, String fechaNacimineto,String sexo,String userU){

        DATABASE.child(type).child(userU).child("DatosGenerales").child("Nombre").setValue(nombre);
        DATABASE.child(type).child(userU).child("DatosGenerales").child("ApellidoPaterno").setValue(appellidoPaterno);
        DATABASE.child(type).child(userU).child("DatosGenerales").child("ApellidoMaterno").setValue(apellidoMaterno);
        DATABASE.child(type).child(userU).child("DatosGenerales").child("Domicilio").setValue(domicilio);
        DATABASE.child(type).child(userU).child("DatosGenerales").child("FehcaNacimiento").setValue(fechaNacimineto);
        DATABASE.child(type).child(userU).child("DatosGenerales").child("Sexo").setValue(sexo);
        DATABASE.child(type).child(userU).child("DatosGenerales").child("NumeroCelular").setValue(numeroCelular);
        DATABASE.child(type).child(userU).child("DatosGenerales").child("Estatus").setValue("Activo");

    }

    protected void addPhoto(String usuario,String filePathPotho,String typeUser){
        DATABASE.child(typeUser).child(usuario).child("Photo").setValue(filePathPotho);
    }

    protected void searchUser(final String numCell){


    }


    private String user;
    private boolean firstTime;
    private int c=0;
    protected synchronized boolean verificarUsuariosPendientes(final String usuario){

        user=usuario;
        firstTime=false;


        DATABASE.child("BabySister").child("pendiente").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Map<String,String> data = (Map<String, String>) dataSnapshot.getValue();
                if(data!=null){
                    Iterator it = data.entrySet().iterator();
                    while (it.hasNext()) {
                        Map.Entry e = (Map.Entry) it.next();
                        if (e.getKey().toString().equals(user)) ;
                        {
                            firstTime = true;
                            DATABASE.child("BabySister").child("pendiente").child(usuario).removeValue();
                        }
                    }
                }
                c++;
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        DATABASE.child("Cliente").child("pendiente").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Map<String,String> data = (Map<String, String>) dataSnapshot.getValue();
                if(data!=null){
                    Iterator it = data.entrySet().iterator();
                    while (it.hasNext()) {
                        Map.Entry e = (Map.Entry) it.next();
                        if (e.getKey().toString().equals(user)) ;
                        {
                            firstTime = true;
                            DATABASE.child("BabySister").child("pendiente").child(usuario).removeValue();
                        }
                    }
                }
                c++;
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
        if(c<2) {
            try {
                wait();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

        return firstTime;

    }
}
