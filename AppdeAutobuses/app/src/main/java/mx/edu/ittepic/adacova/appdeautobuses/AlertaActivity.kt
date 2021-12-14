package mx.edu.ittepic.adacova.appdeautobuses

import android.Manifest
import android.content.pm.PackageManager
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.google.android.gms.location.LocationServices
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.GeoPoint
import kotlinx.android.synthetic.main.activity_alerta.*
import kotlinx.android.synthetic.main.activity_generar_reporte.*

class AlertaActivity : AppCompatActivity() {
    private var posicion = ArrayList<Data>()

    private var firebase = FirebaseFirestore.getInstance()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_alerta)
        if (checkSelfPermission(Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_DENIED) {
            ActivityCompat.requestPermissions(this, arrayOf(Manifest.permission.ACCESS_FINE_LOCATION), 1)
        }
       //miUbicacion()
        botonalerta.setOnClickListener {
            if (ContextCompat.checkSelfPermission(
                    this,
                    Manifest.permission.ACCESS_FINE_LOCATION
                ) != PackageManager.PERMISSION_GRANTED &&
                ContextCompat.checkSelfPermission(
                    this,
                    Manifest.permission.ACCESS_COARSE_LOCATION
                ) != PackageManager.PERMISSION_GRANTED) {
                return@setOnClickListener
            }
            LocationServices.getFusedLocationProviderClient(this).lastLocation.addOnSuccessListener {
                val geoPosicion = GeoPoint(it.latitude, it.longitude)
                txtUbicacion.setText("${it.latitude}"+","+"${it.longitude}")

                var datosInsertar = hashMapOf(
                    "ubicacion" to txtUbicacion.text.toString(),
                )
                firebase.collection("ubicacionalerta")
                    .add(datosInsertar as Any)
                    .addOnSuccessListener {
                        alerta("Se mando la ubicacion!")
                    }
                    .addOnFailureListener{
                        mensaje("Error!: ${it.message!!}")
                    }

                Toast.makeText(this,"Alerta generada", Toast.LENGTH_LONG).show()
                txtUbicacion.setText("${it.latitude}"+","+"${it.longitude}")

                for (item in posicion) {
                    if (item.estoyEn(geoPosicion)) {
                        txtUbicacion.setText(item.nombre)
                    }
                }
            }.addOnFailureListener {
                Toast.makeText(this,"ERROR AL OBTENER UBICACION", Toast.LENGTH_LONG).show()
            }
        }

        }

    private fun mensaje(s: String) {
        Toast.makeText(this,s,Toast.LENGTH_LONG)

    }

    private fun alerta(s: String) {
        AlertDialog.Builder(this).setTitle("Atencion!")
            .setMessage("Realizado con Exito")
            .setPositiveButton("Ok"){d,i->}
            .show()

    }


    /*private fun miUbicacion() {
        if (ContextCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED &&
            ContextCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_COARSE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED) {
            return
        }
        LocationServices.getFusedLocationProviderClient(this).lastLocation.addOnSuccessListener {
            val geoPosicion = GeoPoint(it.latitude, it.longitude)
            Toast.makeText(this,"Alerta generada", Toast.LENGTH_LONG).show()
            txtUbicacion.setText("${it.latitude}"+","+"${it.longitude}")

            for (item in posicion) {
                if (item.estoyEn(geoPosicion)) {
                    txtUbicacion.setText(item.nombre)
                }
            }
        }.addOnFailureListener {
            Toast.makeText(this,"ERROR AL OBTENER UBICACION", Toast.LENGTH_LONG).show()
        }
    }*/
}