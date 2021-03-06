package com.breno.helpdesk.services;

import com.breno.helpdesk.domain.Chamado;
import com.breno.helpdesk.domain.Cliente;
import com.breno.helpdesk.domain.Tecnico;
import com.breno.helpdesk.domain.enums.Perfil;
import com.breno.helpdesk.domain.enums.Prioridade;
import com.breno.helpdesk.domain.enums.Status;
import com.breno.helpdesk.domain.repositores.ChamadoRepository;
import com.breno.helpdesk.domain.repositores.ClienteRepository;
import com.breno.helpdesk.domain.repositores.TecnicoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Arrays;

@Service
public class DBService {

    @Autowired
    private TecnicoRepository tecnicoRepository;
    @Autowired
    private ClienteRepository clienteRepository;
    @Autowired
    private ChamadoRepository chamadoRepository;
    @Autowired
    private BCryptPasswordEncoder enconder;

    public void instanceDB(){
        Tecnico tec1 = new Tecnico(null, "Valdir Cezar", "83575441340", "valdir@mail.com", enconder.encode("123"));
        tec1.addPerfil(Perfil.ADMIN);

        Cliente cli1 = new Cliente(null, "Linus Torvalds", "92412824282", "linus@email.com", enconder.encode("123"));

        Chamado c1 = new Chamado(null, Prioridade.MEDIA, Status.ANDAMENTO, "Chamado 01", "Primeiro chamado", tec1, cli1);

        tecnicoRepository.saveAll(Arrays.asList(tec1));
        clienteRepository.saveAll(Arrays.asList(cli1));
        chamadoRepository.saveAll(Arrays.asList(c1));
    }
}
