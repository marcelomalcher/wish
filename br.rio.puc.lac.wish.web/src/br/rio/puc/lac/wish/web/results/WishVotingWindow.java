package br.rio.puc.lac.wish.web.results;

import java.lang.reflect.Method;
import java.util.Calendar;

import javax.persistence.EntityManager;
import javax.persistence.Query;

import br.com.tecnoinf.aurora.ui.labels.format.H3;
import br.rio.puc.lac.wish.web.model.AnalysisPublicationEvent;
import br.rio.puc.lac.wish.web.results.WishMainResultsWindow.VotingEvent;
import br.rio.puc.lac.wish.web.results.WishMainResultsWindow.VotingListener;

import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.FormLayout;
import com.vaadin.ui.TextField;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.Window;
import com.vaadin.ui.themes.Reindeer;

import eu.livotov.tpt.gui.widgets.TPTCaptcha;

/**
 * 
 * 
 * 
 * @author Marcelo Malcher
 */
public class WishVotingWindow extends Window {

  private boolean vote;

  /**
   * 
   */
  private AnalysisPublicationEvent theEvent;

  /**
   * The application
   */
  private WishResultsApplication application;

  /**
   * 
   * @param application
   * @param event
   * @param vote
   */
  WishVotingWindow(WishResultsApplication application,
    AnalysisPublicationEvent event, boolean vote) {
    this.application = application;
    this.theEvent = event;
    this.vote = vote;

    setModal(true);
    setResizable(false);
    setCaption(application.messagesProp.getProperty("WishVotingWindow.title"));
    setTheme(WishResultsApplication.THEME);

    VerticalLayout mainLayout = new VerticalLayout();
    mainLayout.setStyleName(Reindeer.LAYOUT_WHITE);
    mainLayout.setMargin(true);
    mainLayout.setSpacing(true);
    mainLayout.setSizeUndefined();

    String question =
      vote ? String.format(application.messagesProp
        .getProperty("WishVotingWindow.positive_question"), theEvent
        .getSemantics()) : String.format(application.messagesProp
        .getProperty("WishVotingWindow.negative_question"), theEvent
        .getSemantics());

    H3 confirmationLabel = new H3(question);

    mainLayout.addComponent(confirmationLabel);

    //Button Caption
    String buttonCaption =
      vote ? application.messagesProp
        .getProperty("WishVotingWindow.positive_button_caption")
        : application.messagesProp
          .getProperty("WishVotingWindow.negative_button_caption");

    //
    FormLayout form = new FormLayout();
    final TPTCaptcha captchaField = new TPTCaptcha();
    captchaField.generateCaptchaCode(5);
    captchaField.setCaption("Captcha");
    form.addComponent(captchaField);
    final TextField text = new TextField("Please, type the word: ");
    form.addComponent(text);

    Button button = new Button(buttonCaption);
    button.addListener(new Button.ClickListener() {
      @Override
      public void buttonClick(ClickEvent event) {
        if (captchaField.getCaptchaCode().equals(text.getValue())) {
          //
          if (persistVote() > 0) {
            fireEvent(new VotingEvent(WishVotingWindow.this, true));
          }
          else {
            fireEvent(new VotingEvent(WishVotingWindow.this, false));
          }
          close();
        }
      }
    });

    form.addComponent(button);

    mainLayout.addComponent(form);

    setContent(mainLayout);
  }

  /**
   * 
   * @param listener
   */
  public void addListener(VotingListener listener) {
    try {
      Method method =
        VotingListener.class.getDeclaredMethod("voteFired",
          new Class[] { VotingEvent.class });
      addListener(VotingEvent.class, listener, method);
    }
    catch (final java.lang.NoSuchMethodException e) {
      // This should never happen
      throw new java.lang.RuntimeException(
        "Internal error, entity saved method not found");
    }
  }

  /**
   * 
   * @param listener
   */
  public void removeListener(VotingListener listener) {
    removeListener(VotingEvent.class, listener);
  }

  private int persistVote() {
    //EntityManager
    EntityManager man = application.FACTORY.createEntityManager();

    man.getTransaction().begin();

    //
    String sqlQuery =
      "insert into analysis_publication_event_evaluation(idPublicationEvent, vote, date) "
        + "values (?idPublicationEvent, ?vote, ?date) ";
    Query query = man.createNativeQuery(sqlQuery);
    query.setParameter("idPublicationEvent", theEvent.getId());
    query.setParameter("vote", vote);
    query.setParameter("date", Calendar.getInstance().getTime());
    int ret = query.executeUpdate();

    man.getTransaction().commit();

    man.close();

    return ret;
  }
}