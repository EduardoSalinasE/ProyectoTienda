package com.isil.ProyectoTienda.controller;



import com.isil.ProyectoTienda.model.*;
import com.isil.ProyectoTienda.repository.ProveedorRepository;
import com.isil.ProyectoTienda.service.*;
import com.isil.ProyectoTienda.util.ProductosExporterPDF;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
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
    private ReporteProductosService reporteService;

    @Autowired
    private ProveedorService proveedorService;

    @Autowired
    private DetalleBoletasService detalleBoletasService;

    @Autowired
    private UploadFileService upload;
    @Autowired
    private ProveedorRepository proveedorRepository;

    @GetMapping("")
    public String show(Model model) {
        model.addAttribute("productos", productoService.findAll());
        return "productos/show";
    }

    @GetMapping("/mostrarReporte")
    public String showTable(Model model) {
        model.addAttribute("reporte", reporteService.findAll());
        return "reportes/reportes_productos";
    }

    @GetMapping("/create")
    public String create(Model model, Proveedor proveedor, DetalleBoletas detalleBoletas) {

        model.addAttribute("proveedores", proveedorService.findAll());
        model.addAttribute("boletas", detalleBoletasService.findAll());

        return "productos/create";
    }

    @PostMapping("/save")
    public String save(Producto producto, DetalleBoletas detalleBoletas, ReportesProductos reportes, @RequestParam("img") MultipartFile file, HttpSession session, RedirectAttributes redirectAttributes) throws IOException {
        LOGGER.info("Este es el objeto producto {}",producto);
        Date fechaActual = new Date();

        Usuario u= usuarioService.findById(Integer.parseInt(session.getAttribute("idusuario").toString() )).get();


        producto.setUsuario(u);

        reportes.setUsuario(u);
        reportes.setNombreProducto(producto.getNombre());
        reportes.setFechaCreacion(fechaActual);
        reportes.setTipoOperacion("Creacion");


        //imagen
        if (producto.getId()==null) { // cuando se crea un producto
            String nombreImagen= upload.saveImage(file);
            producto.setImagen(nombreImagen);
        }else {

        }


        productoService.save(producto);
        reporteService.save(reportes);
        redirectAttributes.addFlashAttribute("mensaje", "Se añadio el nuevo producto!")
                .addFlashAttribute("clase", "success");
        return "redirect:/productos";
    }

    @GetMapping("/edit/{id}")
    public String edit( @PathVariable Integer id,Model model) {

        Producto producto= new Producto();
        Optional<Producto> optionalProducto=productoService.get(id);
        producto= optionalProducto.get();


        LOGGER.info("Producto buscado: {}",producto);
        model.addAttribute("producto", producto);

        return "productos/edit";
    }

    @PostMapping("/update")
    public String update(ReportesProductos reportes, Producto producto, @RequestParam("img") MultipartFile file, HttpSession session, RedirectAttributes redirectAttributes ) throws IOException {
        Producto p= new Producto();
        p=productoService.get(producto.getId()).get();

        Usuario u= usuarioService.findById(Integer.parseInt(session.getAttribute("idusuario").toString() )).get();

        Date fechaActual = new Date();

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

        reportes.setUsuario(u);
        reportes.setNombreProducto(producto.getNombre());
        reportes.setFechaCreacion(fechaActual);
        reportes.setTipoOperacion("Actualizacion");

        producto.setUsuario(p.getUsuario());
        productoService.update(producto);
        reporteService.save(reportes);
        redirectAttributes.addFlashAttribute("mensaje", "Se hizo la actualización del producto!")
                .addFlashAttribute("clase", "success");
        return "redirect:/productos";
    }

    @GetMapping("/delete/{id}")
    public String delete(ReportesProductos reportes, @PathVariable Integer id, HttpSession session, RedirectAttributes redirectAttributes) {

        Usuario u= usuarioService.findById(Integer.parseInt(session.getAttribute("idusuario").toString() )).get();

        Date fechaActual = new Date();

        Producto p = new Producto();
        p=productoService.get(id).get();

        reportes.setUsuario(u);
        reportes.setNombreProducto(p.getNombre());
        reportes.setFechaCreacion(fechaActual);
        reportes.setTipoOperacion("Eliminacion");

        //eliminar cuando no sea la imagen por defecto
        if (!p.getImagen().equals("default.jpg")) {
            upload.deleteImage(p.getImagen());
        }

        productoService.delete(id);
        reporteService.save(reportes);
        redirectAttributes.addFlashAttribute("mensaje", "Se eliminó el producto!")
                .addFlashAttribute("clase", "success");
        return "redirect:/productos";
    }

    @GetMapping("/exportarPDF")
    public void exportarListaProductos(ReportesProductos reportes, HttpServletResponse response) throws IOException {
        response.setContentType("application/pdf");

        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd_HH:mm:ss");
        String fechaActual = dateFormat.format(new Date());

        String cabecera = "Content-Disposition";
        String valor = "attachment; filename=Productos_ " + fechaActual + ".pdf";

        response.setHeader(cabecera, valor);

        List<ReportesProductos> reportesList = reporteService.findAll();

        ProductosExporterPDF exporterPDF = new ProductosExporterPDF(reportesList);
        exporterPDF.exportar(response);
    }



}
