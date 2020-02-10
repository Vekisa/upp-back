package naucna_centrala.nc.service;

import naucna_centrala.nc.model.CustomUser;
import naucna_centrala.nc.repositories.UserRepository;
import org.camunda.bpm.engine.delegate.DelegateExecution;
import org.camunda.bpm.engine.delegate.JavaDelegate;
import org.springframework.beans.factory.annotation.Autowired;

public class SendMailTechnicalProblems implements JavaDelegate {
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private EmailSender emailSender;

    @Override
    public void execute(DelegateExecution delegateExecution) throws Exception {
        String username = (String) delegateExecution.getVariable("author");
        CustomUser cu = userRepository.findUserByUsername(username);
        emailSender.send(cu.getEmail(), "Imas tehnickih poblema!");
    }
}
