package com.example.web2_3_ourtuft_be.shop.dto;

import java.util.List;
import lombok.Getter;

@Getter
public class OrderRequest {
    List<Long> items;
}
