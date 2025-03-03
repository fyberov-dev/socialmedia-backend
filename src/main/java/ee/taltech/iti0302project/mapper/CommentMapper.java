package ee.taltech.iti0302project.mapper;

import ee.taltech.iti0302project.dto.CommentDto;
import ee.taltech.iti0302project.entity.CommentEntity;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

import java.util.List;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface CommentMapper {

    CommentDto toDto(CommentEntity commentEntity);

    List<CommentDto> toDtoList(List<CommentEntity> commentEntities);

    CommentEntity toEntity(CommentDto commentDto);
}
