package com.breno.helpdesk.services;

import com.breno.helpdesk.domain.Pessoa;
import com.breno.helpdesk.domain.Cliente;
import com.breno.helpdesk.domain.dtos.ClienteDTO;
import com.breno.helpdesk.domain.repositores.PessoaRepository;
import com.breno.helpdesk.domain.repositores.ClienteRepository;
import com.breno.helpdesk.services.exceptions.DataIntegrityViolationException;
import com.breno.helpdesk.services.exceptions.ObjectNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import javax.validation.Valid;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Service
public class ClienteService {

    @Autowired
    private ClienteRepository repository;
    @Autowired
    private PessoaRepository pessoaRepository;
    @Autowired
    private BCryptPasswordEncoder enconder;

    public Cliente findById(Integer id){
        Optional<Cliente> obj = repository.findById(id);
        return obj.orElseThrow(() -> new ObjectNotFoundException("Objeto não encontrado " + id));
    }

    public List<Cliente> findAll() {
        return repository.findAll();
    }

    public Cliente create(ClienteDTO objDTO) {
        objDTO.setId(null);
        objDTO.setSenha(enconder.encode(objDTO.getSenha()));
        validaPorCpfEEmail(objDTO);
        Cliente newObj = new Cliente(objDTO);
        return repository.save(newObj);
    }

    public Cliente update(Integer id, @Valid ClienteDTO objDTO) {
        objDTO.setId(id);
        findById(id);
        validaPorCpfEEmail(objDTO);
        Cliente oldObj = new Cliente(objDTO);
        return repository.save(oldObj);
    }

    public void delete(Integer id) {
        Cliente obj = findById(id);

        if(obj.getChamados().size() > 0){
            throw new DataIntegrityViolationException("Cliente possuí ordens de serviço e não pode ser deletado!");
        }else{
            repository.deleteById(id);
        }
    }

    private void validaPorCpfEEmail(ClienteDTO objDTO) {
        Optional<Pessoa> obj = pessoaRepository.findByCpf(objDTO.getCpf());
        if(obj.isPresent() && !Objects.equals(obj.get().getId(), objDTO.getId())) {
            //obj.get().getId() != objDTO.getId())
            throw  new DataIntegrityViolationException("CPF já cadastrado no sistema!");
        }

        obj = pessoaRepository.findByEmail(objDTO.getEmail());
        if(obj.isPresent() && !Objects.equals(obj.get().getId(), objDTO.getId())) {
            //obj.get().getId() != objDTO.getId())
            throw  new DataIntegrityViolationException("Email já cadastrado no sistema!");
        }
    }

}
