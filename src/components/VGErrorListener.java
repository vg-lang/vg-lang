package components;

import org.antlr.v4.runtime.*;
import org.antlr.v4.runtime.atn.ATN;
import org.antlr.v4.runtime.atn.ATNState;
import org.antlr.v4.runtime.misc.IntervalSet;
import org.antlr.v4.runtime.misc.ParseCancellationException;

public class VGErrorListener extends BaseErrorListener {

    @Override
    public void syntaxError(Recognizer<?, ?> recognizer,
                            Object offendingSymbol,
                            int line, int charPositionInLine,
                            String msg,
                            RecognitionException e) {

        Token token = (Token) offendingSymbol;
        String errorMessage = formatSyntaxErrorMessage(recognizer, token, msg);

        ErrorHandler.reportSyntaxError(token, errorMessage);

        throw new ParseCancellationException("Syntax error");
    }

    private String formatSyntaxErrorMessage(Recognizer<?, ?> recognizer,
                                            Token offendingToken,
                                            String msg) {
        String errorMsg = msg;

        if (offendingToken != null && offendingToken.getType() != Token.EOF) {
            String unexpectedInput = offendingToken.getText();
            errorMsg = "Unexpected '" + unexpectedInput + "': " + msg;

            if (recognizer instanceof Parser) {
                Parser parser = (Parser) recognizer;
                ATN atn = parser.getATN();
                ATNState state = atn.states.get(parser.getState());

                if (state != null) {
                    IntervalSet expectedTokens = parser.getExpectedTokens();
                    if (!expectedTokens.isNil()) {
                        errorMsg += "\nExpected one of: ";

                        String[] tokenNames = parser.getTokenNames();
                        boolean first = true;

                        for (int tokenType : expectedTokens.toList()) {
                            if (!first) errorMsg += ", ";
                            first = false;

                            if (tokenType < tokenNames.length) {
                                String tokenName = tokenNames[tokenType];
                                if (tokenName.startsWith("'") && tokenName.endsWith("'")) {
                                    errorMsg += tokenName;
                                } else {
                                    errorMsg += "<" + tokenName.toLowerCase() + ">";
                                }
                            }
                        }
                    }
                }
            }
        }

        return errorMsg;
    }
}