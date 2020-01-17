package naucna_centrala.nc.controller;

import naucna_centrala.nc.model.EnablingRequest;
import naucna_centrala.nc.model.MagazineRequest;
import naucna_centrala.nc.repositories.MagazineRepository;
import naucna_centrala.nc.repositories.MagazineRequestRepository;
import naucna_centrala.nc.repositories.RequestRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

@Controller
@RequestMapping("/request")
public class RequestsController {

    @Autowired
    private RequestRepository requestRepository;

    @Autowired
    private MagazineRequestRepository magazineRepository;

    @GetMapping(produces = "application/json")
    public ResponseEntity<List<EnablingRequest>> get() {

        return new ResponseEntity<>(requestRepository.findAll(), HttpStatus.OK);
    }

    @GetMapping(value = "/magazine",produces = "application/json")
    public ResponseEntity<List<MagazineRequest>> getMagazine() {
        return new ResponseEntity<>(magazineRepository.findAll(), HttpStatus.OK);
    }

}
