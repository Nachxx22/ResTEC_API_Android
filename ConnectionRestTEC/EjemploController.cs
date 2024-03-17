namespace ConnectionRestTEC;
using Microsoft.AspNetCore.Mvc;


[ApiController]

[Route("api")]
public class EjemploController : ControllerBase
{
    
    
    [HttpGet]
    [Route("ejemplo2")]
    public ActionResult<string> Get()
    {
        return "Hola desde la API REST en C#";
    }
}
