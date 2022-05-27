package com.ibm.academy.apirest.controllers;



import com.ibm.academy.apirest.entities.Pabellon;
import com.ibm.academy.apirest.entities.Persona;
import com.ibm.academy.apirest.exceptions.NotFoundException;
import com.ibm.academy.apirest.services.EmpleadoDAO;
import com.ibm.academy.apirest.services.PabellonDAOImp;
import com.ibm.academy.apirest.services.PersonaDAO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/empleado")
public class EmpleadoControl
{
    @Autowired
    @Qualifier("empleadoDAOImp")
    private PersonaDAO empleadoDAO;

    @Autowired
    private PabellonDAOImp pabellonDAO;

    @PostMapping()
    public ResponseEntity<?> crearEmpleado(@RequestBody Persona empleado)
    {
        Persona nuevoEmpleado = empleadoDAO.guardar(empleado);
        return new ResponseEntity<Persona>(nuevoEmpleado, HttpStatus.CREATED);
    }

    @GetMapping("/listas/empleados")
    public ResponseEntity<?> mostrarEmpleados()
    {
        List<Persona> empleados = (List<Persona>) empleadoDAO.buscarTodos();
        if (empleados.isEmpty())
            throw new NotFoundException("Lista vacia");
        return new ResponseEntity<List<Persona>>(empleados,HttpStatus.OK);
    }

    @GetMapping("/id/{id}")
    public ResponseEntity<?> mostrarEmpleadoPorId(@PathVariable Integer id)
    {
        Optional<Persona> oEmpleado = empleadoDAO.buscarPorId(id);
        if (!oEmpleado.isPresent())
            throw new NotFoundException(String.format("El empleado con ID %d no existe",id));
        return new ResponseEntity<Persona>(oEmpleado.get(),HttpStatus.OK);
    }

    @DeleteMapping("/del/id/{id}")
    public ResponseEntity<?> borrarEmpleadoPorId(@PathVariable Integer id)
    {
        Map<String, Object> respuesta = new HashMap<String, Object>();
        Optional<Persona> oEmpleado = empleadoDAO.buscarPorId(id);

        if (!oEmpleado.isPresent())
            throw new NotFoundException(String.format("El empleado con ID %d no encontrada", id));

        empleadoDAO.eliminarPorId(id);
        respuesta.put("OK", "Empleado con ID " + id + " eliminad@");
        return new ResponseEntity<Map<String, Object>>(respuesta, HttpStatus.ACCEPTED);
    }

    @PutMapping("/upd/empleadoid/{idEmpleado}/pabellonid/{idPabellon}")
    public ResponseEntity<?> asignarPabellon(@PathVariable Integer idEmpleado,@PathVariable Integer idPabellon)
    {
        Optional<Persona> oEmpleado = empleadoDAO.buscarPorId(idEmpleado);
        if (!oEmpleado.isPresent())
            throw new NotFoundException(String.format("Empleado con ID %d no existe",idEmpleado));
        Optional<Pabellon> oPabellon = pabellonDAO.buscarPorId(idPabellon);
        if (!oPabellon.isPresent())
            throw new NotFoundException(String.format("Pabellon con ID %d no existe",idPabellon));
        Persona empleado = ((EmpleadoDAO)empleadoDAO).asignarPabellon(oEmpleado.get(),oPabellon.get());
        return new ResponseEntity<Persona>(empleado,HttpStatus.OK);
    }

    @PutMapping("/upd/id/{id}")
    public ResponseEntity<Persona> actualizarEmpleado(@PathVariable Integer id,@RequestBody Persona empleado)
    {
        Optional<Persona> oEmpleado = empleadoDAO.buscarPorId(id);
        if (!oEmpleado.isPresent())
            throw new NotFoundException(String.format("Empleado con ID %d no existe",id));

        Persona empleadoActualizado = ((EmpleadoDAO)empleadoDAO).actualizarEmpleado(oEmpleado.get(),empleado);

        return new ResponseEntity<Persona>(empleadoActualizado,HttpStatus.OK);
    }
}
