package vn.com.ecomstore.entities;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter
@Setter
@Table(name = "product_variants")
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ProductVariant extends BaseEntity {

    @Id
    @GeneratedValue( strategy = GenerationType.IDENTITY)
    private Long id;

    @Column
    private Double price;

    @Column
    private Double oldPrice;

    @Column
    private String sku;

    @Column
    private Integer stock;

    @ManyToOne
    @JoinColumn(name = "product_id")
    private Product product;

}