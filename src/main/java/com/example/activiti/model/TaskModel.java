package com.example.activiti.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.activiti.engine.task.DelegationState;

@Data
@AllArgsConstructor @NoArgsConstructor
public class TaskModel {
    private String id;
    private String name;
    private String processInstanceId;
    private String assignee;

}
