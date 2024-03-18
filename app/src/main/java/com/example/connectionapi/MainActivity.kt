package com.example.connectionapi
import android.app.Activity
import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.connectionapi.ui.theme.ConnectionAPITheme
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import retrofit2.Call
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import com.google.android.material.snackbar.Snackbar

data class Platillo(
    val nombre: String,
    val precio: Double,
    val tipo: String,
    val tiempoEstimado: String,
    val descripcion: String,
    val calorias: Int
)
data class PlatillosResponse(
    val platillos: List<Platillo>
)

object CarritoPlatillos {
    private val myList = mutableListOf<Platillo>()

    fun addToList(item: Platillo) {
        myList.add(item)
    }

    fun removeFromList(item: Platillo) {
        myList.remove(item)
    }

    fun getList(): List<Platillo> {
        return myList.toList()
    }
}



class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            // Lista mutable para almacenar las tarjetas seleccionadas
            val selectedPlatillos = remember { mutableStateListOf<Platillo>() }

            ConnectionAPITheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    // Llama al API y maneja la respuesta, pasando la lista de tarjetas seleccionadas
                    CallApi(ApiConfig.apiService.getPlatillos(), selectedPlatillos)
                }
            }
        }
    }
}


//Función para llamar al API y manejar la respuesta



@Composable
fun CallApi(call: Call<List<Platillo>>, selectedPlatillos : MutableList<Platillo>) {
    var platillos by remember { mutableStateOf(emptyList<Platillo>()) }

    val context = LocalContext.current

    LaunchedEffect(Unit) {
        try {
            val response = withContext(Dispatchers.IO) {
                call.execute()
            }
            if (response.isSuccessful) {
                platillos = response.body() ?: emptyList()
            } else {
                // Manejar el error de la llamada
                showSnackbar(context, "Error en la llamada al API: ${response.message()}")
            }
        } catch (e: Exception) {
            // Manejar error de la llamada
            showSnackbar(context, "Error en la llamada al API: ${e.message}")
        }
    }

    // Llama a PlatillosList pasando la lista mutable de tarjetas seleccionadas
    PlatillosList(platillos = platillos, selectedPlatillos = selectedPlatillos)
}

//Para mostrar el error del API
private fun showSnackbar(context: Context, message: String) {
    val rootView = (context as? Activity)?.window?.decorView?.findViewById<View>(android.R.id.content)
    rootView?.let {
        Snackbar.make(it, message, Snackbar.LENGTH_LONG).show()
    }
}



@Composable
fun PlatillosList(platillos: List<Platillo>, selectedPlatillos: MutableList<Platillo>) {
    LazyColumn(
        modifier = Modifier.fillMaxSize()
    ) {
        Log.d("ApiGet", "Platillos Recibidos: $platillos")
        items(platillos) { platillo ->
            val isSelected = platillo in selectedPlatillos
            PlatilloCard(
                platillo = platillo,
                isSelected = isSelected,
                onPlatilloClicked = {
                    if (isSelected) {
                        selectedPlatillos.remove(platillo)
                        CarritoPlatillos.removeFromList(platillo)
                    } else {
                        selectedPlatillos.add(platillo)
                        CarritoPlatillos.addToList(platillo)
                    }
                }
            )
        }
    }
}


fun onPlatilloClicked2(platilloNombre: String) {
    // Aquí puedes realizar cualquier acción que desees con el nombre del platillo
    // Por ejemplo, puedes guardarlo en una variable o realizar una llamada a otra función
    Log.d("MiApp", "Platillo clickeado: $platilloNombre")
    println("Platillo clickeado: $platilloNombre")
}

//Las tarjetas que muestran la información de los platillos
@Composable
fun PlatilloCard(platillo: Platillo, isSelected: Boolean, onPlatilloClicked: (Platillo) -> Unit) {
    val selectedColor = Color.DarkGray
    val defaultColor = Color.White

    val backgroundColor = if (isSelected) selectedColor else defaultColor

    Surface(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp)
            .clickable {
                onPlatilloClicked(platillo)
                onPlatilloClicked2(platillo.nombre)
            },
        color = backgroundColor
    ) {
        Column(
            modifier = Modifier.padding(8.dp)
        ) {
            Text(text = platillo.nombre, fontWeight = FontWeight.Bold)
            Text(text = "Precio: ${platillo.precio}")
            Text(text = "Tipo: ${platillo.tipo}")
            Text(text = "Descripción: ${platillo.descripcion}")
            Text(text = "Calorías: ${platillo.calorias}")
        }
    }
}





@Preview(showBackground = true)
@Composable
fun DefaultPreview() {
    ConnectionAPITheme {
        val platillos = listOf(
            Platillo("Platillo 1", 10.99, "Entrada", "20 minutos", "Descripción del platillo 1", 300),
            Platillo("Platillo 2", 15.99, "Plato Principal", "30 minutos", "Descripción del platillo 2", 500)
        )
        //PlatillosList(platillos = platillos, selectedPlatillo = selectedPlatillo)
    }
}


