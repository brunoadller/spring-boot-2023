package com.example.springboot.controllers;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.springboot.dtos.ProductRecordDto;
import com.example.springboot.models.ProductModel;
import com.example.springboot.repositories.ProductRepository;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api")
public class ProductController {

  @Autowired
  ProductRepository productRepository;

  // metodo postmapping passando a uri
  // recebera um produto dto que converterá para um product models e salvara no BD
  // retorno responseEntity do tipo ProductModel
  // recebe como corpo um productRecorDto
  @PostMapping("/products")
  public ResponseEntity<ProductModel> saveProduct(@RequestBody @Valid ProductRecordDto productRecordDto) {
    // inicializa um product model
    var productModel = new ProductModel();
    // converte de dto para model para salvar na base de dados utilizando o
    // beanUtils
    BeanUtils.copyProperties(productRecordDto, productModel);
    return ResponseEntity.status(HttpStatus.CREATED).body(productRepository.save(productModel));
  }

  // recebe todos os produtos
  @GetMapping("/products")
  public ResponseEntity<List<ProductModel>> getAllProducts() {
    // depois de adicionar o hateoas e o rrepresentationmodel
    List<ProductModel> productList = productRepository.findAll();
    // se alista não for vazia tera de passar por cada um desses elementos
    if (!productList.isEmpty()) {
      // for da lista de produtos do tipo product model
      for (ProductModel product : productList) {
        // recebe o id do produto
        UUID id = product.getIdProduct();
        // utilizou o metodo add para construir este link
        // linkTo: para qual metodo que vou redirecionar o cliente quando tocar neste
        // link
        // methodOn qualque é o controller que está este metodo e qual é o metodo em si
        // que está este redirecionamento
        // controller que esta o metodo qual o produto
        // withselfRel redireciona para cada um de seus produtos em si
        // em resumo ele criara um link para informar ao cliente alguns detalhes do
        // procedimento
        // EM TODOS VE-SE A DE UM PRODUTO ESPECÍFICO
        product.add(
            linkTo(
                methodOn(ProductController.class).getOneProduct(id)).withSelfRel());
      }
    }
    return ResponseEntity.status(HttpStatus.OK).body(productList);
    // forma tradicional:
    // return
    // ResponseEntity.status(HttpStatus.OK).body(productRepository.findAll());
  }

  // recebe um unico produto
  @GetMapping("/products/{id}")
  // retorna um object por causa dos tipos de respostas
  public ResponseEntity<Object> getOneProduct(@PathVariable(value = "id") UUID id) {
    // recebe um optional de product model
    Optional<ProductModel> productO = productRepository.findById(id);
    if (productO.isEmpty()) {
      return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Product not Found");
    }
    // fazendo o mesmo sistema para o getOne
    // E DE UM PRODUTO ESPECÍFICO VÊ-SE DE TODOS
    productO.get().add(
        linkTo(
            methodOn(ProductController.class).getAllProducts()).withRel("Product List"));
    return ResponseEntity.status(HttpStatus.OK).body(productO.get());
  }

  // atualizando produto
  @PutMapping("/products/{id}")
  public ResponseEntity<Object> updateProduct(@PathVariable(value = "id") UUID id,
      @RequestBody @Valid ProductRecordDto productRecordDto) {
    Optional<ProductModel> productO = productRepository.findById(id);
    if (productO.isEmpty()) {
      return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Product not found");
    }
    // dessa forma não instancia um product model do novo pq o id dele já existe
    var productModel = productO.get();
    BeanUtils.copyProperties(productRecordDto, productModel);
    return ResponseEntity.status(HttpStatus.OK).body(productRepository.save(productModel));
  }

  // deletando produto
  @DeleteMapping("/products/{id}")
  public ResponseEntity<Object> deleteProduct(@PathVariable(value = "id") UUID id) {
    Optional<ProductModel> productO = productRepository.findById(id);
    if (productO.isEmpty()) {
      return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Product not found");
    }
    productRepository.delete(productO.get());
    return ResponseEntity.status(HttpStatus.OK).body("Product deleted successfully");
  }
}
