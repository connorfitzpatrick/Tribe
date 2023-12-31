package com.friendsocial.Backend.friend;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface FriendRepository
        extends JpaRepository<Friend, Long> {
  // Custom function to find user by email. Transforms to `SELECT * FROM users WHERE email = ?`
    @Query("SELECT f FROM Friend f WHERE f.user.id = ?1")
    List<Friend> findFriendsOfUserId(Long id);

    @Query("SELECT f FROM Friend f WHERE f.user.id = ?1 AND f.friend.id = ?2")
    Friend findIsFriendByIds(Long userId, Long friendId);
}
