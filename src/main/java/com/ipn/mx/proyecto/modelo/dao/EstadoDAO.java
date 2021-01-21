/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ipn.mx.proyecto.modelo.dao;

import com.ipn.mx.proyecto.modelo.dto.EstadoDTO;
import com.ipn.mx.proyecto.modelo.dto.MunicipioDTO;
import com.ipn.mx.proyecto.modelo.entidades.Estado;
import com.ipn.mx.proyecto.modelo.entidades.Municipio;
import com.ipn.mx.proyecto.utilerias.HibernateUtil;
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
public class EstadoDAO {

    private static final String M_P_STATE = "from municipios m where m.estado_id = :estado";

    public EstadoDTO read(EstadoDTO dto) throws SQLException {
        Session sesion = HibernateUtil.getSessionFactory().getCurrentSession();
        Transaction transaccion = sesion.getTransaction();
        try {
            transaccion.begin();
            dto.setEntidad(sesion.get(dto.getEntidad().getClass(), dto.getEntidad().getId()));
            //sesion.delete(dto.getEntidad());
            transaccion.commit();
        } catch (HibernateException he) {
            if (transaccion != null && transaccion.isActive()) {
                transaccion.rollback();
            }
        }
        return dto;
    }

    public List readAll() {
        Session sesion = HibernateUtil.getSessionFactory().getCurrentSession();
        Transaction transaccion = sesion.getTransaction();
        List lista = new ArrayList();
        try {
            transaccion.begin();
            Query q = sesion.createQuery("from Usuario u order by u.id");
            for (Estado u : (List<Estado>) q.list()) {
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

    public List readMunicipios(EstadoDTO estado) {
        List lista = new ArrayList();
        if (estado != null && estado.getEntidad().getId() != 0) {
            Session sesion = HibernateUtil.getSessionFactory().getCurrentSession();
            Transaction transaccion = sesion.getTransaction();
            try {
                transaccion.begin();
                Query q = sesion.createQuery(M_P_STATE);
                for (Municipio m : (List<Municipio>) q.list()) {
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
}
