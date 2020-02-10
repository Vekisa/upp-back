package naucna_centrala.nc.listeners;

import naucna_centrala.nc.model.Labor;
import org.camunda.bpm.engine.FormService;
import org.camunda.bpm.engine.delegate.DelegateTask;
import org.camunda.bpm.engine.delegate.TaskListener;
import org.camunda.bpm.engine.form.FormField;
import org.camunda.bpm.engine.form.TaskFormData;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
public class Pregled2Create implements TaskListener {

    @Autowired
    private FormService formService;

    @Override
    public void notify(DelegateTask delegateTask) {
        System.out.println("CREATE Pregled1");
        TaskFormData tfd = formService.getTaskFormData(delegateTask.getId());
        List<FormField> formFields = tfd.getFormFields();
        Labor labor = (Labor) delegateTask.getVariable("t2");
        for(FormField formField : formFields) {
            System.out.println(formField.toString());
            if(formField.getId().equals("pdf")) {
                Map<String,String> aaa = formField.getProperties();
                aaa.put("value",labor.getPdf());
            }
        }
    }
}
