package com.cn.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class BaseRequest {
    private Integer page = 1;

    private Integer limit = 10;
}
