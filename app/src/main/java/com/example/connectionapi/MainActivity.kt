package com.example.connectionapi
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.example.connectionapi.ui.theme.ConnectionAPITheme
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import retrofit2.Call
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            ConnectionAPITheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    // Llama al API y maneja la respuesta
                    CallApi(ApiConfig.apiService.getPlatillos())
                }
            }
        }
    }
}

// Define una clase de datos para representar la estructura del JSON
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

@Composable
fun CallApi(call: Call<List<Platillo>>) { // Cambia el tipo del parámetro a Call<List<Platillo>>
    var mensaje by remember { mutableStateOf("Cargando...") }

    LaunchedEffect(Unit) {
        try {
            val response = withContext(Dispatchers.IO) {
                call.execute()
            }
            if (response.isSuccessful) {
                val platillos = response.body()
                if (platillos != null && platillos.isNotEmpty()) {
                    val primerPlatillo = platillos[0] // Aquí solo tomamos el primer platillo para el ejemplo
                    mensaje = "Nombre: ${primerPlatillo.nombre}, Precio: ${primerPlatillo.precio}, Tipo: ${primerPlatillo.tipo}, Descripción: ${primerPlatillo.descripcion}, Calorías: ${primerPlatillo.calorias}"
                } else {
                    mensaje = "Lista de platillos vacía"
                }
            } else {
                mensaje = "Error1: ${response.message()}"
            }
        } catch (e: Exception) {
            mensaje = "Error2: ${e.message}"
        }
    }

    Text(
        text = mensaje,
        modifier = Modifier.fillMaxSize()
    )
}


@Preview(showBackground = true)
@Composable
fun DefaultPreview() {
    ConnectionAPITheme {
        CallApi(ApiConfig.apiService.getPlatillos())
    }
}


