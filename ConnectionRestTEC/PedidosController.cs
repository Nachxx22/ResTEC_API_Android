using System;
using System.Collections.Generic;
using Microsoft.AspNetCore.Mvc;
using System.Text.Json;


namespace ConnectionRestTEC
{
    [ApiController]
    [Route("api")]
    public class PedidosController : ControllerBase
    {
        private static List<Pedido> pedidos = new List<Pedido>(); // Lista para almacenar pedidos
        
        [HttpGet]
        [Route("pedidos")]
        public IActionResult ObtenerPedidos()
        {
            try
            {
                // Leer el contenido del archivo pedidos.json
                string filePath = "pedidos.json";
                string jsonPedidos = System.IO.File.ReadAllText(filePath);

                // Deserializar el contenido del archivo JSON a una lista de objetos Pedido
                List<Pedido> pedidos = JsonSerializer.Deserialize<List<Pedido>>(jsonPedidos);

                // Devolver la lista de pedidos como JSON
                return Ok(pedidos);
            }
            catch (Exception ex)
            {
                // Manejar cualquier error que ocurra durante la lectura del archivo
                return StatusCode(500, $"Error al leer el archivo de pedidos: {ex.Message}");
            }
        }
        [HttpPost]
        [Route("platillos2")]
        public IActionResult ReceivePlatillos2([FromBody] List<Platillo> platillos)
        {
            try
            {
                var nuevoPedido = new Pedido
                {
                    IDPedido = GenerarIDUnicoCorto(),
                    Platillos = new List<string>(),
                    Feedback = "",
                    Status = "En proceso"
                };

                foreach (var platillo in platillos)
                {
                    nuevoPedido.Platillos.Add(platillo.Nombre);
                }

                pedidos.Add(nuevoPedido); // Agregar el nuevo pedido a la lista de pedidos
                GuardarPedidosEnArchivo(); // Guardar los pedidos en un archivo

                return Ok(new { IDPedido = nuevoPedido.IDPedido });
            }
            catch (Exception ex)
            {
                return StatusCode(500, $"Error al procesar el pedido: {ex.Message}");
            }
        }

        private void GuardarPedidosEnArchivo()
        {
            string filePath = "pedidos.json";

            // Crear una lista de objetos anonimos con solo los nombres de los platillos
            var nombresPlatillos = new List<object>();
            foreach (var pedido in pedidos)
            {
                foreach (var platillo in pedido.Platillos)
                {
                    nombresPlatillos.Add(new { nombre = platillo });
                }
            }

            // Serializar la lista de nombres de platillos a formato JSON utilizando System.Text.Json
            string jsonPedidos = JsonSerializer.Serialize(pedidos);

            // Guardar el JSON en un archivo
            System.IO.File.WriteAllText(filePath, jsonPedidos);
        }
        private string GenerarIDUnicoCorto()
        {
            // Obtener una representación de la fecha y hora actual en milisegundos
            long timestamp = DateTime.Now.Ticks / TimeSpan.TicksPerMillisecond;

            // Generar un valor aleatorio
            Random random = new Random();
            int randomValue = random.Next(100000, 999999); // Genera un número aleatorio de 6 dígitos

            // Convertir el timestamp y el valor aleatorio a hexadecimal y concatenarlos
            string hexTimestamp = timestamp.ToString("X");
            string hexRandomValue = randomValue.ToString("X");

            // Tomar solo los primeros 6 caracteres del timestamp y los primeros 3 caracteres del valor aleatorio
            string idCorto = hexTimestamp.Substring(0, Math.Min(hexTimestamp.Length, 6)) + hexRandomValue.Substring(0, 3);

            return idCorto;
        }
        }
    }
//Estructura del pedido para el JSON
    public class Pedido
    {
        public string IDPedido { get; set; }
        public List<string> Platillos { get; set; }
        public string Feedback { get; set; } = ""; // Inicializa como vacío por defecto
        public string Status { get; set; } = "En proceso"; // Pone "En proceso" por defecto
    }