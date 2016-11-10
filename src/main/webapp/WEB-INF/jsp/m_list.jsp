<%@ page session="false"%>
<%@ page contentType="text/html;charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="t" tagdir="/WEB-INF/tags" %>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
 
<t:page>
    <jsp:attribute name="title">Welcome!</jsp:attribute>
    <jsp:body>
        <div class="jumbotron" id="welcome">
 
			<h1>All Messages</h1>
				<div class="row fill">
					<div class="col-md-2">
						<h2>Boards</h2>
						Boards go here
					</div>
					<div class="col-md-10 fill">
						<div class="msgarea">
							<h2>Messages</h2>
							<c:forEach var="msg" items="${allMessages}">
			            	<div id="message_${msg.id}" class="message">
			                	<a href="/m/${board}/${msg.id}"><c:out value="${msg.text}" /></a>
			            	</div>
			        		</c:forEach>
						</div>
						<div class="row">
							<div class="col-sm-8">
								<textarea id="msgTxt">Your message</textarea>
							</div>
							<div class="col-sm-4">
								<div class="row">
									<div class="col-sm-3">
										<select id="attrSel"></select>
									</div>
									<div class="col-sm-3">
										<input type="text" id="valTxt" />
									</div>
								</div>
							</div>
						</div>
					</div>
				</div>
			
			<!-- 
    		<ul>
        		<c:forEach var="msg" items="${allMessages}">
            	<li>
                	<a href="/m/${board}/${msg.id}"><c:out value="${msg.text}" /></a>
            	</li>
        		</c:forEach>
    		</ul>

    		<a href="/m_form/${board}">Post Message</a>
    		 -->
            
        </div>
    </jsp:body>
</t:page>
