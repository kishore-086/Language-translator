import java.util.Scanner;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.Locale;

public class LanguageTranslator {

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        System.out.print("Enter the text to translate: ");
        String textToTranslate = scanner.nextLine();

        System.out.print("Enter the target language: ");
        String targetLanguageName = scanner.nextLine();

        scanner.close();

        try {
            Locale[] locales = Locale.getAvailableLocales();
            String targetLanguageCode = "";
            for (Locale locale : locales) {
                if (targetLanguageName.equalsIgnoreCase(locale.getDisplayLanguage())) {
                    targetLanguageCode = locale.getLanguage();
                    break;
                }
            }

            if (targetLanguageCode.isEmpty()) {
                System.out.println("Invalid target language name.");
                return;
            }

            String apiUrl = "https://translate.googleapis.com/translate_a/single?client=gtx&sl=auto&tl="
                    + targetLanguageCode + "&dt=t&q=" + URLEncoder.encode(textToTranslate, "UTF-8");
            URL url = new URL(apiUrl);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("GET");
            BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream(),"UTF-8"));
            String inputLine;
            StringBuilder response = new StringBuilder();

            while ((inputLine = reader.readLine()) != null) {
                response.append(inputLine);
            }
            reader.close();
            
            String translatedText = parseGoogleTranslateResponse(response.toString());
            System.out.println("Original text: " + textToTranslate);
            System.out.println("Translated text: " + translatedText);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static String parseGoogleTranslateResponse(String jsonResponse) {
        String[] parts = jsonResponse.split("\"");

        if (parts.length >= 2) {
            return parts[1];
        }
        return "Translation not found";
    }
}
