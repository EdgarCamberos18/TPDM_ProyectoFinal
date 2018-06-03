package mx.edu.ittepic.tpdm_proyectofinal;

import android.content.Intent;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

public class BabySisterActivity extends AppCompatActivity implements View.OnClickListener {

    private StorageReference mStorage;
    private ImageView foto;
    private static final int GALERY_INTENT = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_baby_sister);

        mStorage = FirebaseStorage.getInstance().getReference();
        foto = findViewById(R.id.foto);

        foto.setOnClickListener(this);

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.foto:
                Intent intent = new Intent(Intent.ACTION_PICK);
                intent.setType("image/*");
                startActivityForResult(intent,GALERY_INTENT);
                break;
        }



    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode == GALERY_INTENT && resultCode == RESULT_OK){
            Uri uri = data.getData();
            StorageReference filePath = mStorage.child("Fotos").child(uri.getLastPathSegment());
            filePath.putFile(uri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                    Uri descargarFoto = taskSnapshot.getDownloadUrl();
                    Glide.with(BabySisterActivity.this).load(descargarFoto).apply((new RequestOptions().fitCenter().centerCrop())).into(foto);



                    Toast.makeText(getApplicationContext(),"Foto cargada con exito",Toast.LENGTH_SHORT).show();
                }
            });
        }
        else{
            Toast.makeText(getApplicationContext(),"Error al cargara foto",Toast.LENGTH_SHORT).show();
        }
    }
}
