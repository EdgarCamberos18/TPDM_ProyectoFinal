package mx.edu.ittepic.tpdm_proyectofinal;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class AdminActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin);

        initComponents();
    }

    public void initComponents(){
	    Button verNineras       = findViewById(R.id.ver_nineras);
	    Button verUsuarios      = findViewById(R.id.ver_usuarios);
	    Button cancelar         = findViewById(R.id.cancelar);

	    verNineras.setOnClickListener(new View.OnClickListener() {
		    @Override
		    public void onClick(View v) {
				startActivityForResult(new Intent(AdminActivity.this, ViewNinerasActivity.class), 0);
		    }
	    });

	    verUsuarios.setOnClickListener(new View.OnClickListener() {
		    @Override
		    public void onClick(View v) {

		    }
	    });

	    cancelar.setOnClickListener(new View.OnClickListener() {
		    @Override
		    public void onClick(View v) {
			    AdminActivity.this.finish();
		    }
	    });
    }
}
