package edu.example.hw1.api.mapper;

import edu.example.hw1.api.dto.auth.AuthenticationLoginDto;
import edu.example.hw1.api.dto.auth.AuthenticationRegisterDto;
import edu.example.hw1.domain.entity.UserEntity;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface AuthenticationMapper {
  UserEntity authenticationLoginDtoToUser(AuthenticationLoginDto authenticationLoginDto);

  UserEntity authenticationRegisterDtoToUser(AuthenticationRegisterDto authenticationRegisterDto);
}

