package com.breno.helpdesk.services;

import com.breno.helpdesk.domain.Pessoa;
import com.breno.helpdesk.domain.Tecnico;
import com.breno.helpdesk.domain.dtos.TecnicoDTO;
import com.breno.helpdesk.domain.repositores.PessoaRepository;
import com.breno.helpdesk.domain.repositores.TecnicoRepository;
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
public class TecnicoService {

    @Autowired
    private TecnicoRepository repository;
    @Autowired
    private PessoaRepository pessoaRepository;
    @Autowired
    private BCryptPasswordEncoder enconder;

    public Tecnico findById(Integer id){
        Optional<Tecnico> obj = repository.findById(id);
        return obj.orElseThrow(() -> new ObjectNotFoundException("Objeto não encontrado " + id));
    }

    public List<Tecnico> findAll() {
        return repository.findAll();
    }

    public Tecnico create(TecnicoDTO objDTO) {
        objDTO.setId(null);
        objDTO.setSenha(enconder.encode(objDTO.getSenha()));
        validaPorCpfEEmail(objDTO);
        Tecnico newObj = new Tecnico(objDTO);
        return repository.save(newObj);
    }

    public Tecnico update(Integer id, @Valid TecnicoDTO objDTO) {
        objDTO.setId(id);
        findById(id);
        validaPorCpfEEmail(objDTO);
        Tecnico oldObj = new Tecnico(objDTO);
        return repository.save(oldObj);
    }

    public void delete(Integer id) {
        Tecnico obj = findById(id);

        if(obj.getChamados().size() > 0){
            throw new DataIntegrityViolationException("Técnico possuí ordens de serviço e não pode ser deletado!");
        }else{
            repository.deleteById(id);
        }
    }

    private void validaPorCpfEEmail(TecnicoDTO objDTO) {
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
