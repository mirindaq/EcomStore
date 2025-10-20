package vn.com.ecomstore.entities;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter
@Setter
@Table(name = "delivery_images")
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class DeliveryImage {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column
    private String imageUrl;

    @ManyToOne
    @JoinColumn( name = "delivery_assignment_id")
    private DeliveryAssignment deliveryAssignment;


}
