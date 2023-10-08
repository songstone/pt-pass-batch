package com.song.pass.domain.user;

import com.song.pass.domain.BaseEntity;
import com.song.pass.domain.constant.UserStatus;
import lombok.*;
import org.hibernate.annotations.TypeDef;

import javax.persistence.*;

@AllArgsConstructor
@NoArgsConstructor
@ToString
@Setter
@Getter
@Builder
@Entity
@Table(name = "user")
public class User extends BaseEntity {

    @Id
    private String userId;

    private String userName;

    @Enumerated(EnumType.STRING)
    private UserStatus status;
    private String phone;

    private String uuid;
}
