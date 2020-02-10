package naucna_centrala.nc.service;

import naucna_centrala.nc.model.CustomUser;
import naucna_centrala.nc.model.Magazine;
import naucna_centrala.nc.repositories.UserRepository;
import org.camunda.bpm.engine.IdentityService;
import org.camunda.bpm.engine.delegate.DelegateExecution;
import org.camunda.bpm.engine.delegate.JavaDelegate;
import org.camunda.bpm.engine.impl.identity.Authentication;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class IsPayed implements JavaDelegate {

    @Autowired
    private IdentityService identityService;

    @Autowired
    private UserRepository userRepository;

    @Override
    public void execute(DelegateExecution delegateExecution) throws Exception {
        Authentication authentication = identityService.getCurrentAuthentication();
        CustomUser user = userRepository.findUserByUsername(authentication.getUserId());
        if(user == null)
            System.out.println("User je null");
        Magazine magazine = (Magazine) delegateExecution.getVariable("t1");
        if(magazine == null)
            System.out.println("Magazine je null");
        System.out.println("SIZE: " + user.getMagazineList().size());
        if(user.getClanarina().contains(magazine)) {
            System.out.println("Ima clanarinu");
            delegateExecution.setVariable("clanarina_aktivna", true);
        }else {
            System.out.println("Nema clanarinu");
            delegateExecution.setVariable("clanarina_aktivna", false);
        }
    }
}
