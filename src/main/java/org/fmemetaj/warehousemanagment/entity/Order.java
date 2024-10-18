package org.fmemetaj.warehousemanagment.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.fasterxml.jackson.annotation.JsonProperty;
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
    @JsonIgnore
    private User user;

    @Temporal(TemporalType.DATE)
    private Date submittedDate;

    @Enumerated(EnumType.STRING)
    private OrderStatus status;

    private String declineReason;

    @Temporal(TemporalType.DATE)
    private Date deadlineDate;

    @OneToMany(mappedBy = "order", fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    @OrderBy("id ASC")
    private List<OrderItem> orderItems;

    public Order(User user, List<OrderItem> orderItems) {
        this.user = user;
        this.submittedDate = new Date();
        this.status = OrderStatus.CREATED;
        setDeadlineDate();
        this.orderItems = orderItems;
    }

    private void setDeadlineDate() {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(submittedDate);

        calendar.add(Calendar.MONTH, 1);
        this.deadlineDate = calendar.getTime();
    }

    @JsonProperty("username")
    public String getUsername() {
        return (user != null) ? user.getUsername() : null;
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
