package ec.service;

import ec.model.University;
import ec.repository.UniversityRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

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

    @Transactional(rollbackFor = Exception.class)
    @Modifying
    public void deleteUniversityById(Long id) {
        repository.deleteUniversityById(id);
    }

    public Optional<University> findUniversityById(Long id) {
        return repository.findById(id);
    }

}
