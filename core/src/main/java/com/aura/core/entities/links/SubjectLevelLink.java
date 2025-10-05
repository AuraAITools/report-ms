package com.aura.core.entities.links;

import com.aura.core.entities.levels.Level;
import com.aura.core.entities.subjects.Subject;
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
public class SubjectLevelLink {
    private Level level;

    private Subject subject;

}
