package org.fmemetaj.warehousemanagment.entity;

import jakarta.persistence.*;

@Entity
@Table(name = "trucks")
public class Truck {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true)
    private String chassis;

    @Column(unique = true)
    private String licensePlate;
}
