package com.example.activiti.service;


import com.example.activiti.model.ProcessModel;
import com.example.activiti.model.TaskModel;
import org.activiti.api.process.model.builders.ProcessPayloadBuilder;
import org.activiti.api.process.runtime.ProcessRuntime;
import org.activiti.engine.RuntimeService;
import org.activiti.engine.TaskService;
import org.activiti.engine.impl.RuntimeServiceImpl;
import org.activiti.engine.impl.util.json.JSONArray;
import org.activiti.engine.runtime.ProcessInstance;
import org.activiti.engine.task.Task;
import org.apache.catalina.security.SecurityUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;


import java.util.*;
import java.util.stream.Collectors;

@Service
public class MyService {

    @Autowired
    private TaskService taskService;

    @Autowired
    private RuntimeService runtimeService;

    private RuntimeServiceImpl runtimeServiceImpl;


    public String startProcess(String name){
        Map<String,Object> variable=new HashMap<>();
        variable.put("flag",1);
        variable.put("leaveTime",new Date());
        ProcessInstance processInstance=runtimeService.startProcessInstanceByKey(name,variable);
        return ">>> Created Process Instance with instance id = " + processInstance.getId();
    }

    public List<Map<String, Object>> getProcesses(){
        List<ProcessInstance> processInstances = runtimeService.createProcessInstanceQuery().active().list();
        List<Map<String,Object>> processInstancesDTO=new ArrayList<>();
        for (ProcessInstance processInstance:processInstances){
            Map<String,Object> pi=new HashMap<>();
            pi.put("id",processInstance.getProcessInstanceId());
            pi.put("name",processInstance.getName());
            pi.put("variables",processInstance.getProcessVariables());
            pi.put("is_suspended",processInstance.isSuspended());
            processInstancesDTO.add(pi);
        }
        return processInstancesDTO;
    }


    public List<Map<String,Object>> getSuspendedProcesses(){
        List<ProcessInstance> processInstances = runtimeService.createProcessInstanceQuery().suspended().list();
        List<Map<String,Object>> list=new ArrayList<>();
        for(ProcessInstance processInstance:processInstances){
            Map<String,Object> pi=new HashMap<>();
            pi.put("id",processInstance.getProcessInstanceId());
            pi.put("name",processInstance.getName());
            pi.put("variables",processInstance.getProcessVariables());
            pi.put("is_suspended",processInstance.isSuspended());
            list.add(pi);
        }
        return list;
    }

    public List<ProcessModel> suspendedProcesses(){
        List<ProcessInstance> processInstances = runtimeService.createProcessInstanceQuery().suspended().list();
        return processInstances.stream().map(processInstance -> new ProcessModel(processInstance.getId()
                ,processInstance.getName(),processInstance.isSuspended())).collect(Collectors.toList());
    }


    public List<Map<String,Object>> tasks(String proccessInstanceId){
        List<Task> tasks=taskService.createTaskQuery().processInstanceId(proccessInstanceId).list();
        System.out.println("number of tasks : "+tasks.size());
        List<Map<String,Object>> tasksList=new ArrayList<>();
        for (Task task:tasks){
            Map<String,Object> myTask=new HashMap<>();
            myTask.put("id",task.getId());
            myTask.put("name",task.getName());
            myTask.put("process instance id",task.getProcessInstanceId());
            myTask.put("assigne",task.getAssignee());
            myTask.put("delegation state",task.getDelegationState());

            tasksList.add(myTask);
        }
        return tasksList;
    }

    public List<TaskModel> getTasks(String proccessInstanceId){
       //TODO to check TOMOROW :))))))))))))))))))))))))))
        List<Task> tasks=taskService.createTaskQuery().processInstanceId(proccessInstanceId).list();
//        List<Task> taskInstances = taskService.createTaskQuery().processInstanceId(proccessInstanceId).active().list();
//        List<TaskModel> list=new ArrayList<>();
//        Iterator<Task> itTasks = tasks.iterator();
//        while (itTasks.hasNext()){
//            TaskModel taskModel = new TaskModel();
////            taskModel.setAssignee();
//            list.add(new TaskModel(itTasks.next().getId(),itTasks.next().getName(),itTasks.next().getProcessInstanceId(),itTasks.next().getAssignee()));
//        }

        return tasks.stream().map(task -> new TaskModel(task.getId(),task.getName()
                ,task.getProcessInstanceId(),task.getAssignee())).collect(Collectors.toList());
//        return list;
    }


    public String claim(String taskId,String assigneeId){
        taskService.claim(taskId,assigneeId);
        return "the task is assigned to "+assigneeId;
    }


    public String unclaimTask(String taskId){
        taskService.unclaim(taskId);
        return "unclaiming the task : "+taskId;
    }


    public String suspendProcess(String processInstanceId){
        runtimeService.suspendProcessInstanceById(processInstanceId);
        return "the process instance with the given id is suspended";
    }

    public String resumeProcess(String processInstanceId){
//        runtimeService.activateProcessInstanceById(processInstanceId);

//        runtimeService.activateProcessInstanceById(processInstanceId);
        // TODO FIXE MEEEEEEEEEEEEEE
        ProcessInstance processInstance = runtimeService.createProcessInstanceQuery().processInstanceId(processInstanceId)
                .singleResult();

        if (processInstance.isSuspended()) {
            runtimeService.activateProcessInstanceById(processInstanceId);
        }
        return "the process instance with the given id is resumed";
    }


    // delete = finish
    public String deleteProcess(String processInstanceId, String deleteReason){
        runtimeService.deleteProcessInstance(processInstanceId,deleteReason);
        return "the process instance with the given id is deleted for the reason => "+deleteReason;
    }

    public String completeTask(String taskId) {
        taskService.complete(taskId);
        return "Task with id = " + taskId + " has been completed!";

    }

    public Map<String,Object> getVariables(String taskId){
        Map<String, Object> variables = taskService.getVariables(taskId);
        return variables;
    }

    // resume = complete
    public String suspendTask(String taskId){
        taskService.createTaskQuery().suspended().taskId(taskId);
        return "Task with id = " + taskId + " has been suspended!";
    }

    public String getList(String processInstanceId){
        List<Task> taskInstances = taskService.createTaskQuery().processInstanceId(processInstanceId).active().list();
        return "";
    }

    //Marks that the assignee is done with this task and that it can be send back to the owner.
    public TaskModel resolveTask(String taskId){
        Task task=taskService.createTaskQuery().taskId(taskId).singleResult();
        System.out.println("The owner : "+task.getOwner());
        taskService.resolveTask(taskId);
        TaskModel taskModel=new TaskModel(task.getId(),task.getName(),task.getProcessInstanceId(),task.getAssignee());
        return taskModel;
    }


}
