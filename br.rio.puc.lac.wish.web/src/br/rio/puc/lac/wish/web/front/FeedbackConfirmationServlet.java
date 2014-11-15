package br.rio.puc.lac.wish.web.front;

import java.io.IOException;
import java.util.Calendar;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Servlet implementation class Feedback
 */

public class FeedbackConfirmationServlet extends HttpServlet {

  private static final long serialVersionUID = 1L;

  public static final String CONFIRMATION_VALUE = "CONFIRMATION";

  public static final String DENIAL_VALUE = "DENIAL";

  /**
   * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse
   *      response)
   */
  @Override
  protected void doGet(HttpServletRequest request, HttpServletResponse response)
    throws ServletException, IOException {
    doPost(request, response);
  }

  /**
   * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse
   *      response)
   */
  @Override
  protected void doPost(HttpServletRequest request, HttpServletResponse response)
    throws ServletException, IOException {
    String retrievalIdValue = request.getParameter("retrievalId");
    String submitValue = request.getParameter("submit");
    String observationsValue = request.getParameter("message");

    boolean vote = submitValue.equals(CONFIRMATION_VALUE);

    boolean update =
      FeedbackFacade.getInstance().updateFeedbackContent(retrievalIdValue,
        vote, Calendar.getInstance().getTime(), observationsValue);

    String nextJSP = "/message.jsp";
    RequestDispatcher dispatcher =
      getServletContext().getRequestDispatcher(nextJSP);
    if (update) {
      request.setAttribute("caption", "Feedback persisted successfully!");
      request
        .setAttribute(
          "msg",
          "We would like to express our sincere thanks and appreciation to you for helping us to improve our detection algorithm! <b>*Thank you*</b>!!! :-D");
    }
    else {
      request.setAttribute("caption", "Error while persisting feedback");
      request
        .setAttribute(
          "msg",
          "It appears that an error happened while persisting your feedback. We are sorry for this inconvenience and we will investigate why this happened. :-(");
    }
    dispatcher.forward(request, response);
  }
}
