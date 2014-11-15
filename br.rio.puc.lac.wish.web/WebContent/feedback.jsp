<%@page import="br.rio.puc.lac.wish.analyzer.commons.Content"%>
<%@page import="java.util.Map"%>
<%@page import="java.text.SimpleDateFormat"%>
<%@page import="br.rio.puc.lac.wish.web.front.FeedbackServlet"%>
<%@page import="br.rio.puc.lac.wish.web.front.FeedbackContentRetrieval"%>
<%@page import="java.util.Date"%>
<%@include file="header.jsp"%>

<nav class="fright">
	<ul>
		<li><a href="index.jsp">Home</a></li>
		<li><a href="about.jsp">About</a></li>
	</ul>
	<ul>
		<li><a href="research.jsp">Research</a></li>
		<li><a href="http://events.lac-rio.com/wish/results">Results</a></li>
	</ul>
	<ul>
		<li><a href="publications.jsp">Publications</a></li>
		<li><a href="contact.jsp">People</a></li>
	</ul>
</nav>
</header>

<div class="about-page main grid-wrap">

	<%
	  FeedbackContentRetrieval retrieval = (FeedbackContentRetrieval) request.getAttribute(FeedbackServlet.FEEDBACK_CONTENT_ATTRIBUTE);	  								 
	  SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-dd-MM");
      SimpleDateFormat timeFormat = new SimpleDateFormat("HH:mm");
      Date theContentDate = new Date(retrieval.getTheContent().getTimestamp());
	%>

	<header class="grid col-full">
	  <hr>
	  <p class="fleft">Feedback</p>
	</header>

	<!--  <aside class="grid col-one-quarter mq2-col-full">

		<p class="mbottom">The W-IS-H project aims at discovering what is
			happening in the world through the analysis of mobile social data.
			Our goal is to detect local events that are happening at the moment
			in a city and show this information to web users, so they can react
			to their occurrence.</p>
		<p class="mbottom">We thank you in advance!</p>
	</aside>
	-->

	<!-- <section class="grid col-three-quarters mq2-col-full"> -->
	<section class="grid col-full">
		<p class="feedback">
			Hey <a
				href="http://www.twitter.com/<%=retrieval.getTheContent().getCreatorId()%>"
				target="_blank">@<%=retrieval.getTheContent().getCreatorId()%></a>!
			One of your posts and some others from different users were used to
			detect an event related to the topic <b><%=retrieval.getFeedbackContent()
					.getAnalysisPublicationEventContent()
					.getAnalysisPublicationEvent().getSemantics()%></b> <img
				src="front/img/marker/event.png" alt="Event flag">.
		</p>
		<p class="feedback">
			Therefore,your feedback is the <b>utmost value for the
				improvement of our detection techniques</b>.
		</p>
		<p class="feedback">
			On <b><%=dateFormat.format(theContentDate)%></b>, at <b><%=timeFormat.format(theContentDate)%></b>
			you posted the following:
		</p>
		<article>
			<p class="content">
				<img src="front/img/marker/tweet.png" alt="Post flag">
				<%=retrieval.getTheContent().getContent()%>
			</p>
			<div id="map" style="width: 100%; height: 350px"></div>
		</article>

		<br />

		<div class="grid-wrap works">

			<article id="navtogg" class="grid col-one-third">
				<ul class="toggle-view">
					<li>
						<h5 class="toggle-title">
							<img src="front/img/marker/event.png" alt="Post flag">
							Detected Event<span class="toggle-title-detail"></span>
						</h5>

						<div class="toggle grid-wrap">
							<table id="newspaper-a" summary="Information">
								<tbody>
									<tr>
										<td><b>Topic</b></td>
										<td><%=retrieval.getFeedbackContent()
                    .getAnalysisPublicationEventContent()
                    .getAnalysisPublicationEvent().getSemantics()%></td>
									</tr>
									<tr>
										<td><b>Time</b></td>
										<td><%=SimpleDateFormat.getInstance().format(new Date(retrieval.getFeedbackContent().getAnalysisPublicationEventContent().getAnalysisPublicationEvent().getTimestamp()))%></td>
									</tr>
									<tr>
										<td><b>Latitude</b></td>
										<td><%=retrieval.getFeedbackContent().getAnalysisPublicationEventContent().getAnalysisPublicationEvent().getLatitude()%></td>
									</tr>
									<tr>
										<td><b>Longitude</b></td>
										<td><%=retrieval.getFeedbackContent().getAnalysisPublicationEventContent().getAnalysisPublicationEvent().getLongitude()%></td>
									</tr>
									<tr>
										<td><b># of correlated posts</b></td>
										<td><%=retrieval.getFeedbackContent().getAnalysisPublicationEventContent().getAnalysisPublicationEvent().getContents().size()%></td>
									</tr>
								</tbody>
							</table>
						</div>
					</li>
				</ul>
			</article>

			<article id="navtogg" class="grid col-two-thirds">
				<ul class="toggle-view">
					<li>
						<h5 class="toggle-title">
							<img src="front/img/marker/tweet.png" alt="Post flag"> Your
							post <span class="toggle-title-detail">- Detailed
								information about your post</span>
						</h5>

						<div class="toggle grid-wrap">
							<ul class="grid col-full mq3-col-full">
								<li><b>Info:</b>
									<table id="newspaper-a" summary="Information">
										<tbody>
											<tr>
												<td><b>Creator Id.</b></td>
												<td><%=retrieval.getTheContent().getCreatorId()%></td>
											</tr>
											<tr>
												<td><b>Content Message</b></td>
												<td><%=retrieval.getTheContent().getContent()%></td>
											</tr>
											<tr>
												<td><b>Time</b></td>
												<td><%=SimpleDateFormat.getInstance().format(theContentDate)%></td>
											</tr>
											<tr>
												<td><b>Latitude</b></td>
												<td><%=retrieval.getTheContent().getLocation().getLatitude()%></td>
											</tr>
											<tr>
												<td><b>Longitude</b></td>
												<td><%=retrieval.getTheContent().getLocation().getLongitude()%></td>
											</tr>
											<%
											  if (retrieval.getTheContent().getTags() != null)  {
											%>
											<tr>
												<td><b>Tags</b></td>
												<td><%=retrieval.getTheContent().getTags().toString()%></td>
											</tr>
											<%
											  }
											%>
										</tbody>
									</table></li>
								<%
								  if (retrieval.getTheContent().getClassification() != null) {
								%>
								<li><b>Classification map:</b>
									<table id="newspaper-a" summary="Classification Map">
										<thead>
											<tr>
												<th scope="col">Key</th>
												<th scope="col">Value</th>
											</tr>
										</thead>
										<tbody>
											<%
											  Map<String, String> classificationMap = retrieval.getTheContent().getClassification().getClassificationMap();
																																																																																																																									   for (String key : classificationMap.keySet()) {
											%>
											<tr>
												<td><%=key%></td>
												<td><%=classificationMap.get(key)%></td>
											</tr>
											<%
											  }
											%>
										</tbody>
									</table></li>
								<li><b>Information map:</b>
									<table id="newspaper-a" summary="Information Map">
										<thead>
											<tr>
												<th scope="col">Key</th>
												<th scope="col">Value</th>
											</tr>
										</thead>
										<tbody>
											<%
											  Map<String, String> informationMap = retrieval.getTheContent().getClassification().getInformationMap();
																																																																																																														                                           for (String key : informationMap.keySet()) {
											%>
											<tr>
												<td><%=key%></td>
												<td><%=informationMap.get(key)%></td>
											</tr>
											<%
											  }
											%>
										</tbody>
									</table></li>
								<%
								  }
								%>
							</ul>

						</div>
					</li>

					<li>
						<h5 class="toggle-title">
							<img src="front/img/marker/content/symbol_num.png" alt="Num flag">
							Other posts <span class="toggle-title-detail">- With the
								correlation of these posts and yours, our algorithm suggested
								that the event happened</span>
						</h5>
						<div class="toggle grid-wrap">
							<table id="newspaper-a" summary="Other contents">
								<thead>
									<tr>
										<th scope="col">Content</th>
										<th scope="col">Creator</th>
										<th scope="col">Time</th>
										<th scope="col">Latitude</th>
										<th scope="col">Longitude</th>
									</tr>
								</thead>
								<tbody>
									<%
									  for (Content<String, String> content : retrieval.getOthers()) {
									%>
									<tr>
										<td><%=content.getContent()%></td>
										<td><%=content.getCreatorId()%></td>
										<td><%=SimpleDateFormat.getInstance().format(new Date(content.getTimestamp()))%></td>
										<td><%=content.getLocation().getLatitude()%></td>
										<td><%=content.getLocation().getLongitude()%></td>
									</tr>
									<%
									  }
									%>
								</tbody>
							</table>
						</div>
					</li>
				</ul>
			</article>

		</div>
		<!-- grid inside 3/4-->

		<br />

		<p class="feedback">
			So, could you tell us: <b>is your post really about the detected
				event?</b>
		</p>
		<p class="feedback">
			Feel free to add some observations! We will <b>*really*</b>
			appreciate it!
		</p>
		<form id="contact_form" class="feedback_form"
			action="FeedbackConfirmation" method="post" name="contact_form">			
			<input type="hidden" name="Language" value="English">
			<ul>
			    <input type="hidden" name="retrievalId" value="<%= retrieval.getFeedbackContent().getId() %>">
				<li><label for="message">Observations:</label> <textarea
						name="message" id="message" cols="100" rows="3"></textarea></li>
				<li>
					<p>
						<button type="feedback" id="submit" name="submit"
							value="CONFIRMATION" class="button fleft">
							<img src="front/img/button/yes_button.png"> YES, I confirm
							my post was about it!
						</button>
						<button type="feedback" id="submit" name="submit" value="DENIAL"
							class="button fright">
							<img src="front/img/button/no_button.png"> NO!!! My post
							was not about this event!
						</button>

					</p>
				</li>
			</ul>
		</form>

	</section>
</div>
<!--main-->

<script
	src="https://maps.googleapis.com/maps/api/js?v=3.exp&sensor=false"></script>

<script>
	var mapOptions = {
		zoom : 4,
		mapTypeId : google.maps.MapTypeId.ROADMAP
	};
	var theMap = new google.maps.Map(document.getElementById('map'), mapOptions);

	function initialize(latitude, longitude) {
		var centerLatLng = new google.maps.LatLng(latitude, longitude);
		theMap.setCenter(centerLatLng);
	}

	function addMarker(latitude, longitude, message, imageUrl, index) {
		var myLatLng = new google.maps.LatLng(latitude, longitude);
		var image = {
			url : imageUrl,
		};
		var marker = new google.maps.Marker({
			position : myLatLng,
			map : theMap,
			title : message,
			icon : image,
			zIndex : index
		});
	}
</script>

<%
  if (retrieval != null) {
    //Initializing map
    out.println("<script type=\"text/javascript\">initialize("
      + retrieval.getTheContent().getLocation().getLatitude() + ", "
      + retrieval.getTheContent().getLocation().getLongitude() 
      + ")</script>");
    //The event
    out.println("<script type=\"text/javascript\">addMarker("
      + retrieval.getFeedbackContent().getAnalysisPublicationEventContent()
        .getAnalysisPublicationEvent().getLatitude()
      + ", "
      + retrieval.getFeedbackContent().getAnalysisPublicationEventContent()
        .getAnalysisPublicationEvent().getLongitude()
      + ", '"
      + retrieval.getFeedbackContent().getAnalysisPublicationEventContent()
        .getAnalysisPublicationEvent().getSemantics() + "', "
      + "'front/img/marker/event.png'" + ", 1)</script>");
    //The content
    out.println("<script type=\"text/javascript\">addMarker("
      + retrieval.getTheContent().getLocation().getLatitude() + ", "
      + retrieval.getTheContent().getLocation().getLongitude() + ", '"
      + retrieval.getTheContent().getContent() + "', "
      + "'front/img/marker/tweet.png'" + ", 2)</script>");
    //Others
    int i = 1;
    for (Content<String, String> other : retrieval.getOthers()) {
      out.println("<script type=\"text/javascript\">addMarker("
        + other.getLocation().getLatitude() + ", "
        + other.getLocation().getLongitude() + ", '" + other.getContent()
        + "', " + "'front/img/marker/content/number_" + i + ".png'" + ", "
        + (i + 2) + ")</script>");
      i++;
    }
  }
%>
<%@include file="footer.jsp"%>