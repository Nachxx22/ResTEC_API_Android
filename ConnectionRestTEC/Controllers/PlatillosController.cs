using System;
using System.Collections.Generic;
using Microsoft.AspNetCore.Mvc;
using System.Text.Json;


namespace ConnectionRestTEC
{
    [ApiController]
    [Route("api")]
    public class PlatillosController : ControllerBase
    {
        
        [HttpGet]
        [Route("platillos")]
        public ActionResult<IEnumerable<Platillo>> Get()
        {
            try
            {
                // Leer el contenido del archivo platillos.json
                string jsonFilePath = "platillos.json";
                string jsonString = System.IO.File.ReadAllText(jsonFilePath);

                // Deserializar el contenido del archivo JSON a una lista de objetos Platillo
                List<Platillo> platillos = JsonSerializer.Deserialize<List<Platillo>>(jsonString);

                // Devolver la lista de platillos como JSON
                return Ok(platillos);
            }
            catch (Exception ex)
            {
                // Manejar cualquier error que ocurra durante la lectura del archivo
                return StatusCode(500, $"Error al leer el archivo: {ex.Message}");
            }
        }
        
        [HttpPost]
        [Route("platillos")]
        public IActionResult ReceivePlatillos([FromBody] List<Platillo> platillos)
        {
            // Aquí maneja los platillos recibidos en formato JSON
            // Puedes guardarlos en una lista o base de datos
            // Por ahora, solo imprimir el JSON recibido
            Console.WriteLine("Platillos recibidos:");
            foreach (var platillo in platillos)
            {
                Console.WriteLine(platillo.Nombre);
            }
            return Ok();
        }
        

    }

//Estructura del Platillo para el json
    public class Platillo
    {
        public string Nombre { get; set; }
        public decimal Precio { get; set; }
        public string Tipo { get; set; }
        public TimeSpan TiempoEstimado { get; set; }
        public string Descripcion { get; set; }
        public int Calorias { get; set; }
    }
    }
