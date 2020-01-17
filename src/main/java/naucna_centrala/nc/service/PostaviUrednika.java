package naucna_centrala.nc.service;

import naucna_centrala.nc.model.CustomUser;
import naucna_centrala.nc.model.Magazine;
import naucna_centrala.nc.repositories.MagazineRepository;
import naucna_centrala.nc.repositories.UserRepository;
import org.camunda.bpm.engine.IdentityService;
import org.camunda.bpm.engine.delegate.DelegateExecution;
import org.camunda.bpm.engine.delegate.JavaDelegate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class PostaviUrednika implements JavaDelegate {

    @Autowired
    private IdentityService identityService;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private MagazineRepository magazineRepository;

    @Override
    public void execute(DelegateExecution execution) throws Exception {
        System.out.println("Postavljam urednika");
        String username = identityService.getCurrentAuthentication().getUserId();
        CustomUser customUser = userRepository.findUserByUsername(username);
        if(customUser == null)
            System.out.println("user null");
        Magazine magazine = (Magazine) execution.getVariable("magazineDTO");
        if(magazine == null)
            System.out.println("magazine null");
        magazine.setGlavni_urednik(customUser);
        customUser.getMagazineList().add(magazine);
        magazineRepository.save(magazine);
        userRepository.save(customUser);


    }
}
