/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controlador.web;

import com.ipn.mx.proyecto.modelo.dao.EstadoDAO;
import com.ipn.mx.proyecto.modelo.dto.EstadoDTO;
import com.ipn.mx.proyecto.modelo.dto.MunicipioDTO;
import java.io.Serializable;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.event.ValueChangeEvent;

/**
 *
 * @author Alvaro Zúñiga
 */
@ManagedBean(name = "EstadosMB")
@SessionScoped
public class EstadosMB implements Serializable {

    private final EstadoDAO dao = new EstadoDAO();
    private EstadoDTO dto = new EstadoDTO();
    private List<EstadoDTO> ListaEstados;
    private List<MunicipioDTO> ListaMunicipios;

    /**
     * Creates a new instance of EstadosMB
     */
    public EstadosMB() {
    }

    @PostConstruct
    public void init() {
        ListaEstados = new ArrayList<>();
        try {
            ListaEstados = dao.readAll();
        } catch (SQLException ex) {
            Logger.getLogger(UserBean.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public List<EstadoDTO> getListaEstados() {
        init();
        return ListaEstados;
    }

    public void changeEstado(ValueChangeEvent e) {
        int id = Integer.parseInt(e.getNewValue().toString()); 
        dto.getEntidad().setEstado_id(id);
        try {
            dto = dao.read(dto);
        } catch (SQLException ex) {
            Logger.getLogger(EstadosMB.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public List<MunicipioDTO> getMunicipios(EstadoDTO mydto) {
        dto = mydto;
        ListaMunicipios = new ArrayList<>();
        try {
            ListaMunicipios = dao.readMunicipios(dto);
        } catch (SQLException ex) {
            Logger.getLogger(EstadosMB.class.getName()).log(Level.SEVERE, null, ex);
        }
        return ListaMunicipios;
    }

    public EstadoDTO getDto() {
        return dto;
    }

    public void setDto(EstadoDTO dto) {
        this.dto = dto;
    }

    public List<MunicipioDTO> getListaMunicipios() {
        return ListaMunicipios;
    }

    public void setListaMunicipios(List<MunicipioDTO> ListaMunicipios) {
        this.ListaMunicipios = ListaMunicipios;
    }

}
