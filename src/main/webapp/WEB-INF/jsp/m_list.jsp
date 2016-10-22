<%@ taglib prefix="c" uri="http://java.sun.com/jstl/core_rt"%>

<html>
<body>
	<h1>All Messages</h1>

    <ul>
        <c:forEach var="msg" items="${allMessages}">
            <li>
                <a href="/m/${msg.id}"><c:out value="${msg.text}" /></a>
            </li>
        </c:forEach>
    </ul>

    <a href="/m_form">Post Message</a>
</body>
</html>