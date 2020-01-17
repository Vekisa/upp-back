package naucna_centrala.nc.controller;

import naucna_centrala.nc.model.CustomUser;
import naucna_centrala.nc.model.EnablingRequest;
import naucna_centrala.nc.model.FormFieldsDto;
import naucna_centrala.nc.model.FormSubmissionDto;
import naucna_centrala.nc.repositories.RequestRepository;
import naucna_centrala.nc.service.UserService;
import org.camunda.bpm.engine.FormService;
import org.camunda.bpm.engine.IdentityService;
import org.camunda.bpm.engine.RuntimeService;
import org.camunda.bpm.engine.TaskService;
import org.camunda.bpm.engine.form.FormField;
import org.camunda.bpm.engine.form.TaskFormData;
import org.camunda.bpm.engine.runtime.ProcessInstance;
import org.camunda.bpm.engine.task.Task;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;

@Controller
@RequestMapping("/auth")
public class AuthController {

    @Autowired
    private RuntimeService runtimeService;

    @Autowired
    private TaskService taskService;

    @Autowired
    private FormService formService;

    @Autowired
    private UserService userService;

    @Autowired
    private RequestRepository requestRepository;

    @Autowired
    private IdentityService identityService;

    @GetMapping(path = "/get", produces = "application/json")
    public @ResponseBody
    FormFieldsDto get() {
        ProcessInstance pi = runtimeService.startProcessInstanceByKey("registracija");
        Task task = taskService.createTaskQuery().processInstanceId(pi.getId()).list().get(0);

        TaskFormData tfd = formService.getTaskFormData(task.getId());
        List<FormField> properties = tfd.getFormFields();

        return new FormFieldsDto(task.getId(), pi.getId(), properties);
    }

    @PostMapping(path = "/post/{taskId}", produces = "application/json")
    public @ResponseBody
    ResponseEntity post(@RequestBody List<FormSubmissionDto> dto, @PathVariable String taskId) {
        HashMap<String, Object> map = this.mapListToDto(dto);

        Task task = taskService.createTaskQuery().taskId(taskId).singleResult();
        String processInstanceId = task.getProcessInstanceId();
        runtimeService.setVariable(processInstanceId, "regDTO", dto);
        formService.submitTaskForm(taskId, map);
        Boolean valid = (Boolean) runtimeService.getVariable(processInstanceId,"validationValue");
        if(valid)
            return new ResponseEntity<>(HttpStatus.OK);
        else
            return new ResponseEntity<>("Bad parameters!",HttpStatus.OK);
    }

    @GetMapping(path = "/confirm", produces = "application/json")
    public @ResponseBody ResponseEntity confirmRegistration(@RequestParam String processId, @RequestParam String token) {
        System.out.println("POTVRDJUJEM");
        CustomUser user = (CustomUser) runtimeService.getVariable(processId,"user");
        Task task = taskService.createTaskQuery().taskDefinitionKey("potvrda").processInstanceId(processId).list().get(0);
        if(token.equals(user.getConfirmationToken()))
            user.setConfirmed(true);
        else
            return new ResponseEntity<>("Bad token!",HttpStatus.BAD_REQUEST);
        System.out.println("SVE OK BRT");
        userService.save(user);
        if(user.getRecenzent()){
            EnablingRequest enablingRequest = new EnablingRequest();
            enablingRequest.setProcessId(processId);
            enablingRequest.setUsername(user.getKorisnicko_ime());
            requestRepository.save(enablingRequest);
        }else{
            identityService.createMembership(user.getKorisnicko_ime(), "author");
        }

        formService.submitTaskForm(task.getId(), null);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    private HashMap<String, Object> mapListToDto(List<FormSubmissionDto> list)
    {
        HashMap<String, Object> map = new HashMap<String, Object>();
        for(FormSubmissionDto temp : list){
            map.put(temp.getFieldId(), temp.getFieldValue());
        }

        return map;
    }
}
