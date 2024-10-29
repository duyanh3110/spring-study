package vn.duynguyen.service;

import vn.duynguyen.dto.request.UserRequestDTO;
import vn.duynguyen.dto.response.UserDetailResponse;
import vn.duynguyen.util.UserStatus;

import java.util.List;

public interface UserService {

    long saveUser(UserRequestDTO requestDTO);

    void updateUser(long userId, UserRequestDTO requestDTO);

    void changeStatus(long userId, UserStatus status);

    void deleteUser(long userId);

    UserDetailResponse getUser(long userId);

    List<UserDetailResponse> getAllUsers(int pageNo, int pageSize);

}
