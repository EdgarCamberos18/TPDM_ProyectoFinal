package mx.edu.ittepic.tpdm_proyectofinal;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class DatosGenerales extends AppCompatActivity {

	private boolean     ES_USUARIO;
	private EditText    nombre, apellidoP, apellidoM, direccion, edad, contrasena;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_datos_generales);

		initComponents();
	}

	private void initComponents(){
		ES_USUARIO          = getIntent().getBooleanExtra("tipo_usuario", true);

		Button registrar    = findViewById(R.id.registrar);
		Button cancelar     = findViewById(R.id.cancelar);

		nombre              = findViewById(R.id.nombre);
		apellidoP           = findViewById(R.id.apellidoP);
		apellidoM           = findViewById(R.id.apellidoM);
		direccion           = findViewById(R.id.direccion);
		edad                = findViewById(R.id.edad);
		contrasena          = findViewById(R.id.contrasena);

		registrar.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				if (ES_USUARIO){
					// registrar usuario
					Toast.makeText(DatosGenerales.this, "Usuario", Toast.LENGTH_SHORT).show();
				} else {
					// registrar niñera
					Toast.makeText(DatosGenerales.this, "Niñera", Toast.LENGTH_SHORT).show();
				}
			}
		});

		cancelar.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				DatosGenerales.this.finish();
			}
		});
	}
}
