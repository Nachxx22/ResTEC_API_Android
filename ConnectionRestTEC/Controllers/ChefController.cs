using Microsoft.AspNetCore.Mvc;

namespace ConnectionRestTEC.Controllers;

[ApiController]
[Route("api")]

public class ChefController : Controller
{  
    //Este es el GET para la lista de pedidos activos
    [HttpGet]
    [Route("pedidos_activos")]
    // GET
    public ActionResult<IEnumerable<Pedido_activo>> Get_pedidos_activos()
    {
        // Crear una lista de platillos con datos de ejemplo
        List<Pedido_activo> pedidos_activos = new List<Pedido_activo>
        {
            new Pedido_activo
                {
                    id = "1",
                    platillo = "Hamburguesa clásica", 
                    tiempo = "5", 
                    Listo = "No"
                },
            new Pedido_activo
            {
                id = "2",
                platillo = "Pizza Margarita", 
                tiempo = "7", 
                Listo = "No"
            },
            new Pedido_activo
            {
                id = "3",
                platillo = "Filete de salmón a la parrilla", 
                tiempo = "9", 
                Listo = "No"
            },
            new Pedido_activo
            {
                id = "4",
                platillo = "Ensalada César", 
                tiempo = "11", 
                Listo = "No"
            },
            new Pedido_activo
            {
                id = "5",
                platillo = "Pasta Alfredo", 
                tiempo = "13", 
                Listo = "No"
            },
            new Pedido_activo
            {
                id = "6",
                platillo = "Tacos de carne asada", 
                tiempo = "15", 
                Listo = "No"
            }
        };
        // Devolver la lista de platillos como JSON
        return Ok(pedidos_activos);
    }
    
    //Este es el GET para los pedidos seleccionados por el chef
    [HttpGet]
    [Route("pedidos_seleccionados_chef")]
    // GET
    public ActionResult<IEnumerable<Pedido_seleccionado_chef>> Get_pedidos_seleccionados_chef()
    {
        // Crear una lista de platillos con datos de ejemplo
        List<Pedido_seleccionado_chef> pedidos_seleccionados_chef = new List<Pedido_seleccionado_chef>
        {
            new Pedido_seleccionado_chef
            {
                id = "1",
                platillo = "Hamburguesa clásica", 
                tiempo = "5", 
                Listo = "No"
            },
            new Pedido_seleccionado_chef
            {
                id = "2",
                platillo = "Pizza Margarita", 
                tiempo = "7", 
                Listo = "No"
            },
            new Pedido_seleccionado_chef
            {
                id = "3",
                platillo = "Filete de salmón a la parrilla", 
                tiempo = "9", 
                Listo = "No"
            },
            new Pedido_seleccionado_chef
            {
                id = "4",
                platillo = "Ensalada César", 
                tiempo = "11", 
                Listo = "No"
            },
            new Pedido_seleccionado_chef
            {
                id = "5",
                platillo = "Pasta Alfredo", 
                tiempo = "13", 
                Listo = "No"
            },
            new Pedido_seleccionado_chef
            {
                id = "6",
                platillo = "Tacos de carne asada", 
                tiempo = "15", 
                Listo = "No"
            }
        };
        // Devolver la lista de platillos como JSON
        return Ok(pedidos_seleccionados_chef);
    }
    
    //Este es el Get para los pedidos que no han sido asignados
    [HttpGet]
    [Route("pedidos_no_asignados")]
    // GET
    public ActionResult<IEnumerable<Pedido_no_asignado>> Get_pedidos_no_asignados()
    {
        // Crear una lista de platillos con datos de ejemplo
        List<Pedido_no_asignado> pedidos_no_asignados = new List<Pedido_no_asignado>
        {
            new Pedido_no_asignado
            {
                id = "1",
                platillo = "Hamburguesa clásica", 
                tiempo = "5", 
                Listo = "No"
            },
            new Pedido_no_asignado
            {
                id = "2",
                platillo = "Pizza Margarita", 
                tiempo = "7", 
                Listo = "No"
            },
            new Pedido_no_asignado
            {
                id = "3",
                platillo = "Filete de salmón a la parrilla", 
                tiempo = "9", 
                Listo = "No"
            },
            new Pedido_no_asignado
            {
                id = "4",
                platillo = "Ensalada César", 
                tiempo = "11", 
                Listo = "No"
            },
            new Pedido_no_asignado
            {
                id = "5",
                platillo = "Pasta Alfredo", 
                tiempo = "13", 
                Listo = "No"
            },
            new Pedido_no_asignado
            {
                id = "6",
                platillo = "Tacos de carne asada", 
                tiempo = "15", 
                Listo = "No"
            }
        };
        // Devolver la lista de platillos como JSON
        return Ok(pedidos_no_asignados);
    }
    
    public class Pedido_activo
    {
        public string id { get; set; }
        public string platillo { get; set; }
        public string tiempo { get; set; }
        public string Listo { get; set; }
    }
    public class Pedido_seleccionado_chef
    {
        public string id { get; set; }
        public string platillo { get; set; }
        public string tiempo { get; set; }
        public string Listo { get; set; }
    }
    public class Pedido_no_asignado
    {
        public string id { get; set; }
        public string platillo { get; set; }
        public string tiempo { get; set; }
        public string Listo { get; set; }
    }
}