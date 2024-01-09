package com.duktown.domain.post.entity;

import com.duktown.global.type.Category;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
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

    // 일상 0, 장터 1 검색
    @Query("SELECT p FROM Post p WHERE p.category = :category AND (p.title LIKE %:keyword% OR p.content LIKE %:keyword%) ")
    Slice<Post> findByCategoryAndKeyword( @Param("category") Category category, @Param("keyword") String keyword,Pageable pageable);
}
