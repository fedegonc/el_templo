package com.eco.app.repository;

import java.time.LocalDate;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.eco.app.model.Turno;

@Repository
public interface TurnoRepository extends JpaRepository<Turno, Long> {
    
    // Método para encontrar turnos por fecha
    List<Turno> findByFechaOrderByHoraAsc(LocalDate fecha);
    
    // Método para encontrar todos los turnos ordenados por fecha y hora
    List<Turno> findAllByOrderByFechaAscHoraAsc();
}
