package mx.edu.ittepic.tpdm_proyectofinal;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;

public class DatosGeneralesBS extends AppCompatActivity implements View.OnClickListener {
    String datos[];
    String[] usuario;

    private EditText nombre,apellidoP,apellidoM,domicilio,fechaNac,celular;
    private Button editar,cancelar;
    private Connection conexion;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_datos_generales_bs);

        editar = findViewById(R.id.editar);
        cancelar = findViewById(R.id.cancelar);

        editar.setOnClickListener(this);
        cancelar.setOnClickListener(this);

        nombre =    findViewById(R.id.nombre);
        apellidoP = findViewById(R.id.apellidoP);
        apellidoM = findViewById(R.id.apellidoM);
        domicilio = findViewById(R.id.domicilio);
        fechaNac =  findViewById(R.id.fechaNac);
        celular =   findViewById(R.id.celular);

        datos = getIntent().getStringArrayExtra("datos");

        conexion = new Connection();

        nombre.setText(datos[0]);
        apellidoP.setText(datos[1]);
        apellidoM.setText(datos[2]);
        domicilio.setText(datos[4]);
        fechaNac.setText(datos[3]);
        celular.setText(datos[6]);

        String a = editar.getText().toString();
        int ac=0;



    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.cancelar:
                Intent i = new Intent (getApplicationContext(), BabySisterActivity.class);
                i.putExtra("usuario", datos[7]);
                startActivity(i);
                break;

            case R.id.editar:
                if((editar.getText().toString().equals("Editar"))){
                    editar.setText("Guardar");
                    nombre.setEnabled(true);
                    apellidoP.setEnabled(true);
                    apellidoM.setEnabled(true);
                    domicilio.setEnabled(true);
                    fechaNac.setEnabled(true);
                    celular.setEnabled(true);
                }
                else{
                    editar.setText("Editar");
                    nombre.setEnabled(false);
                    apellidoP.setEnabled(false);
                    apellidoM.setEnabled(false);
                    domicilio.setEnabled(false);
                    fechaNac.setEnabled(false);
                    celular.setEnabled(false);

                    conexion.addNewUser("BabySister",celular.getText().toString(),nombre.getText().toString(),apellidoP.getText().toString(),apellidoM.getText().toString(),domicilio.getText().toString(),fechaNac.getText().toString(),datos[5],datos[7]);
                }
                break;

        }


    }
}
