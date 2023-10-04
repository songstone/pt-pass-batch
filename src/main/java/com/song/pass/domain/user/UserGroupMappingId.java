package com.song.pass.domain.user;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.io.Serializable;

@ToString
@Setter
@Getter
public class UserGroupMappingId implements Serializable {
    private String userGroupId;
    private String userId;
}
