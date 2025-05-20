package com.eco.app.service;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.eco.app.model.EstadoTurno;
import com.eco.app.model.Turno;
import com.eco.app.repository.TurnoRepository;

@Service
public class TurnoService {
    
    private static final Logger logger = LoggerFactory.getLogger(TurnoService.class);

    @Autowired
    private TurnoRepository turnoRepository;
    
    /**
     * Obtiene todos los turnos ordenados por fecha y hora
     */
    public List<Turno> obtenerTodosTurnos() {
        return turnoRepository.findAllByOrderByFechaAscHoraAsc();
    }
    
    /**
     * Obtiene los turnos para una fecha específica
     */
    public List<Turno> obtenerTurnosPorFecha(LocalDate fecha) {
        return turnoRepository.findByFechaOrderByHoraAsc(fecha);
    }
    
    /**
     * Guarda un nuevo turno verificando disponibilidad
     * @return true si se guardó correctamente, false si el horario ya está ocupado
     */
    @Transactional
    public boolean guardarTurno(Turno turno) {
        logger.info("Intentando guardar turno: {}", turno);
        
        try {
            // Verificar si ya existe un turno en la misma fecha y hora
            List<Turno> turnosExistentes = turnoRepository.findByFechaOrderByHoraAsc(turno.getFecha());
            logger.info("Turnos existentes para la fecha {}: {}", turno.getFecha(), turnosExistentes);
            
            // Verificar si hay algún turno en la misma hora (con margen de 30 minutos)
            boolean horarioOcupado = false;
            
            if (!turnosExistentes.isEmpty()) {
                horarioOcupado = turnosExistentes.stream()
                    .anyMatch(t -> {
                        LocalTime horaExistente = t.getHora();
                        LocalTime horaNueva = turno.getHora();
                        
                        // Calcular la diferencia en minutos
                        int diferenciaMinutos = Math.abs(
                                horaExistente.getHour() * 60 + horaExistente.getMinute() - 
                                (horaNueva.getHour() * 60 + horaNueva.getMinute())
                        );
                        
                        logger.info("Comparando con turno existente: {} - Diferencia en minutos: {}", t, diferenciaMinutos);
                        
                        // Considerar ocupado si hay menos de 30 minutos de diferencia
                        return diferenciaMinutos < 30;
                    });
            }
            
            if (horarioOcupado) {
                logger.info("No se pudo guardar el turno porque el horario está ocupado");
                return false;
            }
            
            // Guardar el turno
            Turno turnoGuardado = turnoRepository.save(turno);
            logger.info("Turno guardado exitosamente: {}", turnoGuardado);
            return true;
        } catch (Exception e) {
            logger.error("Error al guardar el turno: {}", e.getMessage(), e);
            return false;
        }
    }
    
    /**
     * Elimina un turno por su ID
     */
    @Transactional
    public void eliminarTurno(Long id) {
        turnoRepository.deleteById(id);
    }
    
    /**
     * Obtiene un turno por su ID
     */
    public Optional<Turno> obtenerTurnoPorId(Long id) {
        return turnoRepository.findById(id);
    }
    
    /**
     * Actualiza el estado de un turno
     * @param id ID del turno a actualizar
     * @param nuevoEstado Nuevo estado a aplicar
     * @return true si se actualizó correctamente, false en caso contrario
     */
    @Transactional
    public boolean actualizarEstadoTurno(Long id, EstadoTurno nuevoEstado) {
        logger.info("Intentando actualizar estado del turno {} a {}", id, nuevoEstado);
        
        try {
            Optional<Turno> turnoOpt = turnoRepository.findById(id);
            
            if (turnoOpt.isPresent()) {
                Turno turno = turnoOpt.get();
                turno.setEstado(nuevoEstado);
                turnoRepository.save(turno);
                logger.info("Estado del turno actualizado correctamente");
                return true;
            } else {
                logger.warn("No se encontró el turno con ID: {}", id);
                return false;
            }
        } catch (Exception e) {
            logger.error("Error al actualizar el estado del turno: {}", e.getMessage(), e);
            return false;
        }
    }
}
