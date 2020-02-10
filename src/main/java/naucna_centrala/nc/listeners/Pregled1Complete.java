package naucna_centrala.nc.listeners;

import naucna_centrala.nc.model.FormSubmissionDto;
import naucna_centrala.nc.model.Magazine;
import org.camunda.bpm.engine.delegate.DelegateTask;
import org.camunda.bpm.engine.delegate.TaskListener;

import java.util.List;

public class Pregled1Complete implements TaskListener {
    @Override
    public void notify(DelegateTask delegateTask) {
        System.out.println("COMPLETE Pregled 1");
        List<FormSubmissionDto> dto = (List<FormSubmissionDto>) delegateTask.getVariable("dto");
        for(FormSubmissionDto item : dto) {
            if (item.getFieldId().equals("prikladan")) {
                if(item.getFieldValue().equals("true"))
                    delegateTask.setVariable("prikladan",true);
                else
                    delegateTask.setVariable("prikladan", false);
                break;
            }
        }
    }
}
