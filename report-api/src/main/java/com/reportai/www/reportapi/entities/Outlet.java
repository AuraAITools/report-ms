package com.reportai.www.reportapi.entities;


import com.reportai.www.reportapi.entities.personas.OutletAdminPersona;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import jakarta.validation.constraints.Email;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

@Entity
@Data
@EqualsAndHashCode(callSuper = true)
@SuperBuilder
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "Outlets")
public class Outlet extends BaseEntity {
    private String name;

    private String address;

    private String postalCode;

    private String contactNumber;

    private String description;

    @Email
    private String email;

    @ManyToOne(fetch = FetchType.EAGER)
    private Institution institution;

    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(
            joinColumns = @JoinColumn(name = "outlet_id"),
            inverseJoinColumns = @JoinColumn(name = "outlet_admin_id")
    )
    private List<OutletAdminPersona> outletAdminPersonas;

    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(
            joinColumns = @JoinColumn(name = "outlet_id"),
            inverseJoinColumns = @JoinColumn(name = "student_id")
    )
    private List<Student> students;

    @ManyToMany(fetch = FetchType.LAZY, mappedBy = "outlets")
    private List<Educator> educators;

    @OneToMany(mappedBy = "outlet", fetch = FetchType.LAZY)
    private List<Course> courses;

    @OneToMany(mappedBy = "outlet", fetch = FetchType.LAZY)
    private List<Lesson> lessons;

    @Column(nullable = false)
    private String tenantId;
}
