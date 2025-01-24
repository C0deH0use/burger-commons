package pl.codehouse.commons;

import static org.assertj.core.api.AssertionsForInterfaceTypes.assertThat;
import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

class ApplicableCommandTest {

    @Test
    @DisplayName("should validate is action type applicable when executing Applicable Command")
    void Should_ValidateIsActionTypeApplicable_When_ExecutingApplicableCommand() {
        // given
        ApplicableCommand<ActionEvent, Boolean> command = new TestApplicableCommand();
        ActionEvent actionEvent = new TestActionEvent(true);

        // when
        boolean isApplicable = command.isApplicable(actionEvent);

        // then
        assertThat(isApplicable).isTrue();
    }

    @Test
    @DisplayName("should not validate is action type applicable when executing Applicable Command")
    void Should_NotValidateIsActionTypeApplicable_When_ExecutingApplicableCommand() {
        // given
        ApplicableCommand<ActionEvent, Boolean> command = new TestApplicableCommand();
        ActionEvent actionEvent = new NonApplicableActionEvent();

        // when
        boolean isApplicable = command.isApplicable(actionEvent);

        // then
        assertThat(isApplicable).isFalse();
    }

    @Test
    @DisplayName("should execute command when passing applicable event")
    void Should_ExecuteCommand_When_PassingApplicableEvent() {
        // given
        ApplicableCommand<ActionEvent, Boolean> command = new TestApplicableCommand();
        ActionEvent actionEvent = new TestActionEvent(true);

        // when
        Mono<ExecutionResult<Boolean>> resultMono = command.execute(actionEvent);

        // then
        StepVerifier.create(resultMono)
                .assertNext(result -> {
                    assertThat(result.isSuccess()).isTrue();
                    assertThat(result.handle()).isTrue();
                })
                .verifyComplete();
    }


    private record NonApplicableActionEvent() implements ActionEvent {}
}