package com.reportai.www.reportapi.services.outlets;

import com.reportai.www.reportapi.api.v1.outlets.requests.UpdateOutletRequestDTO;
import com.reportai.www.reportapi.contexts.requests.TenantContext;
import com.reportai.www.reportapi.entities.Outlet;
import com.reportai.www.reportapi.entities.Student;
import com.reportai.www.reportapi.entities.attachments.OutletEducatorAttachment;
import com.reportai.www.reportapi.entities.attachments.StudentOutletRegistration;
import com.reportai.www.reportapi.entities.courses.Course;
import com.reportai.www.reportapi.entities.educators.Educator;
import com.reportai.www.reportapi.entities.helpers.Attachment;
import com.reportai.www.reportapi.exceptions.lib.ResourceNotFoundException;
import com.reportai.www.reportapi.repositories.OutletRepository;
import com.reportai.www.reportapi.repositories.StudentOutletRegistrationRepository;
import com.reportai.www.reportapi.services.common.ISimpleRead;
import com.reportai.www.reportapi.services.students.StudentsService;
import jakarta.transaction.Transactional;
import java.util.Collection;
import java.util.UUID;
import java.util.stream.Collectors;
import lombok.NonNull;
import org.keycloak.admin.client.resource.ClientResource;
import org.keycloak.representations.idm.RoleRepresentation;
import org.modelmapper.ModelMapper;
import org.modelmapper.convention.MatchingStrategies;
import org.modelmapper.internal.util.Assert;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Service;

@Service
public class OutletsService implements ISimpleRead<Outlet> {

    private final OutletRepository outletsRepository;
    private final StudentsService studentsService;
    private final ClientResource clientResource;
    private final ModelMapper modelMapper;
    private final String OUTLET_ROLE_TEMPLATE = "%s_%s_outlet-admin";
    private final StudentOutletRegistrationRepository studentOutletRegistrationRepository;

    @Autowired
    public OutletsService(OutletRepository outletsRepository, StudentsService studentsService, ClientResource clientResource,
                          StudentOutletRegistrationRepository studentOutletRegistrationRepository) {
        this.outletsRepository = outletsRepository;
        this.studentsService = studentsService;
        this.clientResource = clientResource;
        this.modelMapper = new ModelMapper();
        this.modelMapper.getConfiguration()
                .setMatchingStrategy(MatchingStrategies.STRICT)
                .setSkipNullEnabled(true);// skip null values
        this.modelMapper.typeMap(UpdateOutletRequestDTO.class, Outlet.class);
        this.studentOutletRegistrationRepository = studentOutletRegistrationRepository;
    }


    @Override
    public JpaRepository<Outlet, UUID> getRepository() {
        return outletsRepository;
    }


    @Transactional
    public Outlet create(@NonNull Outlet newOutlet) {
        Outlet createdOutlet = outletsRepository.save(newOutlet);
        Assert.notNull(TenantContext.getTenantId());
        clientResource.roles().create(createTenantAwareOutletRole(TenantContext.getTenantId(), createdOutlet.getId().toString()));
        return createdOutlet;
    }

    @Transactional
    public Collection<Outlet> getAllOutletsForInstitution() {
        return outletsRepository.findAll();
    }


    @Transactional
    public Collection<Course> getOutletCourses(@NonNull UUID outletId) {
        return outletsRepository.findById(outletId).orElseThrow(() -> new ResourceNotFoundException("Outlet does not exist")).getCourses();
    }


    @Transactional
    public Collection<Educator> getOutletEducators(@NonNull UUID outletId) {
        return findById(outletId).getOutletEducatorAttachments().stream().map(OutletEducatorAttachment::getEducator).collect(Collectors.toSet());
    }


    @Transactional
    public Collection<Student> getOutletStudents(@NonNull UUID outletId) {
        return findById(outletId).getStudentOutletRegistrations().stream().map(StudentOutletRegistration::getStudent).toList();
    }


    public Student addStudent(@NonNull UUID studentId, @NonNull UUID outletId) {
        Outlet outlet = findById(outletId);
        Student student = studentsService.findById(studentId);
        StudentOutletRegistration studentOutletRegistration = Attachment.createAndSync(student, outlet, new StudentOutletRegistration());
        studentOutletRegistrationRepository.save(studentOutletRegistration);
        return student;
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
