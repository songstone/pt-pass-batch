package com.song.pass.domain.user;

import com.song.pass.domain.BaseEntity;
import lombok.*;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.IdClass;
import javax.persistence.Table;

@AllArgsConstructor
@NoArgsConstructor
@ToString
@Setter
@Getter
@Builder
@Entity
@Table(name = "user_group_mapping")
@IdClass(UserGroupMappingId.class)
public class UserGroupMapping extends BaseEntity {

    @Id
    private String userGroupId;

    @Id
    private String userId;

    private String userGroupNAme;
    private String description;
}
