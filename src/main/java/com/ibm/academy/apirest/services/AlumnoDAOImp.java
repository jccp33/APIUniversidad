package com.ibm.academy.apirest.services;

import com.ibm.academy.apirest.entities.Alumno;
import com.ibm.academy.apirest.entities.Carrera;
import com.ibm.academy.apirest.entities.Persona;
import com.ibm.academy.apirest.repositories.AlumnoRepository;
import com.ibm.academy.apirest.repositories.PersonaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
public class AlumnoDAOImp extends PersonaDAOImp implements AlumnoDAO
{
    @Autowired
        public AlumnoDAOImp(@Qualifier("repositorioAlumnos")PersonaRepository repository) {
        super(repository);
    }

    @Override
    @Transactional
    public Iterable<Carrera> findCarreraByCantidadAnios(Integer cantidadAnios) {
        return ((AlumnoDAO)repository).findCarreraByCantidadAnios(cantidadAnios);
    }

    @Override
    @Transactional
    public Iterable<Persona> buscarAlumnoPorNombreCarrera(String nombre)
    {
        return ((AlumnoDAO)repository).buscarAlumnoPorNombreCarrera(nombre);
    }

    @Override
    public Persona actualizar(Persona alumnoEncontrado, Persona alumno)
    {
        Persona alumnoActualizado = null;
        alumnoEncontrado.setNombre(alumno.getNombre());
        alumnoEncontrado.setApellido(alumno.getApellido());
        alumnoEncontrado.setDireccion(alumno.getDireccion());
        alumnoEncontrado.setDni(alumno.getDni());
        alumnoActualizado=repository.save(alumnoEncontrado);
        return alumnoActualizado;
    }

    @Override
    public Persona asociarCarrera(Persona alumno, Carrera carrera)
    {
        ((Alumno)alumno).setCarrera(carrera);
        return repository.save(alumno);
    }
}
