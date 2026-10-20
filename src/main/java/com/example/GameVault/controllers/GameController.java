package com.example.GameVault.controllers;

import com.example.GameVault.models.Usuario;
import com.example.GameVault.service.UsuarioService;
import com.example.GameVault.service.juegosService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import com.example.GameVault.models.Juego;

import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

@Controller
public class GameController {

    @Autowired
    private juegosService juegoService;

    @Autowired
    private UsuarioService usuarioService;

    @GetMapping("/fragments-demo")
    public String fragmentsDemo() {
        return "fragments-demo";
    }


    @GetMapping({"/", "/juegos"})
    public String listarJuegos(
            @RequestParam(name = "buscar", required = false) String buscar, Model model) {
        if (buscar != null && !buscar.isEmpty()) {
            model.addAttribute("juegos", juegoService.buscarPorTitulo(buscar));
            model.addAttribute("busquedaActual", buscar);
        } else {
            model.addAttribute("juegos", juegoService.obtenerTodos());
        }
        return "juegos";
    }

    @GetMapping("/juegos/nuevo")
    public String mostrarFormulario(Model model) {
        model.addAttribute("juego", new Juego());
        return "formulario";
    }

    @GetMapping("/juegos/editar/{id}")
    public String editarJuego(@PathVariable Long id, Model model) {
        Juego juego = juegoService.obtenerJuegoPorId(id);
        Usuario logueado = usuarioService.obtenerUsuarioLoggeado();

        if (juego != null && logueado != null &&
                juego.getUsuario() != null &&
                juego.getUsuario().getId().equals(logueado.getId())) {

            model.addAttribute("juego", juego);
            return "formulario";
        }

        return "redirect:/mis-juegos";
    }


    @PostMapping("/juegos/editar/{id}")
    public String actualizarJuego(@PathVariable Long id,
                                  @RequestParam("titulo") String titulo,
                                  @RequestParam("descripcion") String descripcion,
                                  @RequestParam("precio") Double precio,
                                  @RequestParam("categoria") String categoria,
                                  @RequestParam(value = "portada", required = false) MultipartFile portada) {
        Juego juegoExistente = juegoService.obtenerJuegoPorId(id);
        Usuario logueado = usuarioService.obtenerUsuarioLoggeado();

        if (juegoExistente != null && logueado != null &&
                juegoExistente.getUsuario() != null &&
                juegoExistente.getUsuario().getId().equals(logueado.getId())) {
            juegoExistente.setTitulo(titulo);
            juegoExistente.setDescripcion(descripcion);
            juegoExistente.setPrecio(precio);
            juegoExistente.setCategoria(categoria);

            juegoService.guardarJuego(juegoExistente, portada);
        }

        return "redirect:/mis-juegos";
    }

    @PostMapping("/juegos/eliminar/{id}")
    public String eliminarJuego(@PathVariable Long id) {
        Juego juego = juegoService.obtenerJuegoPorId(id);
        Usuario logueado = usuarioService.obtenerUsuarioLoggeado();
        if (juego != null && logueado != null &&
                juego.getUsuario() != null &&
                juego.getUsuario().getId().equals(logueado.getId())) {
            juegoService.eliminarJuego(id);
        }

        return "redirect:/mis-juegos";
    }



    @GetMapping("/mis-juegos")
    public String dashboard(Model model) {
        Usuario logueado = usuarioService.obtenerUsuarioLoggeado();
        model.addAttribute("usuario", logueado);
        model.addAttribute("juegos", juegoService.obtenerJuegosPorUsuario(logueado.getId()));
        return "mis-juegos";
    }

    @PostMapping("/juegos")
    public String guardarJuego(@RequestParam("titulo") String titulo,
                               @RequestParam("descripcion") String descripcion,
                               @RequestParam("precio") Double precio,
                               @RequestParam("categoria") String categoria,
                               @RequestParam("portada") MultipartFile portada) {
        Usuario logueado = usuarioService.obtenerUsuarioLoggeado();
        Juego nuevoJuego = new Juego();
        nuevoJuego.setTitulo(titulo);
        nuevoJuego.setDescripcion(descripcion);
        nuevoJuego.setPrecio(precio);
        nuevoJuego.setCategoria(categoria);
        nuevoJuego.setUsuario(logueado);
        juegoService.guardarJuego(nuevoJuego, portada);

        return "redirect:/mis-juegos";
    }


}