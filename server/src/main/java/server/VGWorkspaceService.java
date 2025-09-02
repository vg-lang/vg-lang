package server;

import org.eclipse.lsp4j.services.WorkspaceService;
import org.eclipse.lsp4j.*;

public class VGWorkspaceService implements WorkspaceService {
    @Override
    public void didChangeConfiguration(DidChangeConfigurationParams params) {
        // Handle configuration changes
    }

    @Override
    public void didChangeWatchedFiles(DidChangeWatchedFilesParams params) {
        // Handle file changes
    }
}
