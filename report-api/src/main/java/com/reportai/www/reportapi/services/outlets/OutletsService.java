package com.reportai.www.reportapi.services.outlets;

import com.reportai.www.reportapi.entities.Institution;
import com.reportai.www.reportapi.entities.Outlet;
import com.reportai.www.reportapi.exceptions.http.HttpInstitutionNotFoundException;
import com.reportai.www.reportapi.exceptions.lib.ResourceAlreadyExistsException;
import com.reportai.www.reportapi.repositories.InstitutionRepository;
import com.reportai.www.reportapi.repositories.OutletRepository;
import jakarta.transaction.Transactional;
import java.util.List;
import java.util.UUID;
import org.springframework.stereotype.Service;

@Service
public class OutletsService {

    private final InstitutionRepository institutionRepository;
    private final OutletRepository outletRepository;

    public OutletsService(InstitutionRepository institutionRepository, OutletRepository outletRepository) {
        this.institutionRepository = institutionRepository;
        this.outletRepository = outletRepository;
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
}
