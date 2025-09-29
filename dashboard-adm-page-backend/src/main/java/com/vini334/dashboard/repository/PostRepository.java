package com.vini334.dashboard.repository;

import com.vini334.dashboard.model.Post;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PostRepository extends JpaRepository<Post, Long> {
}
