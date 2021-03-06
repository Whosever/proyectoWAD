/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ipn.mx.proyecto.modelo.dao;

import com.ipn.mx.proyecto.modelo.dto.UsuarioDTO;
import com.ipn.mx.proyecto.modelo.entidades.Usuarios;
import com.ipn.mx.proyecto.utilerias.HibernateUtil;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.query.Query;

/**
 *
 * @author Alvaro Zúñiga
 */
public class UsuarioDAO {

    private static final String SQL_VALIDATE = "from Usuarios u where u.correo = :email";
    private static final String SQL_LOGIN = "from Usuarios u where u.correo = :email AND clave = :clave";

    public void create(UsuarioDTO dto) throws SQLException {
        Session sesion = HibernateUtil.getSessionFactory().getCurrentSession();
        Transaction transaccion = sesion.getTransaction();
        try {
            transaccion.begin();
            sesion.save(dto.getEntidad());
            transaccion.commit();
        } catch (HibernateException he) {
            if (transaccion != null && transaccion.isActive()) {
                transaccion.rollback();
            }
        }
    }

    public void update(UsuarioDTO dto) throws SQLException {
        Session sesion = HibernateUtil.getSessionFactory().getCurrentSession();
        Transaction transaccion = sesion.getTransaction();
        try {
            transaccion.begin();
            sesion.update(dto.getEntidad());
            transaccion.commit();
        } catch (HibernateException he) {
            if (transaccion != null && transaccion.isActive()) {
                transaccion.rollback();
            }
        }
    }

    public void delete(UsuarioDTO dto) throws SQLException {
        Session sesion = HibernateUtil.getSessionFactory().getCurrentSession();
        Transaction transaccion = sesion.getTransaction();
        try {
            transaccion.begin();
            sesion.delete(dto.getEntidad());
            transaccion.commit();
        } catch (HibernateException he) {
            if (transaccion != null && transaccion.isActive()) {
                transaccion.rollback();
            }
        }
    }

    public UsuarioDTO read(UsuarioDTO dto) throws SQLException {
        Session sesion = HibernateUtil.getSessionFactory().getCurrentSession();
        Transaction transaccion = sesion.getTransaction();
        try {
            transaccion.begin();
            dto.setEntidad(sesion.get(dto.getEntidad().getClass(), dto.getEntidad().getUsuario_id()));
            //sesion.delete(dto.getEntidad());
            transaccion.commit();
        } catch (HibernateException he) {
            if (transaccion != null && transaccion.isActive()) {
                transaccion.rollback();
            }
        }
        return dto;
    }

    public List readAll() throws SQLException {
        Session sesion = HibernateUtil.getSessionFactory().getCurrentSession();
        Transaction transaccion = sesion.getTransaction();
        List lista = new ArrayList();
        try {
            transaccion.begin();
            Query q = sesion.createQuery("from Usuarios u order by u.Usuario_id");
            for (Usuarios u : (List<Usuarios>) q.list()) {
                UsuarioDTO dto = new UsuarioDTO();
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

    public int Validate(UsuarioDTO dto) throws SQLException {
        Session sesion = HibernateUtil.getSessionFactory().getCurrentSession();
        Transaction transaccion = sesion.getTransaction();
        List lista = new ArrayList();
        try {
            transaccion.begin();
            Query q = sesion.createQuery(SQL_VALIDATE);
            q.setParameter("email", dto.getEntidad().getCorreo());
            for (Usuarios u : (List<Usuarios>) q.list()) {
                UsuarioDTO dtoAux = new UsuarioDTO();
                dtoAux.setEntidad(u);
                lista.add(dtoAux);
            }
            transaccion.commit();
        } catch (HibernateException he) {
            if (transaccion != null && transaccion.isActive()) {
                transaccion.rollback();
            }
        }
        return lista.size();
    }
    public int Login(UsuarioDTO dto) throws SQLException {
        int val = 0;
        Session sesion = HibernateUtil.getSessionFactory().getCurrentSession();
        Transaction transaccion = sesion.getTransaction();
        List lista = new ArrayList();
        try {
            transaccion.begin();
            Query q = sesion.createQuery(SQL_LOGIN);
            q.setParameter("email", dto.getEntidad().getCorreo());
            q.setParameter("clave", dto.getEntidad().getClave());
            for (Usuarios u : (List<Usuarios>) q.list()) {
                UsuarioDTO dtoAux = new UsuarioDTO();
                dtoAux.setEntidad(u);
                lista.add(dtoAux);
            }
            transaccion.commit();
        } catch (HibernateException he) {
            if (transaccion != null && transaccion.isActive()) {
                transaccion.rollback();
            }
        }
        val = lista.size();
        if(val == 1){
            Usuarios u = ((UsuarioDTO) lista.get(0)).getEntidad();
            return u.getUsuario_id();
        }else{
            return 0;
        }
    }
}
