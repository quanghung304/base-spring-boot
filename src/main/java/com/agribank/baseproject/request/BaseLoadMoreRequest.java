package com.agribank.baseproject.request;

import lombok.Data;

@Data
public class BaseLoadMoreRequest {
    private String lastId;
    private Integer limit;
}
