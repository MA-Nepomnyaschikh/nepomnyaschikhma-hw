package autotesting.practice_3.utils;

import autotesting.practice_3.contract.models.response.AccountResponseDto;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.Objects;

public final class AccountUtils {

    private AccountUtils() {}

    public static AccountResponseDto findById(List<AccountResponseDto> list, int id) {
        return list.stream()
                .filter(acc -> Objects.equals(acc.getId(), id))
                .findFirst()
                .orElseThrow(() -> new NoSuchElementException("Account with id: " + id + " not found"));
    }
}
