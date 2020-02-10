package naucna_centrala.nc.service;

import naucna_centrala.nc.model.Magazine;
import org.camunda.bpm.engine.delegate.DelegateExecution;
import org.camunda.bpm.engine.delegate.JavaDelegate;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

@Service
public class IsOpenAccess implements JavaDelegate {
    @Override
    public void execute(DelegateExecution delegateExecution) throws Exception {
        System.out.println("IsOpenAccess------------------------------------------");
        Magazine magazine = (Magazine) delegateExecution.getVariable("t1");
        if(magazine == null)
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST,"Ne postoji casopis!");
        if(magazine.getPlacaAutor())
            delegateExecution.setVariable("is_open_access",true);
        else
            delegateExecution.setVariable("is_open_access",false);
    }
}
