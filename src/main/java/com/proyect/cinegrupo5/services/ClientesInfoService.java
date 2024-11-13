package com.proyect.cinegrupo5.services;

import com.proyect.cinegrupo5.data.ClientesInfo;
import com.proyect.cinegrupo5.data.ClientesInfoRepository;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

@Service
public class ClientesInfoService {

    private final ClientesInfoRepository repository;

    public ClientesInfoService(ClientesInfoRepository repository) {
        this.repository = repository;
    }

    public Optional<ClientesInfo> get(Long id) {
        return repository.findById(id);
    }

    public ClientesInfo update(ClientesInfo entity) {
        return repository.save(entity);
    }

    public void delete(Long id) {
        repository.deleteById(id);
    }

    public Page<ClientesInfo> list(Pageable pageable) {
        return repository.findAll(pageable);
    }

    public Page<ClientesInfo> list(Pageable pageable, Specification<ClientesInfo> filter) {
        return repository.findAll(filter, pageable);
    }

    public int count() {
        return (int) repository.count();
    }

}
