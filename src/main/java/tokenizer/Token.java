package tokenizer;

import lombok.*;

import java.util.Objects;
import java.util.Set;
import java.util.function.UnaryOperator;

@EqualsAndHashCode
@Getter
@ToString
@RequiredArgsConstructor(access = AccessLevel.PRIVATE)
public class Token {

    private final String token;

    public static Token of(String token) {
        return new Token(token);
    }

    Token map(UnaryOperator<String> mapper) {
        return Token.of(mapper.apply(token));
    }

    public Token toLowerCase() {
        return map(String::toLowerCase);
    }

    public boolean notIn(Set<String> strings) {
        return !strings.contains(token);
    }
}
