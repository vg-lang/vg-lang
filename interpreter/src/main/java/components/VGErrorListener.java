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
        
        if (msg.contains("token recognition error") && msg.contains("\"")) {
            if (msg.contains("\\")) {
                String invalidEscape = extractInvalidEscape(msg);
                throw new ErrorHandler.VGSyntaxException(
                    "Invalid escape sequence in string: \\" + invalidEscape, 
                    line, charPositionInLine);
            } else {
                throw new ErrorHandler.VGSyntaxException(
                    "String literal is missing closing quote", line, charPositionInLine);
            }
        }
        
        if (msg.contains("extraneous input ';'")) {
            throw new ErrorHandler.VGSyntaxException(
                "Unexpected semicolon", line, charPositionInLine);
        }
        
        if (msg.contains("missing ';'")) {
            throw new ErrorHandler.VGSyntaxException(
                "Missing semicolon at end of statement", line, charPositionInLine);
        }
        

        throw new ErrorHandler.VGSyntaxException(msg, line, charPositionInLine);
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

    private String extractInvalidEscape(String errorMsg) {
        int backslashPos = errorMsg.indexOf('\\');
        if (backslashPos >= 0 && backslashPos + 1 < errorMsg.length()) {
            return errorMsg.substring(backslashPos + 1, backslashPos + 2);
        }
        return "?";
    }
}