<%@include file="header.jsp" %>

			<nav class="fright">
				<ul>
					<li><a href="index.jsp">Home</a></li>
					<li><a href="about.jsp" class="navactive">About</a></li>
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

			<header class="grid col-full">
				<hr>
				<p class="fleft">About the project</p>
			</header>



			<aside class="grid col-one-quarter mq2-col-full">
				<p class="mbottom">The W-IS-H project involves the collection of
					social data, the definiton of concepts ant the search for events
					through the analysis of this social data - our goal.</p>
				<menu>
					<ul>
						<li><a href="#navproject" class="arrow">What it is?</a></li>
						<li><a href="#navcollection" class="arrow">Social data</a></li>
						<li><a href="#navgoal" class="arrow">Our goal</a></li>
						<li><a href="#navconcepts" class="arrow">The concepts</a></li>
					</ul>
				</menu>
			</aside>

			<section class="grid col-three-quarters mq2-col-full">
				<!-- <img src="img/front/banner_about.jpg" alt="" align="middle"> -->

				<div id="navproject">
					<h2>What is the W-IS-H project?</h2>
					<p>The W-IS-H project aims at discovering what is happening in
						the world through the analysis of mobile social data. Our goal is
						to detect local events that are happening at the moment in a city
						and show this information to web users, so they can react to their
						occurrence.</p>
				</div>

				<article id="navcollection">
					<h2>Collecting social data</h2>
					<p>We are currently collecting Twitter data created in these
						three cities: New York, São Paulo and Rio de Janeiro. We only
						collect tweets which were attached to their geographic coordinates
						and we know that the users posted them from these cities. This is
						possible through the Twitter Streaminh API that allows receiving
						only data created in specific geographic boundaries. Despite
						having access to small portion of Twitter data, as we do not have
						access to Twitter firehouse, we are convinced that the volume
						received is sufficient for event-detection (our primary results
						encouraged us). And it is always good to remember that, by
						default, all tweets are public and the users who do not want to
						see their tweets in this (or any other) project, must opt for the
						private option.</p>
				</article>

				<article id="navgoal">
					<h2>Achieving the goal</h2>
					<p>Our project consists in the development of an analytical
						approach to detect real-world events through the filtering and
						classification of messages published by mobile users in the
						Twitter social network. The approach is composed by four steps:</p>
					<ul>
						<li>(i) partitioning the messages in a temporal mannager
							(e.g. time windows)</li>
						<li>(ii) identification of spatial correlation between the
							messages belonging to temporal class</li>
						<li>(iii) classifying the messages into one or more
							predefined topics</li>
						<li>(iv) filtering of messages that were created live or not</li>
					</ul>
					<p align="center"><img src="front/img/about_what_is.png" alt=""></p>
				</article>

				<article id="navconcepts">
					<h2>Concepts</h2>
					<p>The following concepts are central to this proposed
						research.</p>
					<h3>Real-world event</h3>
					<p>From [Yang, 1999] defines an event as something that occurs
						at a certain place and has a start and end time. In this research,
						we consider real-world events which are local (restricted to a
						geographic region, like a stadium, a street -block, a festival
						campus, etc.) and arise people's interest and participation in a
						way that leads them to spread about it in social networks. It is
						not the goal of this research to tackle large-scale events like
						hurricanes and earthquakes, nor even detect the ones that are
						spread in many different places like the riot that happened in
						several Arab countries since 2010. Those kinds of events are
						already widespread by traditional news and blogs, and do not need
						any other type of broadcasting.</p>
					<h3>Mobile social content</h3>
					<p>A mobile social content is any kind of content created by
						the mobile user and sent to a social network such as short text
						messages, media like photos and videos, among others. From it, we
						expect to extract and infer information about the mobile user's
						activity and/or surroundings. As we already mentioned, in this
						research we will solely focus on Twitter data. Therefore, a
						Twitter message, when created by the mobile user, is the mobile
						social content we are targeting, and has the following mandatory
						properties:</p>
					<ul>
						<li>- <b>Content</b>: the message per se created by the user.
							As described above, the content is <i>what</i> it is all about.</li>
						<li>- <b>Time</b>: the instant of time <i>when</i> the social
							content was created.</li>
						<li>- <b>Location</b>: the location from <i>where</i> the
							social content was created. In Twitter, users can opt to attach
							their current geographic coordinates to the message they are
							publishing, but just a small fraction of users allow this. We
							will only consider Twitter messages which contain the referenced
							geographic coordinates; although this means to ignore a major
							fraction of Twitter social data. This selection is sustained by
							our hypothesis that even with much less data, we are able to
							detect and classify events.</li>
						<li>Identification: the identification of the user who
							created this mobile social content.</li>
					</ul>
					<p>For brevity, we refer to mobile social content as content.</p>
					<p align="center"><img src="front/img/about_twitter_message.png" alt=""></p>
					<h3>Live-content</h3>
					<p>In [Becker (2011a)], the authors explain about the need to
						select messages from Twitter regarding attributes as quality,
						relevance and usefulness. In a different way, this proposed
						research also believes that is necessary to select messages to
						improve event -detection. The objective is to filter live-content
						from non-live content. We consider content as live when it is
						created and published while the creator is actually witnessing the
						real-world event. For instance, a Twitter message informing about
						a traffic jam may differ regarding time and location. A user may
						create a Twitter message complaining about the traffic jam while
						being stuck in the traffic jam himself, in his car. The same user
						could create a different Twitter message complaining about the
						same traffic jam when he arrives at his destination. These
						messages are about the same event, the traffic jam, but with
						different context attributes, and only the former will be of
						interest in this research, as we consider it a live-content. In
						summary, whenever a user is at the place and within the timeframe
						in between the event's start and end time, he will be considered a
						witness of the event. And all the content created and published by
						them as witness of an event is considered as a live social content
						of this event.</p>
					<p align="center"><img src="front/img/about_live_content.png" alt=""></p>
					<h3>Correlated contents</h3>
					<p>We say that some content is correlated to other content when
						there is a match between their location, time and topic
						attributes. For instance, let's consider that content A and B are
						correlated according to our analytical approach. This happened due
						to, after the classification steps, both were classified with the
						topic sports, while content A was classified as belonging to
						region 1, 2, and 3 and created in time windows 15, 16 and 17. In
						the other hand, content B was classified as belonging to region 3,
						4 and 5 and created in time windows 13, 14 and 15. Therefore, they
						were considered correlated as there was a match between them -
						sports topic, region 3 and time window 15. This is how our
						detection approach identifies an event - when a significant volume
						of correlated contents, which are considered live, is detected
						while analyzing the dataset.</p>
					<p align="center"><img src="front/img/about_correlated_content.png" alt=""></p>
					<h3>Content's topic</h3>
					<p>A topic is the subject of the Twitter message. As this
						research will deal with pre-defined topics when classifying a
						Twitter message, the result might be: not related to an y of the
						When classifying contents regarding location and time, the
						analytical approach considers interpolated regions and time
						windows. pre-defined topics or related to one or more topics. This
						will depend on the application of the topic-detection techniques
						described in section 5.3. Initially, our proposed research aims to
						detect three kinds of events: sports, music and traffic jam.
						However, we foresee the possible need of particularizing them,
						specially sports and music. This might be needed in order to
						improve the chances of two Twitter messages really belonging to
						the same event.</p>
				</article>

			</section>

		</div>
		<!--main-->

<%@include file="footer.jsp" %>