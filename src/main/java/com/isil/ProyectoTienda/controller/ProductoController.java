package com.isil.ProyectoTienda.controller;



import com.isil.ProyectoTienda.model.Producto;
import com.isil.ProyectoTienda.model.Proveedor;
import com.isil.ProyectoTienda.model.Usuario;
import com.isil.ProyectoTienda.repository.ProveedorRepository;
import com.isil.ProyectoTienda.service.ProductoService;
import com.isil.ProyectoTienda.service.ProveedorService;
import com.isil.ProyectoTienda.service.UploadFileService;
import com.isil.ProyectoTienda.service.UsuarioService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.util.*;

@Controller
@RequestMapping("/productos")
public class ProductoController {

    private final Logger LOGGER = LoggerFactory.getLogger(ProductoController.class);
    @Autowired
    private ProductoService productoService;

    @Autowired
    private UsuarioService usuarioService;


    @Autowired
    private ProveedorService proveedorService;

    @Autowired
    private UploadFileService upload;
    @Autowired
    private ProveedorRepository proveedorRepository;

    @GetMapping("")
    public String show(Model model) {
        model.addAttribute("productos", productoService.findAll());
        return "productos/show";
    }

    @GetMapping("/create")
    public String create(Model model) {

        model.addAttribute("proveedores", proveedorService.findAll());

        return "productos/create";
    }

    @PostMapping("/save")
    public String save(Producto producto, @RequestParam("img") MultipartFile file, HttpSession session, RedirectAttributes redirectAttributes) throws IOException {
        LOGGER.info("Este es el objeto producto {}",producto);


        Usuario u= usuarioService.findById(Integer.parseInt(session.getAttribute("idusuario").toString() )).get();
        producto.setProveedor(producto.getProveedor());
        producto.setUsuario(u);


        //imagen
        if (producto.getId()==null) { // cuando se crea un producto
            String nombreImagen= upload.saveImage(file);
            producto.setImagen(nombreImagen);
        }else {

        }

        productoService.save(producto);
        redirectAttributes.addFlashAttribute("mensaje", "Se añadio el nuevo producto!")
                .addFlashAttribute("clase", "success");
        return "redirect:/productos";
    }

    @GetMapping("/edit/{id}")
    public String edit(@PathVariable Integer id, Model model) {
        Producto producto= new Producto();
        Optional<Producto> optionalProducto=productoService.get(id);
        producto= optionalProducto.get();

        LOGGER.info("Producto buscado: {}",producto);
        model.addAttribute("producto", producto);

        return "productos/edit";
    }

    @PostMapping("/update")
    public String update(Producto producto, @RequestParam("img") MultipartFile file, RedirectAttributes redirectAttributes ) throws IOException {
        Producto p= new Producto();
        p=productoService.get(producto.getId()).get();

        if (file.isEmpty()) { // editamos el producto pero no cambiamos la imagem

            producto.setImagen(p.getImagen());
        }else {// cuando se edita tbn la imagen
            //eliminar cuando no sea la imagen por defecto
            if (!p.getImagen().equals("default.jpg")) {
                upload.deleteImage(p.getImagen());
            }
            String nombreImagen= upload.saveImage(file);
            producto.setImagen(nombreImagen);
        }
        producto.setUsuario(p.getUsuario());
        productoService.update(producto);
        redirectAttributes.addFlashAttribute("mensaje", "Se hizo la actualización del producto!")
                .addFlashAttribute("clase", "success");
        return "redirect:/productos";
    }

    @GetMapping("/delete/{id}")
    public String delete(@PathVariable Integer id, RedirectAttributes redirectAttributes) {

        Producto p = new Producto();
        p=productoService.get(id).get();

        //eliminar cuando no sea la imagen por defecto
        if (!p.getImagen().equals("default.jpg")) {
            upload.deleteImage(p.getImagen());
        }

        productoService.delete(id);
        redirectAttributes.addFlashAttribute("mensaje", "Se eliminó el producto!")
                .addFlashAttribute("clase", "success");
        return "redirect:/productos";
    }


}
