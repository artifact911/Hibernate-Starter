package org.art.service;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.art.dao.UserRepository;
import org.art.dto.UserCreateDto;
import org.art.dto.UserReadDto;
import org.art.entity.User;
import org.art.mapper.Mapper;
import org.art.mapper.UserCreateMapper;
import org.art.mapper.UserReadMapper;
import org.hibernate.graph.GraphSemantic;

import java.util.Map;
import java.util.Optional;

@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final UserReadMapper userReadMapper;
    private final UserCreateMapper userCreateMapper;

    @Transactional
    public Long create(UserCreateDto userDto) {
        // validation
        // map
        var userEntity = userCreateMapper.mapFrom(userDto);

        return userRepository.save(userEntity).getId();
    }

    @Transactional
    public <T> Optional<T> findById(Long id, Mapper<User, T> mapper) {
        Map<String, Object> properties = Map.of(
                GraphSemantic.LOAD.getJakartaHintName(),
                userRepository.getEntityManager().getEntityGraph("withCompany"));

        return userRepository.findById(id, properties).map(mapper::mapFrom);
    }

    @Transactional
    public Optional<UserReadDto> findById(Long id) {
        return findById(id, userReadMapper);
    }

    @Transactional
    public boolean delete(Long id) {
        var maybeUser = userRepository.findById(id);
        maybeUser.ifPresent(u -> userRepository.delete(u.getId()));
        return maybeUser.isPresent();
    }
}
