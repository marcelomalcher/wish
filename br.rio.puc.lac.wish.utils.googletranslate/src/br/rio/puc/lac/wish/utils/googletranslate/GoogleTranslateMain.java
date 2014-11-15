package br.rio.puc.lac.wish.utils.googletranslate;

import com.google.api.GoogleAPI;
import com.google.api.detect.Detect;
import com.google.api.detect.DetectResult;
import com.google.api.translate.Language;
import com.google.api.translate.Translate;

public class GoogleTranslateMain {
  public static void main(String[] args) throws Exception {
    // Set the HTTP referrer to your website address.
    GoogleAPI.setHttpReferrer("http://events.lac-rio.com");

    // Set the Google Translate API key
    // See: http://code.google.com/apis/language/translate/v2/getting_started.html
    GoogleAPI.setKey("AIzaSyBX8FBOQiQCLTmbcKgPp8BogIZ14jbMV2k");

    String text =
      "Manifestação no Rio de Janeiro em Botafogo contra o Sergio Cabral";

    DetectResult dR = Detect.execute(text);

    String translatedText = "";

    if (dR.getLanguage() == Language.PORTUGUESE) {
      translatedText =
        Translate.DEFAULT.execute(text, Language.PORTUGUESE, Language.ENGLISH);
    }
    else {
      translatedText =
        Translate.DEFAULT.execute("manifestation", Language.ENGLISH,
          Language.PORTUGUESE);
    }

    System.out.println(translatedText);

  }
}