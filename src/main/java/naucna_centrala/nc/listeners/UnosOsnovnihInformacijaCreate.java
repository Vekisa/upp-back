package naucna_centrala.nc.listeners;

import naucna_centrala.nc.model.Magazine;
import naucna_centrala.nc.model.ScientificArea;
import naucna_centrala.nc.repositories.ScientificAreRepository;
import org.camunda.bpm.engine.FormService;
import org.camunda.bpm.engine.delegate.DelegateTask;
import org.camunda.bpm.engine.delegate.TaskListener;
import org.camunda.bpm.engine.form.FormField;
import org.camunda.bpm.engine.form.TaskFormData;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;

@Service
public class UnosOsnovnihInformacijaCreate implements TaskListener {
    @Autowired
    private FormService formService;

    @Autowired
    private ScientificAreRepository scientificAreRepository;
    @Override
    public void notify(DelegateTask delegateTask) {
        System.out.println("Unos osnovnih informacija create");
        TaskFormData tfd = formService.getTaskFormData(delegateTask.getId());
        List<FormField> formFields = tfd.getFormFields();

        for(FormField formField : formFields) {
            if(formField.getId().equals("naucna_oblast")) {
                HashMap<String, String> enumValues = (HashMap<String, String>) formField.getType()
                        .getInformation("values");
                enumValues.clear();
                for(ScientificArea sc : scientificAreRepository.findAll()) {
                    enumValues.put(sc.getNaziv(), sc.getNaziv());
                }
            }
        }
    }
}
