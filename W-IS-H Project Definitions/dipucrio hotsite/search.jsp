<!DOCTYPE  html>
<html>
	<%@ page import ="java.sql.*" %>
	<%@ page import ="javax.sql.*" %>	

	<head>
		<meta http-equiv="Content-Type" content="text/html;charset=UTF-8" />		
		<title>#dipucrio Hotsite</title>
		
		<!-- CSS -->
		<link rel="stylesheet" href="css/social-icons.css" type="text/css" media="screen" />
		<link rel="stylesheet" href="css/style.css" type="text/css" media="screen" />
		<!--[if IE 7]>
			<link rel="stylesheet" type="text/css" media="screen" href="<?php bloginfo('template_url') ?>/css/ie7-hacks.css" />
		<![endif]-->
		<!--[if IE 8]>
			<link rel="stylesheet" type="text/css" media="screen" href="<?php bloginfo('template_url') ?>/css/ie8-hacks.css" />
		<![endif]-->
		<!-- ENDS CSS -->	

		<link rel="Stylesheet" type="text/css" href="js/scroller/css/smoothDivScroll.css" />
		
		<!--[if IE]>
		<script src="http://html5shim.googlecode.com/svn/trunk/html5.js"></script>
		<![endif]-->
		<!-- ENDS JS -->
		
		<!-- GOOGLE FONTS -->
		<link href='http://fonts.googleapis.com/css?family=Lobster' rel='stylesheet' type='text/css'>
		
		<!-- Skin -->
		<link rel="stylesheet" href="skins/light.css"  type="text/css" media="screen" id="css-skins" />
		
	</head>
	<body>
		
		<!-- Navigation -->
		<div id="nav-wrapper">
			<ul id="nav" class="sf-menu">
				<li class="logo"><a href="index.jsp"><img src="img/logo_dipucrio.png" alt="#dipucrio" id="logo" ></a></li>
				<li><a href="index.jsp">Home</a></li>
				<li><a href="about.jsp">Sobre...</a></li>				
				<li><a href="contact.jsp">Contato</a></li>
				<li><a href="http://www.inf.puc-rio.br" target="_blank">Site do DI</a></li>
				
				<li class="social">
					<!-- Social -->
					<a href="https://twitter.com/di_puc_rio" target="_blank" class="poshytip  twitter" title="Siga os tweets do DI!"></a>
					<a href="https://www.facebook.com/pages/Departamento-de-Inform%C3%A1tica-PUC-Rio/265408443510757?fref=ts" target="_blank" class="poshytip  facebook" title="Curta a fan-page do DI"></a>
					<!-- ENDS Social -->
				</li>
				
				<li class="search-box">
					<form  method="post" id="searchform" action="search.jsp">
						<input type="text" value="Pesquisar..." name="s" id="s" onfocus="defaultInput(this,'Pesquisar...')" onblur="clearInput(this,'Pesquisar...')" />
					</form>		
				</li>						
			</ul>
		</div>
		<!-- Navigation -->

		<!-- Page wrap -->
		<div id="page-wrap">					
			<!-- content -->
			<div id="content" class="fullwidth">
					
				<h2 class="title-divider">Pesquisa...</h2>
				
			<%
				String s = request.getParameter("s"); 			
				Class.forName("com.mysql.jdbc.Driver"); 
				java.sql.Connection con = DriverManager.getConnection("jdbc:mysql://localhost:3306/dipucriodb","dipucrioweb","123456"); 
				PreparedStatement st = con.prepareStatement("select creatorId, content, location_place_name, location_latitude, location_longitude, timestamp_date from contents where (content like ? or creatorId like ?) order by timestamp_date desc"); 
				st.setString(1, "%" + s + "%");
				st.setString(2, "%" + s + "%");
				ResultSet rs = st.executeQuery(); 
				boolean empty = true ;
				while(rs.next()) { 
					empty = false ;
					String creatorId = rs.getString(1);
					String content = rs.getString(2);
					String locationName = rs.getString(3);					
					double lat = rs.getDouble(4);
					double lon = rs.getDouble(5);
					Timestamp t = rs.getTimestamp(6);
			%>
				<div style="border: 1px dashed #800000; padding: 10px;">
				<h6><%=content %> </h6>
				<p>
				<a href="http://www.twitter.com/<%=creatorId %>" target="_blank" ><%=creatorId %></a><br />
				<%=t %><br />
				<% if (locationName != null) { %>
					<%=locationName %> (<%=lat %>, <%=lon %>)				
				<% } %>
				</p>
				</div>
				<br/>
				
			<% } %>			
			<% if (empty) { %>
				<h6>Nenhum resultado foi encontrado para <%=s %>.</h6>
			<% } %>	

	    	</div>
			
	    	<!-- ENDS content -->
	    	
	        
        </div>
		<!-- ENDS Page wrap -->
		
   	
   			
		<!-- Footer -->
		<div id="footer-container">
			<div id="footer-open"><a href="#"></a></div>
	         <div id="footer-overlay">
	         	<div id="footer-wrapper">
	         		<div class="footer-col">
	         			<h6>Desenvolvimento</h6>
	         			<p><a href="http://www.lac-rio.com" ><img src="img/lac_original.gif" class="borderer" alt="Pic" width="200" /></a></p>
						<p> O LAC - Laboratory for Advanced Collaboration é comandado pelo <a href="http://www.inf.puc-rio.br/~endler" >Prof. Markus Endler.</a><p>
	         		</div>
					         			         	
	         		<div id="tweets" class="footer-col tweet">
	         			<h6>Twitter @di_puc_rio</h6>
	         			<script src="js/chirp.min.js"></script>
	         			<script>Chirp({user:'di_puc_rio',max:2})</script>
	         		</div>
	         		
	         		
	         		
	         	</div>
	         </div>
	         
	     </div>
	     <!-- ENDS Footer -->

	    <!-- JS -->
		<!-- jQuery library - Please load it from Google API's -->
		<script src="https://ajax.googleapis.com/ajax/libs/jquery/1.8.1/jquery.min.js" ></script>
	
		<!-- Smoothscroller -->
		<!-- jQuery UI Widget and Effects Core (custom download)
		     You can make your own at: http://jqueryui.com/download -->
		<script src="js/scroller/js/jquery-ui-1.8.23.custom.min.js" ></script>
		
		<!-- Latest version (3.0.6) of jQuery Mouse Wheel by Brandon Aaron
		     You will find it here: http://brandonaaron.net/code/mousewheel/demos -->
		<script src="js/scroller/js/jquery.mousewheel.min.js" ></script>
	
		<!-- jQuery Kinectic (1.5) used for touch scrolling -->
		<script src="js/scroller/js/jquery.kinetic.js" ></script>
	
		<!-- Smooth Div Scroll 1.3 minified-->
		<script src="js/scroller/js/jquery.smoothdivscroll-1.3-min.js" ></script>
		<!-- ENDS Smoothscroller -->
	     
	     
		<script src="js/quicksand.js"></script>
		
		<!-- prettyPhoto -->
		<script  src="js/prettyPhoto/js/jquery.prettyPhoto.js"></script>
		<link rel="stylesheet" href="js/prettyPhoto/css/prettyPhoto.css" type="text/css" media="screen" />
		<!-- ENDS prettyPhoto -->
		
		<!-- superfish -->
		<link rel="stylesheet" media="screen" href="css/superfish.css" /> 
		<script  src="js/superfish-1.4.8/js/hoverIntent.js"></script>
		<script  src="js/superfish-1.4.8/js/superfish.js"></script>
		<script  src="js/superfish-1.4.8/js/supersubs.js"></script>
		<!-- ENDS superfish -->
		
		<!-- poshytip -->
		<link rel="stylesheet" href="js/poshytip-1.0/src/tip-twitter/tip-twitter.css" type="text/css" />
		<link rel="stylesheet" href="js/poshytip-1.0/src/tip-yellowsimple/tip-yellowsimple.css" type="text/css" />
		<script  src="js/poshytip-1.0/src/jquery.poshytip.min.js"></script>
		<!-- ENDS poshytip -->

		<script  src="js/backstretch.js"></script>
		<script  src="js/custom.js"></script>
		<!-- ENDS JS -->

	</body>
</html>
