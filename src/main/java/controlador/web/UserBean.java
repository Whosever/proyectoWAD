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
import java.nio.file.Files;
import java.nio.file.Paths;
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
import javax.servlet.ServletContext;
import javax.servlet.http.HttpSession;
import javax.servlet.http.Part;
import org.apache.commons.io.IOUtils;
import org.apache.commons.io.FilenameUtils;

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

    public String prepareAdd() {
        dto = new UsuarioDTO();
        setAccion(ACC_CREAR);
        return "/user/userForm?faceses-redirect=true";
    }

    public String prepareUpdate() {
        setAccion(ACC_ACTUALIZAR);
        FacesContext facesContext = FacesContext.getCurrentInstance();
        HttpSession session = (HttpSession) facesContext.getExternalContext().getSession(false);
        dto = (UsuarioDTO) session.getAttribute("Usuario");
        return "/user/userForm?faces-redirect=true";
    }

    public String prepareIndex() {
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
        try {
            EstadoDAO edao = new EstadoDAO();
            MunicipioDTO mdto = new MunicipioDTO();
            mdto.getEntidad().setMunicipio_id(dto.getEntidad().getMunicipio_id().getMunicipio_id());
            dto.getEntidad().setMunicipio_id(edao.read(mdto).getEntidad());
            dao.update(dto);
        } catch (SQLException ex) {
            Logger.getLogger(UserBean.class.getName()).log(Level.SEVERE, null, ex);
        }
        return prepareLogin();
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
            FacesContext facesContext = FacesContext.getCurrentInstance();
            HttpSession session = (HttpSession) facesContext.getExternalContext().getSession(false);
            InputStream is = null;
            File output = null;
            try {
                is = uploadedFile.getInputStream();
                String extension = FilenameUtils.getExtension(Paths.get(uploadedFile.getSubmittedFileName()).getFileName().toString());
                FacesContext context = FacesContext.getCurrentInstance();
		ServletContext servletContext = (ServletContext) context.getExternalContext().getContext();
		String path = servletContext.getRealPath("");
                File uploads = new File(path+File.separator+"user"+File.separator+"image");
                output = new File(uploads, "U"+dto.getEntidad().getUsuario_id() + "."+extension);  
                if(output.exists()){
                    output.delete();
                }
                Files.copy(is, output.toPath());
                dto = (UsuarioDTO) session.getAttribute("Usuario");
                dto.getEntidad().setImage(output.getAbsolutePath());
                dao.update(dto);
            } catch (IOException | SQLException ex) {
                Logger.getLogger(UserBean.class.getName()).log(Level.SEVERE, null, ex);
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

    public String logout() {
        FacesContext facesContext = FacesContext.getCurrentInstance();
        HttpSession session = (HttpSession) facesContext.getExternalContext().getSession(true);
        session.invalidate();
        return "/index?faceses-redirect=true";
    }

    public void checkAlreadyLoggedin() throws IOException {
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
