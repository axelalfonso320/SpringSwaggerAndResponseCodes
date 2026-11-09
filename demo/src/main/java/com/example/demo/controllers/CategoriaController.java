package com.example.demo.controllers;

import java.util.List;
import java.util.Optional;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.model.Department;
import com.example.demo.model.Producto;
import com.example.demo.repo.categoriaRepo;
import com.example.demo.repo.productoRepo;

@RestController
public class CategoriaController {

  @Autowired
  categoriaRepo repo;
  @Autowired
  productoRepo repoProducto;

  @GetMapping("categorias")
  public List<Department> findAllcategoria() {
    return this.repo.findAll();
  }

  @PostMapping("categoria")
  public Department addcategoria(@RequestBody Department categoria) {
    return repo.save(categoria);
  }

  @PostMapping("addProductoCategoria/{productId}/{categoriaId}")
  public ResponseEntity<Department> addcategoria(@PathVariable String productId, @PathVariable String categoriaId) {
    Optional<Producto> p = repoProducto.findById(productId);
    Optional<Department> d = repo.findById(categoriaId);
    if (p.isEmpty() || d.isEmpty()) {
      return ResponseEntity.notFound().build();
    }
    d.get().getProductos().add(p.get());
    return ResponseEntity.ok(repo.save(d.get()));
  }

  @GetMapping("categoria/producto/{productId}")
  public ResponseEntity<Set<Department>> getDepartmentsByProduct(@PathVariable String productId) {
    Optional<Producto> p = repoProducto.findById(productId);
    if (p.isEmpty()) {
      return ResponseEntity.notFound().build();
    }
    return ResponseEntity.ok(p.get().getDeparments());
  }

  @GetMapping("categoria/{id}")
  public ResponseEntity<Department> getById(@PathVariable String id) {
    Optional<Department> result = repo.findById(id);
    if (result.isEmpty()) {
      return ResponseEntity.notFound().build();
    }
    return ResponseEntity.ok(result.get());
  }

  @PutMapping("categoria/{id}")
  public Department putDepartment(@PathVariable String id, @RequestBody Department categoria) {
    categoria.setId(id);
    return repo.save(categoria);
  }

  @DeleteMapping("categoria/{id}")
  public String deleteDeparment(@PathVariable String id) {
    repo.deleteById(id);
    return "{\"success\":true}";
  }
}