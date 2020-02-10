package naucna_centrala.nc.listeners;

import naucna_centrala.nc.model.*;
import naucna_centrala.nc.repositories.LaborRepository;
import naucna_centrala.nc.repositories.MagazineRepository;
import naucna_centrala.nc.repositories.ScientificAreRepository;
import naucna_centrala.nc.repositories.UserRepository;
import org.camunda.bpm.engine.FormService;
import org.camunda.bpm.engine.IdentityService;
import org.camunda.bpm.engine.delegate.DelegateTask;
import org.camunda.bpm.engine.delegate.TaskListener;
import org.camunda.bpm.engine.form.TaskFormData;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class OdabirCasopisaComplete implements TaskListener {

    @Autowired
    private FormService formService;

    @Autowired
    private MagazineRepository magazineRepository;

    @Autowired
    private ScientificAreRepository scientificAreRepository;

    @Autowired
    private LaborRepository laborRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private IdentityService identityService;

    @Override
    public void notify(DelegateTask delegateTask) {
        System.out.println("COMPLETE Odabir casopisa");
        List<FormSubmissionDto> dto = (List<FormSubmissionDto>) delegateTask.getVariable("dto");
        for(FormSubmissionDto item : dto) {
            if (item.getFieldId().equals("casopis")) {
                Magazine magazine = magazineRepository.findMagazineByName(item.getFieldValue());
                if(magazine == null)
                    System.out.println("JBG NULL JE");
                delegateTask.setVariable("t1",magazine);
                break;
            }
        }
        delegateTask.setVariable("author", identityService.getCurrentAuthentication().getUserId());
    }


}
