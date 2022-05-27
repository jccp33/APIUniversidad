package com.ibm.academy.apirest.controllers;

import com.ibm.academy.apirest.entities.Aula;
import com.ibm.academy.apirest.entities.Pabellon;
import com.ibm.academy.apirest.entities.Persona;
import com.ibm.academy.apirest.exceptions.NotFoundException;
import com.ibm.academy.apirest.services.AulaDAOImp;
import com.ibm.academy.apirest.services.PabellonDAOImp;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.persistence.criteria.CriteriaBuilder;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/aula")
public class AulaControl
{
    @Autowired
    private AulaDAOImp aulaDAO;

    @Autowired
    private PabellonDAOImp pabellonDAO;

    @PostMapping()
    public ResponseEntity<?> crearAula(@RequestBody Aula aula)
    {
        Aula aulaGuardada = aulaDAO.guardar(aula);
        return new ResponseEntity<Aula>(aulaGuardada, HttpStatus.CREATED);
    }

    @GetMapping("search/id/{id}")
    public  ResponseEntity<?> buscarAulaPorId(@PathVariable Integer id)
    {
        Optional<Aula> oAula = aulaDAO.buscarPorId(id);
        if (!oAula.isPresent())
            throw new NotFoundException(String.format("Aula con ID %i no existe",id));
        return new ResponseEntity<Aula>(oAula.get(),HttpStatus.OK);
    }

    @GetMapping("/listas/aulas")
    public ResponseEntity<?> mostrarAulas()
    {
        List<Aula> aulas = (List<Aula>) aulaDAO.buscarTodos();
        if (aulas.isEmpty())
            throw new NotFoundException("Lista vacia");
        return new ResponseEntity<List<Aula>>(aulas,HttpStatus.OK);
    }

    @DeleteMapping("/del/id/{id}")
    public  ResponseEntity<?> borrarAulaPorId(@PathVariable Integer id)
    {
        Map<String, Object> respuesta = new HashMap<String, Object>();
        Optional<Aula> oAula = aulaDAO.buscarPorId(id);

        if (!oAula.isPresent())
            throw new NotFoundException(String.format("Aula con ID %d no encontrado", id));

        aulaDAO.eliminarPorId(id);
        respuesta.put("OK", "aula con ID " + id + " eliminad@");
        return new ResponseEntity<Map<String, Object>>(respuesta, HttpStatus.ACCEPTED);
    }

    @PutMapping("/upd/id/{id}")
    public ResponseEntity<Aula> actualizarAula(@PathVariable Integer id, @RequestBody Aula aula)
    {
        Optional<Aula> oAula = aulaDAO.buscarPorId(id);
        if (!oAula.isPresent())
            throw new NotFoundException(String.format("Aula con Id %d no existe",id));
        Aula aulaNueva = aulaDAO.actualizarAula(oAula.get(),aula);
        return new ResponseEntity<Aula>(aulaNueva,HttpStatus.OK);
    }

    @PutMapping("/link/aulaid/{idAula}/pabellonid/{idPabellon}")
    public ResponseEntity<?> asignarPabellon(@PathVariable Integer idAula,@PathVariable Integer idPabellon)
    {
        Optional<Aula> oAula = aulaDAO.buscarPorId(idAula);
        if (!oAula.isPresent())
            throw new NotFoundException(String.format("Aula con Id %d no existe",idAula));
        Optional<Pabellon> oPabellon = pabellonDAO.buscarPorId(idPabellon);
        if (!oPabellon.isPresent())
            throw new NotFoundException(String.format("Pabellon con Id %d no existe",idPabellon));
        Aula aulaNueva = aulaDAO.asignarPabellon(oAula.get(),oPabellon.get());
        return new ResponseEntity<Aula>(aulaNueva,HttpStatus.OK);
    }
}
