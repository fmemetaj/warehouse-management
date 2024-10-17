package org.fmemetaj.warehousemanagment.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.experimental.Accessors;

import java.util.Calendar;
import java.util.Date;
import java.util.List;

@Getter
@Setter
@RequiredArgsConstructor
@Accessors(chain = true)
@Entity
@Table(name = "orders")
public class Order {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "user_id")
    @JsonBackReference
    private User user;

    @Temporal(TemporalType.DATE)
    private Date submittedDate;

    @Enumerated(EnumType.STRING)
    private OrderStatus status;

    @Temporal(TemporalType.DATE)
    private Date deadlineDate;

    @OneToMany(mappedBy = "order", fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    @JsonManagedReference
    @OrderBy("id ASC")
    private List<OrderItem> items;

    public Order(User user, List<OrderItem> items) {
        this.user = user;
        this.submittedDate = new Date();
        this.status = OrderStatus.CREATED;
        setDeadlineDate();
        this.items = items;
    }

    private void setDeadlineDate() {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(submittedDate);

        calendar.add(Calendar.MONTH, 1);
        this.deadlineDate = calendar.getTime();
    }

    public enum OrderStatus {
        CREATED,
        AWAITING_APPROVAL,
        APPROVED,
        DECLINED,
        UNDER_DELIVERY,
        FULFILLED,
        CANCELED
    }
}
