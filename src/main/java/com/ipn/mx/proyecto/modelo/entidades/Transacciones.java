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
@Table(name = "Transacciones",schema = "public")
public class Transacciones implements Serializable{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int transaction_id;
    @ManyToOne
    @JoinColumn(name="origen", nullable=false)
    private Usuarios origen;
    @ManyToOne
    @JoinColumn(name="destino", nullable=false)
    private Usuarios destino;
    private int monto;
}
