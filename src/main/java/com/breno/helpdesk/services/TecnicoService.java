package com.breno.helpdesk.services;

import com.breno.helpdesk.domain.Tecnico;
import com.breno.helpdesk.domain.repositores.TecnicoRepository;
import com.breno.helpdesk.services.exceptions.ObjectNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class TecnicoService {

    @Autowired
    private TecnicoRepository repository;

    public Tecnico findById(Integer id){
        Optional<Tecnico> obj = repository.findById(id);
        return obj.orElseThrow(() -> new ObjectNotFoundException("Objeto não encontrado " + id));
    }

    public List<Tecnico> findAll() {
        return repository.findAll();
    }
}
