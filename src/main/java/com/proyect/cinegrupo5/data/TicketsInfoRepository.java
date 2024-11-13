package com.proyect.cinegrupo5.data;


import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

public interface TicketsInfoRepository extends JpaRepository<TicketsInfo, Long>, JpaSpecificationExecutor<TicketsInfo> {

}
