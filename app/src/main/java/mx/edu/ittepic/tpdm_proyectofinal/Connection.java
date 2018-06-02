package mx.edu.ittepic.tpdm_proyectofinal;


import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class Connection {
    private final DatabaseReference DATABASE = FirebaseDatabase.getInstance().getReference();;

    protected void addNewBabySister(String numeroCelular,String nombre,String appellidoPaterno,
                                  String apellidoMaterno,String domicilio, int edad,
                                  String perfil,int tipoServicio,String foto){



        DATABASE.child("Nineras").child(numeroCelular).child("DatosGenerales").setValue("");


    }
    protected void addNewUser(String email, String type){

        DATABASE.child(type).child("email").setValue(email);
    }
}
