package pl.codehouse.commons;

import reactor.core.publisher.Mono;

class TestApplicableCommand implements ApplicableCommand<ActionEvent, Boolean> {

    @Override
    public boolean isApplicable(ActionEvent actionEvent) {
        return actionEvent instanceof TestActionEvent;
    }

    @Override
    public Mono<ExecutionResult<Boolean>> execute(ActionEvent context) {
        var testActionEvent = (TestActionEvent) context ;

        return Mono.just(ExecutionResult.success(testActionEvent.input()));
    }
}