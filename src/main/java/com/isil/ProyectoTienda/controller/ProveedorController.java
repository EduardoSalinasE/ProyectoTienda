package com.isil.ProyectoTienda.controller;

import com.isil.ProyectoTienda.model.DetalleBoletas;
import com.isil.ProyectoTienda.model.DetalleComprasProv;
import com.isil.ProyectoTienda.model.Proveedor;
import com.isil.ProyectoTienda.model.Usuario;
import com.isil.ProyectoTienda.repository.DetalleBoletasRepository;
import com.isil.ProyectoTienda.service.DetalleBoletasService;
import com.isil.ProyectoTienda.service.DetalleComprasProvService;
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
import java.util.ArrayList;
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



    List<DetalleBoletas> detalleBol = new ArrayList<DetalleBoletas>();

    DetalleComprasProv detalleComprasProv = new DetalleComprasProv();
    @Autowired
    private DetalleBoletasRepository detalleBoletasRepository;

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


}
