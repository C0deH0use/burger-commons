package pl.codehouse.commons;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;

import org.apache.commons.lang3.StringUtils;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

class CommandTest {

    @Test
    @DisplayName("should return success when command executed")
    void Should_ReturnSuccess_When_CommandExecuted() {
        // given
        String requestData = "RequestData";
        MockCommand mockCommand = new MockCommand();
        Context<String> ctx = new Context<String>(requestData);

        // when
        Mono<ExecutionResult<String>> resultMono = mockCommand.execute(ctx);

        // then
        StepVerifier.create(resultMono)
                .assertNext(result -> {
                    assertThat(result.isSuccess()).isTrue();
                    assertThat(result.handle())
                            .contains("Finished Command with: ")
                            .contains(requestData)
                    ;
                })
                .verifyComplete();
    }

    @Test
    @DisplayName("should return failure when command has no request")
    void Should_ReturnFailure_When_CommandHasNoRequest() {
        // given
        MockCommand mockCommand = new MockCommand();
        Context<String> ctx = new Context<String>(StringUtils.SPACE);

        // when
        Mono<ExecutionResult<String>> resultMono = mockCommand.execute(ctx);

        // then
        StepVerifier.create(resultMono)
                .assertNext(result -> {
                    assertThat(result.isSuccess()).isFalse();
                    assertThatThrownBy(result::handle)
                            .isInstanceOf(RuntimeException.class)
                            .hasMessage("Empty request")
                    ;
                })
                .verifyComplete();
    }
}