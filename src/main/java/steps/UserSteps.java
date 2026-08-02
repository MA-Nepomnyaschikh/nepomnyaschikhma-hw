package steps;

import models.request.CreateUserRequestDto;
import models.request.UpdateUserRequestDto;
import models.response.CreateUserResponseDto;
import models.response.UpdateUserResponseDto;
import requests.Endpoint;
import requests.RestRequest;
import requests.ValidatableRestRequest;
import specs.RequestSpecs;
import specs.ResponseSpecs;
import supports.CleanupManager;
import supports.StepLogger;
import testdata.randommodelgenerator.RandomModelGenerator;
import io.restassured.response.ValidatableResponse;
import io.restassured.specification.RequestSpecification;
import io.restassured.specification.ResponseSpecification;

import java.util.List;
import java.util.NoSuchElementException;

public class UserSteps {

    private final CleanupManager cleanupManager;

    public UserSteps(CleanupManager cleanupManager) {
        this.cleanupManager = cleanupManager;
    }

    public CreateUserResponseDto createUser(CreateUserRequestDto userDto) {
        CreateUserResponseDto createdUser = new ValidatableRestRequest<CreateUserResponseDto>(
                RequestSpecs.authAsAdmin(),
                Endpoint.CREATE_USER,
                ResponseSpecs.created())
                .post(userDto);

        cleanupManager.register(() -> deleteUserById(createdUser.getId()));
        return createdUser;
    }

    public ValidatableResponse createUser(CreateUserRequestDto userDto, RequestSpecification requestSpec, ResponseSpecification responseSpec) {
        return new RestRequest(
                requestSpec,
                Endpoint.CREATE_USER,
                responseSpec)
                .post(userDto);
    }

    public CreateUserRequestDto createRandomUser() {
        CreateUserRequestDto userDto = RandomModelGenerator.generate(CreateUserRequestDto.class);
        CreateUserResponseDto createdUser = new ValidatableRestRequest<CreateUserResponseDto>(
                RequestSpecs.authAsAdmin(),
                Endpoint.CREATE_USER,
                ResponseSpecs.created())
                .post(userDto);

        cleanupManager.register(
                () -> deleteUserById(createdUser.getId())
        );
        return userDto;
    }

    public CreateUserResponseDto getCustomerProfile(String token) {
        return new ValidatableRestRequest<CreateUserResponseDto>(
                RequestSpecs.authAsUser(token),
                Endpoint.GET_CUSTOMER_PROFILE,
                ResponseSpecs.ok())
                .get();
    }

    public UpdateUserResponseDto updateCustomerProfile(String token, UpdateUserRequestDto dto) {
        return new ValidatableRestRequest<UpdateUserResponseDto>(
                RequestSpecs.authAsUser(token),
                Endpoint.UPDATE_CUSTOMER_PROFILE,
                ResponseSpecs.ok())
                .put(dto);
    }

    public ValidatableResponse updateCustomerProfile(UpdateUserRequestDto dto, RequestSpecification requestSpec, ResponseSpecification responseSpec) {
        return new RestRequest(
                requestSpec,
                Endpoint.UPDATE_CUSTOMER_PROFILE,
                responseSpec)
                .put(dto);
    }

    public List<CreateUserResponseDto> getAllUsers() {
        return new ValidatableRestRequest<CreateUserResponseDto>(
                RequestSpecs.authAsAdmin(),
                Endpoint.GET_ALL_USERS,
                ResponseSpecs.ok())
                .getAll();
    }

    public CreateUserResponseDto getUserById(long id) {
        List<CreateUserResponseDto> usersList = getAllUsers();
        return usersList.stream()
                .filter(user -> user.getId() == id)
                .findFirst()
                .orElseThrow(() -> new NoSuchElementException("User with id: " + id + " not found"));
    }

    public CreateUserResponseDto getUserByUsername(String username) {
        List<CreateUserResponseDto> usersList = getAllUsers();
        return usersList.stream()
                .filter(user -> user.getUsername().equals(username))
                .findFirst()
                .orElseThrow(() -> new NoSuchElementException("User with username: " + username + " not found"));
    }

    public ValidatableResponse deleteUserById(long id) {
        return new RestRequest(
                RequestSpecs.authAsAdmin(),
                Endpoint.DELETE_USER,
                ResponseSpecs.ok())
                .delete(id);
    }
}
