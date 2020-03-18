package com.easyim.response.dto;


import lombok.Data;
import lombok.experimental.Accessors;

@Accessors(chain = true)
@Data
public class StockData {
    private Long id = 0L;
    private String name = "";
    private String valueList = "";
}
