import components.CryptoUtil;
import java.nio.charset.StandardCharsets;
import java.nio.file.*;

public class ConfigEncrypter {
    public static void main(String[] args) {
        try {
            // Define the file path
            String appPath = System.getProperty("user.dir");
            String configfilepath = appPath + "/configuration/"+"allowed_configurations.vgenv";
            Path filePath = Paths.get(configfilepath);
            System.out.println("Reading from: " + filePath.toAbsolutePath());

            // Check if file exists
            if (!Files.exists(filePath)) {
                System.out.println("Error: File does not exist!");
                return;
            }

            // Read the plain text configuration file
            String plainText = new String(Files.readAllBytes(filePath), StandardCharsets.UTF_8);
            System.out.println("Original Content:\n" + plainText);

            // Encrypt the content
            String encryptedContent = CryptoUtil.encrypt(plainText);
            System.out.println("Encrypted Content:\n" + encryptedContent);

            // Prepend a marker to indicate that the file is encrypted
            encryptedContent = "ENCRYPTED:" + encryptedContent;

            // Write back the encrypted content (force overwrite)
            Files.write(filePath, encryptedContent.getBytes(StandardCharsets.UTF_8),
                    StandardOpenOption.CREATE, StandardOpenOption.TRUNCATE_EXISTING);

            System.out.println("Configuration file encrypted successfully.");

            // Read back the file to verify if it was written correctly
            String newContent = new String(Files.readAllBytes(filePath), StandardCharsets.UTF_8);
            System.out.println("Written File Content:\n" + newContent);

            // Verify that encryption actually changed the file
            if (plainText.equals(newContent)) {
                System.out.println(" Warning: The file content has NOT changed. Something is wrong.");
            } else {
                System.out.println(" File content successfully updated.");
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
