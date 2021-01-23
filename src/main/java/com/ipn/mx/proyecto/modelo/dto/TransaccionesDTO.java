/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ipn.mx.proyecto.modelo.dto;

import com.ipn.mx.proyecto.modelo.entidades.Transacciones;
import com.ipn.mx.proyecto.modelo.entidades.Usuarios;
import java.io.Serializable;
import lombok.AllArgsConstructor;
import lombok.Data;

/**
 *
 * @author Alvaro Zúñiga
 */
@Data
@AllArgsConstructor
public class TransaccionesDTO implements Serializable{
    Transacciones entidad;

    public TransaccionesDTO() {
        entidad = new Transacciones();
        entidad.setOrigen(new Usuarios());
        entidad.setDestino(new Usuarios());
    }
    
}
