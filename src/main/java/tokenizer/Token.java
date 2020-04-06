package tokenizer;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.Objects;
import java.util.function.UnaryOperator;

@RequiredArgsConstructor
@EqualsAndHashCode
@Getter
public class Token {
    private final String token;

    public static Token of(String x) {
        return new Token(x);
    }

    Token map(UnaryOperator<String> mapper) {
        return Token.of(mapper.apply(token));
    }

    public Token toLowerCase() {
        return map(String::toLowerCase);
    }

    public boolean isEqual(String expression) {
        return Objects.equals(token, expression);
    }
}
