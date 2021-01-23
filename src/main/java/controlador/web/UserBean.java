/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controlador.web;

import com.ipn.mx.proyecto.modelo.dao.EstadoDAO;
import com.ipn.mx.proyecto.modelo.dao.UsuarioDAO;
import com.ipn.mx.proyecto.modelo.dto.MunicipioDTO;
import com.ipn.mx.proyecto.modelo.dto.UsuarioDTO;
import com.ipn.mx.proyecto.utilerias.EnviarEmail;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.Serializable;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.faces.event.ActionEvent;
import javax.servlet.http.HttpSession;
import javax.servlet.http.Part;
import org.apache.commons.io.IOUtils;

/**
 *
 * @author Alvaro Zúñiga
 */
@ManagedBean(name = "UserBean")
@SessionScoped
public class UserBean extends BaseBean implements Serializable {

    private final UsuarioDAO dao = new UsuarioDAO();
    private UsuarioDTO dto;
    private List<UsuarioDTO> ListaUsuarios;
    private Part uploadedFile;

    public UserBean() {
    }

    @PostConstruct
    public void init() {
        ListaUsuarios = new ArrayList<>();
        try {
            ListaUsuarios = dao.readAll();
        } catch (SQLException ex) {
            Logger.getLogger(UserBean.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public String prepareAdd() {
        dto = new UsuarioDTO();
        setAccion(ACC_CREAR);
        return "/user/userForm?faceses-redirect=true";
    }

    public String prepareUpdate() {
        setAccion(ACC_ACTUALIZAR);
        return "/user/userForm?faces-redirect=true";
    }

    public String prepareIndex() {
        init();
        return "/index?faces-redirect=true";
    }

    public String prepareLogin() {
        FacesContext facesContext = FacesContext.getCurrentInstance();
        HttpSession session = (HttpSession) facesContext.getExternalContext().getSession(false);
        if (session == null || session.getAttribute("Usuario") == null) {
            dto = new UsuarioDTO();
            return "/login?faceses-redirect=true";
        } else {
            return "/user/dashboard";
        }
    }
    
    public String back() {
        return prepareIndex();
    }

    public boolean validate() {
        boolean valido = true;
        try {
            if (dao.Validate(dto) != 0) {
                valido = false;
            }
        } catch (SQLException ex) {
            Logger.getLogger(UserBean.class.getName()).log(Level.SEVERE, null, ex);
        }
        return valido;
    }

    public String add() {
        boolean valido = validate();
        if (valido) {
            try {
                EstadoDAO edao = new EstadoDAO();
                MunicipioDTO mdto = new MunicipioDTO();
                mdto.getEntidad().setMunicipio_id(dto.getEntidad().getMunicipio_id().getMunicipio_id());
                dto.getEntidad().setMunicipio_id(edao.read(mdto).getEntidad());
                dto.getEntidad().setCoins(1000);
                dao.create(dto);
                EnviarEmail em = new EnviarEmail();
                em.enviarCorreo(dto.getEntidad().getCorreo(), "registro", "registro de datos exitoso");
            } catch (SQLException ex) {
                Logger.getLogger(UserBean.class.getName()).log(Level.SEVERE, null, ex);
            }
            return prepareIndex();
        } else {
            return prepareAdd();
        }
    }

    public String update() {
        boolean valido = validate();
        if (valido) {
            try {
                dao.update(dto);
            } catch (SQLException ex) {
                Logger.getLogger(UserBean.class.getName()).log(Level.SEVERE, null, ex);
            }
            return prepareIndex();
        } else {
            return prepareUpdate();
        }
    }

    public String delete() {
        try {
            dao.delete(dto);
        } catch (SQLException ex) {
            Logger.getLogger(UserBean.class.getName()).log(Level.SEVERE, null, ex);
        }
        return prepareIndex();
    }

    public void upload() {

        if (null != uploadedFile) {
            InputStream is = null;
            OutputStream output = null;
            try {
                is = uploadedFile.getInputStream();
                output = new FileOutputStream(new File("/user/image", dto.getEntidad().getUsuario_id() + ""));
                IOUtils.copy(is, output);
                dto.getEntidad().setImage("/user/image/" + dto.getEntidad().getUsuario_id());

            } catch (IOException ex) {
            } finally {
                IOUtils.closeQuietly(is);
                IOUtils.closeQuietly(output);
            }
        }
    }

    public void seleccionarUsuario(ActionEvent event) {
        String claveSel = (String) FacesContext.getCurrentInstance().
                getExternalContext().
                getRequestParameterMap().
                get("UsuarioID");
        dto = new UsuarioDTO();
        dto.getEntidad().setUsuario_id(Integer.parseInt(claveSel));
        try {
            dao.read(dto);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public String Login() {
        int id = 0;
        try {
            id = dao.Login(dto);
        } catch (SQLException ex) {
            Logger.getLogger(UserBean.class.getName()).log(Level.SEVERE, null, ex);
        }
        if (id <= 0) {
            return prepareLogin();
        } else {
            dto.getEntidad().setUsuario_id(id);
            try {
                dto = dao.read(dto);
                FacesContext facesContext = FacesContext.getCurrentInstance();
                //facesContext.getExternalContext().getSessionMap().put("usuario", dto);
                HttpSession session = (HttpSession) facesContext.getExternalContext().getSession(true);
                session.setAttribute("Usuario", dto);
            } catch (SQLException ex) {
                Logger.getLogger(UserBean.class.getName()).log(Level.SEVERE, null, ex);
            }

            return "/user/dashboard";
        }
    }
    
    public String logout(){
        FacesContext facesContext = FacesContext.getCurrentInstance();
        HttpSession session = (HttpSession) facesContext.getExternalContext().getSession(true);
        session.invalidate();
        return "/index?faceses-redirect=true";
    }
    
    public void checkAlreadyLoggedin() throws IOException{
        FacesContext facesContext = FacesContext.getCurrentInstance();
        HttpSession session = (HttpSession) facesContext.getExternalContext().getSession(false);
        if (session == null || session.getAttribute("Usuario") == null) {
            dto = new UsuarioDTO();
            ExternalContext ec = FacesContext.getCurrentInstance().getExternalContext();
            ec.redirect(ec.getRequestContextPath() + "/login.xhtml");
        }
    }

    public Part getUploadedFile() {
        return uploadedFile;
    }

    public void setUploadedFile(Part uploadedFile) {
        this.uploadedFile = uploadedFile;
    }

    public UsuarioDTO getDto() {
        return dto;
    }

    public void setDto(UsuarioDTO dto) {
        this.dto = dto;
    }

    public List<UsuarioDTO> getListaUsuarios() {
        return ListaUsuarios;
    }

    public void setListaUsuarios(List<UsuarioDTO> ListaUsuarios) {
        this.ListaUsuarios = ListaUsuarios;
    }

}
