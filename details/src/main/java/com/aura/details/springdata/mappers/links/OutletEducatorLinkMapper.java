package com.aura.details.springdata.mappers.links;

import com.aura.core.entities.links.OutletEducatorLink;
import com.aura.details.springdata.models.educators.EducatorModel;
import com.aura.details.springdata.models.links.OutletEducatorLinkModel;
import com.aura.details.springdata.models.outlets.OutletModel;
import java.util.UUID;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;
import org.mapstruct.factory.Mappers;

@Mapper
public interface OutletEducatorLinkMapper {

    OutletEducatorLinkMapper INSTANCE = Mappers.getMapper(OutletEducatorLinkMapper.class);

    @Mapping(source = "educator.id", target = "educatorId")
    @Mapping(source = "outlet.id", target = "outletId")
    OutletEducatorLink toDomain(OutletEducatorLinkModel model);

    @Mapping(source = "educatorId", target = "educator", qualifiedByName = "educatorRef")
    @Mapping(source = "outletId", target = "outlet", qualifiedByName = "outletRef")
    OutletEducatorLinkModel toTable(OutletEducatorLink link);

    @Named("educatorRef")
    default EducatorModel educatorRef(String educatorId) {
        if (educatorId == null) {
            return null;
        }
        return EducatorModel.builder().id(UUID.fromString(educatorId)).build();
    }

    @Named("outletRef")
    default OutletModel outletRef(String outletId) {
        if (outletId == null) {
            return null;
        }
        return OutletModel.builder().id(UUID.fromString(outletId)).build();
    }
}
