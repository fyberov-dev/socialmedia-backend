package ee.taltech.iti0302project.mapper;

import ee.taltech.iti0302project.dto.ChatDto;
import ee.taltech.iti0302project.entity.ChatEntity;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

import java.util.List;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface ChatMapper {

    ChatDto toDto(ChatEntity chatEntity);

    ChatEntity toEntity(ChatDto chatDto);

    List<ChatDto> toDtoList(List<ChatEntity> chatEntities);
}
