package com.fkisking.rpc.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ReferenceConfig <T>{
    private Class<? extends T> Interface;
}
