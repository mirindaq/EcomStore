package vn.com.ecomstore.entities;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter
@Setter
@Table(name = "feedback_images")
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class FeedbackImage {

    @Id
    @GeneratedValue( strategy = GenerationType.IDENTITY)
    private Long id;

    @Column
    private String imgUrl;

    @ManyToOne
    @JoinColumn(name = "feedback_id")
    private Feedback feedback;


}
