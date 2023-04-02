<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%@ page import="constants.AttributeConst"%>
<%@ page import="constants.ForwardConst"%>

<c:set var="actTop" value="${ForwardConst.ACT_TOP.getValue()}" />
<c:set var="actEmp" value="${ForwardConst.ACT_EMP.getValue()}" />
<c:set var="actRep" value="${ForwardConst.ACT_REP.getValue()}" />

<c:set var="commShow" value="${ForwardConst.CMD_SHOW.getValue()}" />
<c:set var="commIdx" value="${ForwardConst.CMD_INDEX.getValue()}" />
<c:set var="commNew" value="${ForwardConst.CMD_NEW.getValue()}" />

<c:import url="../layout/app.jsp">
    <c:param name="content">
        <c:if test="${flush != null}">
            <div id="flush_success">
                <c:out value="${flush}"></c:out>
            </div>
        </c:if>
        <div class="calendar-title">

        <h2><span><c:out value="${sessionScope.login_employee.name}" /></span>&nbsp;さんの日報</h2>
            <h1>
                <c:out value="${year}" />
                /
                <c:out value="${month}" />
            </h1>
        </div>

        <div class="calendar-container">
                    <div id="next-prev-button">
                        <form method="GET" action="<c:url value=''/>">
                            <input type="hidden"
                                name="${AttributeConst.TOP_ACTION.getValue()}"
                                value="<c:out value='${actTop}' />" />
                            <input type="hidden"
                                name="${AttributeConst.TOP_COMMAND.getValue()}"
                                value="<c:out value='${commIdx}' />" />
                            <input type="hidden" name="${AttributeConst.TOP_YEAR.getValue()}"
                                value="<c:out value='${previousYear}' />" />
                            <input type="hidden"
                                name="${AttributeConst.TOP_MONTH.getValue()}"
                                value="<c:out value='${previousMonth}' />" />
                            <p><button type="submit" id="prev">‹</button>前月&nbsp;</p>
                        </form>

                        <form method="GET" action="<c:url value=''/>">
                            <input type="hidden"
                                name="${AttributeConst.TOP_ACTION.getValue()}"
                                value="<c:out value='${actTop}' />" />
                            <input type="hidden"
                                name="${AttributeConst.TOP_COMMAND.getValue()}"
                                value="<c:out value='${commIdx}' />" />
                            <input type="hidden" name="${AttributeConst.TOP_YEAR.getValue()}"
                                value="<c:out value='${nextYear}' />" />
                            <input type="hidden"
                                name="${AttributeConst.TOP_MONTH.getValue()}"
                                value="<c:out value='${nextMonth}' />" />
                            <p>翌月<button type="submit" id="next">›</button></p>
                        </form>
            </div>


            <table class="calendar">
                <tr>
                    <th>MON</th>
                    <th>TUE</th>
                    <th>WED</th>
                    <th>THU</th>
                    <th>FRI</th>
                    <th class="sat">SAT</th>
                    <th class="sun">SUN</th>
                </tr>

                <c:forEach var="week" items="${weekList}">
                    <tr class="day">
                        <td>
                            <c:choose>
                                <c:when test="${week.monDateOfcurrentMonth == true}">
                                    <span class="thisMonth"><c:out value="${week.monDate}" /></span>
                                </c:when>
                                <c:otherwise>
                                    <span class="otherMonth"><c:out value="${week.monDate}" /></span>
                                </c:otherwise>
                            </c:choose>
                            <c:forEach var="receive" items="${week.monDateReport}">

                             <a
                                    href="<c:url value='?action=${actRep}&command=${commShow}&id=${receive.id}' />">
                                    <span><c:out value="${receive.title}" /></span>
                                </a>
                            </c:forEach>
                        </td>
                        <td>
                            <c:choose>
                                <c:when test="${week.tueDateOfcurrentMonth == true}">
                                    <span class="thisMonth"><c:out value="${week.tueDate}" /></span>
                                </c:when>
                                <c:otherwise>
                                    <span class="otherMonth"><c:out value="${week.tueDate}" /></span>
                                </c:otherwise>
                            </c:choose>
                            <c:forEach var="receive" items="${week.tueDateReport}">

                                <a
                                    href="<c:url value='?action=${actRep}&command=${commShow}&id=${receive.id}' />">
                                    <span><c:out value="${receive.title}" /></span>
                                </a>
                            </c:forEach>
                        </td>
                        <td>
                            <c:choose>
                                <c:when test="${week.wedDateOfcurrentMonth == true}">
                                    <span class="thisMonth"><c:out value="${week.wedDate}" /></span>
                                </c:when>
                                <c:otherwise>
                                    <span class="otherMonth"><c:out value="${week.wedDate}" /></span>
                                </c:otherwise>
                            </c:choose>
                           <c:forEach var="receive" items="${week.wedDateReport}">
                                <a
                                    href="<c:url value='?action=${actRep}&command=${commShow}&id=${receive.id}' />">
                                    <span><c:out value="${receive.title}" /></span>
                                </a>
                            </c:forEach>
                        </td>
                        <td>
                         <c:choose>
                                <c:when test="${week.thuDateOfcurrentMonth == true}">
                                    <span class="thisMonth"><c:out value="${week.thuDate}" /></span>
                                </c:when>
                                <c:otherwise>
                                    <span class="otherMonth"><c:out value="${week.thuDate}" /></span>
                                </c:otherwise>
                            </c:choose>
                            <c:forEach var="receive" items="${week.thuDateReport}">
                                <a
                                    href="<c:url value='?action=${actRep}&command=${commShow}&id=${receive.id}' />">
                                    <span><c:out value="${receive.title}" /></span>
                                </a>
                            </c:forEach>
                        </td>
                        <td>
                         <c:choose>
                                <c:when test="${week.friDateOfcurrentMonth == true}">
                                    <span class="thisMonth"><c:out value="${week.friDate}" /></span>
                                </c:when>
                                <c:otherwise>
                                    <span class="otherMonth"><c:out value="${week.friDate}" /></span>
                                </c:otherwise>
                            </c:choose>
                            <c:forEach var="receive" items="${week.friDateReport}">
                                <a
                                    href="<c:url value='?action=${actRep}&command=${commShow}&id=${receive.id}' />">
                                    <span><c:out value="${receive.title}" /></span>
                                </a>
                            </c:forEach>
                        </td>
                        <td>
                            <c:choose>
                                <c:when test="${week.satDateOfcurrentMonth == true}">
                                    <span class="satThisMonth"><c:out value="${week.satDate}" /></span></span>
                                </c:when>
                                <c:otherwise>
                                    <span class="satOtherMonth"><c:out value="${week.satDate}" /></span></span>
                                </c:otherwise>
                            </c:choose>
                            <c:forEach var="receive" items="${week.satDateReport}">
                                <a
                                    href="<c:url value='?action=${actRep}&command=${commShow}&id=${receive.id}' />">
                                    <span><c:out value="${receive.title}" /></span>
                                </a>
                            </c:forEach>
                        </td>
                        <td>
                            <c:choose>
                                <c:when test="${week.sunDateOfcurrentMonth == true}">
                                    <span class="sunThisMonth"><c:out value="${week.sunDate}" /></span>
                                </c:when>
                                <c:otherwise>
                                    <span class="sunOtherMonth"><c:out value="${week.sunDate}" /></span>
                                </c:otherwise>
                            </c:choose>
                            <c:forEach var="receive" items="${week.sunDateReport}">
                                <a
                                    href="<c:url value='?action=${actRep}&command=${commShow}&id=${receive.id}' />">
                                    <span><c:out value="${receive.title}" /></span>
                                </a>
                            </c:forEach>
                        </td>
                    </tr>
                </c:forEach>
            </table>
        </div>
        <p>
            <a href="<c:url value='?action=${actRep}&command=${commNew}' />">新規日報の登録</a>
        </p>
    </c:param>
</c:import>