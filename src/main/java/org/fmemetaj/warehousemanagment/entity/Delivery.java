package org.fmemetaj.warehousemanagment.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.Accessors;

import java.util.Date;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Accessors(chain = true)
@Entity
@Table(name = "deliveries")
public class Delivery {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne
    @JoinColumn(name = "order_id", nullable = false, unique = true)
    private Order order;

    @Temporal(TemporalType.DATE)
    @Column(nullable = false)
    private Date deliveryDate;

    @Enumerated(EnumType.STRING)
    private DeliveryStatus status = DeliveryStatus.SCHEDULED;

    @OneToMany(mappedBy = "delivery", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<DeliveryTruck> deliveryTrucks;

    public enum DeliveryStatus {
        SCHEDULED,
        COMPLETED
    }
}
