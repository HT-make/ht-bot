package com.htmake.htbot.domain.inventory.presentation.data.request;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Getter
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class InsertDropItemListRequest {

    private List<DropItemRequest> dropItemList;
}
