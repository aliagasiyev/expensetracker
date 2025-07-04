package az.edu.msnotification.mapper;

import az.edu.msnotification.dto.NotificationSettingsDto;
import az.edu.msnotification.entity.NotificationSettings;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper(componentModel = "spring")
public interface NotificationSettingsMapper {

    NotificationSettingsMapper INSTANCE = Mappers.getMapper(NotificationSettingsMapper.class);

    NotificationSettings toEntity(NotificationSettingsDto dto);

    NotificationSettingsDto toDto(NotificationSettings entity);
}
