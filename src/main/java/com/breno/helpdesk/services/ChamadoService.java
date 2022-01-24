package com.breno.helpdesk.services;

import com.breno.helpdesk.domain.Chamado;
import com.breno.helpdesk.domain.repositores.ChamadoRepository;
import com.breno.helpdesk.services.exceptions.ObjectNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ChamadoService {

    @Autowired
    private ChamadoRepository chamadoRepository;

    public Chamado findById(Integer id){
        Optional<Chamado>  obj = chamadoRepository.findById(id);
        return obj.orElseThrow(() -> new ObjectNotFoundException("Objeto não encontrado, ID: " + id));
    }

    public List<Chamado> findAll() {
        return chamadoRepository.findAll();
    }
}
