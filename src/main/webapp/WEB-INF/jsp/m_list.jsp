<%@ page session="false"%>
<%@ page contentType="text/html;charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="t" tagdir="/WEB-INF/tags" %>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
 
<t:page>
    <jsp:attribute name="title">Welcome!</jsp:attribute>
    <jsp:body>
    	<button id="connect">Connect</button>
    	<button id="disconnect">Disconnect</button>
    	<input type="text" id="message" />
    	<button id="send">Send</button>
        <div class="jumbotron" id="welcome">
 
			<h1>All Messages</h1>

    		<ul id="messages">
        		<c:forEach var="msg" items="${allMessages}">
            	<li>
                	<a href="/m/${board}/${msg.id}"><c:out value="${msg.text}" /></a>
            	</li>
        		</c:forEach>
    		</ul>

    		<a href="/m_form/${board}">Post Message</a>
            
        </div>
        
        <script type="text/javascript">
        	var boardId = ${board};
        </script>
        <script src="/resources/js/app.js"></script>
        
    </jsp:body>
</t:page>
