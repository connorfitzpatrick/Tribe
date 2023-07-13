package com.friendsocial.Backend.like;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface LikeRepository extends JpaRepository<Like, Long> {
  @Query("SELECT l as like, pr.username as username, pr.profilePic as profilePic FROM Like l JOIN l.profile pr JOIN l.post po WHERE l.postId = ?1")
  List<Object[]> findLikesOfPostId(Long id);
}
