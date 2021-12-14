package mx.edu.ittepic.adacova.appdeautobuses

import android.os.Bundle
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.android.synthetic.main.activity_historial_viajes.*

class HistorialViajesActivity : AppCompatActivity() {

    var fireBase = FirebaseFirestore.getInstance()
    var lista = ArrayList<String>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_historial_viajes)

        fireBase.collection("id_Travel")
            .addSnapshotListener { querySnapshop, error ->
                if (error != null) {
                    mensaje(error.message!!)
                    Toast.makeText(this,"hola, falla",Toast.LENGTH_LONG).show()
                    return@addSnapshotListener
                }
                lista.clear()
//                var cadena =""
                for (document in querySnapshop!!) {
                    var cadena = "Fecha de salida: ${document.getString("arrival_date")} \nEstado de autobus: ${document.getString("bus_status")} \nEstado de viaje:${document.getString("completed")} \nFecha de partida: ${document.getString("departure_date")} \nDestino: ${document.getString("destiny")} \nOrigen: ${document.getString("origen")} \nNumero de pasajeros: ${document.getString("passengers_number")} \nDatos de bitacora: ${document.getString("travel_log")}"

                    lista.add(cadena)

                }
                //crear lista con adapter
                listahistorial.adapter = ArrayAdapter<String>(
                    this,
                    android.R.layout.simple_list_item_1,
                    lista
                )
            }
    }
        private fun mensaje(s:String){
            AlertDialog.Builder(this)
                .setTitle("ATENCIÓN!!")
                .setMessage(s)
                .setPositiveButton("OK"){d,i->d.dismiss()}
                .show()
        }



//
//        buttonactualizar.setOnClickListener {
//            Toast.makeText(this, "hola!", Toast.LENGTH_SHORT).show()
//            verHistorial()
//
//        }
//
//
//        val arrival_date: String?
//        val bus_status: String?
//        val completed: String?
//        val departure_date: String
//        val destiny: String
//        val origen: String
//        val passengers_number: String
//        val travel_log: String
//    }
//
//        fun datos(
//            arrival_date: String,
//            bus_status: String,
//            completed: String,
//            departure_date: String,
//            destiny: String,
//            origen: String,
//            passengers_number: String,
//            travel_log: String
//        ) {
//            val datosConsultar = hashMapOf(
//                arrival_date to arrival_date.toString(),
//                bus_status to bus_status.toString(),
//                completed to completed.toString(),
//                departure_date to departure_date.toString(),
//                destiny to destiny.toString(),
//                origen to origen.toString(),
//                passengers_number to passengers_number.toString(),
//                travel_log to travel_log.toString()
//            )
//        }
//
//
//         private fun verHistorial() {
//            val res = fireBase.collection("id_Travel").document("3lQlrmRtAL0IxHPiZiH3").get()
//                .addOnSuccessListener { documento ->
//
//                        val arrival_date: String = documento.getString("arrival_date").toString()
//                        val bus_status: String? = documento.getString("bus_status")
//                        val departure_date: String? = documento.getString("departure_date")
//                        val destiny: String? = documento.getString("destiny")
//                        val completed: String? = documento.getString("completed")
//                        val origen: String? = documento.getString("origen")
//                        val passengers_number: String? = documento.getString("passengers_number")
//                        val travel_log: String? = documento.getString("travel_log")
//
//                        val elementos: List<String?> = listOf(
//                            arrival_date,
//                            bus_status,
//                            departure_date,
//                            destiny,
//                            completed,
//                            origen,
//                            passengers_number,
//                            travel_log
//                        )
//
//                    lista.adapter = ArrayAdapter<String>(this,android.R.layout.simple_list_item_1,elementos)
//
//                }
//                .addOnFailureListener {
//                    Toast.makeText(this ,"hola, fallo:(!", Toast.LENGTH_SHORT).show()
//                }
//
//        }
//
//       fun obtener() = buttonactualizar?.setOnClickListener {
//           Toast.makeText(this, "hola!", Toast.LENGTH_SHORT).show()
//           verHistorial()
//
//       }
    }




