package ec.service;

import ec.model.University;
import ec.repository.UniversityRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class UniversityService {
    private final UniversityRepository repository;

    @Transactional(rollbackFor = Exception.class)
    public University saveUniversity(University university) {
        return repository.save(university);
    }

    public List<University> getAllActiveUniversities() {
        return repository.findAllActiveUniversities();
    }

    public University getUniversityByName(String name) {
        return repository.getUniversityByName(name);
    }

}
