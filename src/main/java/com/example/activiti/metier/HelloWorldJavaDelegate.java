package com.example.activiti.metier;

import org.activiti.engine.delegate.DelegateExecution;
import org.activiti.engine.delegate.JavaDelegate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class HelloWorldJavaDelegate implements JavaDelegate {
    private static Logger logger = LoggerFactory.getLogger(HelloWorldJavaDelegate.class);
    @Override
    public void execute(DelegateExecution execution) {
        logger.info("[Process=" + execution.getProcessInstanceId() +
                "][Java Delegate=" + this + "]");
    }
}
