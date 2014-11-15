package br.rio.puc.lac.social.analyzer.execution.job.semantic.classifier.weka.lucenetoarff;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Writer;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.TreeMap;

import org.apache.lucene.document.Document;
import org.apache.lucene.document.FieldSelector;
import org.apache.lucene.document.FieldSelectorResult;
import org.apache.lucene.index.CorruptIndexException;
import org.apache.lucene.index.IndexReader;
import org.apache.lucene.index.Term;
import org.apache.lucene.index.TermEnum;
import org.apache.lucene.index.TermFreqVector;
import org.apache.lucene.store.Directory;

public class LuceneToArff {

  /**
   * 
   * @param directory
   * @param normalizedFieldLengths
   * @param outputArfFile
   * @param idField
   * @param acceptedFields
   * @throws CorruptIndexException
   * @throws IOException
   */
  public static void writeArff(Directory directory,
    Map<String, Double> normalizedFieldLengths, File outputArfFile,
    String idField, String... acceptedFields) throws CorruptIndexException,
    IOException {
    Coordinator coordinator = new Coordinator(normalizedFieldLengths);
    TermSelector termSelector = new TermSelector();
    IndexReader reader = IndexReader.open(directory);
    Writer writer = new BufferedWriter(new FileWriter(outputArfFile));
    LuceneToArff.writeArff(writer, reader, coordinator, termSelector, idField,
      acceptedFields);
    writer.close();
  }

  /**
   * 
   * 
   * @param writer
   * @param reader
   * @param coordinator
   * @param termSelector
   * @param idField
   * @param acceptedFields
   * @throws CorruptIndexException
   * @throws IOException
   */
  public static void writeArff(Writer writer, IndexReader reader,
    Coordinator coordinator, TermSelector termSelector, String idField,
    String... acceptedFields) throws CorruptIndexException, IOException {
    Set<String> acceptedFieldsSet =
      new HashSet<String>(Arrays.asList(acceptedFields));
    writer.write("@RELATION " + reader.directory() + "\n");
    Map<Term, Integer> termToIndex =
      writeHeader(reader, acceptedFieldsSet, termSelector, writer, idField);
    writeData(reader, acceptedFields, termToIndex, writer, coordinator,
      termSelector, idField);
  }

  /**
   * in order to make an arff with only the last documents added that will also
   * get removed after the classifier says into which class the test documents
   * fall
   * 
   * it was supposed to work like" 1. add to the index the test documents 2.
   * write the atff only with the test documents 3. remove these documents from
   * the index 4. lay back and be impressed
   */
  public static void writeArffSelectedDocs(List<Integer> docnoList,
    Writer writer, IndexReader reader, Coordinator coordinator,
    TermSelector termSelector, String idField, String... acceptedFields)
    throws CorruptIndexException, IOException {
    Set<String> acceptedFieldsSet =
      new HashSet<String>(Arrays.asList(acceptedFields));
    writer.write("@RELATION " + reader.directory() + "\n");
    Map<Term, Integer> termToIndex =
      writeHeader(reader, acceptedFieldsSet, termSelector, writer, idField);
    writeSelectedDocuments(docnoList, reader, acceptedFields, termToIndex,
      writer, coordinator, termSelector, idField);
  }

  private static void writeSelectedDocuments(List<Integer> docnrs,
    IndexReader reader, String[] acceptedFields,
    Map<Term, Integer> termToIndex, Writer out, Coordinator coordinator,
    TermSelector termSelector, final String idField)
    throws CorruptIndexException, IOException {

    out.write('\n');
    out.write("@DATA\n");
    Term[] acceptedTerms = new Term[acceptedFields.length];
    FieldSelector idSelector = new FieldSelector() {
      @Override
      public FieldSelectorResult accept(String fieldName) {
        if (idField.equals(fieldName)) {
          return FieldSelectorResult.LOAD_AND_BREAK;
        }
        else {
          return FieldSelectorResult.NO_LOAD;
        }
      }
    };
    int idIndex = termToIndex.get(new Term(idField, idField));
    for (int i = 0; i < acceptedFields.length; i++) {
      acceptedTerms[i] = new Term(acceptedFields[i]);
    }
    for (int docID : docnrs) {
      out.write("{");
      Map<Integer, Double> coordinates = new TreeMap<Integer, Double>();
      for (Term term : acceptedTerms) {
        TermFreqVector v = reader.getTermFreqVector(docID, term.field());
        // for documents whose field does not contain any terms, the
        // termFreqVector is null
        if (v != null) {
          int[] tfs = v.getTermFrequencies();
          String[] termTexts = v.getTerms();
          for (int i = 0; i < tfs.length; i++) {

            Term thisTerm = term.createTerm(termTexts[i]);
            if (termSelector.acceptTerm(thisTerm, reader)) {
              double coordinate =
                coordinator.coordinate(thisTerm.field(), tfs[i], tfs.length,
                  reader.docFreq(thisTerm), reader.numDocs());
              coordinates.put(termToIndex.get(thisTerm), coordinate);
            }
          }
        }
      }
      boolean first = true;

      for (Entry<Integer, Double> coordinate : coordinates.entrySet()) {
        double value = coordinate.getValue();
        if (value != 0.0) {
          if (first) {
            first = false;
          }
          else {
            out.write(", ");
          }
          out.write(coordinate.getKey() + " " + value);
        }
      }
      // write id
      Document doc = reader.document(docID, idSelector);
      String id = doc.get(idField);
      if (!first) {
        out.write(", ");
      }
      out.write(idIndex + " '" + id + "'");
      out.write("}\n");
    }

  }

  private static void writeData(IndexReader reader, String[] acceptedFields,
    Map<Term, Integer> termToIndex, Writer out, Coordinator coordinator,
    TermSelector termSelector, final String idField)
    throws CorruptIndexException, IOException {
    out.write('\n');
    out.write("@DATA\n");
    Term[] acceptedTerms = new Term[acceptedFields.length];
    FieldSelector idSelector = new FieldSelector() {
      @Override
      public FieldSelectorResult accept(String fieldName) {
        if (idField.equals(fieldName)) {
          return FieldSelectorResult.LOAD_AND_BREAK;
        }
        else {
          return FieldSelectorResult.NO_LOAD;
        }
      }
    };
    int idIndex = termToIndex.get(new Term(idField, idField));
    for (int i = 0; i < acceptedFields.length; i++) {
      acceptedTerms[i] = new Term(acceptedFields[i]);
    }
    for (int docID = 0; docID < reader.maxDoc(); docID++) {
      out.write("{");
      Map<Integer, Double> coordinates = new TreeMap<Integer, Double>();
      for (Term term : acceptedTerms) {
        TermFreqVector v = reader.getTermFreqVector(docID, term.field());
        // for documents whose field does not contain any terms, the
        // termFreqVector is null
        if (v != null) {
          int[] tfs = v.getTermFrequencies();
          String[] termTexts = v.getTerms();
          for (int i = 0; i < tfs.length; i++) {

            Term thisTerm = term.createTerm(termTexts[i]);
            if (termSelector.acceptTerm(thisTerm, reader)) {
              double coordinate =
                coordinator.coordinate(thisTerm.field(), tfs[i], tfs.length,
                  reader.docFreq(thisTerm), reader.numDocs());
              coordinates.put(termToIndex.get(thisTerm), coordinate);
            }
          }
        }
      }
      boolean first = true;

      for (Entry<Integer, Double> coordinate : coordinates.entrySet()) {
        double value = coordinate.getValue();
        if (value != 0.0) {
          if (first) {
            first = false;
          }
          else {
            out.write(", ");
          }
          out.write(coordinate.getKey() + " " + value);
        }
      }
      // write id
      Document doc = reader.document(docID, idSelector);
      String id = doc.get(idField);
      if (!first) {
        out.write(", ");
      }
      out.write(idIndex + " '" + id + "'");
      out.write("}\n");
    }
  }

  private static Map<Term, Integer> writeHeader(IndexReader reader,
    Set<String> acceptedFieldsSet, TermSelector termSelector, Writer out,
    String idField) throws IOException {
    Map<Term, Integer> qualifiedTermToIndex = new HashMap<Term, Integer>();
    int termIndex = 0;
    TermEnum terms = reader.terms();
    while (terms.next()) {
      Term term = terms.term();
      String field = term.field();
      // field is accepted
      if (acceptedFieldsSet.contains(field)
        && termSelector.acceptTerm(term, reader)) {
        String qualifiedTerm = qualifyTerm(field, term.text());
        out.write("@ATTRIBUTE " + qualifiedTerm + " numeric");
        Integer index = qualifiedTermToIndex.get(term);
        if (index == null) {
          index = termIndex++;
          qualifiedTermToIndex.put(term, index);
        }
        out.write("\t% " + index.toString() + "\n");
      }
    }
    out.write("@ATTRIBUTE " + idField + " string\t% ID");
    qualifiedTermToIndex.put(new Term(idField, idField), termIndex++);
    return qualifiedTermToIndex;
  }

  private static String qualifyTerm(String field, String term) {
    return field + ":"
      + term.replace(' ', '_').replace(',', '_').replace('\'', '_');
  }

}
