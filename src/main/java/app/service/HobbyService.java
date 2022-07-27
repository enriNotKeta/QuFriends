package app.service;


import app.model.Hobby;
import app.model.Report;
import app.repository.HobbyRepository;
import app.repository.ReportRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.rest.webmvc.ResourceNotFoundException;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class HobbyService {
    private final HobbyRepository hobbyRepository;

    @Autowired
    public HobbyService(HobbyRepository hobbyRepository) {
        this.hobbyRepository = hobbyRepository;
    }

    public List<Hobby> findAll() {
        List<Hobby> hobbies = new ArrayList<>();
        hobbyRepository.findAll().forEach(hobbies::add);
        return hobbies;
    }

    public Hobby findById(Long id) throws ResourceNotFoundException {
        Hobby hobby = hobbyRepository.findById(id).orElse(null);
        if (hobby==null) {
            throw new ResourceNotFoundException("Cannot find hobby with id: " + id);
        }
        else return hobby;
    }
}
