package az.edu.msauth.mapper;

import az.edu.msauth.dto.request.RegisterRequest;
import az.edu.msauth.dto.request.UpdateProfileRequest;
import az.edu.msauth.dto.response.UserResponse;
import az.edu.msauth.entity.User;
import org.mapstruct.*;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface UserMapper {

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "password", ignore = true)
    @Mapping(target = "role", constant = "USER")
    @Mapping(target = "active", constant = "true")
    User toEntity(RegisterRequest request);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "password", ignore = true)
    @Mapping(target = "role", ignore = true)
    void updateEntity(@MappingTarget User user, UpdateProfileRequest request);

    UserResponse toResponse(User user);
}