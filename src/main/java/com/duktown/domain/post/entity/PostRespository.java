package com.duktown.domain.post.entity;

import com.duktown.global.type.Category;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PostRespository extends JpaRepository<Post,Long> {
    // 목록 조회 - 페이지네이션, 카테고리

    @Query("select p from Post p where p.category = :category order by p.createdAt desc")
    List<Post> findAllByCategory(@Param(value = "category") Category category);

    // 검색 - 키워드, 페이지네이션
    @Query("SELECT p FROM Post p WHERE LOWER(p.title) LIKE LOWER(CONCAT('%', :keyword, '%')) OR LOWER(p.content) LIKE LOWER(CONCAT('%', :keyword, '%'))")
    List<Post> searchByKeyword(@Param("keyword") String keyword);

}
