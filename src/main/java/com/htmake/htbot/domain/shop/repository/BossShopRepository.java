package com.htmake.htbot.domain.shop.repository;

import com.htmake.htbot.domain.shop.entity.BossShop;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface BossShopRepository extends JpaRepository<BossShop, String> {

    Optional<BossShop> findByName(String name);
}
