package br.rio.puc.lac.wish.web.results;

import java.awt.geom.Point2D;
import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import javax.persistence.EntityManager;
import javax.persistence.Query;

import br.com.tecnoinf.aurora.ui.labels.format.H2;
import br.com.tecnoinf.aurora.ui.labels.format.H3;
import br.com.tecnoinf.aurora.utils.jpacontainer.AuroraJPAContainerFactory;
import br.rio.puc.lac.wish.web.model.AnalysisPublication;
import br.rio.puc.lac.wish.web.model.AnalysisPublicationEvent;
import br.rio.puc.lac.wish.web.model.AnalysisPublicationEventContent;

import com.vaadin.addon.jpacontainer.EntityContainer;
import com.vaadin.addon.jpacontainer.EntityItem;
import com.vaadin.data.Item;
import com.vaadin.data.util.HierarchicalContainer;
import com.vaadin.event.ItemClickEvent;
import com.vaadin.event.ItemClickEvent.ItemClickListener;
import com.vaadin.terminal.ExternalResource;
import com.vaadin.terminal.ThemeResource;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Button.ClickListener;
import com.vaadin.ui.Component;
import com.vaadin.ui.CssLayout;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.HorizontalSplitPanel;
import com.vaadin.ui.Link;
import com.vaadin.ui.TabSheet;
import com.vaadin.ui.Table;
import com.vaadin.ui.Tree;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.Window;
import com.vaadin.ui.themes.BaseTheme;
import com.vaadin.ui.themes.Reindeer;
import com.wildboar.vaadin.addon.googlemap.overlay.BasicMarker;
import com.wildboar.vaadin.addon.googlemap.server.GoogleMap;

/**
 * 
 * 
 * 
 * @author Marcelo Malcher
 */
class WishMainResultsWindow extends Window implements ItemClickListener {

  /**
   * 
   */
  private AnalysisPublicationEvent theEvent = null;

  /**
   * The application
   */
  private WishResultsApplication application;

  /**
   * 
   */
  private Tree tree;

  /**
   * 
   */
  private GoogleMap googleMap;

  /**
   * 
   */
  private VerticalLayout mapLayout;

  /**
   * 
   */
  private VerticalLayout eventDescriptionVerticalLayout;

  /**
   * 
   */
  private VerticalLayout eventDescriptionTableAndButtonsVerticalLayout;

  /**
   * 
   */
  private H2 semanticDescriptionLabel;

  /**
   * 
   */
  private H3 spatialDescriptionLabel;

  /**
   * 
   */
  private H3 temporalDescriptionLabel;

  /**
   * 
   */
  private Button centerButton;

  /**
   * 
   */
  private BasicMarker eventMarker;

  /**
   * 
   */
  private Map<Integer, BasicMarker> contentsMarkerMap;

  /**
   * 
   */
  private HashSet<Integer> voteSet = new HashSet<Integer>();

  private Button positiveButton;

  private Button negativeButton;

  /**
   * 
   */
  private Map<Item, AnalysisPublicationEvent> mapItemEvent =
    new HashMap<Item, AnalysisPublicationEvent>();

  private String eventUrl, contentUrl, selectedUrl;

  /**
   * 
   * @param application
   */
  WishMainResultsWindow(WishResultsApplication application) {
    this.application = application;
    setCaption(application.messagesProp
      .getProperty("WishMainResultsWindow.application_title"));
    setTheme(WishResultsApplication.THEME);

    VerticalLayout mainLayout = new VerticalLayout();
    mainLayout.setStyleName(Reindeer.LAYOUT_WHITE);
    mainLayout.setSpacing(true);
    mainLayout.setSizeFull();

    //top bar
    {
      HorizontalLayout nav = new HorizontalLayout();
      nav.setHeight("115px");
      nav.setWidth("100%");
      nav.setSpacing(true);
      nav.setMargin(true);
      mainLayout.addComponent(nav);

      HorizontalLayout logos = new HorizontalLayout();
      logos.setSizeUndefined();

      // Upper W-IS-H left logo
      Link lnkWishLogo =
        new Link("", new ExternalResource("http://events.lac-rio.com/wish"));
      lnkWishLogo.setIcon(new ThemeResource("images/results_logo_wish.png"));
      lnkWishLogo.setSizeUndefined();
      logos.addComponent(lnkWishLogo);

      // Upper LAC left logo
      Link lnkLacLogo =
        new Link("", new ExternalResource("http://www.lac-rio.com"));
      lnkLacLogo.setIcon(new ThemeResource("images/results_logo_lac.png"));
      lnkLacLogo.setSizeUndefined();
      logos.addComponent(lnkLacLogo);

      nav.addComponent(logos);
    }

    //Tab - main
    {
      CssLayout margin = new CssLayout();
      margin.setMargin(false, true, true, true);
      margin.setSizeFull();
      TabSheet tabSheet = new TabSheet();
      tabSheet.setSizeFull();
      margin.addComponent(tabSheet);

      //Setting main layout...
      mainLayout.addComponent(margin);
      mainLayout.setExpandRatio(margin, 1);

      //
      HorizontalSplitPanel mainSplit = new HorizontalSplitPanel();
      mainSplit.setSizeFull();
      mainSplit.setStyleName("main-split");
      mainSplit.setSplitPosition(20); //20%
      tabSheet.addTab(mainSplit, application.messagesProp
        .getProperty("WishMainResultsWindow.tab_caption"), new ThemeResource(
        "../aurora/icons/map_magnify.png"));

      { // Tree Layout 

        VerticalLayout treeLayout = new VerticalLayout();
        treeLayout.setSpacing(true);
        treeLayout.setSizeFull();

        //Tree
        tree = new Tree();
        tree.setSizeFull();
        tree.addListener(this);
        tree.setImmediate(true);

        //Update button 
        HorizontalLayout buttonLayout = new HorizontalLayout();
        buttonLayout.setMargin(true);
        buttonLayout.setSpacing(true);
        buttonLayout.setWidth("100%");

        Button updateButton =
          new Button(application.messagesProp
            .getProperty("WishMainResultsWindow.update_button_caption"),
            new ClickListener() {

              @Override
              public void buttonClick(ClickEvent event) {
                WishMainResultsWindow.this.updateEventList();
              }
            });
        updateButton.setWidth("100%");
        updateButton.setIcon(new ThemeResource(
          "../aurora/icons/arrow_refresh.png"));

        buttonLayout.addComponent(updateButton);

        treeLayout.addComponent(tree);
        treeLayout.addComponent(buttonLayout);

        treeLayout.setExpandRatio(tree, 2);

        mainSplit.addComponent(treeLayout);
      }

      { // Google Map & Info
        mapLayout = new VerticalLayout();
        mapLayout.setSpacing(true);
        mapLayout.setWidth("100%");

        //String apiKey = application.configProp.getProperty("WishMainResultsWindow.apiKey");
        String zoom =
          application.configProp.getProperty("WishMainResultsWindow.zoom");
        String width =
          application.configProp.getProperty("WishMainResultsWindow.width");
        String height =
          application.configProp.getProperty("WishMainResultsWindow.height");
        //googleMap = new GoogleMap(application, apiKey);
        googleMap = new GoogleMap(application);
        googleMap.setWidth(width);
        googleMap.setHeight(height);
        googleMap.setZoom(Integer.valueOf(zoom));

        eventDescriptionVerticalLayout = new VerticalLayout();
        eventDescriptionVerticalLayout.setMargin(true);
        eventDescriptionVerticalLayout.setSpacing(true);
        eventDescriptionVerticalLayout.setWidth("100%");

        {
          HorizontalLayout eventHeader = new HorizontalLayout();
          eventHeader.setSpacing(true);
          eventHeader.setWidth("100%");

          VerticalLayout leftLayout = new VerticalLayout();
          leftLayout.setSpacing(true);

          semanticDescriptionLabel = new H2("");
          leftLayout.addComponent(semanticDescriptionLabel);

          spatialDescriptionLabel = new H3("");
          leftLayout.addComponent(spatialDescriptionLabel);

          temporalDescriptionLabel = new H3("");
          leftLayout.addComponent(temporalDescriptionLabel);

          eventHeader.addComponent(leftLayout);
          eventHeader.setComponentAlignment(leftLayout, Alignment.TOP_LEFT);

          HorizontalLayout rightLayout = new HorizontalLayout();
          rightLayout.setSpacing(true);

          //
          centerButton =
            new Button(application.messagesProp
              .getProperty("WishMainResultsWindow.center_button_caption"));
          centerButton.setStyleName(BaseTheme.BUTTON_LINK);
          centerButton.addListener(new ClickListener() {
            @Override
            public void buttonClick(ClickEvent event) {
              //
              if (theEvent != null && eventMarker != null) {
                googleMap.setCenter(eventMarker.getLatLng());
              }
            }
          });
          rightLayout.addComponent(centerButton);

          eventHeader.addComponent(rightLayout);
          eventHeader.setComponentAlignment(rightLayout, Alignment.TOP_RIGHT);

          eventDescriptionVerticalLayout.addComponent(eventHeader);

          //Table
          eventDescriptionTableAndButtonsVerticalLayout = new VerticalLayout();
          eventDescriptionTableAndButtonsVerticalLayout.setSizeFull();
          eventDescriptionTableAndButtonsVerticalLayout.setSpacing(true);
          eventDescriptionVerticalLayout
            .addComponent(eventDescriptionTableAndButtonsVerticalLayout);

        }

        mapLayout.addComponent(googleMap);
        mapLayout.addComponent(eventDescriptionVerticalLayout);

        mapLayout.setExpandRatio(googleMap, 2);

        //expandLayout(true);

        mainSplit.addComponent(mapLayout);

        //Marker - Properties
        String eventIcon =
          application.configProp
            .getProperty("AnalysisPublicationEventViewerLayout.event_marker_icon");
        String contentIcon =
          application.configProp
            .getProperty("AnalysisPublicationEventViewerLayout.content_marker_icon");
        String selectedIcon =
          application.configProp
            .getProperty("AnalysisPublicationEventViewerLayout.selected_content_marker_icon");

        eventUrl = "VAADIN/themes/wish/" + eventIcon;
        contentUrl = "VAADIN/themes/wish/" + contentIcon;
        selectedUrl = "VAADIN/themes/wish/" + selectedIcon;

      }
    }

    updateEventList();

    setContent(mainLayout);
  }

  /**
   * 
   * @param b
   */
  protected void persistVoting(boolean b) {
    WishVotingWindow votingWindow =
      new WishVotingWindow(application, theEvent, b);
    votingWindow.addListener(new VotingListener() {

      @Override
      public void voteFired(VotingEvent event) {
        if (event.voteFired) {
          voteSet.add(theEvent.getId());
          displayWarning(WishMainResultsWindow.this, application.messagesProp
            .getProperty("WishMainResultsWindow.voting_thank_caption"),
            application.messagesProp
              .getProperty("WishMainResultsWindow.voting_thank_description"));
        }
        positiveButton.setVisible(!event.voteFired);
        negativeButton.setVisible(!event.voteFired);
      }
    });
    addWindow(votingWindow);
  }

  /**
   * Updating event list
   */
  private void updateEventList() {
    //Creating container
    EntityContainer<AnalysisPublication> container =
      AuroraJPAContainerFactory.make(AnalysisPublication.class,
        WishResultsApplication.FACTORY.createEntityManager());
    //Sorting 
    container.sort(new Object[] { "date" }, new boolean[] { false });

    HierarchicalContainer hwContainer = new HierarchicalContainer();
    hwContainer.addContainerProperty("description", String.class, null);

    int itemId = 0;

    //Clearing the map
    mapItemEvent.clear();

    // Iterate over the item identifiers of the table.
    for (Iterator i = container.getItemIds().iterator(); i.hasNext();) {
      int iid = (Integer) i.next();
      // Now get the actual item from the table.
      EntityItem<AnalysisPublication> item = container.getItem(iid);
      // Gets actual object 
      AnalysisPublication publication = item.getEntity();
      //
      if (publication.getOpenPublication()) {
        String description =
          SimpleDateFormat.getInstance().format(publication.getDate()) + " - "
            + publication.getEvents().size() + " event(s)";
        Item hwItem = hwContainer.addItem(itemId);
        // Add name property for item
        hwItem.getItemProperty("description").setValue(description);
        hwContainer.setChildrenAllowed(itemId, true);
        int parentItemId = itemId;
        itemId++;

        //Events
        for (AnalysisPublicationEvent event : publication.getEvents()) {
          hwItem = hwContainer.addItem(itemId);
          hwItem.getItemProperty("description").setValue(
            event.getSemantics() + " - " + event.getContents().size()
              + " content(s)");
          hwContainer.setParent(itemId, parentItemId);
          hwContainer.setChildrenAllowed(itemId, false);

          //
          mapItemEvent.put(hwItem, event);

          itemId++;
        }
      }
    }

    tree.setContainerDataSource(hwContainer);
    tree.setItemCaptionPropertyId("description");

    // Expand whole tree
    for (int i = 0; i < tree.getContainerDataSource().size(); i++) {
      tree.expandItemsRecursively(i);
    }
  }

  /**
   * 
   * {@inheritDoc}
   */
  @Override
  public void itemClick(ItemClickEvent event) {
    theEvent = mapItemEvent.get(event.getItem());
    if (theEvent != null) {
      semanticDescriptionLabel.setCaption(theEvent.getSemantics());
      spatialDescriptionLabel.setCaption("Latitude: " + theEvent.getLatitude()
        + " - Longitude: " + theEvent.getLongitude());
      temporalDescriptionLabel.setCaption(SimpleDateFormat.getInstance()
        .format(theEvent.getTimestamp()));

      //
      googleMap.removeAllMarkers();

      //
      eventMarker =
        new BasicMarker(0L, new Point2D.Double(theEvent.getLongitude(),
          theEvent.getLatitude()), theEvent.getSemantics());
      eventMarker.setIconUrl(eventUrl);
      eventMarker.setDraggable(false);
      googleMap.addMarker(eventMarker);
      googleMap.setCenter(eventMarker.getLatLng());

      //
      contentsMarkerMap = new HashMap<Integer, BasicMarker>();

      //Table
      final Table contentsTable = new Table();
      contentsTable.setSelectable(true);
      contentsTable.setImmediate(true);
      contentsTable.setSizeFull();

      //Static properties
      contentsTable.addContainerProperty("id", Integer.class, null);
      contentsTable.addContainerProperty("contentId", Integer.class, null);
      contentsTable.addContainerProperty("creatorId", String.class, null);
      contentsTable.addContainerProperty("content", String.class, null);
      contentsTable.addContainerProperty("latitude", Double.class, null);
      contentsTable.addContainerProperty("longitude", Double.class, null);
      contentsTable.addContainerProperty("date", Date.class, null);

      //   
      contentsTable.setColumnReorderingAllowed(true);
      contentsTable.setColumnCollapsingAllowed(true);

      //EntityManager
      EntityManager man = application.FACTORY.createEntityManager();

      //
      String sqlQuery =
        "select id, content, creatorId, location_latitude, location_longitude, timestamp_date "
          + "from contents c "
          + "where c.id = ?contentId order by timestamp_date";

      int i = 0;
      for (AnalysisPublicationEventContent c : theEvent.getContents()) {
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

          //
          contentsTable.addItem(new Object[] { ++i, rId, rCreatorId, rContent,
              rLatitude, rLongitude, rDate }, i);

          //Marker
          BasicMarker marker =
            new BasicMarker((long) rId, new Point2D.Double(rLongitude,
              rLatitude), rContent);
          marker.setDraggable(false);
          marker.setIconUrl(contentUrl + i + ".png");
          contentsMarkerMap.put(i, marker);
          googleMap.addMarker(marker);
        }
      }

      man.close();

      contentsTable.setPageLength(i);

      //Defining visible columns...
      contentsTable.setVisibleColumns(new Object[] { "id", "contentId",
          "creatorId", "content", "latitude", "longitude", "date" });

      contentsTable.addListener(new ItemClickListener() {
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

      eventDescriptionTableAndButtonsVerticalLayout.removeAllComponents();
      eventDescriptionTableAndButtonsVerticalLayout.addComponent(contentsTable);

      //
      if (!voteSet.contains(theEvent.getId())
        && theEvent.getAnalysisPublication().getReceiveVote()) {
        //
        HorizontalLayout buttonLayout = new HorizontalLayout();
        buttonLayout.setSpacing(true);

        positiveButton =
          new Button(application.messagesProp
            .getProperty("WishMainResultsWindow.positive_button_caption"));
        positiveButton.setIcon(new ThemeResource(
          "images/voting/yes_button_16.png"));
        positiveButton.addListener(new ClickListener() {
          @Override
          public void buttonClick(ClickEvent event) {
            //
            if (theEvent != null && (!voteSet.contains(theEvent.getId()))) {
              persistVoting(true);
            }
          }
        });

        negativeButton =
          new Button(application.messagesProp
            .getProperty("WishMainResultsWindow.negative_button_caption"));
        negativeButton.setIcon(new ThemeResource(
          "images/voting/no_button_16.png"));
        negativeButton.addListener(new ClickListener() {
          @Override
          public void buttonClick(ClickEvent event) {
            //
            if (theEvent != null && (!voteSet.contains(theEvent.getId()))) {
              persistVoting(false);
            }
          }
        });

        buttonLayout.addComponent(positiveButton);
        buttonLayout.addComponent(negativeButton);

        eventDescriptionTableAndButtonsVerticalLayout
          .addComponent(buttonLayout);

      }

      mapLayout.addComponent(eventDescriptionVerticalLayout);
    }
  }

  /**
   * 
   * 
   * 
   * @author Marcelo Malcher
   */
  public interface VotingListener extends Serializable {
    public void voteFired(VotingEvent event);
  }

  public static class VotingEvent extends Component.Event {

    private boolean voteFired;

    public VotingEvent(Component source, boolean voteFired) {
      super(source);
      this.voteFired = voteFired;
    }

    public boolean isVoteFired() {
      return voteFired;
    }
  }

  /**
   * 
   * 
   * @param window
   * @param title
   * @param details
   */
  private void displayWarning(Window window, String title, String details) {
    Window.Notification notification =
      new Window.Notification(title, details,
        Window.Notification.TYPE_WARNING_MESSAGE);
    notification.setDelayMsec(5000);
    window.showNotification(notification);
  }
}