package com.allocation.trade.entity;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Objects;

import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import com.allocation.trade.enums.Side;
import com.allocation.trade.enums.TradeStatus;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.PrePersist;
import jakarta.persistence.PreUpdate;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "trades")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Trade {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "trade_id", nullable = false, unique = true, length = 50)
    private String tradeId;

    @Column(name = "symbol", nullable = false, length = 20)
    private String symbol;

    @Enumerated(EnumType.STRING)
    @Column(name = "side", nullable = false, length = 10)
    private Side side;

    @Column(name = "quantity", nullable = false, precision = 18, scale = 4)
    private BigDecimal quantity;

    @Column(name = "price", nullable = false, precision = 18, scale = 4)
    private BigDecimal price;

    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false, length = 20)
    private TradeStatus status;

    @Column(name = "event_timestamp", nullable = false)
    private LocalDateTime eventTimestamp;

    @Column(name = "source_system", length = 50)
    private String sourceSystem;

    @Column(name = "version_number", nullable = false)
    private Integer versionNumber;

    @Column(name = "created_at", nullable = false, updatable = false)
    @CreationTimestamp
    private LocalDateTime createdAt;


    @Column(name = "updated_at", nullable = false)
    @UpdateTimestamp
    private LocalDateTime updatedAt;

    @PrePersist    // This annotation is used to set the createdAt and updatedAt fields when the entity is created.
    protected void onCreate() {
        LocalDateTime now = LocalDateTime.now();
        if (createdAt == null) {
            createdAt = now;
        }
        if (updatedAt == null) {
            updatedAt = now;
        }
        if (versionNumber == null) {
            versionNumber = 1;
        }
    }

    @PreUpdate
    protected void onUpdate() {
        updatedAt = LocalDateTime.now();
    }

    /**
     * JPA-friendly equality: same persisted id means same row. Transient instances ({@code id == null})
     * only equal themselves; {@code hashCode} uses identity until an id is assigned.
     */
    @Override
    public boolean equals(Object o) {
        if (this == o) {     // if the object is the same as the current object, then return true.
            return true;      // this is the current object.
            // trade t1 = new Trade();
            // trade t2 = new Trade();
            // t1.equals(t2); // false
        }
        if (o == null || getClass() != o.getClass()) { // getClass() gives the class of the object.
            // if the object is null or the class is not the same as the current object, then return false.
            //if both are from different classes, then return false.
            return false;
        }
        Trade trade = (Trade) o;  // type casting the object to Trade.
        // type casting is needed to access the id field of the Trade object.
        
        // the first 3 checks are common for all the objects.
        
        return id != null && id.equals(trade.id); // not a recursive call, it is a comparison of the id of the current object with the id of the other object. 
        // if id is not null and the id of the current object is equal to the id of the other object, then return true.
    }

    @Override
    public int hashCode() {
        return id != null ? Objects.hashCode(id) : System.identityHashCode(this);
    }
}
