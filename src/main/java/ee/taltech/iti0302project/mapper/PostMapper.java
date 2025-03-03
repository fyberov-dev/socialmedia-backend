package ee.taltech.iti0302project.mapper;

import ee.taltech.iti0302project.dto.PostDto;
import ee.taltech.iti0302project.entity.PostEntity;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;

import java.util.Collection;
import java.util.List;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface PostMapper {

    @Mapping(source = "user", target = "author")
    PostDto toDto(PostEntity postEntity);

    List<PostDto> toDtoList(Collection<PostEntity> postEntities);

    PostEntity toEntity(PostDto postDto);
}
