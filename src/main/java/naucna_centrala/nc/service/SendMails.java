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
public class SendMails implements JavaDelegate {
    @Autowired
    private EmailSender emailSender;
    @Autowired
    private MagazineRepository magazineRepository;
    @Autowired
    private IdentityService identityService;
    @Autowired
    private UserRepository userRepository;
    @Override
    public void execute(DelegateExecution delegateExecution) throws Exception {
        System.out.println("STIGAO U SEND MAILS");
        Magazine magazineDTO = (Magazine) delegateExecution.getVariable("t1");
        Magazine magazine = magazineRepository.findMagazineByName(magazineDTO.getNaziv());
        String username = identityService.getCurrentAuthentication().getUserId();
        if(magazine != null){
            CustomUser cu = magazine.getGlavni_urednik();
            if(cu != null){
                String text = "E brate ovaj baja hoce da ti ubaci rad!";
                System.out.println("USER: " + cu.getKorisnicko_ime());
                if(cu.getEmail() == null) {
                    System.out.println("EMAIL JE NUUUUUUUUUUULL");
                }
                emailSender.send(cu.getEmail(), text);
                delegateExecution.setVariable("main_editor",magazine.getGlavni_urednik().getKorisnicko_ime());
            }else{
                System.out.println("Nema urednika!");
            }
        }

    }
}
