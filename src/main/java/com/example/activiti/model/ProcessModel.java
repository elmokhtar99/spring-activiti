package com.example.activiti.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor @NoArgsConstructor
public class ProcessModel {
    private String id;
    private String name;
    private Boolean isSuspended;
}
