/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ipn.mx.proyecto.modelo.dto;

import com.ipn.mx.proyecto.modelo.entidades.Estados;
import java.io.Serializable;
import lombok.AllArgsConstructor;
import lombok.Data;

/**
 *
 * @author Alvaro Zúñiga
 */
@Data
@AllArgsConstructor
public class EstadoDTO implements Serializable{
    Estados entidad;

    public EstadoDTO() {
        entidad = new Estados();
    }
    
}
