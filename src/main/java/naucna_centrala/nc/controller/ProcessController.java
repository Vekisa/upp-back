package naucna_centrala.nc.controller;

import naucna_centrala.nc.model.*;
import naucna_centrala.nc.repositories.*;
import naucna_centrala.nc.service.UserService;
import org.camunda.bpm.engine.FormService;
import org.camunda.bpm.engine.IdentityService;
import org.camunda.bpm.engine.RuntimeService;
import org.camunda.bpm.engine.TaskService;
import org.camunda.bpm.engine.form.FormField;
import org.camunda.bpm.engine.form.TaskFormData;
import org.camunda.bpm.engine.identity.User;
import org.camunda.bpm.engine.identity.UserQuery;
import org.camunda.bpm.engine.runtime.ProcessInstance;
import org.camunda.bpm.engine.task.Task;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.HttpClientErrorException;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

@Controller
@RequestMapping("/process")
public class ProcessController {

    @Autowired
    private RuntimeService runtimeService;

    @Autowired
    private TaskService taskService;

    @Autowired
    private FormService formService;

    @Autowired
    private IdentityService identityService;

    @Autowired
    private UserService userService;

    @Autowired
    private RequestRepository requestRepository;

    @Autowired
    private ScientificAreRepository scientificAreRepository;

    @Autowired
    private MagazineRepository magazineRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private MagazineRequestRepository magazineRequestRepository;

    /*for(FormField formField : formFields) {
        if(formField.getId().equals("scienceField")) {
            HashMap<String, String> enumValues = (HashMap<String, String>) formField.getType()
                    .getInformation("values");
            enumValues.clear();
            for(String field : scienceFields) {
                enumValues.put(field, field);
            }
        }
    }*/

    @GetMapping(path = "/auth", produces = "application/json")
    public @ResponseBody ResponseEntity<?> auth() {
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @GetMapping(path = "/groups", produces = "application/json")
    public @ResponseBody ResponseEntity<List<String>> groups() {
        System.out.println("USO U GROUPS");
        return new ResponseEntity<>(identityService.getCurrentAuthentication().getGroupIds(),HttpStatus.OK);
    }

    @GetMapping(path = "/magazines", produces = "application/json")
    public @ResponseBody ResponseEntity<List<Magazine>> magazines() {
        return new ResponseEntity<>(magazineRepository.findAll(),HttpStatus.OK);
    }

    @PostMapping(path= "/odabir_casopisa/{taskId}", consumes = "application/json")
    public @ResponseBody ResponseEntity<List<Magazine>> odabirCasopisa(@RequestBody List<FormSubmissionDto> dto, @PathVariable String taskId) {
        ispisiListu(dto);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @GetMapping(path = "/get_magazine", produces = "application/json")
    public @ResponseBody FormFieldsDto getMagazine() {
        ProcessInstance pi = runtimeService.startProcessInstanceByKey("new_magazine");
        Task task = taskService.createTaskQuery().processInstanceId(pi.getId()).list().get(0);
        runtimeService.setVariable(pi.getId(), "odobravan",false);
        TaskFormData tfd = formService.getTaskFormData(task.getId());
        List<FormField> properties = tfd.getFormFields();

        return new FormFieldsDto(task.getId(), pi.getId(), properties);
    }


    @GetMapping(path = "/get_labor", produces = "application/json")
    public @ResponseBody FormFieldsDto getLabor() {
        ProcessInstance pi = runtimeService.startProcessInstanceByKey("new_labor");
        Task task = taskService.createTaskQuery().processInstanceId(pi.getId()).list().get(0);
        TaskFormData tfd = formService.getTaskFormData(task.getId());
        List<FormField> properties = tfd.getFormFields();

        return new FormFieldsDto(task.getId(), pi.getId(), properties);
    }

    @GetMapping(path = "/refresh_labor/{processId}", produces = "application/json")
    public @ResponseBody FormFieldsDto refreshLabor(@PathVariable String processId) {
        Task task = taskService.createTaskQuery().processInstanceId(processId).list().get(0);
        TaskFormData tfd = formService.getTaskFormData(task.getId());
        List<FormField> properties = tfd.getFormFields();
        return new FormFieldsDto(task.getId(), processId, properties);
    }

    @PostMapping(path = "/post_labor/{taskId}", produces = "application/json")
    public @ResponseBody ResponseEntity post(@RequestBody List<FormSubmissionDto> dto, @PathVariable String taskId) {
        HashMap<String, Object> map = this.mapListToDto(dto);
        System.out.println("Stiglo ovo: " + dto.size());
        Task task = taskService.createTaskQuery().taskId(taskId).singleResult();
        String processInstanceId = task.getProcessInstanceId();
        runtimeService.setVariable(processInstanceId, "dto", dto);
        //formService.submitTaskForm(taskId, map);
        taskService.complete(task.getId());
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @GetMapping(path = "/processes", produces = "application/json")
    public @ResponseBody ResponseEntity post() {
        String user = identityService.getCurrentAuthentication().getUserId();
        List<Task> tasks = taskService.createTaskQuery().taskAssignee(user).list();
        System.out.println("BROJ TASKOVA: " + tasks.size());
        List<String> list = new ArrayList<>();
        for(Task t : tasks)
            list.add(t.getProcessInstanceId());
        return new ResponseEntity<>(list,HttpStatus.OK);
    }

    @PostMapping(path = "/post_magazine/{taskId}", produces = "application/json")
    public @ResponseBody ResponseEntity postMagazine(@RequestBody List<FormSubmissionDto> dto, @PathVariable String taskId) {
        HashMap<String, Object> map = this.mapListToDto(dto);
        Task task = taskService.createTaskQuery().taskId(taskId).singleResult();
        String processInstanceId = task.getProcessInstanceId();
        runtimeService.setVariable(processInstanceId, "magazineDTO", dto);
        try {
            formService.submitTaskForm(taskId, map);
        }catch (Exception e){
            return new ResponseEntity<>("Moras uneti obavezna polja!", HttpStatus.BAD_REQUEST);
        }
        System.out.println(taskId + " " + task.getName());
        System.out.println("DOBIO CASOPIS");
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @PostMapping(path = "/enable_user", produces = "application/json")
    public @ResponseBody ResponseEntity enableUser(@RequestParam Boolean value, @RequestParam String processId) {
        Task task = taskService.createTaskQuery().taskDefinitionKey("potvrda_admina")
                .processInstanceId(processId).list().get(0);
        CustomUser user = (CustomUser) runtimeService.getVariable(processId,"user");
        user.setEnabled(value);
        userService.save(user);
        if(user.getEnabled()) {
            identityService.createMembership(user.getKorisnicko_ime(), "reviewer");
            user.setRecenzent(true);
        } else {
            identityService.createMembership(user.getKorisnicko_ime(), "author");
            user.setRecenzent(false);
        }
        userService.save(user);
        EnablingRequest er = requestRepository.findByProcessId(processId);
        requestRepository.delete(er);
        formService.submitTaskForm(task.getId(), null);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @PostMapping(path = "/enable_magazine", produces = "application/json")
    public @ResponseBody ResponseEntity enableMagazine(@RequestParam Boolean value, @RequestParam String processId) {
        Task task = taskService.createTaskQuery().taskDefinitionKey("odobrenje_admina")
                .processInstanceId(processId).list().get(0);
        Magazine magazine = (Magazine) runtimeService.getVariable(processId,"magazineDTO");
        magazine.setAktivan(value);
        magazineRepository.save(magazine);
        if(magazine.getAktivan()) {
            runtimeService.setVariable(processId, "odobri", true);
        } else {
            runtimeService.setVariable(processId, "odobri", false);
            runtimeService.setVariable(processId, "odobravan", true);
        }
        MagazineRequest mg = magazineRequestRepository.findByProcessId(processId);
        magazineRequestRepository.delete(mg);
        formService.submitTaskForm(task.getId(), null);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @GetMapping(path = "/tasks/{processInstanceId}", produces = "application/json")
    public @ResponseBody ResponseEntity<List<TaskDto>> get(@PathVariable String processInstanceId) {

        List<Task> tasks = taskService.createTaskQuery().processInstanceId(processInstanceId).list();
        List<TaskDto> dtos = new ArrayList<TaskDto>();
        for (Task task : tasks) {
            TaskDto t = new TaskDto(task.getId(), task.getName(), task.getAssignee());
            dtos.add(t);
        }

        return new ResponseEntity(dtos,  HttpStatus.OK);
    }

    @PostMapping(path = "/claim/{taskId}", produces = "application/json")
    public @ResponseBody ResponseEntity claim(@PathVariable String taskId) {
        Task task = taskService.createTaskQuery().taskId(taskId).singleResult();
        String processInstanceId = task.getProcessInstanceId();
        String user = (String) runtimeService.getVariable(processInstanceId, "username");
        taskService.claim(taskId, user);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @PostMapping(path = "/complete/{taskId}", produces = "application/json")
    public @ResponseBody ResponseEntity<List<TaskDto>> complete(@PathVariable String taskId) {
        Task taskTemp = taskService.createTaskQuery().taskId(taskId).singleResult();
        taskService.complete(taskId);
        List<Task> tasks = taskService.createTaskQuery().processInstanceId(taskTemp.getProcessInstanceId()).list();
        List<TaskDto> dtos = new ArrayList<TaskDto>();
        for (Task task : tasks) {
            TaskDto t = new TaskDto(task.getId(), task.getName(), task.getAssignee());
            dtos.add(t);
        }
        return new ResponseEntity<List<TaskDto>>(dtos, HttpStatus.OK);
    }

    @GetMapping(path = "/scientific_areas", produces = "application/json")
    public @ResponseBody ResponseEntity<List<String>> scientificAreas() {
        List<String> no = new ArrayList<>();
        for(ScientificArea sc : scientificAreRepository.findAll())
            no.add(sc.getNaziv());
        return new ResponseEntity<List<String>>(no,HttpStatus.OK);
    }

    @PostMapping(path = "/add_scientific_areas", produces = "application/json")
    public @ResponseBody ResponseEntity<List<String>> addScientificAreas(@RequestBody List<String> areas, @RequestParam String processId) {
        System.out.println("Dodajem oblasti");
        Task task = taskService.createTaskQuery().taskDefinitionKey("unos_oblasti")
                .processInstanceId(processId).list().get(0);
        Magazine magazine = (Magazine) runtimeService.getVariable(processId,"magazineDTO");
        for(String s : areas) {
            ScientificArea sa = scientificAreRepository.findByTitle(s);
            System.out.println(sa.getNaziv());
            if(sa != null){
                System.out.println(sa.getNaziv());
                magazine.getScientificAreaList().add(sa);
                sa.getMagazineList().add(magazine);
                scientificAreRepository.save(sa);
            }
        }
        HashMap<String, Object> map = new HashMap<>();
        if(!areas.isEmpty())
            map.put("naziv_naucne_oblasti", "NUCNA OBLAST");
        System.out.println("Snimio");
        magazineRepository.save(magazine);
        MagazineRequest enablingRequest = new MagazineRequest();
        enablingRequest.setProcessId(processId);
        enablingRequest.setIssn(magazine.getIssn());
        enablingRequest.setNaziv(magazine.getNaziv());
        magazineRequestRepository.save(enablingRequest);
        runtimeService.setVariable(processId, "magazineDTO", magazine);

        formService.submitTaskForm(task.getId(), map);
        return new ResponseEntity<List<String>>(HttpStatus.OK);
    }

    @PostMapping(path = "/addrw", produces = "application/json")
    public @ResponseBody ResponseEntity<List<String>> addrw(@RequestParam String editor1, @RequestParam String editor2,
                                                            @RequestParam String reviewer1, @RequestParam String reviewer2,
                                                            @RequestParam String processId) {
        CustomUser editor1U = userRepository.findUserByUsername(editor1);
        CustomUser editor2U = userRepository.findUserByUsername(editor2);
        CustomUser reviewer1U = userRepository.findUserByUsername(reviewer1);
        CustomUser reviewer2U = userRepository.findUserByUsername(reviewer2);
        if(reviewer1U == null || reviewer2U == null)
            return new ResponseEntity<List<String>>(HttpStatus.BAD_REQUEST);

        Magazine magazine = (Magazine) runtimeService.getVariable(processId,"magazineDTO");

        Task task = taskService.createTaskQuery().taskDefinitionKey("postavljanje_urednika")
                .processInstanceId(processId).list().get(0);

        magazine.setEditor1(editor1);
        magazine.setEditor2(editor2);
        magazine.setReviewer1(reviewer1);
        magazine.setReviewer2(reviewer2);
        magazineRepository.save(magazine);

        HashMap<String, Object> map = new HashMap<>();
        map.put("editor1", "edit");
        formService.submitTaskForm(task.getId(), map);
        return new ResponseEntity<List<String>>(HttpStatus.OK);
    }

    @GetMapping(path = "/reviewers", produces = "application/json")
    public @ResponseBody ResponseEntity<List<String>> reviewers() {
        List<String> usersS = new ArrayList<>();
        List<User> users = (List<User>) identityService.createUserQuery().memberOfGroup("reviewer").list();
        for(User u : users)
            usersS.add(u.getId());

        return new ResponseEntity<List<String>>(usersS,HttpStatus.OK);
    }

    @GetMapping(path = "/editors", produces = "application/json")
    public @ResponseBody ResponseEntity<List<String>> editors() {
        List<String> usersS = new ArrayList<>();
        List<User> users = (List<User>) identityService.createUserQuery().memberOfGroup("editor").list();
        for(User u : users)
            usersS.add(u.getId());

        return new ResponseEntity<List<String>>(usersS,HttpStatus.OK);
    }

    private HashMap<String, Object> mapListToDto(List<FormSubmissionDto> list)
    {
        HashMap<String, Object> map = new HashMap<String, Object>();
        for(FormSubmissionDto temp : list){
            map.put(temp.getFieldId(), temp.getFieldValue());
        }

        return map;
    }

    public void ispisiListu(List<FormSubmissionDto> list){
        System.out.println("----------------");
        for(FormSubmissionDto f : list)
            System.out.println(f.getFieldId() + " : " + f.getFieldValue());

        System.out.println("----------------");
    }

}
