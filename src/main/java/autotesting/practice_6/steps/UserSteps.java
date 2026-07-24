package autotesting.practice_6.steps;

import autotesting.practice_6.models.request.CreateUserRequestDto;
import autotesting.practice_6.models.request.UpdateUserRequestDto;
import autotesting.practice_6.models.response.CreateUserResponseDto;
import autotesting.practice_6.models.response.UpdateUserResponseDto;
import autotesting.practice_6.requests.Endpoint;
import autotesting.practice_6.requests.RestRequest;
import autotesting.practice_6.requests.ValidatableRestRequest;
import autotesting.practice_6.specs.RequestSpecs;
import autotesting.practice_6.specs.ResponseSpecs;
import autotesting.practice_6.supports.CleanupManager;
import io.restassured.response.ValidatableResponse;
import io.restassured.specification.RequestSpecification;
import io.restassured.specification.ResponseSpecification;

import java.util.List;
import java.util.NoSuchElementException;

import static autotesting.practice_6.testdata.UserData.generateRandomUserDto;

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

        cleanupManager.register(
                () -> deleteUser(createdUser.getId())
        );

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
        CreateUserRequestDto userDto = generateRandomUserDto();

        CreateUserResponseDto createdUser = new ValidatableRestRequest<CreateUserResponseDto>(
                RequestSpecs.authAsAdmin(),
                Endpoint.CREATE_USER,
                ResponseSpecs.created())
                .post(userDto);

        cleanupManager.register(
                () -> deleteUser(createdUser.getId())
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

    public ValidatableResponse deleteUser(long id) {
        return new RestRequest(
                RequestSpecs.authAsAdmin(),
                Endpoint.DELETE_USER,
                ResponseSpecs.ok())
                .delete(id);
    }
}
