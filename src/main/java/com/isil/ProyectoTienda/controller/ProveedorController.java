package com.isil.ProyectoTienda.controller;

import com.isil.ProyectoTienda.model.Proveedor;
import com.isil.ProyectoTienda.model.Usuario;
import com.isil.ProyectoTienda.service.ProveedorService;
import com.isil.ProyectoTienda.service.UsuarioService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.util.Optional;

@Controller
@RequestMapping("/proveedor")
public class ProveedorController {

    private final Logger LOGGER = LoggerFactory.getLogger(ProveedorController.class);

    @Autowired
    private ProveedorService proveedorService;

    @Autowired
    private UsuarioService usuarioService;

    @GetMapping("")
    public String show(Model model) {
        model.addAttribute("proveedor", proveedorService.findAll());
        return "proveedor/show";
    }

    @GetMapping("create")
    public String create() {
        return "proveedor/create";
    }

    @PostMapping("/save")
    public String save(Proveedor proveedor, HttpSession session, RedirectAttributes redirectAttributes) throws IOException {
        LOGGER.info("Este es el objeto Proveedores {}",proveedor);

        Usuario usuario = usuarioService.findById(Integer.parseInt(session.getAttribute("idusuario").toString())).get();
        proveedor.setUsuario(usuario);

        proveedorService.save(proveedor);
        redirectAttributes.addFlashAttribute("mensaje", "Se añadio nueva compra dle proveedor!")
                .addFlashAttribute("clase", "success");
        return "redirect:/proveedor";
    }

    @GetMapping("/edit/{id}")
    public String edit(@PathVariable Integer id, Model model){
        Proveedor proveedor = new Proveedor();
        Optional<Proveedor> optionalProveedor = proveedorService.get(id);
        proveedor = optionalProveedor.get();

        LOGGER.info("Proveedor buscado: {}",proveedor);
        model.addAttribute("proveedor",proveedor);

        return "proveedor/edit";
    }

    @PostMapping("/update")
    public String update(Proveedor proveedor, RedirectAttributes redirectAttributes) throws IOException {
        Proveedor proveedor1 = new Proveedor();
        proveedor1 = proveedorService.get(proveedor.getId()).get();

        proveedor.setUsuario(proveedor1.getUsuario());
        proveedorService.update(proveedor);
        redirectAttributes.addFlashAttribute("mensaje", "Se hizo la actualización de la compra del proveedor!")
                .addFlashAttribute("clase", "success");
        return "redirect:/proveedor";
    }

    @GetMapping("/delete/{id}")
    public String delete(@PathVariable Integer id, RedirectAttributes redirectAttributes) {
        Proveedor proveedor = new Proveedor();
        proveedor = proveedorService.get(id).get();

        proveedorService.delete(id);
        redirectAttributes.addFlashAttribute("mensaje", "Se eliminó la compra del proveedor!")
                .addFlashAttribute("clase", "success");
        return "redirect:/proveedor";
    }



}
