import components.DocGenerator;

import java.io.IOException;
import java.io.File;

public class VGDocGenerator {
    public static void main(String[] args) {
        if (args.length < 2) {
            showUsage();
            System.exit(1);
        }

        String inputPath = args[0];
        String outputDir = args[1];

        try {
            DocGenerator docGen = new DocGenerator(outputDir);

            File inputFile = new File(inputPath);
            if (inputFile.isDirectory()) {

                docGen.generateProjectDocs(inputPath);
            } else if (inputFile.isFile()) {

                docGen.generateFileDoc(inputPath);
            } else {
                System.err.println("Error: Input path does not exist: " + inputPath);
                showUsage();
                System.exit(1);
            }
        } catch (IOException e) {
            System.err.println("Error generating documentation: " + e.getMessage());
            e.printStackTrace();
        }
    }

    private static void showUsage() {
        System.out.println("Usage:");
        System.out.println("  For a single file:  VGDocGen <file-path> <output-directory>");
        System.out.println("  For entire project: VGDocGen <project-directory> <output-directory>");
        System.out.println();
        System.out.println("Examples:");
        System.out.println("  VGDocGen libraries/Guilibrary.vglib ./docs");
        System.out.println("  VGDocGen projects/calculator.vg ./docs");
        System.out.println("  VGDocGen . ./docs");
    }
} 