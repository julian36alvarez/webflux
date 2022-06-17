package com.webflux.webflux.controller;

import com.webflux.webflux.models.dao.ProductoDao;
import com.webflux.webflux.models.documents.Producto;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/api/productos")
public class ProductoRestController {

    @Autowired
    private ProductoDao dao;
    private static final Logger log = LoggerFactory.getLogger(ProductoController.class);

    @GetMapping()
    public Flux<Producto> index(){
        Flux<Producto> productoFlux = dao.findAll()
                .map(producto -> {
                    producto.setNombre(producto.getNombre().toUpperCase());
                    return producto;
                }).doOnNext(producto -> log.info(producto.getNombre()));
        return productoFlux;
    }


    @GetMapping("/{id}")
    public Mono<Producto> show(@PathVariable String id){
        //Mono<Producto> productoMono = dao.findById(id);
        Flux<Producto> productoFlux = dao.findAll();

        Mono<Producto> productoMono = productoFlux.filter(p-> p.getId().equals(id))
                .next()
                .doOnNext(prod-> log.info(prod.getNombre()));
        return productoMono;
    }

}
