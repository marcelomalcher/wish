package br.rio.puc.lac.wish.web.front;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import javax.persistence.Query;

import br.rio.puc.lac.wish.analyzer.commons.Classification;
import br.rio.puc.lac.wish.analyzer.commons.Content;
import br.rio.puc.lac.wish.analyzer.commons.Location;
import br.rio.puc.lac.wish.analyzer.commons.Place;
import br.rio.puc.lac.wish.analyzer.utils.JSon;
import br.rio.puc.lac.wish.web.model.AnalysisFeedbackContent;

public class FeedbackFacade {

  private static EntityManagerFactory factory;
  static {
    factory = Persistence.createEntityManagerFactory("br.rio.puc.lac.wish.web");
  }

  private static FeedbackFacade instance;
  static {
    instance = new FeedbackFacade();
  }

  public static FeedbackFacade getInstance() {
    return instance;
  }

  /**
   * 
   * @param requestId
   * @return
   */
  public FeedbackContentRetrieval getAnalysisFeedbackRetrieval(String requestId) {

    EntityManager em = factory.createEntityManager();

    //Retrieving
    try {
      AnalysisFeedbackContent feedbackContent =
        em.createQuery(
          "select c from AnalysisFeedbackContent c WHERE c.requestFeedbackId = :id",
          AnalysisFeedbackContent.class).setParameter("id", requestId)
          .getSingleResult();

      //Returning list of contents      
      String sqlContentsQuery =
        "select c.id, c.socialId, c.content, c.creatorId, "
          + "c.location_latitude, c.location_longitude, "
          + "c.location_place_name, c.location_place_placeType, c.location_place_countryCode, "
          + "c.timestamp, c.timestamp_date, "
          + "c.tags "
          + "from contents c "
          + "inner join analysis_publication_event_content apec on (c.id = apec.idContent)"
          + "inner join analysis_publication_event ape on (apec.idPublicationEvent = ape.id) "
          + "where ape.id in (select apecB.idPublicationEvent "
          + "                   from analysis_publication_event_content apecB "
          + "                        inner join analysis_feedback_content afec on (apecB.id = afec.idPublicationEventContent) "
          + "                  where afec.request_feedback_id = ?requestId) "
          + "order by c.timestamp";

      //Querying...
      Query queryContents = em.createNativeQuery(sqlContentsQuery);
      queryContents.setParameter("requestId", requestId);

      //The main content
      Content<String, String> theContent = null;
      //List of contents
      List<Content<String, String>> contents =
        new ArrayList<Content<String, String>>();

      List<Object[]> results = queryContents.getResultList();
      for (Object[] result : results) {
        Integer rId = (Integer) result[0];
        String rSocialId = (String) result[1];
        String rContent = (String) result[2];
        String rCreatorId = (String) result[3];
        Double rLatitude = (Double) result[4];
        Double rLongitude = (Double) result[5];
        String rPlaceName = null;
        if (result[6] != null) {
          rPlaceName = (String) result[6];
        }
        String rPlaceType = null;
        if (result[7] != null) {
          rPlaceType = (String) result[7];
        }
        String rPlaceCountry = null;
        if (result[8] != null) {
          rPlaceCountry = (String) result[8];
        }
        Long rTimestamp = (Long) result[9];
        Date rDate = (Date) result[10];
        String rTags = (String) result[11];

        //
        Content<String, String> content = new Content<String, String>();
        content.setId(rSocialId);
        content.setContent(rContent);
        content.setCreatorId(rCreatorId);
        {
          Location location = new Location(rLatitude, rLongitude);
          content.setLocation(location);
          {
            Place place = new Place();
            place.setName(rPlaceName);
            place.setPlaceType(rPlaceType);
            place.setCountryCode(rPlaceCountry);
            location.setPlace(place);
          }
        }
        content.setTimestamp(rTimestamp);
        if (rTags != null) {
          content.setTags(rTags.split(","));
        }

        //if content id is equal to the one from publication event content
        if (rId == feedbackContent.getAnalysisPublicationEventContent()
          .getContentId()) {
          theContent = content;
          //Classification
          if (feedbackContent.getAnalysisPublicationEventContent()
            .getClassificationJson() != null) {
            String json =
              feedbackContent.getAnalysisPublicationEventContent()
                .getClassificationJson();
            Classification classification =
              JSon.getFromJSONString(json, Classification.class);
            theContent.setClassification(classification);
          }
        }
        else {
          contents.add(content);
        }
      }

      //
      FeedbackContentRetrieval retrieval =
        new FeedbackContentRetrieval(feedbackContent, theContent, contents);

      return retrieval;
    }
    catch (Exception e) {
      e.printStackTrace();
      return null;
    }
    finally {
      if (em != null && em.isOpen()) {
        em.close();
      }
    }
  }

  /**
   * 
   * @param retrievalId
   * @param updateValue
   * @param date
   * @param observations
   * @return
   */
  public boolean updateFeedbackContent(String retrievalId, boolean updateValue,
    Date date, String observations) {
    EntityManager em = factory.createEntityManager();

    //Retrieving
    try {
      AnalysisFeedbackContent feedbackContent =
        em.createQuery(
          "select c from AnalysisFeedbackContent c WHERE c.id = :id",
          AnalysisFeedbackContent.class).setParameter("id",
          Integer.parseInt(retrievalId)).getSingleResult();

      em.getTransaction().begin();

      feedbackContent.setVote(updateValue);
      feedbackContent.setDate(date);
      feedbackContent.setObservations(observations);

      em.merge(feedbackContent);

      em.getTransaction().commit();
    }
    catch (Exception e) {
      e.printStackTrace();
      return false;
    }
    finally {
      if (em != null && em.isOpen()) {
        em.close();
      }
    }
    return true;
  }
}
