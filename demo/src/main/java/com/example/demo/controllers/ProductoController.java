package com.example.demo.controllers;

import java.net.URI;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.example.demo.model.Producto;
import com.example.demo.repo.productoRepo;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;

@RestController
@Tag(name = "Productos", description = "Crear, buscar y administrar productos")
public class ProductoController {

  @Autowired
  productoRepo repo;

  @GetMapping("productos")
  @Operation(summary = "Listar todos los productos")
  @ApiResponse(responseCode = "200", description = "Lista de productos (puede estar vacía)")
  public ResponseEntity<List<Producto>> findAllProductos() {
    return ResponseEntity.ok(repo.findAll());
  }

  @PostMapping("producto")
  @Operation(summary = "Crear un producto")
  @ApiResponse(responseCode = "201", description = "Producto creado")
  public ResponseEntity<Producto> addProducto(@RequestBody Producto producto) {
    Producto saved = repo.save(producto);
    URI location = URI.create("/producto/" + saved.getId());
    return ResponseEntity.created(location).body(saved);
  }

  @GetMapping("producto/{id}")
  @Operation(summary = "Buscar un producto por ID")
  @ApiResponses({
    @ApiResponse(responseCode = "200", description = "Producto encontrado"),
    @ApiResponse(responseCode = "404", description = "No se encontró el producto")
  })
  public ResponseEntity<Producto> getById(@PathVariable String id) {
    Optional<Producto> result = repo.findById(id);
    if (result.isEmpty()) {
      return ResponseEntity.notFound().build();
    }
    return ResponseEntity.ok(result.get());
  }

  @PutMapping("producto/{id}")
  @Operation(summary = "Actualizar un producto existente")
  @ApiResponses({
    @ApiResponse(responseCode = "200", description = "Producto actualizado"),
    @ApiResponse(responseCode = "404", description = "No se encontró el producto")
  })
  public ResponseEntity<Producto> putProduct(@PathVariable String id, @RequestBody Producto product) {
    if (!repo.existsById(id)) {
      return ResponseEntity.notFound().build();
    }
    product.setId(id);
    return ResponseEntity.ok(repo.save(product));
  }

  @DeleteMapping("producto/{id}")
  @Operation(summary = "Eliminar un producto")
  @ApiResponses({
    @ApiResponse(responseCode = "204", description = "Producto eliminado"),
    @ApiResponse(responseCode = "404", description = "No se encontró el producto")
  })
  public ResponseEntity<Void> deleteProducto(@PathVariable("id") String id) {
    if (!repo.existsById(id)) {
      return ResponseEntity.notFound().build();
    }
    repo.deleteById(id);
    return ResponseEntity.noContent().build();
  }
}