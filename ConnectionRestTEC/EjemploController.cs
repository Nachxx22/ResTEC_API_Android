using System;
using System.Collections.Generic;
using Microsoft.AspNetCore.Mvc;

namespace ConnectionRestTEC
{
    [ApiController]
    [Route("api")]
    public class EjemploController : ControllerBase
    {
        [HttpGet]
        [Route("ejemplo2")]
        public ActionResult<IEnumerable<Platillo>> Get()
        {
            // Crear una lista de platillos con datos de ejemplo
            List<Platillo> platillos = new List<Platillo>
            {
                new Platillo
                {
                    Nombre = "Platillo 1",
                    Precio = 10.99m,
                    Tipo = "Entrada",
                    TiempoEstimado = TimeSpan.FromMinutes(20),
                    Descripcion = "Este es el platillo 1",
                    Calorias = 300
                },
                new Platillo
                {
                    Nombre = "Platillo 2",
                    Precio = 15.99m,
                    Tipo = "Plato Principal",
                    TiempoEstimado = TimeSpan.FromMinutes(30),
                    Descripcion = "Este es el platillo 2",
                    Calorias = 500
                },
                new Platillo
                {
                    Nombre = "Platillo 3",
                    Precio = 19.99m,
                    Tipo = "Plato Principal",
                    TiempoEstimado = TimeSpan.FromMinutes(30),
                    Descripcion = "Este es el platillo 3",
                    Calorias = 700
                }
            };

            // Devolver la lista de platillos como JSON
         
            return Ok(platillos);
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
        
    }

    public class Platillo
    {
        public string Nombre { get; set; }
        public decimal Precio { get; set; }
        public string Tipo { get; set; }
        public TimeSpan TiempoEstimado { get; set; }
        public string Descripcion { get; set; }
        public int Calorias { get; set; }
    }


