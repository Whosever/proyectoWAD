/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ipn.mx.proyecto.modelo.dao;

import com.ipn.mx.proyecto.modelo.dto.EstadoDTO;
import com.ipn.mx.proyecto.modelo.dto.MunicipioDTO;
import com.ipn.mx.proyecto.modelo.entidades.Estados;
import com.ipn.mx.proyecto.modelo.entidades.Municipios;
import com.ipn.mx.proyecto.utilerias.HibernateUtil;
import java.io.Serializable;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.query.Query;

/**
 *
 * @author Alvaro Zúñiga
 */
public class EstadoDAO implements Serializable{

    private static final String M_P_STATE = "from Municipios m where m.estado_id = :estado";

    public EstadoDTO read(EstadoDTO dto) throws SQLException {
        Session sesion = HibernateUtil.getSessionFactory().getCurrentSession();
        Transaction transaccion = sesion.getTransaction();
        try {
            transaccion.begin();
            dto.setEntidad(sesion.get(dto.getEntidad().getClass(), dto.getEntidad().getEstado_id()));
            //sesion.delete(dto.getEntidad());
            transaccion.commit();
        } catch (HibernateException he) {
            if (transaccion != null && transaccion.isActive()) {
                transaccion.rollback();
            }
        }
        return dto;
    }

    public List<EstadoDTO> readAll() throws SQLException{
        Session sesion = HibernateUtil.getSessionFactory().getCurrentSession();
        Transaction transaccion = sesion.getTransaction();
        List<EstadoDTO> lista = new ArrayList();
        try {
            transaccion.begin();
            Query q = sesion.createQuery("from Estados u order by u.estado_id");
            for (Estados u : (List<Estados>) q.list()) {
                EstadoDTO dto = new EstadoDTO();
                dto.setEntidad(u);
                lista.add(dto);
            }
            transaccion.commit();
        } catch (HibernateException he) {
            if (transaccion != null && transaccion.isActive()) {
                transaccion.rollback();
            }
        }
        return lista;
    }
    
    public List readMunicipios(EstadoDTO estado) throws SQLException{
        List lista = new ArrayList();
        if (estado != null && estado.getEntidad().getEstado_id() != 0) {
            Session sesion = HibernateUtil.getSessionFactory().getCurrentSession();
            Transaction transaccion = sesion.getTransaction();
            try {
                transaccion.begin();
                Query q = sesion.createQuery(M_P_STATE);
                q.setParameter("estado", estado.getEntidad());
                for (Municipios m : (List<Municipios>) q.list()) {
                    MunicipioDTO dto = new MunicipioDTO();
                    dto.setEntidad(m);
                    lista.add(dto);
                }
                transaccion.commit();
            } catch (HibernateException he) {
                if (transaccion != null && transaccion.isActive()) {
                    transaccion.rollback();
                }
            }
        }
        return lista;
    }
    
    public MunicipioDTO read(MunicipioDTO dto)throws SQLException{
        Session sesion = HibernateUtil.getSessionFactory().getCurrentSession();
        Transaction transaccion = sesion.getTransaction();
        try {
            transaccion.begin();
            dto.setEntidad(sesion.get(dto.getEntidad().getClass(), dto.getEntidad().getMunicipio_id()));
            //sesion.delete(dto.getEntidad());
            transaccion.commit();
        } catch (HibernateException he) {
            if (transaccion != null && transaccion.isActive()) {
                transaccion.rollback();
            }
        }
        return dto;
    }
    
}
