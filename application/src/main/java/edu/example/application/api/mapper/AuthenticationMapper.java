package edu.example.application.api.mapper;

import edu.example.application.api.dto.auth.AuthenticationLoginDto;
import edu.example.application.api.dto.auth.AuthenticationRegisterDto;
import edu.example.application.domain.entity.UserEntity;
import org.mapstruct.Mapper;

/**
 * Mapper for authentication entities and data transfer objects.
 */
@Mapper(componentModel = "spring")
public interface AuthenticationMapper {
  UserEntity authenticationLoginDtoToUser(AuthenticationLoginDto authenticationLoginDto);

  UserEntity authenticationRegisterDtoToUser(AuthenticationRegisterDto authenticationRegisterDto);
}

