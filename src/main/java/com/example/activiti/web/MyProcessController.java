package com.example.activiti.web;


import com.example.activiti.model.ProcessModel;
import com.example.activiti.model.TaskModel;
import com.example.activiti.service.MyService;
import org.activiti.bpmn.model.FlowElement;
import org.activiti.engine.HistoryService;
import org.activiti.engine.RepositoryService;
import org.activiti.engine.RuntimeService;
import org.activiti.engine.TaskService;
import org.activiti.engine.runtime.ProcessInstance;
import org.activiti.engine.task.Task;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RestController
public class MyProcessController {



    @Autowired
    private MyService service;
    private RuntimeService runtimeService;
    private TaskService taskService;
    private HistoryService historyService;
    private RepositoryService repositoryService;


    public MyProcessController(RuntimeService runtimeService, TaskService taskService, HistoryService historyService, RepositoryService repositoryService){
        this.runtimeService = runtimeService;
        this.taskService = taskService;
        this.historyService = historyService;
        this.repositoryService = repositoryService;
    }


    //@GetMapping("/start-process")
    @RequestMapping("/start-process")
    public String startMyProcess(){

        ProcessInstance instance=runtimeService.startProcessInstanceByKey("jmsProcess");
        System.out.println("Nombre de process : "+runtimeService.createProcessInstanceQuery().count()+" process instances \t instance id: "+instance.getProcessInstanceId());
        return "Nombre de process : "+runtimeService.createProcessInstanceQuery().count()+" process instances \t instance id: "+instance.getProcessInstanceId();
    }




    @RequestMapping("/complete-task")
    public String completeTaskA(@RequestParam String taskId) {
        taskService.complete(taskId+"");
        //taskService.
        return "Task with id " + taskId + " has been completed!";

    }


    @RequestMapping("/finish-process")
    public void finishProcess(@RequestParam String processInstanceId) {
        historyService.createHistoricDetailQuery()
                .variableUpdates()
                .processInstanceId(processInstanceId)
                .orderByVariableName().asc()
                .list();
        System.out.println(" done ! " );

    }
    //TODO à revoir
    @RequestMapping("/suspend-process")
    public String suspendProcessInstance() {
        // deploy the process definition
        repositoryService.suspendProcessDefinitionByKey("vacationRequest");
        runtimeService.startProcessInstanceByKey("vacationRequest");
        return "This process is suspended";
    }


    ////////////////////////////////////==========================================> MyService test

    @RequestMapping("/start")
    public String startProcess(@RequestParam String name){
        return service.startProcess(name);
    }

    @RequestMapping("/processes")
    public List<Map<String, Object>> getProcesses(){
        return service.getProcesses();
    }


    // process with ProcessModel :
    @RequestMapping("/v1/processes")
    public List<ProcessModel> processes(){
        return service.suspendedProcesses();
    }

    @RequestMapping("/suspended-processes")
    public List<Map<String,Object>> getSuspendedProcesses(){
        return service.getSuspendedProcesses();
    }

    @RequestMapping("/tasks")
    public List<Map<String,Object>> tasks(@RequestParam String proccessInstanceId){
        return service.tasks(proccessInstanceId);
    }
    // process with TaskModel :
    @RequestMapping("/v1/tasks")
    public List<TaskModel> getTasks(@RequestParam String proccessInstanceId){
        return service.getTasks(proccessInstanceId);
    }

    @RequestMapping("/claim")
    public String claim(@RequestParam String taskId,@RequestParam String assigneeId){
        return service.claim(taskId,assigneeId);
    }

    @RequestMapping("/unclaim")
    public String unclaim(@RequestParam String taskId){
        return service.unclaimTask(taskId);
    }

   /* @RequestMapping("/active")
    public List<Map<String,Object>> getActiveProcess(){
        return service.getActiveProcess();
    }
*/
    @RequestMapping("/suspend")
    public String suspendProcess(@RequestParam String processInstanceId){
        return service.suspendProcess(processInstanceId);
    }

    @RequestMapping("/activate")
    public String resumedProcess(@RequestParam String processInstanceId){
        return service.resumeProcess(processInstanceId);
    }

    @RequestMapping("/delete")
    public String deleteProcess(@RequestParam String processInstanceId,@RequestParam String deleteReason){
        return service.deleteProcess(processInstanceId,deleteReason);
    }

    @RequestMapping("/complete")
    public String completeTask(@RequestParam String taskId){
        return service.completeTask(taskId);
    }

    @RequestMapping("/variables")
    public Map<String,Object> getVariables(@RequestParam String taskId){
        return service.getVariables(taskId);
    }

    @RequestMapping("/suspendTask")
    public String suspendTask(@RequestParam String taskId){
        return service.suspendTask(taskId);
    }

    @RequestMapping("/resolveTask")
    public TaskModel resolveTask(@RequestParam String taskId){
        return service.resolveTask(taskId);
    }


}
