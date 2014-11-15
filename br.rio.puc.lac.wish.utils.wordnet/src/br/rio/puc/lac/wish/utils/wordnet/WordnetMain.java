package br.rio.puc.lac.wish.utils.wordnet;

import edu.smu.tspell.wordnet.NounSynset;
import edu.smu.tspell.wordnet.Synset;
import edu.smu.tspell.wordnet.SynsetType;
import edu.smu.tspell.wordnet.WordNetDatabase;

public class WordnetMain {

  /**
   * Main entry point. The command-line arguments are concatenated together
   * (separated by spaces) and used as the word form to look up.
   */
  public static void main(String[] args) {

    System.setProperty("wordnet.database.dir", "C://Work//puc//wn31dict//dict");
    //    System
    //      .setProperty("wordnet.database.dir", "C://Work//puc//WordNet21//dict");

    String wordForm = "basketball";

    NounSynset nounSynset;
    NounSynset[] hyponyms;
    NounSynset[] hypernyms;

    WordNetDatabase database = WordNetDatabase.getFileInstance();

    // Get the synsets containing the wrod form
    Synset[] synsets = database.getSynsets(wordForm, SynsetType.NOUN);
    //  Display the word forms and definitions for synsets retrieved
    if (synsets.length > 0) {
      System.out.println("The following synsets contain '" + wordForm
        + "' or a possible base form " + "of that text:");
      for (int i = 0; i < synsets.length; i++) {
        System.out.println("");
        String[] wordForms = synsets[i].getWordForms();
        for (int j = 0; j < wordForms.length; j++) {
          System.out.print((j > 0 ? ", " : "") + wordForms[j]);
        }
        System.out.println(": " + synsets[i].getDefinition());

        //Hyponyms
        nounSynset = (NounSynset) (synsets[i]);
        hyponyms = nounSynset.getHyponyms();
        System.out.println(nounSynset.getWordForms()[0] + ": "
          + nounSynset.getDefinition() + " has " + hyponyms.length
          + " hyponyms");
        for (int k = 0; k < hyponyms.length; k++) {
          System.out.println("-" + hyponyms[k]);
        }

        //Hypernims
        nounSynset = (NounSynset) (synsets[i]);
        hypernyms = nounSynset.getHypernyms();
        System.out.println(nounSynset.getWordForms()[0] + ": "
          + nounSynset.getDefinition() + " has " + hypernyms.length
          + " hyponyms");
        for (int k = 0; k < hypernyms.length; k++) {
          System.out.println("-" + hypernyms[k]);
        }
      }
    }
    else {
      System.err.println("No synsets exist that contain " + "the word form '"
        + wordForm + "'");
    }

    //
    String[] baseForms =
      database.getBaseFormCandidates(wordForm, SynsetType.NOUN);
    for (String base : baseForms) {
      System.out.println("");
      System.out.print(base);
    }
  }
}
