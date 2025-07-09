import components.CryptoUtil;
import java.nio.charset.StandardCharsets;
import java.nio.file.*;

public class ConfigEncrypter {
    public static void main(String[] args) {
        try {
            String appPath = System.getProperty("user.dir");
            String configfilepath = appPath + "/configuration/"+"allowed_configurations.vgenv";
            Path filePath = Paths.get(configfilepath);
            System.out.println("Reading from: " + filePath.toAbsolutePath());

            if (!Files.exists(filePath)) {
                System.out.println("Error: File does not exist!");
                return;
            }

            String plainText = new String(Files.readAllBytes(filePath), StandardCharsets.UTF_8);
            System.out.println("Original Content:\n" + plainText);

            String encryptedContent = CryptoUtil.encrypt(plainText);
            System.out.println("Encrypted Content:\n" + encryptedContent);

            encryptedContent = "ENCRYPTED:" + encryptedContent;

            Files.write(filePath, encryptedContent.getBytes(StandardCharsets.UTF_8),
                    StandardOpenOption.CREATE, StandardOpenOption.TRUNCATE_EXISTING);

            System.out.println("Configuration file encrypted successfully.");

            String newContent = new String(Files.readAllBytes(filePath), StandardCharsets.UTF_8);
            System.out.println("Written File Content:\n" + newContent);

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
