package com.ibm.academy.apirest.controllers;

import com.ibm.academy.apirest.entities.Alumno;
import com.ibm.academy.apirest.entities.Carrera;
import com.ibm.academy.apirest.entities.Persona;
import com.ibm.academy.apirest.exceptions.NotFoundException;
import com.ibm.academy.apirest.services.AlumnoDAO;
import com.ibm.academy.apirest.services.CarreraDAO;
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
@RequestMapping("/alumno")
public class AlumnoControl
{
    @Autowired
    @Qualifier("alumnoDAOImp")
    private PersonaDAO alumnoDAO;

    @Autowired
    private CarreraDAO carreraDAO;

    @PostMapping()
    public ResponseEntity<Persona> crearAlumno(@RequestBody Persona alumno)
    {
        Persona alumnoGuardado = alumnoDAO.guardar(alumno);
        return new ResponseEntity<Persona>(alumnoGuardado,HttpStatus.CREATED);
    }

    @GetMapping("/lista/alumnos")
    public ResponseEntity<?> buscarAlumnos()
    {
        List<Persona> alumnos = (List<Persona>) alumnoDAO.buscarTodos();
        if (alumnos.isEmpty())
            throw new NotFoundException("No existen alumnos registrados");
        return new ResponseEntity<List<Persona>>(alumnos, HttpStatus.OK);
    }

    @GetMapping("/id/{id}")
    public Persona buscarAlumnosPorId(@PathVariable Integer id)
    {
        Optional<Persona> oAlumno = alumnoDAO.buscarPorId(id);
        if (!oAlumno.isPresent())
            throw new NotFoundException(String.format("El alumno con ID %d no encontrado",id));
        return oAlumno.get();
    }

    @PutMapping("/upd/alumnoid/{id}")
    public ResponseEntity<?> actualizarAlumno(@PathVariable Integer id,@RequestBody Persona alumno)
    {
        Optional<Persona> oAlumno = alumnoDAO.buscarPorId(id);

        if (!oAlumno.isPresent())
            throw new NotFoundException(String.format("El alumno con ID %d no existe",id));

        Persona alumnoActualizado = ((AlumnoDAO)alumnoDAO).actualizar(oAlumno.get(),alumno);
        return new ResponseEntity<Persona>(alumnoActualizado,HttpStatus.OK);
    }

    @DeleteMapping("/alumnoId/{id}")
    public ResponseEntity<?> borrarAlumno(@PathVariable Integer id)
    {
        Map<String, Object> respuesta = new HashMap<String, Object>();
        Optional<Persona> oAlumno = alumnoDAO.buscarPorId(id);

        if (!oAlumno.isPresent())
            throw new NotFoundException(String.format("Alumno con ID %d no encontrado", id));

        alumnoDAO.eliminarPorId(id);
        respuesta.put("OK", "alumno con ID " + id + " eliminad@");
        return new ResponseEntity<Map<String, Object>>(respuesta, HttpStatus.ACCEPTED);
    }

    @PutMapping("/alumnoid/{alumnoId}/carreraid/{carreraId}")
    public ResponseEntity<?> asignarCarrera(@PathVariable Integer alumnoId,@PathVariable Integer carreraId)
    {
        Optional<Persona> oAlumno = alumnoDAO.buscarPorId(alumnoId);
        if (!oAlumno.isPresent())
            throw new NotFoundException(String.format("El alumno  con ID %d no existe",alumnoId));

        Optional<Carrera> oCarrera = carreraDAO.buscarPorId(carreraId);
        if (!oCarrera.isPresent())
            throw new NotFoundException(String.format("La carrera con ID %d no existe",carreraId));

        Persona alumno = ((AlumnoDAO)alumnoDAO).asociarCarrera(oAlumno.get(),oCarrera.get());
        return new ResponseEntity<Persona>(alumno,HttpStatus.OK);
    }
}
