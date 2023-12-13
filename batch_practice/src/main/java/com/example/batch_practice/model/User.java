package com.example.batch_practice.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String userName;

    @Enumerated(EnumType.STRING)
    private Level level;

    @OneToMany(cascade = CascadeType.PERSIST, fetch = FetchType.EAGER)
    @JoinColumn(name = "user_id")
    private List<Orders> orders;

    private LocalDateTime createdDate;

    private LocalDate updatedDate;

    private int getTotalAmount() {
        return this.orders.stream()
                .mapToInt(Orders::getAmount)
                .sum();
    }

    public boolean availableLevelSet() {
        return Level.availableLevelSet(this.getLevel(), this.getTotalAmount());
    }

    public void levelSet() {
        Level nextLevel = Level.getNextLevel(this.getTotalAmount());
        this.level = nextLevel;
        this.updatedDate = LocalDate.now();
    }

    public enum Level {
        VIP(500000, null),
        GOLD(500000, VIP),
        SILVER(300000, GOLD),
        NORMAL(200000, SILVER);

        private final int nextAmount;
        private final Level nextLevel;

        Level(int nextAmount, Level nextLevel) {
            this.nextAmount = nextAmount;
            this.nextLevel = nextLevel;
        }

        public static boolean availableLevelSet(Level level, int totalAmount) {
            if (Objects.isNull(level) || Objects.isNull(level.nextLevel)) {
                return false;
            }
            return totalAmount >= level.nextAmount;
        }

        private static Level getNextLevel(int totalAmount) {
            if (totalAmount >= Level.VIP.nextAmount) {
                return VIP;
            }
            if (totalAmount >= Level.GOLD.nextAmount) {
                return GOLD.nextLevel;
            }
            if (totalAmount >= Level.SILVER.nextAmount) {
                return SILVER.nextLevel;
            }
            if (totalAmount >= Level.NORMAL.nextAmount) {
                return NORMAL.nextLevel;
            }
            return NORMAL;
        }
    }
}
