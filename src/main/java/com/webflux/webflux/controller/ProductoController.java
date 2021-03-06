package com.webflux.webflux.controller;

import com.webflux.webflux.models.dao.ProductoDao;
import com.webflux.webflux.models.documents.Producto;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.thymeleaf.spring5.context.webflux.ReactiveDataDriverContextVariable;
import reactor.core.publisher.Flux;

import java.time.Duration;
import java.util.Locale;

@Controller
public class ProductoController {

    @Autowired
    private ProductoDao dao;
    private static final Logger log = LoggerFactory.getLogger(ProductoController.class);
    @GetMapping({"/listar"})
    public String listar(Model model){
        Flux<Producto> productoFlux = dao.findAll()
                .map(producto -> {
                    producto.setNombre(producto.getNombre().toUpperCase());
                    return producto;
                });
        productoFlux.subscribe(prod -> log.info(prod.getNombre()));
                model.addAttribute("productos", productoFlux);
                model.addAttribute("titulo", "Listado de productos");
                return "listar";
    }


    @GetMapping({"/listar-data-driver"})
    public String listarDataDriver(Model model){
        Flux<Producto> productoFlux = dao.findAll()
                .map(producto -> {
                    producto.setNombre(producto.getNombre().toUpperCase());
                    return producto;
                }).delayElements(Duration.ofSeconds(1));
        productoFlux.subscribe(prod -> log.info(prod.getNombre()));
        model.addAttribute("productos", new ReactiveDataDriverContextVariable(productoFlux, 2));
        model.addAttribute("titulo", "Listado de productos");
        return "listar";
    }

    @GetMapping({"/listar-fill"})
    public String listarDataFull(Model model){
        Flux<Producto> productoFlux = dao.findAll()
                .map(producto -> {
                    producto.setNombre(producto.getNombre().toUpperCase());
                    return producto;
                }).repeat(5000);

        model.addAttribute("productos", new ReactiveDataDriverContextVariable(productoFlux, 2));
        model.addAttribute("titulo", "Listado de productos");
        return "listar";
    }

    @GetMapping({"/listar-chunked"})
    public String listarDataChunked(Model model){
        Flux<Producto> productoFlux = dao.findAll()
                .map(producto -> {
                    producto.setNombre(producto.getNombre().toUpperCase());
                    return producto;
                }).repeat(5000);

        model.addAttribute("productos", new ReactiveDataDriverContextVariable(productoFlux, 2));
        model.addAttribute("titulo", "Listado de productos");
        return "listar-chunked";
    }
}
