package com.duktown.domain.foodMenus;

import org.springframework.data.jpa.repository.JpaRepository;

public interface MenuRepository extends JpaRepository<foodMenu,Long> {
}
