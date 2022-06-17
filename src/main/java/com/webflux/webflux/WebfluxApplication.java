package com.webflux.webflux;

import com.webflux.webflux.models.dao.ProductoDao;
import com.webflux.webflux.models.documents.Producto;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.mongodb.core.ReactiveMongoTemplate;
import reactor.core.publisher.Flux;

import java.util.Date;

@SpringBootApplication
public class WebfluxApplication implements CommandLineRunner {


	@Autowired
	private ProductoDao dao;
	@Autowired
	private ReactiveMongoTemplate reactiveMongoTemplate;

	private static final Logger log = LoggerFactory.getLogger(WebfluxApplication.class);
	public static void main(String[] args) {
		SpringApplication.run(WebfluxApplication.class, args);
	}

	@Override
	public void run(String... args) throws Exception {
		reactiveMongoTemplate.dropCollection("productos").subscribe();
		Flux.just(
				new Producto("Core i7 12700k", 300d),
				new Producto("Chasis", 99d),
				new Producto("DDR5", 200d),
				new Producto("Noctua", 105d),
				new Producto("Termalteck 800 Gold", 90d),
				new Producto("Board ASIS WIFI6", 250d)
		).flatMap(producto -> {
					producto.setCreateAt(new Date());
					return dao.save(producto);
				})
				.subscribe(productoMono -> log.info("Insert: "+ productoMono.getId()+" "+ productoMono.getNombre()));
	}
}
