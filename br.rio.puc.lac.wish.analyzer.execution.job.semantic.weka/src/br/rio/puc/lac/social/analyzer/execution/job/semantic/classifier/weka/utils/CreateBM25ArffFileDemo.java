package br.rio.puc.lac.social.analyzer.execution.job.semantic.classifier.weka.utils;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Writer;
import java.util.Map;

import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.snowball.SnowballAnalyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import org.apache.lucene.document.Field.Index;
import org.apache.lucene.document.Field.Store;
import org.apache.lucene.document.Field.TermVector;
import org.apache.lucene.index.CorruptIndexException;
import org.apache.lucene.index.IndexReader;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.IndexWriter.MaxFieldLength;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.SimpleFSDirectory;
import org.apache.lucene.util.Version;

import br.rio.puc.lac.social.analyzer.execution.job.semantic.classifier.weka.luceneindexing.CollectionSimilarity;
import br.rio.puc.lac.social.analyzer.execution.job.semantic.classifier.weka.lucenetoarff.Coordinator;
import br.rio.puc.lac.social.analyzer.execution.job.semantic.classifier.weka.lucenetoarff.LuceneToArff;
import br.rio.puc.lac.social.analyzer.execution.job.semantic.classifier.weka.lucenetoarff.TermSelector;

public class CreateBM25ArffFileDemo {
  private static final File TMP_DIR = new File(System
    .getProperty("java.io.tmpdir"));

  public static void main(String[] args) throws IOException {
    System.out.println(System.getProperty("java.io.tmpdir"));
    File indexLocation = File.createTempFile("index", "", TMP_DIR);
    indexLocation.delete();
    Directory directory = new SimpleFSDirectory(indexLocation);
    Analyzer analyzer =
      new SnowballAnalyzer(Version.LUCENE_30, "English",
        StopWords.ENGLISH_STOPWORDS);
    IndexWriter indexWriter =
      new IndexWriter(directory, analyzer, true, MaxFieldLength.UNLIMITED);

    CollectionSimilarity collectionSimilarity = new CollectionSimilarity();
    indexWriter.setSimilarity(collectionSimilarity);

    String idField = "id";
    String textField = "body";
    String titleField = "title";

    addDocumentsToIndex(indexWriter, idField, textField, titleField);

    indexWriter.optimize();
    Map<String, Double> normalizedFieldLengths =
      CollectionSimilarity.getNormalizedFieldLengths(indexWriter.numDocs());

    indexWriter.close();

    File outputArfFile = File.createTempFile("bowFeatures", ".arff", TMP_DIR);
    Writer fileWriter = new BufferedWriter(new FileWriter(outputArfFile));
    IndexReader indexReader = IndexReader.open(directory);
    Coordinator coordinator = new Coordinator(normalizedFieldLengths);
    TermSelector termSelector = new TermSelector(2, Integer.MAX_VALUE);

    LuceneToArff.writeArff(fileWriter, indexReader, coordinator, termSelector,
      idField, titleField, textField);

    System.out.format("Wrote bag of word file to %s\n", outputArfFile);

    indexReader.close();
    fileWriter.close();

  }

  public static void addDocumentsToIndex(IndexWriter indexWriter,
    String idField, String textField, String titleField)
    throws CorruptIndexException, IOException {
    addDocument(
      indexWriter,
      idField,
      "01",
      titleField,
      "event impact - bag of words arffs for each event",
      textField,
      "Hi Julien,\n"
        + "\n"
        + "The arffs are in jobbare.kbs.uni-hannover.de/home/impact/arffs/bow/duc\n"
        + "and\n"
        + "jobbare.kbs.uni-hannover.de/home/impact/arffs/bow/trec\n"
        + "\n"
        + "They don't have tfidf as features, but only if the word is contained in the text or not.  That's what the filter already provided by weka was doing.\n"
        + "\n" + "Bests,\n" + "Mihai");

    addDocument(
      indexWriter,
      idField,
      "02",
      titleField,
      "RE:event impact - bag of words arffs for each event",
      textField,
      "Hi Mihai!\n"
        + "\n"
        + "Thanks looks good!\n"
        + "\n"
        + "Just one modif: the HOUR_DURATION doesn't have the correct data type:\n"
        + "it should a double value representing the number of hours since the\n"
        + "event began (can also be a number with decimals like 25.324). Have a\n"
        + "look in ./event_features/duc for examples.\n"
        + "\n"
        + "Also I moved your files to ./event_features/bow and committed it to\n"
        + "svn, please update them there as well. Also while you're at it could\n"
        + "you please change the names of the arff files and prepend \"duc_\" or\n"
        + "\"trec_\" as in ./event_features/duc ./event_features/trec ?\n"
        + "\n" + "Many thanks,\n" + "\n" + "Julien");
    addDocument(
      indexWriter,
      idField,
      "03",
      titleField,
      "Forksville Covered Bridge",
      textField,
      "The Forksville Covered Bridge is a Burr arch truss covered bridge over Loyalsock Creek in the borough of Forksville, Sullivan County, in the U.S. state of Pennsylvania. It was built in 1850 and is 152 feet 11 inches (46.6 m) in length. The bridge was placed on the National Register of Historic Places in 1980. The Forksville bridge is named for the borough it is in, which is named for its location at the confluence or \"forks\" of the Little Loyalsock and Loyalsock Creeks. The Forksville bridge is a Burr arch truss type, with a load-bearing arch sandwiching multiple vertical king posts, for strength and rigidity. The building of the Forksville bridge was supervised by the 18-year-old Sadler Rogers, who used his hand-carved model of the structure. It served as the site of a stream gauge from 1908 to 1913 and is still an official Pennsylvania state highway bridge. The United States Department of Transportation Federal Highway Administration uses it as the model of a covered bridge \"classic gable roof\", and it serves as the logo of a Pennsylvania insurance company. The bridge was restored in 1970 and 2004 and is still in use, with average daily traffic of 224 vehicles in 2009. Despite the restorations, as of 2009 the bridge structure's sufficiency rating on the National Bridge Inventory was only 17.7 percent and its condition was deemed \"basically intolerable requiring high priority of corrective action\".");
    addDocument(
      indexWriter,
      idField,
      "04",
      titleField,
      "Background",
      textField,
      "The first covered bridge in the United States was built in 1800 over the Schuylkill River in Philadelphia, Pennsylvania. According to Zacher, the first covered bridges of the Burr arch truss design were also built in the state. Pennsylvania is estimated to have once had at least 1,500 covered bridges and is believed to have had the most in the country between 1830 and 1875.[13] In 2001, Pennsylvania had more surviving historic covered bridges than any other state, with 221 remaining in 40 of its 67 counties.[7]\n"
        + "\n"
        + "Covered bridges were a transition between stone and metal bridges, the latter made of cast-iron or steel. In 19th-century Pennsylvania, lumber was an abundant resource for bridge construction,[13] but did not last long when exposed to the elements. The roof and enclosed sides of covered bridges protected the structural elements, allowing some of these bridges to survive for well over a century. A Burr arch truss consists of a load-bearing arch sandwiching multiple king posts, resulting in stronger and more rigid structure than one made of either element alone.[7]");
    addDocument(
      indexWriter,
      idField,
      "05",
      titleField,
      "Construction",
      textField,
      "Construction and description\n"
        + "\n"
        + "Although there were 30 covered bridges in Sullivan County in 1890, only five were left by 1954, and as of 2011 only three remain: Forksville, Hillsgrove, and Sonestown. All three are Burr arch truss covered bridges and were built in 1850.[3][7] The Forksville Covered Bridge was built for Sullivan County by Sadler Rogers (or Rodgers), a native of Forksville who was only 18 at the time. He hand-carved a model of the bridge before work began and used it to supervise construction.[3][14] Rogers built the Forksville and Hillsgrove bridges across Loyalsock Creek, with the latter about 5 miles (8.0 km) downstream of the former.[3][7] Although most sources do not list the builder of the Sonestown bridge, a 1997 newspaper article on the remaining Sullivan County covered bridges reported that Rodgers had designed it too.[15]\n"
        + "Bridge interior view showing Burr arches\n"
        + "\n"
        + "The Forksville Covered Bridge was added to the NRHP on July 24, 1980, in a Multiple Property Submission of seven Covered Bridges of Bradford, Sullivan and Lycoming Counties.[13] The 2009 National Bridge Inventory (NBI) lists the covered bridge as 152 feet 11 inches (46.6 m) long, with a roadway 12 feet 2 inches (3.7 m) wide, and a maximum load of 3.0 short tons (2.7 metric tons).[2] According to the NRHP, the bridge's \"road surface width\" is 15 feet (4.6 m),[3] which is only sufficient for a single lane of traffic.[2] As of 2011, each portal has a small sign reading \"1850 Sadler Rogers\" at the top, above a sign with the posted clearance height of 8.0 feet (2.4 m), and a \"No Trucks Allowed\" sign hanging below these.[16]\n"
        + "\n"
        + "The covered bridge rests on the original stone abutments, which have since been reinforced with concrete.[3] The bridge deck, which is now supported by steel beams, is made of \"very narrow crosswise planks\".[7] Wheel guards on the deck separate the roadway from the pedestrian walkways on either side and protect the sides, which are covered with vertical planks almost to the eaves.[3][7][17] The bridge has long, narrow windows with wooden shutters: the south side has four windows, and the north side has three. An opening between the eaves and the siding runs the length of the bridge on both sides.[7] The bridge is supported by a Burr arch truss of 16 panels, with wooden beams.[3] The gable roof is sheet metal[6][7] and is used as the model illustration of a \"classic gable roof\" for a covered bridge by the U.S. Department of Transportation Federal Highway Administration Turner-Fairbank Highway Research Center.[5]");
  }

  public static void addDocument(IndexWriter indexWriter, String idField,
    String id, String titleField, String title, String textField, String body)
    throws CorruptIndexException, IOException {
    Document doc = new Document();
    doc
      .add(new Field(idField, id, Store.YES, Index.NOT_ANALYZED, TermVector.NO));
    doc.add(new Field(titleField, title, Store.YES, Index.ANALYZED,
      TermVector.YES));
    doc.add(new Field(textField, body, Store.YES, Index.ANALYZED,
      TermVector.YES));

    indexWriter.addDocument(doc);
  }
}
