package naucna_centrala.nc.listeners;

import naucna_centrala.nc.model.Magazine;
import naucna_centrala.nc.repositories.MagazineRepository;
import org.camunda.bpm.engine.FormService;
import org.camunda.bpm.engine.TaskService;
import org.camunda.bpm.engine.delegate.DelegateTask;
import org.camunda.bpm.engine.delegate.TaskListener;
import org.camunda.bpm.engine.form.FormField;
import org.camunda.bpm.engine.form.TaskFormData;
import org.camunda.bpm.engine.task.Task;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;

@Service
public class OdabirCasopisaCreate implements TaskListener {

    @Autowired
    private TaskService taskService;

    @Autowired
    private FormService formService;

    @Autowired
    private MagazineRepository magazineRepository;


    @Override
    public void notify(DelegateTask delegateTask) {
        System.out.println("CREATE");
        System.out.println(delegateTask.getId());
        TaskFormData tfd = formService.getTaskFormData(delegateTask.getId());
        List<FormField> formFields = tfd.getFormFields();
        delegateTask.setVariable("odobravan", false);

        for(FormField formField : formFields) {
            if(formField.getId().equals("casopis")) {
                HashMap<String, String> enumValues = (HashMap<String, String>) formField.getType()
                        .getInformation("values");
                enumValues.clear();
                for(Magazine magazine : magazineRepository.findAll()) {
                    enumValues.put(magazine.getNaziv(), magazine.getNaziv());
                }
            }
        }

    }
}
