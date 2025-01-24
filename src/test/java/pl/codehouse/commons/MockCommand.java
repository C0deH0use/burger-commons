package pl.codehouse.commons;

import org.apache.commons.lang3.StringUtils;
import reactor.core.publisher.Mono;

class MockCommand implements Command<String, String> {

    @Override
    public Mono<ExecutionResult<String>> execute(Context<String> context) {
        if(StringUtils.isBlank(context.request())) {
            return Mono.just(ExecutionResult.failure(new RuntimeException("Empty request")));
        }

        return Mono.just(ExecutionResult.success("Finished Command with: " + context.request()));
    }
}