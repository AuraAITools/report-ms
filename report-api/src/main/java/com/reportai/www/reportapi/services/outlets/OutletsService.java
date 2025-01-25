package com.reportai.www.reportapi.services.outlets;

import com.reportai.www.reportapi.entities.Institution;
import com.reportai.www.reportapi.entities.Outlet;
import com.reportai.www.reportapi.entities.Student;
import com.reportai.www.reportapi.exceptions.http.HttpInstitutionNotFoundException;
import com.reportai.www.reportapi.exceptions.lib.ResourceAlreadyExistsException;
import com.reportai.www.reportapi.exceptions.lib.ResourceNotFoundException;
import com.reportai.www.reportapi.repositories.InstitutionRepository;
import com.reportai.www.reportapi.repositories.OutletRepository;
import com.reportai.www.reportapi.repositories.StudentRepository;
import jakarta.transaction.Transactional;
import java.util.List;
import java.util.UUID;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class OutletsService {

    private final InstitutionRepository institutionRepository;
    private final OutletRepository outletRepository;
    private final StudentRepository studentRepository;

    @Autowired
    public OutletsService(InstitutionRepository institutionRepository, OutletRepository outletRepository, StudentRepository studentRepository) {
        this.institutionRepository = institutionRepository;
        this.outletRepository = outletRepository;
        this.studentRepository = studentRepository;
    }

    @Transactional
    public Outlet createOutletForInstitution(UUID id, Outlet newOutlet) {
        Institution institution = institutionRepository.findById(id).orElseThrow(HttpInstitutionNotFoundException::new);
        // TODO: maybe got better way to do this using SQL on DB
        institution.getOutlets().forEach(outlet -> {
            if (outlet.getName().equals(newOutlet.getName())) {
                throw new ResourceAlreadyExistsException("Outlet already exists");
            }
        });
        newOutlet.setInstitution(institution);
        return outletRepository.save(newOutlet);
    }

    @Transactional
    public List<Outlet> getAllOutletsForInstitution(UUID id) {
        Institution institution = institutionRepository.findById(id).orElseThrow(HttpInstitutionNotFoundException::new);
        return institution.getOutlets();
    }

    public Outlet findById(UUID id) {
        return outletRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Outlet does not exist"));
    }

    public Student addStudentToOutlet(UUID studentId, UUID outletId) {
        Outlet outlet = outletRepository.findById(outletId).orElseThrow(() -> new ResourceNotFoundException("Outlet does not exist"));
        Student student = studentRepository.findById(studentId).orElseThrow(() -> new ResourceNotFoundException("Student does not exist"));
        outlet.getStudents().add(student);
        outletRepository.save(outlet);
        return student;
    }
}
