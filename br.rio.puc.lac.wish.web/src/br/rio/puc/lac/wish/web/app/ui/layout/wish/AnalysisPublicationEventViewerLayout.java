package br.rio.puc.lac.wish.web.app.ui.layout.wish;

import java.awt.geom.Point2D;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import javax.persistence.EntityManager;
import javax.persistence.Query;

import br.com.tecnoinf.aurora.app.AuroraApplication;
import br.com.tecnoinf.aurora.ui.AuroraTable;
import br.com.tecnoinf.aurora.ui.labels.format.H2;
import br.com.tecnoinf.aurora.ui.labels.format.H3;
import br.rio.puc.lac.wish.web.model.AnalysisPublicationEvent;
import br.rio.puc.lac.wish.web.model.AnalysisPublicationEventContent;

import com.vaadin.event.ItemClickEvent;
import com.vaadin.event.ItemClickEvent.ItemClickListener;
import com.vaadin.terminal.Resource;
import com.vaadin.terminal.ThemeResource;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Component;
import com.vaadin.ui.Embedded;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Table;
import com.vaadin.ui.Table.ColumnGenerator;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.themes.Reindeer;
import com.wildboar.vaadin.addon.googlemap.overlay.BasicMarker;
import com.wildboar.vaadin.addon.googlemap.server.GoogleMap;

public class AnalysisPublicationEventViewerLayout extends VerticalLayout {

  private AuroraApplication application;

  private AnalysisPublicationEvent event;

  private GoogleMap googleMap;

  private Map<Integer, BasicMarker> contentsMarkerMap;

  private String eventUrl, contentUrl, selectedUrl;

  /**
   * 
   * @param application
   * @param event
   */
  public AnalysisPublicationEventViewerLayout(AuroraApplication application,
    AnalysisPublicationEvent event) {
    this.application = application;
    //Event
    this.event = event;

    build();
  }

  /**
   * 
   */
  private void build() {
    setWidth("100%");
    setSpacing(true);
    setMargin(true);

    HorizontalLayout hLayout = new HorizontalLayout();
    hLayout.setWidth("100%");
    hLayout.setSpacing(true);

    //Map - Properties
    String apiKey =
      application.getApplicationConfiguration().getConfigurationValue(
        "AnalysisPublicationEventViewerLayout.apiKey");
    String zoom =
      application.getApplicationConfiguration().getConfigurationValue(
        "AnalysisPublicationEventViewerLayout.zoom");
    //    String width =
    //      application.getApplicationConfiguration().getConfigurationValue(
    //        "AnalysisPublicationEventViewerLayout.width");
    String height =
      application.getApplicationConfiguration().getConfigurationValue(
        "AnalysisPublicationEventViewerLayout.height");
    //googleMap = new GoogleMap(application, apiKey);
    googleMap = new GoogleMap(application);
    googleMap.setWidth("100%");
    googleMap.setHeight(height);
    googleMap.setZoom(Integer.valueOf(zoom));

    //Marker - Properties
    String eventIcon =
      application.getApplicationConfiguration().getConfigurationValue(
        "AnalysisPublicationEventViewerLayout.event_marker_icon");
    String contentIcon =
      application.getApplicationConfiguration().getConfigurationValue(
        "AnalysisPublicationEventViewerLayout.content_marker_icon");
    String selectedIcon =
      application.getApplicationConfiguration().getConfigurationValue(
        "AnalysisPublicationEventViewerLayout.selected_content_marker_icon");

    eventUrl = "VAADIN/themes/" + application.getTheme() + "/" + eventIcon;
    contentUrl = "VAADIN/themes/" + application.getTheme() + "/" + contentIcon;
    selectedUrl =
      "VAADIN/themes/" + application.getTheme() + "/" + selectedIcon;

    //Setting marker at center
    final BasicMarker marker =
      new BasicMarker(0L, new Point2D.Double(event.getLongitude(), event
        .getLatitude()), event.getSemantics());
    marker.setIconUrl(eventUrl);
    marker.setDraggable(false);
    googleMap.addMarker(marker);
    googleMap.setCenter(marker.getLatLng());

    hLayout.addComponent(googleMap);

    //Description
    VerticalLayout vDescLayout = new VerticalLayout();
    vDescLayout.setWidth("100%");
    vDescLayout.setStyleName(Reindeer.LAYOUT_WHITE);
    vDescLayout.setSpacing(true);
    vDescLayout.setMargin(true);

    Date date = new Date(event.getTimestamp());
    H2 semanticDescription = new H2(event.getSemantics());
    vDescLayout.addComponent(semanticDescription);
    H3 spatialDescription =
      new H3("Latitude: " + event.getLatitude() + " - Longitude: "
        + event.getLongitude());
    vDescLayout.addComponent(spatialDescription);

    //Button
    Button centerButton =
      new Button(
        application
          .getMessage("AnalysisPublicationEventViewerLayout.center_button_caption"));
    centerButton.setStyleName(Reindeer.BUTTON_SMALL);
    centerButton.addListener(new Button.ClickListener() {
      @Override
      public void buttonClick(ClickEvent event) {
        googleMap.setCenter(marker.getLatLng());
      }
    });
    vDescLayout.addComponent(centerButton);

    H3 temporalDescription =
      new H3("Time: " + SimpleDateFormat.getInstance().format(date));
    vDescLayout.addComponent(temporalDescription);

    //Votes
    vDescLayout.addComponent(getVoteLayout());

    hLayout.addComponent(vDescLayout);

    addComponent(hLayout);

    //Content's table
    addComponent(getTableOfContents());
  }

  private HorizontalLayout getVoteLayout() {
    HorizontalLayout voteLayout = new HorizontalLayout();
    voteLayout.setSpacing(true);

    //EntityManager
    EntityManager man =
      application.getJPAHandler().getEntityManagerFactory()
        .createEntityManager();
    //
    String sqlQuery =
      "select vote from analysis_publication_event_evaluation where idPublicationEvent = ?eventId";
    Query query = man.createNativeQuery(sqlQuery);
    query.setParameter("eventId", event.getId());

    //variables
    Integer up = 0;
    Integer down = 0;

    List<Boolean> results = query.getResultList();
    for (Boolean result : results) {
      if (result) {
        up++;
      }
      else {
        down++;
      }
    }

    //
    voteLayout.addComponent(new H3(up.toString()));
    voteLayout.addComponent(new Embedded(null, new ThemeResource(
      "images/feedback/up.png")));
    voteLayout.addComponent(new H3(down.toString()));
    voteLayout.addComponent(new Embedded(null, new ThemeResource(
      "images/feedback/down.png")));

    return voteLayout;
  }

  private AuroraTable getTableOfContents() {
    //
    contentsMarkerMap = new HashMap<Integer, BasicMarker>();

    //Table
    final AuroraTable table = new AuroraTable(application, null);
    table.setSelectable(true);
    table.setImmediate(true);
    table.setSizeFull();

    //Static properties
    table.addContainerProperty("id", Integer.class, null);
    table.addContainerProperty("contentId", Integer.class, null);
    table.addContainerProperty("creatorId", String.class, null);
    table.addContainerProperty("content", String.class, null);
    table.addContainerProperty("latitude", Double.class, null);
    table.addContainerProperty("longitude", Double.class, null);
    table.addContainerProperty("date", Date.class, null);
    table.addContainerProperty("voteUp", Integer.class, null);
    table.addContainerProperty("voteDown", Integer.class, null);
    table.addContainerProperty("feedback", Resource.class, null);
    table.addContainerProperty("feedbackObservations", String.class, null);
    //
    table.setColumnIcon("voteUp", new ThemeResource("images/feedback/up.png"));
    table.setColumnIcon("voteDown", new ThemeResource(
      "images/feedback/down.png"));

    //   
    table.setColumnReorderingAllowed(true);
    table.setColumnCollapsingAllowed(true);
    table.setRowHeaderMode(Table.ROW_HEADER_MODE_ICON_ONLY);
    table.setItemIconPropertyId("feedback");

    //
    table.addGeneratedColumn("feedbackIcon", new ColumnGenerator() {
      private static final long serialVersionUID = -8133822448459398254L;

      @Override
      public Component generateCell(Table source, final Object itemId,
        Object columnId) {
        Embedded icon = null;
        if (source.getContainerProperty(itemId, "feedback").getValue() != null) {
          icon =
            new Embedded(null, (Resource) source.getContainerProperty(itemId,
              "feedback").getValue());
        }
        return icon;
      }
    });

    //EntityManager
    EntityManager man =
      application.getJPAHandler().getEntityManagerFactory()
        .createEntityManager();

    //
    String sqlQuery =
      "select id, content, creatorId, location_latitude, location_longitude, timestamp_date "
        + "from contents c "
        + "where c.id = ?contentId order by timestamp_date";

    int i = 0;
    for (AnalysisPublicationEventContent c : event.getContents()) {
      //Querying...
      Query query = man.createNativeQuery(sqlQuery);
      query.setParameter("contentId", c.getContentId());

      //      
      List<Object[]> results = query.getResultList();
      if (results.size() > 0) {
        Object[] result = results.get(0);
        //
        int rId = ((Integer) result[0]);
        String rContent = (String) result[1];
        String rCreatorId = (String) result[2];
        Double rLatitude = (Double) result[3];
        Double rLongitude = (Double) result[4];
        Date rDate = (Date) result[5];

        //Vote
        String voteQuerySql =
          "select vote from analysis_publication_event_content_evaluation where idPublicationEventContent = ?eventContentId";
        Query voteQuery = man.createNativeQuery(voteQuerySql);
        voteQuery.setParameter("eventContentId", c.getId());
        //variables
        Integer up = 0;
        Integer down = 0;
        List<Boolean> voteResults = voteQuery.getResultList();
        for (Boolean voteResult : voteResults) {
          if (voteResult) {
            up++;
          }
          else {
            down++;
          }
        }

        //Retrieving feedback information
        Resource feedbackResource = null;
        String feedbackObservations = "";
        String feedbackQuerySql =
          "select date, vote, observations from analysis_feedback_content where idPublicationEventContent = ?eventContentId";
        Query feedbackQuery = man.createNativeQuery(feedbackQuerySql);
        feedbackQuery.setParameter("eventContentId", c.getId());
        List<Object[]> feedbackResults = feedbackQuery.getResultList();
        for (Object[] feedbackResult : feedbackResults) {
          if (feedbackResult[0] == null) {
            feedbackResource = new ThemeResource("images/feedback/zero.png");
          }
          else {
            boolean feedback = ((Boolean) feedbackResult[1]);
            feedbackObservations = ((String) feedbackResult[2]);
            if (feedback) {
              feedbackResource = new ThemeResource("images/feedback/pos.png");
            }
            else {
              feedbackResource = new ThemeResource("images/feedback/neg.png");
            }
          }
        }

        //
        table.addItem(
          new Object[] { ++i, rId, rCreatorId, rContent, rLatitude, rLongitude,
              rDate, up, down, feedbackResource, feedbackObservations }, i);

        //Marker
        BasicMarker marker =
          new BasicMarker((long) rId,
            new Point2D.Double(rLongitude, rLatitude), rContent);
        marker.setDraggable(false);
        marker.setIconUrl(contentUrl + i + ".png");
        contentsMarkerMap.put(i, marker);
        googleMap.addMarker(marker);
      }
    }

    man.close();

    table.setPageLength(i);

    //Defining visible columns...
    table.setVisibleColumns(new Object[] { "id", "contentId", "creatorId",
        "content", "latitude", "longitude", "date", "voteUp", "voteDown" });

    table.addListener(new ItemClickListener() {
      @Override
      public void itemClick(ItemClickEvent event) {
        int i = (Integer) event.getItemId();
        for (Entry<Integer, BasicMarker> entry : contentsMarkerMap.entrySet()) {
          entry.getValue().setIconUrl(contentUrl + entry.getKey() + ".png");
        }
        BasicMarker marker = contentsMarkerMap.get(i);
        marker.setIconUrl(selectedUrl + i + ".png");
        if (event.isDoubleClick()) {
          //Show details
        }
        googleMap.setCenter(marker.getLatLng());
      }
    });

    return table;
  }
}
