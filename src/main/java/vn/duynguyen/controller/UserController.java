package vn.duynguyen.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Min;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import vn.duynguyen.configuration.Translator;
import vn.duynguyen.dto.request.UserRequestDTO;
import vn.duynguyen.dto.response.ResponseData;
import vn.duynguyen.dto.response.ResponseError;
import vn.duynguyen.dto.response.UserDetailResponse;
import vn.duynguyen.exception.ResourceNotFoundException;
import vn.duynguyen.service.UserService;
import vn.duynguyen.util.UserStatus;

import java.util.List;

@RestController
@RequestMapping("/user")
@Validated
@Slf4j
@Tag(name = "User Controller")
@RequiredArgsConstructor
public class UserController {
    private final UserService userService;

    @Operation(summary = "Add new user", description = "API create new user")
    @PostMapping("/")
    public ResponseData<Long> addUser(@Valid @RequestBody UserRequestDTO user) {
        log.info("Request add user: {} {}", user.getFirstName(), user.getLastName());
        try {
            long userId = userService.saveUser(user);
            return new ResponseData<>(HttpStatus.CREATED.value(), Translator.toLocale("user.add.success"), userId);
        } catch (Exception e) {
            log.error("errorMessage={}", e.getMessage(), e.getCause());
            return new ResponseError(HttpStatus.BAD_REQUEST.value(), "Save failed!");
        }
    }

    @Operation(summary = "Update user", description = "API update user data")
    @PutMapping("/{userId}")
    public ResponseData<?> updateUser(@Min(1) @PathVariable int userId, @Valid @RequestBody UserRequestDTO userDTO) {
        log.info("Request update userId = {}", userId);

        try {
            userService.updateUser(userId, userDTO);
            return new ResponseData<>(HttpStatus.OK.value(), Translator.toLocale("user.update.success"), userId);
        } catch (Exception e) {
            log.error("errorMessage={}", e.getMessage(), e.getCause());
            return new ResponseError(HttpStatus.BAD_REQUEST.value(), "Update user failed!");
        }
    }

    @Operation(summary = "Change user status", description = "API change user status")
    @PatchMapping("/{userId}")
    public ResponseData<?> changeStatus(@Min(1) @PathVariable int userId, @RequestParam UserStatus status) {
        log.info("Request change user status userId = {}", userId);

        try {
            userService.changeStatus(userId, status);
            return new ResponseData<>(HttpStatus.OK.value(), Translator.toLocale("user.status.success"), userId);
        } catch (Exception e) {
            log.error("errorMessage={}", e.getMessage(), e.getCause());
            return new ResponseError(HttpStatus.BAD_REQUEST.value(), "Update user failed!");
        }
    }

    @Operation(summary = "Remove user", description = "API remove user")
    @DeleteMapping("/{userId}")
    public ResponseData<?> deleteUser(@Min(1) @PathVariable int userId) {
        log.info("Request delete userId = {}", userId);

        try {
            userService.deleteUser(userId);
            return new ResponseData<>(HttpStatus.OK.value(), Translator.toLocale("user.delete.success"), userId);
        } catch (Exception e) {
            log.error("errorMessage={}", e.getMessage(), e.getCause());
            return new ResponseError(HttpStatus.BAD_REQUEST.value(), "Delete user failed!");
        }
    }

    @Operation(summary = "Get user detail", description = "API get new detail")
    @GetMapping("/{userId}")
    public ResponseData<UserDetailResponse> getUser(@Min(1) @PathVariable long userId) {
        log.info("Request get userId = { }", userId);
        try {
            return new ResponseData<>(HttpStatus.OK.value(), "user", userService.getUser(userId));
        } catch (ResourceNotFoundException e) {
            log.error("errorMessage={}", e.getMessage(), e.getCause());
            return new ResponseError(HttpStatus.BAD_REQUEST.value(), e.getMessage());
        }
    }

    @Operation(summary = "Get user list per page", description = "Return user by pageNo and pageSize")
    @GetMapping("/list")
    @ResponseStatus(HttpStatus.OK)
    public ResponseData<List<UserDetailResponse>> getAllUsers(
            @RequestParam(required = false) String email,
            @RequestParam(defaultValue = "0") int pageNo,
            @RequestParam(defaultValue = "10") @Min(10) int pageSize) {
        log.info("Request getAllUsers");
        return new ResponseData<>(HttpStatus.OK.value(), "users", userService.getAllUsers(pageNo, pageSize));
    }
}
