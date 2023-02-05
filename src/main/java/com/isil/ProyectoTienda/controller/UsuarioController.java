package com.isil.ProyectoTienda.controller;

import com.isil.ProyectoTienda.model.ConfirmationToken;
import com.isil.ProyectoTienda.model.Orden;
import com.isil.ProyectoTienda.model.Usuario;
import com.isil.ProyectoTienda.repository.ConfirmationTokenRepository;
import com.isil.ProyectoTienda.repository.UsuarioRepository;
import com.isil.ProyectoTienda.service.EmailService;
import com.isil.ProyectoTienda.service.OrdenService;
import com.isil.ProyectoTienda.service.UsuarioService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.servlet.http.HttpSession;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.ThreadLocalRandom;

@Controller
@RequestMapping("/usuario")
public class UsuarioController {

    private final Logger logger= LoggerFactory.getLogger(UsuarioController.class);


    private final EmailService emailService;

    @Autowired
    private ConfirmationTokenRepository confirmationTokenRepository;

    @Autowired
    private UsuarioService usuarioService;

    @Autowired
    private OrdenService ordenService;


    private Usuario usuario;

    BCryptPasswordEncoder passEncode= new BCryptPasswordEncoder();
    private final UsuarioRepository usuarioRepository;

    public UsuarioController(EmailService emailService,
                             UsuarioRepository usuarioRepository) {
        this.emailService = emailService;
        this.usuarioRepository = usuarioRepository;
    }

    public static int numeroAleatorioEnRango(int minimo, int maximo) {
        // nextInt regresa en rango pero con límite superior exclusivo, por eso sumamos 1
        return ThreadLocalRandom.current().nextInt(minimo, maximo + 1);
    }

    public static String cadenaAleatoria(int longitud) {
        // El banco de caracteres
        String banco = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ1234567890";
        // La cadena en donde iremos agregando un carácter aleatorio
        String cadena = "";
        for (int x = 0; x < longitud; x++) {
            int indiceAleatorio = numeroAleatorioEnRango(0, banco.length() - 1);
            char caracterAleatorio = banco.charAt(indiceAleatorio);
            cadena += caracterAleatorio;
        }
        return cadena;
    }


    // /usuario/registro
    @GetMapping("/registro")
    public String create() {
        return "usuario/registro";
    }

    @PostMapping("/save")
    public String save( Usuario usuario, RedirectAttributes redirectAttributes) {


        logger.info("Usuario registro: {}", usuario);
        usuario.setTipo("USER");
        usuario.setToken(cadenaAleatoria(20));
        usuario.setPassword( passEncode.encode(usuario.getPassword()));
        this.emailService.sendListEmail(usuario.getEmail(), usuario.getToken());
        usuarioService.save(usuario);
        redirectAttributes.addFlashAttribute("mensaje", "revise su correo para terminar registro!")
                .addFlashAttribute("clase", "success");
        return "redirect:/usuario/login";

    }

    @GetMapping("/confirmacion/{token}")
    public String confirmation(@PathVariable String token)
    {
        Usuario usuarioToken = usuarioService.findByToken(token);
        if (usuarioToken.getToken().equals(token))
        {
            usuarioToken.setEneable(1);
            usuarioService.update(usuarioToken);
            return "usuario/paginaConfirmacion";
        }else {
            return "usuario/login";
        }


    }

    @GetMapping("/login")
    public String login() {
        return "usuario/login";
    }

    @GetMapping("/acceder")
    public String acceder(Usuario usuario, HttpSession session, RedirectAttributes redirectAttributes) {
        logger.info("Accesos : {}", usuario);

        Optional<Usuario> user=usuarioService.findById(Integer.parseInt(session.getAttribute("idusuario").toString()));

        if (user.get().getEneable() == 1){
            if (user.isPresent()) {
                session.setAttribute("idusuario", user.get().getId());

                if (user.get().getTipo().equals("ADMIN")) {
                    return "redirect:/administrador";
                }else {
                    return "redirect:/";
                }
            }else {
                logger.info("Usuario no existe");
            }
        }else {
            session.removeAttribute("idusuario");
            redirectAttributes.addFlashAttribute("mensaje", "revise su correo para terminar registro!")
                    .addFlashAttribute("clase", "success");
            return "redirect:/usuario/login";
        }



        return "redirect:/";
    }

    @GetMapping("/compras")
    public String obtenerCompras(Model model, HttpSession session) {
        model.addAttribute("sesion", session.getAttribute("idusuario"));

        Usuario usuario= usuarioService.findById(  Integer.parseInt(session.getAttribute("idusuario").toString()) ).get();
        List<Orden> ordenes= ordenService.findByUsuario(usuario);
        logger.info("ordenes {}", ordenes);

        model.addAttribute("ordenes", ordenes);

        return "usuario/compras";
    }

    @GetMapping("/detalle/{id}")
    public String detalleCompra(@PathVariable Integer id, HttpSession session, Model model) {
        logger.info("Id de la orden: {}", id);
        Optional<Orden> orden=ordenService.findById(id);

        model.addAttribute("detalles", orden.get().getDetalle());


        //session
        model.addAttribute("sesion", session.getAttribute("idusuario"));
        return "usuario/detallecompra";
    }

    @GetMapping("/agregarCarrito/{id}")
    public String agregarCarrito(@PathVariable Integer id, HttpSession session, Model model) {
        logger.info("Id de la orden: {}", id);
        Optional<Orden> orden=ordenService.findById(id);

        model.addAttribute("detalles", orden.get().getDetalle());


        //session
        model.addAttribute("sesion", session.getAttribute("idusuario"));
        return "redirect:/";
    }

    @GetMapping("/cerrar")
    public String cerrarSesion( HttpSession session ) {
        session.removeAttribute("idusuario");
        return "redirect:/";
    }
}
