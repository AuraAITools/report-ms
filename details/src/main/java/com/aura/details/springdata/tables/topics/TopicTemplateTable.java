package com.aura.details.springdata.tables.topics;


import com.aura.details.springdata.tables.base.BaseTable;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;
import org.hibernate.envers.Audited;

@Entity
@Audited
@Getter
@Setter
@SuperBuilder
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "TopicsTemplates")
public class TopicTemplateTable extends BaseTable {

    private String name;

}
