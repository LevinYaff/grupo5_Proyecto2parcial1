package com.proyect.cinegrupo5.services;

import com.proyect.cinegrupo5.data.TicketsInfo;
import com.proyect.cinegrupo5.data.TicketsInfoRepository;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

@Service
public class TicketsInfoService {

    private final TicketsInfoRepository repository;

    public TicketsInfoService(TicketsInfoRepository repository) {
        this.repository = repository;
    }

    public Optional<TicketsInfo> get(Long id) {
        return repository.findById(id);
    }

    public TicketsInfo update(TicketsInfo entity) {
        return repository.save(entity);
    }

    public void delete(Long id) {
        repository.deleteById(id);
    }

    public Page<TicketsInfo> list(Pageable pageable) {
        return repository.findAll(pageable);
    }

    public Page<TicketsInfo> list(Pageable pageable, Specification<TicketsInfo> filter) {
        return repository.findAll(filter, pageable);
    }

    public int count() {
        return (int) repository.count();
    }

}
