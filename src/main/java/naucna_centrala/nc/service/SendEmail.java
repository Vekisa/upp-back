package naucna_centrala.nc.service;

import naucna_centrala.nc.model.CustomUser;
import org.camunda.bpm.engine.delegate.DelegateExecution;
import org.camunda.bpm.engine.delegate.JavaDelegate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class SendEmail implements JavaDelegate {

    @Autowired
    private EmailSender emailSender;

    @Override
    public void execute(DelegateExecution delegateExecution) throws Exception {
       System.out.println("STIGAO U SEND EMAIL");
       CustomUser user = (CustomUser) delegateExecution.getVariable("user");
       String text = "http://localhost:8080/auth/confirm?token=" + user.getConfirmationToken() +
               "&processId=" + delegateExecution.getProcessInstance().getProcessInstanceId();
       emailSender.send(user.getEmail(), text);
    }
}
