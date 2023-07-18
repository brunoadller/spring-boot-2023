package com.example.springboot.models;

import com.fasterxml.jackson.databind.ser.std.SerializableSerializer;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.UUID;

import org.springframework.hateoas.RepresentationModel;

@Entity
@Table(name = "TB_PRODUCTS")
// ao implementar a interface serializable diz que a classe será
// uma classe que passara por serializações
// extendeu-se depois de fazer todo o crud o Representation model para facilitar
// a navegabilidade do productModel
// ao usar tara alguns metodos para incluir esses links para navegabilidade com
// add por exemplo
public class ProductModel extends RepresentationModel<ProductModel> implements Serializable {
  // numero de controle de versão para quando as classes forem serializadas
  private static final long serialVersionUID = 1L;
  @Id
  @GeneratedValue(strategy = GenerationType.AUTO) // não será preciso iniciar um valor para o id
  private UUID idProduct;// focar e utilizar desta forma identificadores distribuidos UUID
  private String name;
  private BigDecimal value;

  public static long getSerialversionuid() {
    return serialVersionUID;
  }

  public UUID getIdProduct() {
    return idProduct;
  }

  public void setIdProduct(UUID idProduct) {
    this.idProduct = idProduct;
  }

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public BigDecimal getValue() {
    return value;
  }

  public void setValue(BigDecimal value) {
    this.value = value;
  }

}
