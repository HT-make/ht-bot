package com.htmake.htbot.domain.dungeon.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;

import java.util.List;

@Entity
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Dungeon {

    @Id
    @Column(name = "dungeon_id")
    private String id;

    @Column(name = "dungeon_name", nullable = false)
    private String name;

    @JsonIgnore
    @OneToMany(mappedBy = "dungeon", cascade = CascadeType.ALL)
    private List<Monster> monsters;
}