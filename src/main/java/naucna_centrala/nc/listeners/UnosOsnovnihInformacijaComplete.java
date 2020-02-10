package naucna_centrala.nc.listeners;

import naucna_centrala.nc.model.*;
import naucna_centrala.nc.repositories.LaborRepository;
import naucna_centrala.nc.repositories.MagazineRepository;
import naucna_centrala.nc.repositories.ScientificAreRepository;
import naucna_centrala.nc.repositories.UserRepository;
import org.camunda.bpm.engine.FormService;
import org.camunda.bpm.engine.delegate.DelegateTask;
import org.camunda.bpm.engine.delegate.TaskListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UnosOsnovnihInformacijaComplete implements TaskListener {

    @Autowired
    private FormService formService;

    @Autowired
    private MagazineRepository magazineRepository;

    @Autowired
    private ScientificAreRepository scientificAreRepository;

    @Autowired
    private LaborRepository laborRepository;

    @Autowired
    private UserRepository userRepository;

    @Override
    public void notify(DelegateTask delegateTask) {
        System.out.println("Unos osnovnih informacija complete");
        List<FormSubmissionDto> dto = (List<FormSubmissionDto>) delegateTask.getVariable("dto");
        Magazine magazineDTO = (Magazine) delegateTask.getVariable("t1");
        if(magazineDTO == null)
            System.out.println("JBG dto ti je NULL");
        else
            System.out.println("JBG dto nije NULL");
        if(magazineDTO.getNaziv() == null)
            System.out.println("JBG getNaziv ti je NULL");
        if(magazineRepository == null)
            System.out.println("JBG MagazineRepo ti je NULL");

        Magazine magazine = magazineRepository.findMagazineByName(magazineDTO.getNaziv());
        Labor labor = new Labor();
        laborRepository.save(labor);
        for(FormSubmissionDto item : dto){
            if(item.getFieldId().equals("naslov")){
                labor.setNaslov(item.getFieldValue());
            }else if(item.getFieldId().equals("abstract")){
                labor.setSazetak(item.getFieldValue());
            }else if(item.getFieldId().equals("naucna_oblast")){
                ScientificArea sc = scientificAreRepository.findByTitle(item.getFieldValue());
                if(sc != null){
                    sc.getLabors().add(labor);
                    scientificAreRepository.save(sc);
                    labor.setScientificArea(sc);
                }
            }else if(item.getFieldId().equals("pdf")){
                labor.setPdf(item.getFieldValue());
            }else if(item.getFieldId().equals("kljucni_pojmovi")){
                labor.setKljucniPojmovi(item.getFieldValue());
            }else if(item.getFieldId().equals("koautori")){
                postaviKoautore(labor, item.getFieldValue());
            }

        }
        labor.setMagazine(magazine);
        laborRepository.save(labor);
        magazine.getLabors().add(labor);
        magazineRepository.save(magazine);
        delegateTask.setVariable("t2",labor);

    }

    public void postaviKoautore(Labor labor, String koautori){
        String[] list = koautori.split(",");
        for(String s : list){
            CustomUser customUser = userRepository.findUserByUsername(s.trim());
            if(customUser != null){
                labor.getKoautori().add(customUser);
                customUser.getKoautor().add(labor);
                userRepository.save(customUser);
            }
        }
    }
}
