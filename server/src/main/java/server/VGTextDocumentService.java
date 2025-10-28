package server;

import org.eclipse.lsp4j.services.TextDocumentService;
import org.eclipse.lsp4j.*;
import org.eclipse.lsp4j.jsonrpc.messages.Either;
import java.util.concurrent.CompletableFuture;
import java.util.List;
import components.*;

public class VGTextDocumentService implements TextDocumentService {

    @Override
    public void didOpen(DidOpenTextDocumentParams params) {
        publishDiagnostics(params.getTextDocument().getUri(), params.getTextDocument().getText());
    }

    @Override
    public void didChange(DidChangeTextDocumentParams params) {
        StringBuilder text = new StringBuilder();
        for (TextDocumentContentChangeEvent change : params.getContentChanges()) {
            text.append(change.getText());
        }
        publishDiagnostics(params.getTextDocument().getUri(), text.toString());
    }

    @Override
    public void didClose(DidCloseTextDocumentParams params) {
        // Clear diagnostics on close
        PublishDiagnosticsParams diagnostics = new PublishDiagnosticsParams();
        diagnostics.setUri(params.getTextDocument().getUri());
        diagnostics.setDiagnostics(new java.util.ArrayList<>());
        sendDiagnostics(diagnostics);
    }

    @Override
    public void didSave(DidSaveTextDocumentParams params) {
        // Re-run diagnostics on save
        publishDiagnostics(params.getTextDocument().getUri(), params.getText());
    }

    private void publishDiagnostics(String uri, String text) {
        java.util.List<Diagnostic> diagnostics = new java.util.ArrayList<>();
        try {
            // Use the VG Interpreter to analyze code
            Interpreter interpreter = new Interpreter("");
            interpreter.interpret(text);
        } catch (ErrorHandler.VGException e) {
            Diagnostic diagnostic = new Diagnostic();
            diagnostic.setSeverity(DiagnosticSeverity.Error);
            diagnostic.setMessage(e.getMessage());
            diagnostic.setSource("vg-lang");
            Range range = new Range(
                new Position(Math.max(e.getLine()-1,0), Math.max(e.getColumn()-1,0)),
                new Position(Math.max(e.getLine()-1,0), Math.max(e.getColumn(),0))
            );
            diagnostic.setRange(range);
            diagnostics.add(diagnostic);
        } catch (Exception e) {
            Diagnostic diagnostic = new Diagnostic();
            diagnostic.setSeverity(DiagnosticSeverity.Error);
            diagnostic.setMessage("Parse error: " + e.getMessage());
            diagnostic.setSource("vg-lang");
            diagnostic.setRange(new Range(
                new Position(0,0),
                new Position(0,1)
            ));
            diagnostics.add(diagnostic);
        }
        PublishDiagnosticsParams diagnosticsParams = new PublishDiagnosticsParams();
        diagnosticsParams.setUri(uri);
        diagnosticsParams.setDiagnostics(diagnostics);
        sendDiagnostics(diagnosticsParams);
    }

    private void sendDiagnostics(PublishDiagnosticsParams diagnosticsParams) {
        // This would typically send to the language client
        // Implementation depends on how the server is connected
    }

    @Override
    public CompletableFuture<Hover> hover(HoverParams params) {
        return CompletableFuture.completedFuture(null);
    }

    @Override
    public CompletableFuture<Either<List<CompletionItem>, CompletionList>> completion(CompletionParams params) {
        return CompletableFuture.completedFuture(Either.forRight(new CompletionList(false, java.util.Collections.emptyList())));
    }
}
