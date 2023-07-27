package com.anvi.dabba.model;

import jakarta.persistence.*;
import lombok.Data;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;

import java.io.Serializable;
@NamedQuery(name="Product.getAllProducts",query = "select new com.anvi.dabba.wrapper.ProductWrapper(p.id,p.name,p.description,p.price,p.status) from Product p")
@Data
@Entity
@DynamicInsert
@DynamicUpdate
@Table
public class Product implements Serializable{
    public static final long serialVersionUid=123456L;
    @Id
    @Column(name="id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    private String name;
    private String description;
    private Integer price;
    private String status;
}



