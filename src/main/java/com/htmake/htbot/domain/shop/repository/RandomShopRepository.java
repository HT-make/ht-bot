package com.htmake.htbot.domain.shop.repository;

import com.htmake.htbot.domain.shop.entity.RandomShop;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface RandomShopRepository extends JpaRepository<RandomShop, String> {

    Optional<RandomShop> findByName(String name);
}
