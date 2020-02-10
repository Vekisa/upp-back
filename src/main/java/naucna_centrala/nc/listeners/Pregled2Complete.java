package naucna_centrala.nc.listeners;

import naucna_centrala.nc.model.FormSubmissionDto;
import org.camunda.bpm.engine.delegate.DelegateTask;
import org.camunda.bpm.engine.delegate.TaskListener;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class Pregled2Complete implements TaskListener {
    @Override
    public void notify(DelegateTask delegateTask) {
        System.out.println("COMPLETE Pregled 2");
        List<FormSubmissionDto> dto = (List<FormSubmissionDto>) delegateTask.getVariable("dto");
        for(FormSubmissionDto item : dto) {
            if (item.getFieldId().equals("dobar")) {
                if(item.getFieldValue().equals("true"))
                    delegateTask.setVariable("dobar",true);
                else
                    delegateTask.setVariable("dobar", false);
                break;
            }
        }
    }
}
