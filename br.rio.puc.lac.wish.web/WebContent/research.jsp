<%@include file="header.jsp"%>

<nav class="fright">
	<ul>
		<li><a href="index.jsp">Home</a></li>
		<li><a href="about.jsp">About</a></li>
	</ul>
	<ul>
		<li><a href="research.jsp" class="navactive">Research</a></li>
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
		<p class="fleft">Research</p>
	</header>



	<aside class="grid col-one-quarter mq2-col-full">
		<p class="mbottom">The W-IS-H project involves the collection of
			social data, the definiton of concepts and the search for events
			through the analysis of this social data - our goal.</p>
		<menu>
			<ul>
				<li><a href="#navsteps" class="arrow">Steps</a></li>
				<li><a href="#navsfiltering" class="arrow">Filtering data</a></li>
				<li><a href="#navseval" class="arrow">Evaluation</a></li>
				<li><a href="#navsarch" class="arrow">Architecture</a></li>
				<li><a href="#navsprivacy" class="arrow">Privacy</a></li>
			</ul>
		</menu>
	</aside>

	<section class="grid col-three-quarters mq2-col-full">
		<!--  <img src="img/banner_architecture.jpg" alt="" align="middle"> -->

		<div id="navproject">
			<h2>W-IS-H Research</h2>
		</div>

		<article id="navsteps">
			<h2>Steps</h2>
			<iframe
				src="http://prezi.com/embed/ccfq2sjszncz/?bgcolor=ffffff&amp;lock_to_path=0&amp;autoplay=0&amp;autohide_ctrls=0&amp;features=undefined&amp;disabled_features=undefined"
				width="100%" height="400" frameBorder="0"></iframe>
		</article>

		<article id="navsfiltering">
			<h2>Filtering data</h2>
			<p align="center">
				<img src="front/img/architecture/research_proposal.png" alt="">
			</p>
		</article>

		<article id="navseval">
			<h2>Evaluating</h2>
			<p>Evaluation will be made in two ways:</p>
			<ul>
				<li>- Subjective analysis for a set of events and its related
					messages (voting)</li>
				<li>- Retrieval of feedback from the mobile users who created
					and published the messages</li>
			</ul>
			<p align="center">
				<img src="front/img/architecture/evaluation.png" alt="">
			</p>
			<p>Adjustment of detection parameters - weights of each technique
				and improvement of positive and negative samples dataset</p>

		</article>

		<article id="navsarch">
			<h2>Architecture</h2>
			<p>The analytical approach is being developed using Apache
				Hadoop:</p>
			<ul>
				<li>- HDFS (Distributed File System) for the storage of Twitter
					messages</li>
				<li>- Each step is developed as a Map/Reduce Job</li>
			</ul>
			<p align="center">
				<img src="front/img/architecture/architecture.png" alt="">
			</p>
		</article>

		<article id="navsprivacy">
			<h2>Important Note on Privacy</h2>
			<p>None of the collected data - neither the original tweet, nor the user feedback - will be used for whatever commercial purpose or distributed to third parties. If the user so desires, we can permanetely remove his/her data from our log.</p>
		</article>

	</section>

</div>
<!--main-->

<%@include file="footer.jsp"%>