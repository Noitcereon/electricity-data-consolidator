package me.noitcereon.external.api.orchestration;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import static org.junit.jupiter.api.Assertions.*;

import java.util.Optional;

class ApiCallResultTest {

    @ParameterizedTest
    @ValueSource(ints = {200, 204, 299})
    void givenSuccessStatusCode_WhenCheckingForSuccess_ThenReturnTrue(int httpStatusCode) {
        ApiCallResult<String> apiCallResult = new ApiCallResult<>(null, Optional.empty(), null, httpStatusCode);

        boolean isSuccess = apiCallResult.isSuccess();

        assertTrue(isSuccess, "Status code " + httpStatusCode + " should be considered successful");
    }

    @ParameterizedTest
    @ValueSource(ints = {199, 300})
    void givenFailureStatusCode_WhenCheckingForSuccess_ThenReturnFalse(int httpStatusCode) {
        ApiCallResult<String> apiCallResult = new ApiCallResult<>(null, Optional.empty(), null, httpStatusCode);

        boolean isSuccess = apiCallResult.isSuccess();

        assertFalse(isSuccess, "Status code " + httpStatusCode + " should not be considered successful");
    }

}