package com.song.pass.domain.user;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface UserGroupMappingRepository extends JpaRepository<UserGroupMapping, Integer> {
    List<UserGroupMapping> findByUserGroupId(String userGroupId);
}
