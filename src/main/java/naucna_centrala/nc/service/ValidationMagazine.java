package naucna_centrala.nc.service;

import naucna_centrala.nc.model.FormSubmissionDto;
import naucna_centrala.nc.model.Magazine;
import naucna_centrala.nc.repositories.MagazineRepository;
import org.camunda.bpm.engine.delegate.DelegateExecution;
import org.camunda.bpm.engine.delegate.JavaDelegate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ValidationMagazine implements JavaDelegate {

    @Autowired
    private MagazineRepository magazineRepository;

    @Override
    public void execute(DelegateExecution execution) throws Exception {
        System.out.println("Validiram casopis!");
        List<FormSubmissionDto> magazine = (List<FormSubmissionDto>) execution.getVariable("magazineDTO");
        for(FormSubmissionDto fd : magazine)
            System.out.println(fd.getFieldId() + " " + fd.getFieldValue());

        if(valid(magazine)){
            save(magazine,execution);
            execution.setVariable("valid", true);
        } else {
            execution.setVariable("valid", false);
        }
    }

    public Boolean valid(List<FormSubmissionDto> magazine){
        for (FormSubmissionDto formField : magazine) {
            if (formField.getFieldId().equals("naziv")) {
                if(formField.getFieldValue().length() < 2 || formField.getFieldValue().length() > 35)
                    return false;
            }
            if (formField.getFieldId().equals("issn")) {
                if(formField.getFieldValue().length() < 2 || formField.getFieldValue().length() > 35)
                    return false;
            }
            if (formField.getFieldId().equals("nacin_placanja")) {
                if(!formField.getFieldValue().equals("Banka") && !formField.getFieldValue().equals("PayPal") &&
                        !formField.getFieldValue().equals("Bitcoin"))
                    return false;
            }
        }
        return true;
    }

    public void save(List<FormSubmissionDto> magazineList, DelegateExecution execution) {
        Magazine magazine = new Magazine();
        for (FormSubmissionDto formField : magazineList) {
            if (formField.getFieldId().equals("naziv")) {
                magazine.setNaziv(formField.getFieldValue());
            }
            if (formField.getFieldId().equals("issn")) {
                magazine.setIssn(formField.getFieldValue());
            }
            if (formField.getFieldId().equals("nacin_placanja")) {
                magazine.setIssn(formField.getFieldValue());
            }
        }

        magazineRepository.save(magazine);
        execution.setVariable( "magazineDTO", magazine);
    }
}
