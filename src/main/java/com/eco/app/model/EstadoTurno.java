package com.eco.app.model;

/**
 * Representa los posibles estados de un turno en su ciclo de vida
 */
public enum EstadoTurno {
    EN_ESPERA("En espera"),
    CONFIRMADO("Confirmado"),
    CANCELADO("Cancelado"),
    COMPLETADO("Completado");
    
    private String descripcion;
    
    EstadoTurno(String descripcion) {
        this.descripcion = descripcion;
    }
    
    public String getDescripcion() {
        return descripcion;
    }
}
