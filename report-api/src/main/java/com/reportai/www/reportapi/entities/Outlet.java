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
import java.util.ArrayList;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;
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
    @EqualsAndHashCode.Exclude
    @ToString.Exclude
    private Institution institution;

    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(
            joinColumns = @JoinColumn(name = "outlet_id"),
            inverseJoinColumns = @JoinColumn(name = "outlet_admin_id")
    )
    @Builder.Default
    @EqualsAndHashCode.Exclude
    @ToString.Exclude
    private List<OutletAdminPersona> outletAdminPersonas = new ArrayList<>();

    @ManyToMany(fetch = FetchType.LAZY, mappedBy = "outlets")
    @Builder.Default
    @EqualsAndHashCode.Exclude
    @ToString.Exclude
    private List<Student> students = new ArrayList<>();

    @ManyToMany(fetch = FetchType.LAZY, mappedBy = "outlets")
    @Builder.Default
    @EqualsAndHashCode.Exclude
    @ToString.Exclude
    private List<Educator> educators = new ArrayList<>();

    @OneToMany(mappedBy = "outlet", fetch = FetchType.LAZY)
    @Builder.Default
    @EqualsAndHashCode.Exclude
    @ToString.Exclude
    private List<Course> courses = new ArrayList<>();

    @OneToMany(mappedBy = "outlet", fetch = FetchType.LAZY)
    @Builder.Default
    @EqualsAndHashCode.Exclude
    @ToString.Exclude
    private List<Lesson> lessons = new ArrayList<>();

    @Column(nullable = false)
    private String tenantId;
}
