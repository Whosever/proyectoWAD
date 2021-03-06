/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ipn.mx.proyecto.modelo.entidades;

import java.io.Serializable;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 *
 * @author Alvaro Zúñiga
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "Municipios",schema = "public")
public class Municipios implements Serializable{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int municipio_id;
    @ManyToOne
    @JoinColumn(name="estado_id", nullable=false)
    private Estados estado_id;
    private String clave;
    private String nombre;
    private int activo;
}
