package com.ibm.academy.apirest.services;

import com.ibm.academy.apirest.entities.Aula;
import com.ibm.academy.apirest.entities.Pabellon;
import com.ibm.academy.apirest.entities.Persona;
import com.ibm.academy.apirest.enums.Pizarron;
import com.ibm.academy.apirest.repositories.AulaRepository;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Optional;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class AulaDAOImp extends GenericoDAOImp<Aula, AulaRepository> implements AulaDAO
{
    @Autowired
    public AulaDAOImp(AulaRepository repository) {
        super(repository);
    }

    @Override
    @Transactional
    public Iterable<Aula> buscarAulasPorPizarron(Pizarron pizarron) {
        return ((AulaDAO)repository).buscarAulasPorPizarron(pizarron);
    }

    @Override
    @Transactional
    public Iterable<Aula> buscarAulasPorNombrePabellon(String nombrePabellon) {
        return ((AulaDAO)repository).buscarAulasPorNombrePabellon(nombrePabellon);
    }

    @Override
    @Transactional
    public Optional<Aula> buscarAulaPorNumero(Integer numero) {
        return ((AulaDAO)repository).buscarAulaPorNumero(numero);
    }

    @Override
    public Aula actualizarAula(Aula aulaEncontrada, Aula aula)
    {
        aulaEncontrada.setNumeroAula(aula.getNumeroAula());
        aulaEncontrada.setMedidas(aula.getMedidas());
        aulaEncontrada.setCantidadPupitres(aula.getCantidadPupitres());
        aulaEncontrada.setPizarron(aula.getPizarron());
        Aula aulaNueva = repository.save(aulaEncontrada);
        return aulaNueva;
    }

    @Override
    public Aula asignarPabellon(Aula aula, Pabellon pabellon)
    {
        aula.setPabellon(pabellon);
        return aula;
    }

}
