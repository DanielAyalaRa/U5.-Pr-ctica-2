package mx.edu.ittepic.adacova.appdeautobuses
import android.app.Activity
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.android.synthetic.main.fragment_historial_viajes.*


class HistorialViajesFragment : Fragment(R.layout.fragment_historial_viajes) {

    var button_actualizar : Button ?=null
    private var fireBase = FirebaseFirestore.getInstance()

    private fun datos(
        arrival_date: String,
        bus_status: String,
        completed: String,
        departure_date: String,
        destiny: String,
        origen: String,
        passengers_number: String,
        travel_log: String
    ) {
        val datosConsultar = hashMapOf(
            "arrival_date" to arrival_date,
            "bus_status" to bus_status,
            "completed" to completed,
            "departure_date" to departure_date,
            "destiny" to destiny,
            "origen" to origen,
            "passengers_number" to passengers_number,
            "travel_log" to travel_log
        )
    }


        private fun verHistorial() {

            val res = fireBase.collection("id_Travel").document("3lQlrmRtAL0IxHPiZiH3")
            res.get()
                .addOnSuccessListener { documento ->
                    if (documento.exists()) {
                        val arrival_date: String? = documento.getString("arrival_date")
                        val bus_status: String? = documento.getString("bus_status")
                        val departure_date: String? = documento.getString("departure_date")
                        val destiny: String? = documento.getString("destiny")
                        val completed: String? = documento.getString("completed")
                        val origen: String? = documento.getString("origen")
                        val passengers_number: String? = documento.getString("passengers_number")
                        val travel_log: String? = documento.getString("travel_log")


                        val elementos: List<String?> = listOf(
                            arrival_date,
                            bus_status,
                            departure_date,
                            destiny,
                            completed,
                            origen,
                            passengers_number,
                            travel_log
                        )

                        lista_historial.adapter = ArrayAdapter<String>(
                            requireContext() as Activity,
                            android.R.layout.simple_list_item_1,
                            elementos
                        )
                        print(elementos)
                    } else {


                    }


                }
                .addOnFailureListener {
                    Toast.makeText(context, "hola, fallo:(!", Toast.LENGTH_SHORT).show()
                }

        }

    private fun obtener() {
        button_actualizar?.setOnClickListener {
            Toast.makeText(requireContext(), "hola!", Toast.LENGTH_SHORT).show()
            //verHistorial()

        }

    }
}




