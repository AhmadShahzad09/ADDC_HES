package com.minsait.oum.mdc.data.model.dto;

import java.util.ArrayList;
import java.util.List;

import lombok.Data;

@Data
public class Pagination<T> {

    private Long total = 0L;
    private Integer pageSize = 0;
    private Integer pages = 0;
    
    private List<T> data = new ArrayList<>();

}
