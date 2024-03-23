package com.example.connectionapi
import android.app.Activity
import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.appcompat.app.AlertDialog
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
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
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock
import retrofit2.Callback
import retrofit2.Response
import android.content.Intent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding


data class Platillo(
    val nombre: String,
    val precio: Double,
    val tipo: String,
    val tiempoEstimado: String,
    val descripcion: String,
    val calorias: Int
)
data class Pedido(
    val idPedido: String,
    val platillos: List<String>,
    val feedback: String,
    val status: String
)
data class Usuario(
    val cedula: String,
    val nombre: String,
    val apellidos: String,
    val direccion: String,
    val fechaNacimiento: String,
    val telefono: String,
    val correo: String,
    val contraseña: String
)





object CarritoPlatillos {
    private val myList = mutableListOf<Platillo>()

    fun addToList(item: Platillo) {
        myList.add(item)
    }

    fun removeFromList(item: Platillo) {
        myList.remove(item)
    }


}



@Composable
fun PlatillosComposable(selectedPlatillos: MutableList<Platillo>, apiMutex: Mutex) {
        // Llamar a CallApi dentro del cuerpo de una función @Composable
        CallApi(call = ApiConfig.apiService.getPlatillos(), selectedPlatillos = selectedPlatillos, mutex = apiMutex)
}


@Composable
fun MainActivityContent() {
    // Lista mutable para almacenar las tarjetas seleccionadas
    val selectedPlatillos = remember { mutableStateListOf<Platillo>() }
    // Lista mutable para almacenar las tarjetas seleccionadas
    val pedidos = remember { mutableStateListOf<Pedido>() }
    // Mutex para sincronizar el acceso a la API
    val apiMutex = remember { Mutex() }

    // Usamos un estado booleano para controlar si el diálogo debe mostrarse
    var showDialog by remember { mutableStateOf(false) }
    var pedidosDialog by remember { mutableStateOf(false) }

    ConnectionAPITheme {
        Surface(
            modifier = Modifier.fillMaxSize(),
            color = MaterialTheme.colorScheme.background
        ) {
            Column(
                modifier = Modifier.fillMaxSize(),
                verticalArrangement = Arrangement.Top
            ) {
                // Botón para mostrar los platillos en el carrito
                Button(
                    onClick = {
                        showDialog = true
                    }
                ) {
                    Text(text = "Ver Carrito")
                }

                // Botón para enviar los platillos al servidor
                Button(
                    onClick = {
                        // Envía los platillos al servidor
                        sendPlatillosToServer(selectedPlatillos)
                    }
                ) {
                    Text(text = "Realizar Pedido")
                }

                // Botón para mostrar los pedidos
                Button(
                    onClick = {
                        //Pide los pedidos al servidor
                        pedidosDialog= true
                    }
                ) {
                    Text(text = "Pedidos")
                }

                // Llama al composable PlatillosComposable, pasando la lista de tarjetas seleccionadas y el Mutex
                PlatillosComposable(selectedPlatillos = selectedPlatillos, apiMutex = apiMutex)
                ApiPedidos(call = ApiConfig.apiService.getPedidos(),pedidos2=pedidos, mutex = apiMutex)


                // Muestra el diálogo si showDialog es true
                if (showDialog) {
                    ShowPlatillosInCartDialog(selectedPlatillos)
                    showDialog = false
                }

                // Llama a la función ApiPedidos para pedir los pedidos al servidor
                    if(pedidosDialog){
                        Log.d("API pedidos", "Pidiendo Pedidos al backend")
                        ShowPedidosDialog(pedidos)
                        pedidosDialog= false
                    }
            }
        }
    }
}


class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Verificar si el usuario ya ha iniciado sesión
        if (usuarioHaIniciadoSesion()) {
            // Si el usuario ya ha iniciado sesión, mostrar el contenido de MainActivity
            mostrarContenidoPrincipal()
        } else {
            // Si el usuario no ha iniciado sesión, iniciar MiActividad para que pueda hacerlo
            iniciarMiActividad()
        }
    }

    private fun usuarioHaIniciadoSesion(): Boolean {
        // Leer el indicador de inicio de sesión desde SharedPreferences
        val preferencias = getSharedPreferences("preferencias", Context.MODE_PRIVATE)
        return preferencias.getBoolean("usuario_autenticado", false)
    }

    private fun iniciarMiActividad() {
        val intent = Intent(this@MainActivity, MiActividad::class.java)
        startActivity(intent)
        finish() // Termina la actividad actual para que el usuario no pueda volver atrás presionando el botón de retroceso
    }

    private fun mostrarContenidoPrincipal() {
        setContent {
            MainActivityContent()
        }
    }
}

    //Función para que el carrito se muestre como ventana emergente con los platillos seleccionados
    @Composable
    private fun ShowPlatillosInCartDialog(selectedPlatillos: List<Platillo>) {
        val context = LocalContext.current
        val dialog = AlertDialog.Builder(context)
            .setTitle("Platillos en el Carrito")
            .setMessage(selectedPlatillos.joinToString("\n") { it.nombre })
            .setPositiveButton("Cerrar") { _, _ -> }
            .create()
        dialog.show()
    }

@Composable
fun ShowPedidosDialog(pedidos: List<Pedido>) {
    val context = LocalContext.current
    val dialog = AlertDialog.Builder(context)
        .setTitle("Pedidos")
        .setMessage(buildString {
            for (pedido in pedidos) {
                append("ID del Pedido: ${pedido.idPedido}\n")
                append("Platillos:\n")
                for (platillo in pedido.platillos) {
                    append("   - $platillo\n")
                }
                append("Feedback: ${pedido.feedback}\n")
                append("Status: ${pedido.status}\n\n")
            }
        })
        .setPositiveButton("Cerrar") { _, _ -> }
        .create()

    DisposableEffect(Unit) {
        dialog.show()
        onDispose {
            dialog.dismiss()
        }
    }
}






//Función para enviar los platillos al servidor en forma JSON
private fun sendPlatillosToServer(selectedPlatillos: List<Platillo>) {
    // Puedes usar Gson para convertir la lista de platillos a JSON
    //val gson = Gson()
    //val platillosJson = gson.toJson(selectedPlatillos)

    // Llama al método sendPlatillos de ApiService para enviar los platillos al servidor
    ApiConfig.apiService.sendPlatillos(selectedPlatillos).enqueue(object : Callback<Void> {
        override fun onResponse(call: Call<Void>, response: Response<Void>) {
            if (response.isSuccessful) {
                Log.d("API", "Platillos enviados correctamente")
            } else {
                Log.e("API", "Error al enviar los platillos: ${response.code()}")
            }
        }

        override fun onFailure(call: Call<Void>, t: Throwable) {
            Log.e("API", "Error al enviar los platillos: ${t.message}")
        }
    })
}

//Función para llamar al API y manejar la respuesta

@Composable
fun CallApi(call: Call<List<Platillo>>, selectedPlatillos: MutableList<Platillo>, mutex: Mutex) {
    var platillos by remember { mutableStateOf(emptyList<Platillo>()) }
    val context = LocalContext.current

    LaunchedEffect(Unit) {
        mutex.withLock {
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
    }
    // Llama a PlatillosList pasando la lista mutable de tarjetas seleccionadas
    PlatillosList(platillos = platillos, selectedPlatillos = selectedPlatillos)
}

@Composable
fun ApiPedidos(call: Call<List<Pedido>>, pedidos2: MutableList<Pedido>,mutex: Mutex) {
    var pedidos by remember { mutableStateOf(emptyList<Pedido>()) }
    val context = LocalContext.current

    LaunchedEffect(Unit) {
        mutex.withLock {
            try {
                val response = withContext(Dispatchers.IO) {
                    call.execute()
                }
                if (response.isSuccessful) {
                    pedidos = response.body() ?: emptyList()
                } else {
                    // Manejar el error de la llamada
                    showSnackbar(context, "Error en la llamada al API: ${response.message()}")
                    Log.d("pedidos error", "Pidiendo Pedidos al backend ${response.message()}")
                }
            } catch (e: Exception) {
                // Manejar error de la llamada
                showSnackbar(context, "Error en la llamada al API: ${e.message}")
                Log.d("pedidos error3", "Pidiendo Pedidos al backend ${e.message}")
            }
        }
    }

    // Mostrar los pedidos en un AlertDialog
    if (pedidos.isNotEmpty()) {
        ShowPedidosDialog(pedidos)
    }
}



//Para mostrar el error del API
private fun showSnackbar(context: Context, message: String) {
    val rootView = (context as? Activity)?.window?.decorView?.findViewById<View>(android.R.id.content)
    rootView?.let {
        Snackbar.make(it, message, Snackbar.LENGTH_LONG).show()
    }
}



//Función para guardar/eliminar los platillos seleccionados en el carrito, además del isSelected para cambiar el color de la tarjeta
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

//función simple para mostrar el nombre del platillo clickeado
fun onPlatilloClicked2(platilloNombre: String) {
    // Aquí puedes realizar cualquier acción que desees con el nombre del platillo
    // Por ejemplo, puedes guardarlo en una variable o realizar una llamada a otra función
    Log.d("MiApp", "Platillo clickeado: $platilloNombre")
    println("Platillo clickeado: $platilloNombre")
}

//Función para mostrar la tarjeta de cada platillon y puedan ser clickeados
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
        /*val platillos = listOf(
            Platillo("Platillo 1", 10.99, "Entrada", "20 minutos", "Descripción del platillo 1", 300),
            Platillo("Platillo 2", 15.99, "Plato Principal", "30 minutos", "Descripción del platillo 2", 500)
        )

        PlatillosList(platillos = platillos, selectedPlatillo = selectedPlatillo)
         */
    }
}



