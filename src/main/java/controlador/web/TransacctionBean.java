/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controlador.web;

import com.ipn.mx.proyecto.modelo.dao.TransaccionesDAO;
import com.ipn.mx.proyecto.modelo.dao.UsuarioDAO;
import com.ipn.mx.proyecto.modelo.dto.TransaccionesDTO;
import com.ipn.mx.proyecto.modelo.dto.UsuarioDTO;
import com.ipn.mx.proyecto.modelo.entidades.Usuarios;
import com.ipn.mx.proyecto.utilerias.DBconnection;
import java.io.File;
import java.io.IOException;
import java.io.Serializable;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.context.FacesContext;
import javax.servlet.ServletContext;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JasperExportManager;
import net.sf.jasperreports.engine.JasperRunManager;

/**
 *
 * @author Alvaro Zúñiga
 */
@ManagedBean(name = "TransacctionBean")
@SessionScoped
public class TransacctionBean implements Serializable {

    private final TransaccionesDAO dao = new TransaccionesDAO();
    private TransaccionesDTO dto;
    private List<TransaccionesDTO> ListaTransacciones;
    private String message1 = "";
    private String message2 = "";

    public TransacctionBean() {
    }

    public String roll() {
        int reward = 0;
        dto = new TransaccionesDTO();
        UsuarioDAO udao = new UsuarioDAO();
        UsuarioDTO udto = new UsuarioDTO();
        udto.getEntidad().setUsuario_id(1);
        try {
            udto = udao.read(udto);
        } catch (SQLException ex) {
            Logger.getLogger(TransacctionBean.class.getName()).log(Level.SEVERE, null, ex);
        }
        FacesContext facesContext = FacesContext.getCurrentInstance();
        HttpSession session = (HttpSession) facesContext.getExternalContext().getSession(false);

        //ejecutar primer pago
        dto.getEntidad().setOrigen(((UsuarioDTO) session.getAttribute("Usuario")).getEntidad());
        dto.getEntidad().setDestino(udto.getEntidad());
        dto.getEntidad().setMonto(20);
        ejecutarTransaccion();

        int valorDado = (int) Math.floor(Math.random() * 99999 + 1);
        if (valorDado < 70000) {
            reward = 15;
        } else {
            if (valorDado < 90000) {
                reward = 25;
            } else {
                if (valorDado < 97000) {
                    reward = 46;
                } else {
                    if (valorDado < 99000) {
                        reward = 95;
                    } else {
                        if (valorDado < 99999) {
                            reward = 176;
                        } else {
                            reward = 1000;
                        }
                    }
                }
            }
        }
        message1 = "Rolled: " + valorDado;
        message2 = "won: " + reward;

        //ejecutar segundo pago
        dto.getEntidad().setDestino(dto.getEntidad().getOrigen());
        dto.getEntidad().setOrigen(udto.getEntidad());
        dto.getEntidad().setMonto(reward);
        ejecutarTransaccion();

        return "/user/ruleta";
    }

    public String prepareTransactions() {
        ListaTransacciones = new ArrayList<>();

        FacesContext facesContext = FacesContext.getCurrentInstance();
        HttpSession session = (HttpSession) facesContext.getExternalContext().getSession(false);
        UsuarioDTO udto = (UsuarioDTO) session.getAttribute("Usuario");
        ListaTransacciones = dao.readUser(udto);

        return "/user/transactions";
    }

    public void ejecutarTransaccion() {
        UsuarioDAO udao = new UsuarioDAO();
        try {
            dao.create(dto);
            dto.getEntidad().getOrigen().setCoins(dto.getEntidad().getOrigen().getCoins() - dto.getEntidad().getMonto());
            dto.getEntidad().getDestino().setCoins(dto.getEntidad().getDestino().getCoins() + dto.getEntidad().getMonto());
            udao.update(new UsuarioDTO(dto.getEntidad().getOrigen()));
            udao.update(new UsuarioDTO(dto.getEntidad().getDestino()));
        } catch (SQLException ex) {
            Logger.getLogger(TransacctionBean.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void verPDF() {
        FacesContext facesContext = FacesContext.getCurrentInstance();
        HttpSession session = (HttpSession) facesContext.getExternalContext().getSession(false);
        HttpServletResponse httpServletResponse = (HttpServletResponse) FacesContext.getCurrentInstance().getExternalContext().getResponse();
        httpServletResponse.addHeader("Content-disposition", "attachment; filename=reporte.pdf");
        
        FacesContext.getCurrentInstance().responseComplete();
        try {
            ServletOutputStream servletOutputStream=httpServletResponse.getOutputStream();   
            File reporte = new File(((ServletContext) FacesContext.getCurrentInstance().getExternalContext().getContext()).getRealPath("/reportes/Cherry.jasper"));
            
            Map parameters = new HashMap();      
            parameters.put("usuarioID",((UsuarioDTO) session.getAttribute("Usuario")).getEntidad().getUsuario_id());
            DBconnection db = new DBconnection();
            
            byte[] bytes = JasperRunManager.runReportToPdf(reporte.getPath(), parameters, db.getConnection());
            httpServletResponse.setContentType("application/pdf");
            httpServletResponse.setContentLength(bytes.length);
            servletOutputStream.write(bytes, 0, bytes.length);
            servletOutputStream.flush();
            servletOutputStream.close();
        } catch (IOException | JRException ex) {
            Logger.getLogger(TransacctionBean.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public TransaccionesDTO getDto() {
        return dto;
    }

    public void setDto(TransaccionesDTO dto) {
        this.dto = dto;
    }

    public List<TransaccionesDTO> getListaTransacciones() {
        return ListaTransacciones;
    }

    public void setListaTransacciones(List<TransaccionesDTO> ListaTransacciones) {
        this.ListaTransacciones = ListaTransacciones;
    }

    public String getMessage1() {
        return message1;
    }

    public void setMessage1(String message1) {
        this.message1 = message1;
    }

    public String getMessage2() {
        return message2;
    }

    public void setMessage2(String message2) {
        this.message2 = message2;
    }

}
