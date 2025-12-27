package com.aura.core.entities.links;

import com.aura.core.entities.subjects.Subject;
import com.aura.core.entities.tests.TestGroup;
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
public class SubjectTestGroupLink {
    private Subject subject;

    private TestGroup testGroup;

}
