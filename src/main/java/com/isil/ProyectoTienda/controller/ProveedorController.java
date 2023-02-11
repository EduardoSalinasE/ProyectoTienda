package com.isil.ProyectoTienda.controller;

import com.isil.ProyectoTienda.model.*;
import com.isil.ProyectoTienda.repository.DetalleBoletasRepository;
import com.isil.ProyectoTienda.service.*;
import com.isil.ProyectoTienda.util.ProveedoresExporterPDF;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;

@Controller
@RequestMapping("/proveedor")
public class ProveedorController {

    private final Logger LOGGER = LoggerFactory.getLogger(ProveedorController.class);

    @Autowired
    private ProveedorService proveedorService;

    @Autowired
    private UsuarioService usuarioService;

    @Autowired
    private DetalleBoletasService detalleBoletasService;

    @Autowired
    private ReporteProveedoresService reporteService;

    List<DetalleBoletas> detalleBol = new ArrayList<DetalleBoletas>();

    DetalleComprasProv detalleComprasProv = new DetalleComprasProv();
    @Autowired
    private DetalleBoletasRepository detalleBoletasRepository;

    @GetMapping("")
    public String show(Model model) {
        model.addAttribute("proveedor", proveedorService.findAll());
        return "proveedor/show";
    }

    @GetMapping("/mostrarReporte")
    public String showTable(Model model) {
        model.addAttribute("reporte", reporteService.findAll());
        return "reportes/reportes_proveedores";
    }

    @GetMapping("create")
    public String create() {
        return "proveedor/create";
    }

    @PostMapping("/save")
    public String save(Proveedor proveedor, ReportesProveedores reportes, HttpSession session, RedirectAttributes redirectAttributes) throws IOException {
        LOGGER.info("Este es el objeto Proveedores {}",proveedor);
        Date fechaActual = new Date();

        Usuario usuario = usuarioService.findById(Integer.parseInt(session.getAttribute("idusuario").toString())).get();
        proveedor.setUsuario(usuario);

        reportes.setUsuario(usuario);
        reportes.setNombreProveedor(proveedor.getNombre());
        reportes.setFechaCreacion(fechaActual);
        reportes.setTipoOperacion("Creacion");

        proveedorService.save(proveedor);
        reporteService.save(reportes);
        redirectAttributes.addFlashAttribute("mensaje", "Se añadió nueva compra del proveedor!")
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
    public String update(ReportesProveedores reportes, Proveedor proveedor, HttpSession session, RedirectAttributes redirectAttributes) throws IOException {
        Proveedor proveedor1 = new Proveedor();
        proveedor1 = proveedorService.get(proveedor.getId()).get();

        Usuario u= usuarioService.findById(Integer.parseInt(session.getAttribute("idusuario").toString() )).get();
        Date fechaActual = new Date();

        proveedor.setUsuario(proveedor1.getUsuario());
        reportes.setUsuario(u);
        reportes.setNombreProveedor(proveedor.getNombre());
        reportes.setFechaCreacion(fechaActual);
        reportes.setTipoOperacion("Actualizacion");

        proveedorService.update(proveedor);
        reporteService.save(reportes);
        redirectAttributes.addFlashAttribute("mensaje", "Se hizo la actualización de la compra del proveedor!")
                .addFlashAttribute("clase", "success");
        return "redirect:/proveedor";
    }

    @GetMapping("/delete/{id}")
    public String delete(ReportesProveedores reportes, @PathVariable Integer id, HttpSession session, RedirectAttributes redirectAttributes) {

        Usuario u= usuarioService.findById(Integer.parseInt(session.getAttribute("idusuario").toString() )).get();
        Date fechaActual = new Date();

        Proveedor proveedor = new Proveedor();
        proveedor = proveedorService.get(id).get();

        reportes.setUsuario(u);
        reportes.setNombreProveedor(proveedor.getNombre());
        reportes.setFechaCreacion(fechaActual);
        reportes.setTipoOperacion("Eliminacion");

        proveedorService.delete(id);
        reporteService.save(reportes);
        redirectAttributes.addFlashAttribute("mensaje", "Se eliminó la compra del proveedor!")
                .addFlashAttribute("clase", "success");
        return "redirect:/proveedor";
    }

    @GetMapping("/productos/{id}")
    public String productos(@PathVariable Integer id, Model model)
    {
        Proveedor proveedor = new Proveedor();
        Optional<Proveedor> proveedorOptional = proveedorService.get(id);
        proveedor = proveedorOptional.get();

        model.addAttribute("proveedor", proveedor);
        model.addAttribute("listaBoletas", detalleBoletasService.findAllByProveedor_id(id));

        return "proveedor/boleta";

    }

    @PostMapping("/crearBoleta")
    public String addBoleta( @RequestParam Integer id, @RequestParam Integer cantidad, @RequestParam String nombreProducto, @RequestParam Integer total  ,RedirectAttributes redirectAttributes)
    {
        DetalleBoletas detalleBoletas = new DetalleBoletas();
        Proveedor proveedor = new Proveedor();

        Optional<Proveedor> optionalProveedor = proveedorService.get(id);
        proveedor = optionalProveedor.get();

        detalleBoletas.setCantidad(cantidad);
        detalleBoletas.setNombreProducto(nombreProducto);
        detalleBoletas.setTotal(total);
        detalleBoletas.setProveedor(proveedor);

        double sumaTotal = 0;
        sumaTotal = detalleBol.stream().mapToDouble(dt -> dt.getTotal()).sum();

        detalleComprasProv.setPrecio(sumaTotal);
        detalleBoletasRepository.save(detalleBoletas);

        redirectAttributes.addFlashAttribute("mensaje", "Producto añadido!")
                .addFlashAttribute("clase", "success");


        return "redirect:/proveedor/productos/" + id;

    }

    @GetMapping("/exportarPDF")
    public void exportarListaProductos(HttpServletResponse response) throws IOException {
        response.setContentType("application/pdf");

        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd_HH:mm:ss");
        String fechaActual = dateFormat.format(new Date());

        String cabecera = "Content-Disposition";
        String valor = "attachment; filename=Proveedores_ " + fechaActual + ".pdf";

        response.setHeader(cabecera, valor);

        List<ReportesProveedores> reportes = reporteService.findAll();

        ProveedoresExporterPDF exporterPDF = new ProveedoresExporterPDF(reportes);
        exporterPDF.exportar(response);
    }


}
