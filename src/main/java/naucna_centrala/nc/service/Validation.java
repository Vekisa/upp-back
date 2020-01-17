package naucna_centrala.nc.service;

import naucna_centrala.nc.model.CustomUser;
import naucna_centrala.nc.model.FormSubmissionDto;
import org.camunda.bpm.engine.IdentityService;
import org.camunda.bpm.engine.RuntimeService;
import org.camunda.bpm.engine.delegate.DelegateExecution;
import org.camunda.bpm.engine.delegate.JavaDelegate;
import org.camunda.bpm.engine.identity.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@Service
public class Validation implements JavaDelegate {

    @Autowired
    private IdentityService identityService;

    @Autowired
    private RuntimeService runtimeService;

    @Autowired
    private UserService userService;

    @Override
    public void execute(DelegateExecution execution) throws Exception {
        System.out.println("OVDEEEE SAAAAAAAM");
        List<FormSubmissionDto> registration = (List<FormSubmissionDto>) execution.getVariable("regDTO");
        //for(FormSubmissionDto fd : registration)
        //    System.out.println(fd.getFieldId() + " " + fd.getFieldValue());

        if(valid(registration)){
            save(registration,execution);
            execution.setVariable("validationValue", true);
        } else {
            execution.setVariable( "validationValue", false);
        }
    }

    public Boolean valid(List<FormSubmissionDto> registration){
        for (FormSubmissionDto formField : registration) {
            if (formField.getFieldId().equals("ime")) {
                if(formField.getFieldValue().length() < 2 || formField.getFieldValue().length() > 35)
                    return false;
            }
            if (formField.getFieldId().equals("prezime") ) {
                if(formField.getFieldValue().length() < 2 || formField.getFieldValue().length() > 35)
                    return false;
            }
            if (formField.getFieldId().equals("grad")) {
                if(formField.getFieldValue().length() < 2 || formField.getFieldValue().length() > 35)
                    return false;
            }
            if (formField.getFieldId().equals("drzava")) {
                if(formField.getFieldValue().length() < 3 || formField.getFieldValue().length() > 35)
                    return false;
            }
            if (formField.getFieldId().equals("titula")) {
                if(formField.getFieldValue().length() < 3 || formField.getFieldValue().length() > 25)
                    return false;
            }
            if (formField.getFieldId().equals("email")) {
                if(formField.getFieldValue().length() < 3 || formField.getFieldValue().length() > 35 ||
                        userService.userExistEmail(formField.getFieldValue()))
                    return false;
            }
            if (formField.getFieldId().equals("korisnicko_ime")) {
                if(formField.getFieldValue().length() < 3 || formField.getFieldValue().length() > 35 ||
                        userService.userExistUsername(formField.getFieldValue()))
                    return false;
            }
            if (formField.getFieldId().equals("sifra")) {
                if(formField.getFieldValue().length() < 3 || formField.getFieldValue().length() > 35)
                    return false;
            }
        }
        return true;
    }

    public void save(List<FormSubmissionDto> registration, DelegateExecution execution){
        CustomUser customUser = new CustomUser();
        customUser.setConfirmationToken(userService.generateToken());
        User user = identityService.newUser("something");
        for (FormSubmissionDto formField : registration) {
            if (formField.getFieldId().equals("ime")) {
                customUser.setIme(formField.getFieldValue());
            }
            if (formField.getFieldId().equals("prezime") ) {
                customUser.setPrezime(formField.getFieldValue());
            }
            if (formField.getFieldId().equals("grad")) {
                customUser.setGrad(formField.getFieldValue());
            }
            if (formField.getFieldId().equals("drzava")) {
                customUser.setDrzava(formField.getFieldValue());
            }
            if (formField.getFieldId().equals("titula")) {
                customUser.setTitula(formField.getFieldValue());
            }
            if (formField.getFieldId().equals("email")) {
                customUser.setEmail(formField.getFieldValue());
            }
            if (formField.getFieldId().equals("korisnicko_ime")) {
                customUser.setKorisnicko_ime(formField.getFieldValue());
                user.setId(formField.getFieldValue());
            }
            if (formField.getFieldId().equals("sifra")) {
                user.setPassword(formField.getFieldValue());
                
            }
            if (formField.getFieldId().equals("recenzent")) {
                if(formField.getFieldValue().equals("true")) {
                    customUser.setRecenzent(true);
                    execution.setVariable("recenzent", true);
                }else {
                    customUser.setRecenzent(false);
                    execution.setVariable("recenzent", false);
                }
            }
        }
        userService.save(customUser);
        execution.setVariable("user",customUser);
        identityService.saveUser(user);
    }
}
