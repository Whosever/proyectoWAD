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
@Table(name = "usuarios",schema = "public")
public class Usuarios implements Serializable{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int Usuario_id;
    private String nombre;
    private String paterno;
    private String materno;
    private String correo;
    private String clave;
    @ManyToOne
    @JoinColumn(name="municipio_id", nullable=false)
    private Municipios municipio_id;
    private int coins;
    private int tipo;
    private String image;
}
