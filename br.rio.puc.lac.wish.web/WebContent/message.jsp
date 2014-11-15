<%@include file="header.jsp" %>

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

            <header class="grid col-full">
                <hr>
                <p class="fleft">${requestScope.caption}</p>
            </header>

            <section class="grid col-full">               

                <article>                
                    <p class="content">${requestScope.msg}</p>
                </article>

            </section>

        </div>
        <!--main-->

<%@include file="footer.jsp" %>