package com.duktown.domain.post.service;

import com.duktown.domain.post.entity.Post;
import com.duktown.domain.post.entity.PostRepository;
import com.duktown.global.type.Category;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.domain.Sort;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;


@Component
@RequiredArgsConstructor
public class Post1PageCache {
    private Slice<Post> page1;
    private final PostRepository postRepository;

    @Scheduled(cron ="30 * * * * *" )
    public void updateCache(){
        Pageable pageable = PageRequest.of(0, 5, Sort.by(Sort.Order.desc("createdAt")));
        page1 = postRepository.findAllByCategory(Category.DAILY,pageable);
    }
    public Slice<Post> getPage1(){
        return page1;
    }
}
