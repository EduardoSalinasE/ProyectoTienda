package com.isil.ProyectoTienda.controller;

import com.isil.ProyectoTienda.model.DetalleOrden;
import com.isil.ProyectoTienda.model.Orden;
import com.isil.ProyectoTienda.model.Producto;
import com.isil.ProyectoTienda.model.Usuario;
import com.isil.ProyectoTienda.service.DetalleOrdenService;
import com.isil.ProyectoTienda.service.OrdenService;
import com.isil.ProyectoTienda.service.ProductoService;
import com.isil.ProyectoTienda.service.UsuarioService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.servlet.http.HttpSession;
import javax.swing.*;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Controller
@RequestMapping("/")
public class HomeController {

    private final Logger log = LoggerFactory.getLogger(HomeController.class);

    @Autowired
    private UsuarioService usuarioService;

    @Autowired
    private ProductoService productoService;

    @Autowired
    private OrdenService ordenService;

    @Autowired
    private DetalleOrdenService detalleOrdenService;

    List<DetalleOrden> detalles = new ArrayList<DetalleOrden>();

    Orden orden = new Orden();

    @GetMapping("")
    public String home(Model model, HttpSession session) {
        log.info("Sesion del usuario: {}", session.getAttribute("idusuario"));
        model.addAttribute("productos", productoService.findAll());

        model.addAttribute("sesion", session.getAttribute("idusuario"));
        return "usuario/home";
    }

    @GetMapping("productohome/{id}")
    public String productoHome(@PathVariable Integer id, Model model) {
        log.info("Id producto enviado como parámetro {}", id);
        Producto producto = new Producto();
        Optional<Producto> productoOptional = productoService.get(id);
        producto = productoOptional.get();

        model.addAttribute("producto", producto);

        return "usuario/productohome";
    }


    @PostMapping("/cart")
    public String addCart(@RequestParam Integer id, @RequestParam Integer cantidad, Model model) {
        DetalleOrden detalleOrden = new DetalleOrden();
        Producto producto = new Producto();
        double sumaTotal = 0;

        Optional<Producto> optionalProducto = productoService.get(id);
        log.info("Producto añadido: {}", optionalProducto.get());
        log.info("Cantidad: {}", cantidad);
        producto = optionalProducto.get();

        detalleOrden.setCantidad(cantidad);
        detalleOrden.setPrecio(producto.getPrecio());
        detalleOrden.setNombre(producto.getNombre());
        detalleOrden.setTotal(producto.getPrecio() * cantidad);
        detalleOrden.setProducto(producto);

        //validar que le producto no se añada 2 veces
        Integer idProducto=producto.getId();
        boolean ingresado=detalles.stream().anyMatch(p -> p.getProducto().getId()==idProducto);

        if (!ingresado) {
            detalles.add(detalleOrden);
        }

        sumaTotal = detalles.stream().mapToDouble(dt -> dt.getTotal()).sum();

        orden.setTotal(sumaTotal);
        model.addAttribute("cart", detalles);
        model.addAttribute("orden", orden);

        return "usuario/carrito";
    }



    // adición al carrito de forma directa desde le home
    @GetMapping("/cartFlash/{id}")
    public String addCartFlah(@PathVariable Integer id, @RequestParam Integer cantidad, Model model, RedirectAttributes redirectAttributes) {

        DetalleOrden detalleOrden = new DetalleOrden();
        Producto producto = new Producto();
        double sumaTotal = 0;

        Optional<Producto> optionalProducto = productoService.get(id);
        log.info("Producto añadido: {}", optionalProducto.get());

        producto = optionalProducto.get();


        detalleOrden.setCantidad(cantidad);
        detalleOrden.setPrecio(producto.getPrecio());
        detalleOrden.setNombre(producto.getNombre());
        detalleOrden.setTotal(producto.getPrecio() * cantidad);
        detalleOrden.setProducto(producto);

        //validar que le producto no se añada 2 veces
        Integer idProducto=producto.getId();
        boolean ingresado=detalles.stream().anyMatch(p -> p.getProducto().getId()==idProducto);

        if (!ingresado) {
            detalles.add(detalleOrden);
        }

        sumaTotal = detalles.stream().mapToDouble(dt -> dt.getTotal()).sum();

        orden.setTotal(sumaTotal);
        model.addAttribute("cart", detalles);
        model.addAttribute("orden", orden);

        redirectAttributes.addFlashAttribute("mensaje", "Se añadió " + producto.getNombre() + " al carrito!")
                .addFlashAttribute("clase", "success");

        return "redirect:/";
    }



    // quitar un producto del carrito
    @GetMapping("/delete/cart/{id}")
    public String deleteProductoCart(@PathVariable Integer id, Model model) {

        // lista nueva de prodcutos
        List<DetalleOrden> ordenesNueva = new ArrayList<DetalleOrden>();

        for (DetalleOrden detalleOrden : detalles) {
            if (detalleOrden.getProducto().getId() != id) {
                ordenesNueva.add(detalleOrden);
            }
        }

        // poner la nueva lista con los productos restantes
        detalles = ordenesNueva;

        double sumaTotal = 0;
        sumaTotal = detalles.stream().mapToDouble(dt -> dt.getTotal()).sum();

        orden.setTotal(sumaTotal);
        model.addAttribute("cart", detalles);
        model.addAttribute("orden", orden);

        return "usuario/carrito";
    }

    @GetMapping("/getCart")
    public String getCart(Model model, HttpSession session) {

        model.addAttribute("cart", detalles);
        model.addAttribute("orden", orden);

        //sesion
        model.addAttribute("sesion", session.getAttribute("idusuario"));
        return "/usuario/carrito";
    }

    @GetMapping("/order")
    public String order(Model model, HttpSession session, RedirectAttributes redirectAttributes, Usuario usuario) {


        if (session.getAttribute("idusuario") == null){
            redirectAttributes.addFlashAttribute("mensaje", "Error: Tiene que loguearse para hacer compras")
                    .addFlashAttribute("clase", "success");
            return "redirect:/usuario/login";
        }
        else {

            usuario = usuarioService.findById(Integer.parseInt(session.getAttribute("idusuario").toString())).get();

            model.addAttribute("cart", detalles);
            model.addAttribute("orden", orden);
            model.addAttribute("usuario", usuario);

            if (detalles.isEmpty()) {
                redirectAttributes.addFlashAttribute("mensaje", "Error: No se encuentra el producto")
                        .addFlashAttribute("clase", "success");
                return "redirect:/getCart";
            } else {
                return "usuario/resumenorden";
            }
        }

    }



    // guardar la orden
    @GetMapping("/saveOrder")
    public String saveOrder(HttpSession session, RedirectAttributes redirectAttributes) {
        Date fechaCreacion = new Date();
        orden.setFechaCreacion(fechaCreacion);
        orden.setNumero(ordenService.generarNumeroOrden());

        //usuario
        Usuario usuario =usuarioService.findById( Integer.parseInt(session.getAttribute("idusuario").toString())  ).get();

        orden.setUsuario(usuario);
        ordenService.save(orden);


            //guardar detalles
            for (DetalleOrden dt:detalles) {
                dt.setOrden(orden);
                detalleOrdenService.save(dt);
            }

            ///limpiar lista y orden
            orden = new Orden();
            detalles.clear();

        redirectAttributes.addFlashAttribute("mensaje", "Se hizo la compra exitosamente!")
                .addFlashAttribute("clase", "success");

            return "redirect:/";


    }



}
