package com.proyect.cinegrupo5.data;


import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

public interface ClientesInfoRepository
        extends
            JpaRepository<ClientesInfo, Long>,
            JpaSpecificationExecutor<ClientesInfo> {

}
