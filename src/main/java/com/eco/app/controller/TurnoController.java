package com.eco.app.controller;

import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.propertyeditors.StringTrimmerEditor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.eco.app.model.EstadoTurno;
import com.eco.app.model.Turno;
import com.eco.app.service.TurnoService;

import javax.validation.Valid;

@Controller
public class TurnoController {
    
    private static final Logger logger = LoggerFactory.getLogger(TurnoController.class);

    @Autowired
    private TurnoService turnoService;
    
    /**
     * Inicializa el binder para la conversión de datos
     */
    @InitBinder
    public void initBinder(WebDataBinder binder) {
        // Elimina espacios en blanco de los strings
        binder.registerCustomEditor(String.class, new StringTrimmerEditor(true));
        
        // Registra formateadores personalizados para fechas y horas
        logger.info("Configurando formateadores de fecha y hora");
    }
    
    // Página principal para clientes
    @GetMapping({"/", "/agenda"})
    public String mostrarAgenda(Model model) {
        model.addAttribute("nuevoTurno", new Turno());
        model.addAttribute("turnos", turnoService.obtenerTodosTurnos());
        model.addAttribute("fechaHoy", LocalDate.now());
        return "agenda";
    }
    
    // Página de administración
    @GetMapping("/admin")
    public String mostrarAdmin(Model model) {
        model.addAttribute("turnos", turnoService.obtenerTodosTurnos());
        return "admin";
    }
    
    @PostMapping("/agendar")
    public String agendarTurno(@Valid @ModelAttribute("nuevoTurno") Turno nuevoTurno, 
                              BindingResult result, 
                              RedirectAttributes redirectAttributes,
                              Model model) {
        
        logger.info("Recibiendo solicitud para agendar turno: {}", nuevoTurno);
        
        if (result.hasErrors()) {
            logger.warn("Errores de validación en el formulario: {}", result.getAllErrors());
            model.addAttribute("turnos", turnoService.obtenerTodosTurnos());
            return "agenda";
        }
        
        try {
            logger.info("Intentando guardar turno con nombre: {}, fecha: {}, hora: {}", 
                    nuevoTurno.getNombreCliente(), nuevoTurno.getFecha(), nuevoTurno.getHora());
            
            boolean turnoGuardado = turnoService.guardarTurno(nuevoTurno);
            
            if (turnoGuardado) {
                logger.info("Turno guardado exitosamente");
                redirectAttributes.addFlashAttribute("mensaje", "Turno reservado correctamente");
            } else {
                logger.warn("No se pudo guardar el turno, horario ocupado");
                redirectAttributes.addFlashAttribute("mensaje", "No se pudo reservar el turno. El horario ya está ocupado");
            }
            
            return "redirect:/agenda";
        } catch (Exception e) {
            logger.error("Error al guardar el turno: {}", e.getMessage(), e);
            redirectAttributes.addFlashAttribute("mensaje", "Error al guardar el turno: " + e.getMessage());
            return "redirect:/agenda";
        }
    }
    
    // Borrar turno desde la vista del cliente
    @PostMapping("/borrar/{id}")
    public String borrarTurno(@PathVariable("id") Long id, RedirectAttributes redirectAttributes) {
        turnoService.eliminarTurno(id);
        redirectAttributes.addFlashAttribute("mensaje", "Turno cancelado correctamente");
        return "redirect:/agenda";
    }
    
    // Borrar turno desde la vista de administración
    @PostMapping("/admin/borrar/{id}")
    public String borrarTurnoAdmin(@PathVariable("id") Long id, RedirectAttributes redirectAttributes) {
        turnoService.eliminarTurno(id);
        redirectAttributes.addFlashAttribute("mensaje", "Turno eliminado correctamente");
        return "redirect:/admin";
    }
    
    // Cambiar el estado de un turno
    @PostMapping("/admin/estado/{id}")
    public String cambiarEstadoTurno(
            @PathVariable("id") Long id, 
            @RequestParam("estado") String estadoStr,
            RedirectAttributes redirectAttributes) {
        
        try {
            EstadoTurno nuevoEstado = EstadoTurno.valueOf(estadoStr);
            boolean actualizado = turnoService.actualizarEstadoTurno(id, nuevoEstado);
            
            if (actualizado) {
                redirectAttributes.addFlashAttribute("mensaje", "Estado del turno actualizado a " + nuevoEstado.getDescripcion());
            } else {
                redirectAttributes.addFlashAttribute("mensaje", "No se pudo actualizar el estado del turno");
            }
        } catch (Exception e) {
            logger.error("Error al cambiar estado del turno: {}", e.getMessage(), e);
            redirectAttributes.addFlashAttribute("mensaje", "Error al cambiar el estado del turno");
        }
        
        return "redirect:/admin";
    }
}
