package naucna_centrala.nc.listeners;

import naucna_centrala.nc.model.CustomUser;
import naucna_centrala.nc.model.FormSubmissionDto;
import naucna_centrala.nc.model.Magazine;
import naucna_centrala.nc.repositories.MagazineRepository;
import naucna_centrala.nc.repositories.UserRepository;
import org.camunda.bpm.engine.IdentityService;
import org.camunda.bpm.engine.delegate.DelegateTask;
import org.camunda.bpm.engine.delegate.TaskListener;
import org.camunda.bpm.engine.impl.identity.Authentication;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class PlatiComplete implements TaskListener {

    @Autowired
    private MagazineRepository magazineRepository;

    @Autowired
    private IdentityService identityService;

    @Autowired
    private UserRepository userRepository;

    @Override
    public void notify(DelegateTask delegateTask) {

        Magazine magazineDTO = (Magazine)delegateTask.getVariable("t1");
        Magazine magazine = magazineRepository.findMagazineByName(magazineDTO.getNaziv());
        Authentication authentication = identityService.getCurrentAuthentication();
        CustomUser user = userRepository.findUserByUsername(authentication.getUserId());
        user.getClanarina().add(magazine);
        magazine.getImaClanarinu().add(user);
        userRepository.save(user);
        magazineRepository.save(magazine);
        System.out.println("Napravio sam mu clanarinu!");
    }
}
