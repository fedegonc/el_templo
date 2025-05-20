package com.eco.app.model;

import java.time.LocalDate;
import java.time.LocalTime;

import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "turnos2")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Turno {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @NotBlank(message = "El nombre es obligatorio")
    private String nombreCliente;
    
    @NotNull(message = "La fecha es obligatoria")
    private LocalDate fecha;
    
    @NotNull(message = "La hora es obligatoria")
    private LocalTime hora;
    
    @NotBlank(message = "El número de celular es obligatorio")
    @Pattern(regexp = "^[0-9]{3}\\s?[0-9]{3}\\s?[0-9]{3}$", message = "Formato de celular inválido. Debe ingresar 9 dígitos (formatos aceptados: Uruguay: 099 123 456, Brasil: 048 555 123)")
    private String celular;
    
    @Enumerated(EnumType.STRING)
    @NotNull(message = "El estado es obligatorio")
    private EstadoTurno estado = EstadoTurno.EN_ESPERA; // Valor por defecto
    
    // Constructores
    // No son necesarios gracias a las anotaciones de Lombok
    
    // Getters y setters
    // No son necesarios gracias a la anotación @Data de Lombok
}
