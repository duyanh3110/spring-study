package vn.duynguyen.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import vn.duynguyen.dto.request.AddressDTO;
import vn.duynguyen.dto.request.UserRequestDTO;
import vn.duynguyen.dto.response.UserDetailResponse;
import vn.duynguyen.exception.ResourceNotFoundException;
import vn.duynguyen.model.Address;
import vn.duynguyen.model.User;
import vn.duynguyen.repository.UserRepository;
import vn.duynguyen.service.UserService;
import vn.duynguyen.util.UserStatus;
import vn.duynguyen.util.UserType;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Service
@Slf4j
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;

    @Override
    public long saveUser(UserRequestDTO requestDTO) {
        User user = User.builder()
                .firstName(requestDTO.getFirstName())
                .lastName(requestDTO.getLastName())
                .dateOfBirth(requestDTO.getDateOfBirth())
                .gender(requestDTO.getGender())
                .phone(requestDTO.getPhone())
                .email(requestDTO.getEmail())
                .username(requestDTO.getUsername())
                .password(requestDTO.getPassword())
                .status(requestDTO.getStatus())
                .type(UserType.valueOf(requestDTO.getType().toUpperCase()))
                .addresses(convertToAddress(requestDTO.getAddresses()))
                .build();

        requestDTO.getAddresses().forEach(address ->
                user.saveAddress(Address.builder()
                        .apartmentNumber(address.getApartmentNumber())
                        .floor(address.getFloor())
                        .building(address.getBuilding())
                        .streetNumber(address.getStreetNumber())
                        .street(address.getStreet())
                        .city(address.getCity())
                        .country(address.getCountry())
                        .addressType(address.getAddressType())
                        .build()));

        userRepository.save(user);

        log.info("User has been saved!");

        return user.getId();
    }

    @Override
    public void updateUser(long userId, UserRequestDTO requestDTO) {
        User user = getUserById(userId);

        user.setFirstName(requestDTO.getFirstName());
        user.setLastName(requestDTO.getLastName());
        user.setDateOfBirth(requestDTO.getDateOfBirth());
        user.setGender(requestDTO.getGender());
        user.setPhone(requestDTO.getPhone());
        if (!requestDTO.getEmail().equals(user.getEmail())) {
            // check email from db if not exist then allow update email other throw exception
            user.setEmail(requestDTO.getEmail());
        }
        user.setUsername(requestDTO.getUsername());
        user.setPassword(requestDTO.getPassword());
        user.setStatus(requestDTO.getStatus());
        user.setType(UserType.valueOf(requestDTO.getType().toUpperCase()));
        user.setAddresses(convertToAddress(requestDTO.getAddresses()));

        userRepository.save(user);

        log.info("User updated successfully");
    }

    @Override
    public void changeStatus(long userId, UserStatus status) {
        User user = getUserById(userId);
        user.setStatus(status);

        userRepository.save(user);

        log.info("User status changed");
    }

    @Override
    public void deleteUser(long userId) {
        userRepository.deleteById(userId);

        log.info("User deleted, userId={}", userId);
    }

    @Override
    public UserDetailResponse getUser(long userId) {
        User user = getUserById(userId);

        return UserDetailResponse.builder()
                .firstName(user.getFirstName())
                .lastName(user.getLastName())
                .phone(user.getPhone())
                .email(user.getEmail())
                .build();
    }

    @Override
    public List<UserDetailResponse> getAllUsers(int pageNo, int pageSize) {
        int page = 0;
        if (pageNo > 0) {
            page = pageNo - 1;
        }

        Pageable pageable = PageRequest.of(page, pageSize);

        Page<User> users = userRepository.findAll(pageable);
        return users.stream().map(user -> UserDetailResponse.builder()
                .firstName(user.getFirstName())
                .lastName(user.getLastName())
                .email(user.getEmail())
                .phone(user.getPhone())
                .build())
                .toList();
    }

    private Set<Address> convertToAddress(Set<AddressDTO> addresses) {
        Set<Address> result = new HashSet<>();
        addresses.forEach(address ->
                result.add(Address.builder()
                        .apartmentNumber(address.getApartmentNumber())
                        .floor(address.getFloor())
                        .building(address.getBuilding())
                        .streetNumber(address.getStreetNumber())
                        .street(address.getStreet())
                        .city(address.getCity())
                        .country(address.getCountry())
                        .addressType(address.getAddressType())
                        .build())
        );
        return result;
    }

    private User getUserById(long userId) {
        return userRepository.findById(userId).orElseThrow(() -> new ResourceNotFoundException("User no found"));
    }
}
