package com.reportai.www.reportapi.services.outlets;

import com.reportai.www.reportapi.api.v1.outlets.requests.UpdateOutletRequestDTO;
import com.reportai.www.reportapi.entities.Course;
import com.reportai.www.reportapi.entities.Educator;
import com.reportai.www.reportapi.entities.Institution;
import com.reportai.www.reportapi.entities.Outlet;
import com.reportai.www.reportapi.entities.Student;
import com.reportai.www.reportapi.entities.personas.OutletAdminPersona;
import com.reportai.www.reportapi.exceptions.lib.ResourceAlreadyExistsException;
import com.reportai.www.reportapi.exceptions.lib.ResourceNotFoundException;
import com.reportai.www.reportapi.repositories.OutletAdminPersonaRepository;
import com.reportai.www.reportapi.repositories.OutletRepository;
import com.reportai.www.reportapi.services.common.BaseServiceTemplate;
import com.reportai.www.reportapi.services.institutions.InstitutionsService;
import com.reportai.www.reportapi.services.students.StudentsService;
import jakarta.transaction.Transactional;
import java.util.List;
import java.util.UUID;
import lombok.NonNull;
import org.keycloak.admin.client.resource.ClientResource;
import org.keycloak.representations.idm.RoleRepresentation;
import org.modelmapper.ModelMapper;
import org.modelmapper.convention.MatchingStrategies;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Service;

@Service
public class OutletsService implements BaseServiceTemplate<Outlet, UUID> {

    private final InstitutionsService institutionsService;
    private final OutletRepository outletsRepository;
    private final StudentsService studentsService;
    private final ClientResource clientResource;
    private final OutletAdminPersonaRepository outletAdminPersonaRepository;
    private final ModelMapper modelMapper;
    private final String OUTLET_ROLE_TEMPLATE = "%s_%s_outlet-admin";

    @Autowired
    public OutletsService(InstitutionsService institutionsService, OutletRepository outletsRepository, StudentsService studentsService, ClientResource clientResource, OutletAdminPersonaRepository outletAdminPersonaRepository) {
        this.institutionsService = institutionsService;
        this.outletsRepository = outletsRepository;
        this.studentsService = studentsService;
        this.clientResource = clientResource;
        this.outletAdminPersonaRepository = outletAdminPersonaRepository;
        this.modelMapper = new ModelMapper();
        this.modelMapper.getConfiguration()
                .setMatchingStrategy(MatchingStrategies.STRICT)
                .setSkipNullEnabled(true);// skip null values
        this.modelMapper.typeMap(UpdateOutletRequestDTO.class, Outlet.class);
    }

    @Override
    public JpaRepository<Outlet, UUID> getRepository() {
        return outletsRepository;
    }

    @Transactional
    public Outlet createOutletForInstitution(@NonNull UUID id, @NonNull Outlet newOutlet) {
        Institution institution = institutionsService.findById(id);
        // TODO: maybe got better way to do this using SQL on DB
        institution.getOutlets().forEach(outlet -> {
            if (outlet.getName().equals(newOutlet.getName())) {
                throw new ResourceAlreadyExistsException("Outlet already exists");
            }
        });
        newOutlet.setInstitution(institution);
        Outlet createdOutlet = outletsRepository.save(newOutlet);
        clientResource.roles().create(createTenantAwareOutletRole(id.toString(), createdOutlet.getId().toString()));
        return createdOutlet;
    }

    @Transactional
    public List<Outlet> getAllOutletsForInstitution(@NonNull UUID id) {
        Institution institution = institutionsService.findById(id);
        return institution.getOutlets();
    }


    public List<Course> getOutletCourses(@NonNull UUID outletId) {
        return outletsRepository.findById(outletId).orElseThrow(() -> new ResourceNotFoundException("Outlet does not exist")).getCourses();
    }


    public List<Educator> getOutletEducators(@NonNull UUID outletId) {
        return findById(outletId).getEducators();
    }


    public List<Student> getOutletStudents(@NonNull UUID outletId) {
        return findById(outletId).getStudents();
    }


    public Student addStudent(@NonNull UUID studentId, @NonNull UUID outletId) {
        Outlet outlet = findById(outletId);
        Student student = studentsService.findById(studentId);
        /**
         * TODO: add method in entity
         */
        outlet.getStudents().add(student);
        outletsRepository.save(outlet);
        return student;
    }

    public Outlet addOutletAdminPersona(UUID outletId, UUID outletAdminPersonaId) {
        Outlet outlet = findById(outletId);
        OutletAdminPersona outletAdminPersona = outletAdminPersonaRepository.findById(outletAdminPersonaId).orElseThrow(() -> new ResourceNotFoundException("no outletAdminPersona found"));
        outlet.addOutletAdminPersona(outletAdminPersona);
        return outlet;
    }

    @Transactional
    public Outlet addOutletAdminPersonas(UUID outletId, List<UUID> outletAdminPersonaIds) {
        Outlet outlet = findById(outletId);
        List<OutletAdminPersona> outletAdminPersonas = outletAdminPersonaRepository.findAllById(outletAdminPersonaIds);
        outlet.addOutletAdminPersonas(outletAdminPersonas);
        return outlet;
    }

    @Transactional
    public Outlet update(UUID outletId, UpdateOutletRequestDTO updateOutletRequestDTO) {
        Outlet outlet = findById(outletId);
        modelMapper.map(updateOutletRequestDTO, outlet);
        return outlet;
    }

    private RoleRepresentation createTenantAwareOutletRole(@NonNull String tenantId, @NonNull String outletId) {
        RoleRepresentation clientRole = new RoleRepresentation();
        clientRole.setClientRole(true);
        clientRole.setDescription(tenantId);
        clientRole.setName(String.format(OUTLET_ROLE_TEMPLATE, tenantId, outletId));
        return clientRole;
    }

}
