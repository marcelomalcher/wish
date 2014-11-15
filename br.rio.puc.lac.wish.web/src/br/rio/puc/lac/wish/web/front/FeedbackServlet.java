package br.rio.puc.lac.wish.web.front;

import java.io.IOException;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Servlet implementation class Feedback
 */

public class FeedbackServlet extends HttpServlet {

  private static final long serialVersionUID = 1L;

  private static final String REQUEST_ID_PARAMETER = "rid";

  public static final String FEEDBACK_CONTENT_ATTRIBUTE = "feebackContent";

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
    String requestId = request.getParameter(REQUEST_ID_PARAMETER);

    //Feedback retrieval
    FeedbackContentRetrieval retrieval =
      FeedbackFacade.getInstance().getAnalysisFeedbackRetrieval(requestId);

    //If the feedback can be processed
    if (retrieval != null && retrieval.getFeedbackContent().getDate() == null) {
      String nextJSP = "/feedback.jsp";
      RequestDispatcher dispatcher =
        getServletContext().getRequestDispatcher(nextJSP);
      request.setAttribute(FEEDBACK_CONTENT_ATTRIBUTE, retrieval);
      dispatcher.forward(request, response);
    }
    //If not, error message
    else {
      String nextJSP = "/message.jsp";
      RequestDispatcher dispatcher =
        getServletContext().getRequestDispatcher(nextJSP);
      request.setAttribute("caption", "Feedback error");
      request
        .setAttribute(
          "msg",
          "Error while retrieving feedback content. Please try again or contact us through the Contact page.");
      dispatcher.forward(request, response);
    }
  }
}
