package com.aura.core.entities.outlets;


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

@Getter
@Setter
@SuperBuilder
@AllArgsConstructor
@NoArgsConstructor
public class Outlet {
    private String name;

    private String address;

    private String postalCode;

    private String contactNumber;

    private String description;

    private String email;


}
