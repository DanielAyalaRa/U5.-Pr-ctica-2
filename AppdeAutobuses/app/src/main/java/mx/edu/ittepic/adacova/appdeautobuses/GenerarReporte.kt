package mx.edu.ittepic.adacova.appdeautobuses
import android.os.Bundle
import android.Manifest
import android.annotation.SuppressLint
import android.content.pm.PackageManager
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import android.widget.ArrayAdapter
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat.checkSelfPermission
import com.google.android.gms.location.LocationServices
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.GeoPoint
import kotlinx.android.synthetic.main.activity_generar_reporte.*
import kotlin.collections.ArrayList
import android.content.ContentValues
import android.content.Intent
import android.util.Log
import android.database.sqlite.SQLiteException

class GenerarReporte : AppCompatActivity()
{
    private var posicion = ArrayList<Data>()
    var listaID = ArrayList<String>()
    var datos = ArrayList<String>()
    var DATA= ArrayList<String>()

    //Firebase
    var Fire_Base = FirebaseFirestore.getInstance();
    //sql local
    var baseDatos = DataBase(this,"basedatos",null,1)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_generar_reporte)
        if (checkSelfPermission(Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_DENIED) {
            ActivityCompat.requestPermissions(this, arrayOf(Manifest.permission.ACCESS_FINE_LOCATION), 1)
        }


        miUbicacion()

        btnGenerarR.setOnClickListener {
            insertar()
            sincronizar()
        }

        btnRegresar.setOnClickListener {
            finish()
        }
    }

    @SuppressLint("MissingPermission", "SetTextI18n")
    private fun miUbicacion() {
        if (checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED &&
            checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return
        }
        LocationServices.getFusedLocationProviderClient(this).lastLocation.addOnSuccessListener {
            val geoPosicion = GeoPoint(it.latitude, it.longitude)
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

    //Metodo insertar datos de la pantalla inicial
    private fun insertar(){
        try {
            var b = baseDatos.writableDatabase
            var valores = ContentValues()

            //Agregar a la BD valores de los campos de texto
            valores.put("type",txtTipo.text.toString())
            valores.put("location",txtUbicacion.text.toString())
            valores.put("descripcion",txtDescripcion.text.toString())
            valores.put("id_travel",txtViaje.text.toString())


            var res = b.insert("Incidents",null,valores)

            if(res==-1L){
                mensaje("FALLÓ AL INSERTAR")
            }else{
                mensaje("INSERCIÓN EXITOSA")
                limpiarValores()
            }

            b.close()

        }catch (e: SQLiteException){
            mensaje(e.message!!)
        }
        agregarLista()
    }

    //Limpiar campos de texto
    private fun limpiarValores(){
        txtTipo.setText("")
        txtDescripcion.setText("")
    }

    //Mensaje
    private fun mensaje(s:String){
        AlertDialog.Builder(this)
            .setTitle("ATENCIÓN")
            .setMessage(s)
            .setPositiveButton("OK"){d,i->d.dismiss()}
            .show()
    }

    private fun agregarLista(){
        datos.clear()
        listaID.clear()

        try{
            var b = baseDatos.readableDatabase
            var eventos = ArrayList<String>()
            var res = b.query("Incidents", arrayOf("*"),null,null,null,null,null)
            listaID.clear()

            if (res.moveToFirst()){
                do{
                    var cadena = "type: ${res.getString(1)}\nlocation: ${res.getString(2)}\ndescripcion :${res.getString(3)}\n" +
                            "id_travel :${res.getString(4)}"
                    eventos.add(cadena)
                    datos.add(cadena)
                    listaID.add(res.getInt(0).toString())
                }while (res.moveToNext())
            }else{
                eventos.add("NO TIENES EVENTOS")
            }

            lista.adapter = ArrayAdapter<String>(this,android.R.layout.simple_list_item_1,eventos)
            this.registerForContextMenu(lista)
            b.close()

        }catch (e: SQLiteException){ mensaje("ERROR!! " + e.message!!) }
    }

    private fun sincronizar() {
        DATA.clear()
        Fire_Base.collection("id_Incidents")
            .addSnapshotListener { querySnapshot, firebaseFirestoreException ->
                if (firebaseFirestoreException != null) {
                    mensaje("Error: No se pudo recuperar data desde Firebase")
                    Log.d("tag","no")
                    return@addSnapshotListener

                }
                var cadena = ""
                for (registro in querySnapshot!!) {
                    cadena = registro.id.toString()
                    DATA.add(cadena)
                }
                try {
                    var trans = baseDatos.readableDatabase
                    var respuesta =
                        trans.query("Incidents", arrayOf("*"), null, null, null, null, null)
                    if (respuesta.moveToFirst()) {
                        do {

                            if (DATA.any {
                                    respuesta.getString(0).toString() == it
                                })
                            {
                                DATA.remove(respuesta.getString(0).toString())
                                Fire_Base.collection("id_Incidents")
                                    .document(respuesta.getString(0))
                                    .update(
                                        "type", respuesta.getString(1),
                                        "location", respuesta.getString(2),
                                        "descripcion", respuesta.getString(3),
                                        "id_travel", respuesta.getString(4)
                                    ).addOnSuccessListener {

                                    }.addOnFailureListener {
                                        AlertDialog.Builder(this)
                                            .setTitle("Error")
                                            .setMessage("No se pudo ACTUALIZAR\n${it.message!!}")
                                            .setPositiveButton("Ok") { d, i -> }
                                            .show()
                                    }
                            } else {
                                var datosInsertar = hashMapOf(
                                    "type" to respuesta.getString(1),
                                    "location" to respuesta.getString(2),
                                    "descripcion" to respuesta.getString(3),
                                    "id_travel" to respuesta.getString(4)
                                )
                                Fire_Base.collection("id_Incidents")
                                    .document("${respuesta.getString(0)}")
                                    .set(datosInsertar as Any).addOnSuccessListener {

                                    }
                                    .addOnFailureListener {
                                        mensaje("No se pudo INSERTAR:\n${it.message!!}")
                                    }
                            }
                        } while (respuesta.moveToNext())

                    } else {
                        mensaje("No ha ingresado los datos requeridos")
                    }
                    trans.close()
                } catch (e: SQLiteException) {
                    mensaje("ERROR: " + e.message!!)
                }
                var el = DATA.subtract(listaID)
                if (el.isEmpty()) {

                } else {
                    el.forEach {
                        Fire_Base.collection("id_Incidents")
                            .document(it)
                            .delete()
                            .addOnSuccessListener {}
                            .addOnFailureListener { mensaje("ERROR No se puedo ELIMINAR\n" + it.message!!) }
                    }
                }

            }

        Toast.makeText(this, "Sincronizacion Exitosa!", Toast.LENGTH_SHORT).show()
    }
}