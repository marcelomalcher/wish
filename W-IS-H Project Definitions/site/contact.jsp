<!DOCTYPE  html>
<html>
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
				<li class="current-menu-item"><a href="contact.jsp">Contato</a></li>
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
			<div id="content">
					
				<h2 class="title-divider">Contato</h2>	
				
				<p>Entre em contato conosco para saber mais sobre o departamento, nossos cursos de graduação e nossa pós-graduação.</p>							
				
				<div id="map"></div>									
				
	    	</div>
	    	<!-- ENDS content -->
	    	
	    	<!-- Sidebar -->
	    	<div id="sidebar">
				<h6 class="side-title">Informações</h6>
				
				<p>Departamento de Informática<br/>
				PUC-Rio<br/>
				Marquês de São Vicente, 225 RDC <br/>
				CEP 22451-900 – Gávea<br/>
				Rio de Janeiro - RJ – Brasil</p>
				
				<p class="contact-data">
					<span class="ico-tel">(21) 3217-1510</span>					
					<span class="ico-email"><a href="mailto:pos@inf.puc-rio.br">pos@inf.puc-rio.br</a></span>
				</p>
				
	    	</div>
	    	<!-- ENDS Sidebar -->
	        
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
		
		<!-- Map script -->
        <script type="text/javascript" src="http://maps.google.com/maps/api/js?sensor=true"></script>
		<script src="js/gmaps.js"></script>
        <script type="text/javascript">
		var map;
		$(document).ready(function(){
			map = new GMaps({
				div: '#map',
				lat: -22.980787,
				lng: -43.233547
			});
			map.addMarker({
			  lat: -22.980787,
			  lng: -43.233547
			});
	    });
	  </script>
	  <!-- ENDS Map script -->
      <!--https://maps.google.com.br/maps?q=PUC-Rio&hl=en&ll=-22.980787,-43.233547&spn=0.001032,0.002064&sll=-17.654686,-48.702718&sspn=17.442082,33.815918&t=h&hq=PUC-Rio&z=20 --:
	  
	</body>
</html>
