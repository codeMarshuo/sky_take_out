package com.sky.dto;

import lombok.Data;
import java.io.Serializable;

/**
 * DTO用于接受前端的数据
 */
@Data
public class ShoppingCartDTO implements Serializable {

    private Long dishId;
    private Long setmealId;
    private String dishFlavor;

}
