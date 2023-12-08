package com.example.bookmanager.domain;

import com.example.bookmanager.listener.UserEntityListener;
import lombok.*;

import javax.persistence.*;
import java.time.LocalDateTime;

@Data
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true)
@NoArgsConstructor
@AllArgsConstructor
@RequiredArgsConstructor
@Builder
@Entity
@EntityListeners(value = UserEntityListener.class)
//@Table//(indexes = { @Index(columnList = "createdAt") }, uniqueConstraints = { @UniqueConstraint(columnNames = {"email"}) })
public class User extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NonNull
    private Long userId;

    @NonNull
    private String name;

    private String email;

    @Enumerated(value = EnumType.STRING) // 설정해주지 않으면 DB값엔 index값이 들어가게 됨
    private Gender gender;

//    @CreatedDate
//    private LocalDateTime createdAt;
//
//    @LastModifiedDate
//    private LocalDateTime updatedAt;

//    @PrePersist // insert이후
//    public void prePersist() {
//        this.createdAt = LocalDateTime.now();
//        this.updatedAt = LocalDateTime.now();
//    }
//
//    @PreUpdate // update 이후
//    public void preUpdate() {
//        this.updatedAt = LocalDateTime.now();
//    }

    @Transient
    private String testData;
}
