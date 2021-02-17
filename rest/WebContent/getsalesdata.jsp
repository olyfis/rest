<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=iso-8859-1" />
<title>Olympus FIS Sales Support Data Application</title>
<!--  <link href="includes/appstyle.css" rel="stylesheet" type="text/css" /> 

<style><%@include file="includes/css/reports.css"%></style>
<style><%@include file="includes/css/table.css"%></style>
-->
<style><%@include file="includes/css/header.css"%></style>
<style><%@include file="includes/css/menu.css"%></style>
 <link rel="stylesheet" href="includes/css/calendar.css" />
    <script type="text/javascript" src="includes/scripts/pureJSCalendar.js"></script>




<script language="javascript" type="text/javascript">
 
//Browser Support Code
function ajaxFunction(){
	var ajaxRequest;  // The variable that makes Ajax possible!
	
	try{
		// Opera 8.0+, Firefox, Safari
		ajaxRequest = new XMLHttpRequest();
	} catch (e){
		// Internet Explorer Browsers
		try{
			ajaxRequest = new ActiveXObject("Msxml2.XMLHTTP");
		} catch (e) {
			try{
				ajaxRequest = new ActiveXObject("Microsoft.XMLHTTP");
			} catch (e){
				// Something went wrong
				alert("Your browser broke!");
				return false;
			}
		}
	}
	// Create a function that will receive data sent from the server
	ajaxRequest.onreadystatechange = function(){
		if(ajaxRequest.readyState == 4){
			var ajaxDisplay = document.getElementById('ajaxDiv');
			ajaxDisplay.innerHTML = ajaxRequest.responseText;
		// document.actionform.actiontypeprogram.value = ajaxRequest.responseText;

		}
	}

//	var atype = document.actionform.getElementById('actiontype').value;
	var atype = document.actionform.actiontype.value;
	var date2 = document.actionform.startDate.value;
	//alert("atype=" + atype);
	
	//alert("date2=" + date2);
	//var queryString = "?atype=" + atype + "&wpm=" + wpm + "&ex=" + ex;
	var queryString = "/webreport/ajaxIL.jsp?atype=" + atype + "&date2=" + date2;
	

	  
	//alert("QS=" + queryString);
	//ajaxRequest.open("GET", "ajax.jsp" + queryString, true);
	ajaxRequest.open("POST", queryString, true);
	ajaxRequest.send(); 
}

 
</script>

<!-- 
http://localhost:8181/webreport/valboth?startDate=2021-02-05&actiontype=15&id=101-0018009-002
 -->

<!-- ********************************************************************************************************************************************************* -->

</head>
<body>
    
    
 <%@include  file="includes/header.html" %>


<%-- Req for Salesforce Ajax menu 
<jsp:include page="/sfquery" flush="true" /> --%>
<c:out value="${strArrID}"></c:out>
 
  <c:forEach items="${strArrID}" var="id">
            ${id.id} <br />
        </c:forEach>
        
<!--   <img src="includes\images\logo.jpg" alt="logo"  height="100" width="225" align="right"> -->


<div style="padding-left:20px">
  <h3>Olympus FIS Sales Support Data Application</h3>
</div>

<BR>

<h5>This page will provide a view of the Sales Support Data in JSON format.</h5>



<h5>Note: <font color="red">Requires Javascript to be enabled.</font> <BR>

</h5>

<BR>


	<form name="actionform" method="get" action="ssbook">

<BR>


<table class="a" width="40%"  border="1" cellpadding="1" cellspacing="1">
<tr> <th class="theader"> Olympus FIS Sales Support Data by Contract ID</th> </tr>
  <tr>
    <td class="table_cell">
    <!--  Inner Table -->
    <table class="a" width="100%"  border="1" cellpadding="1" cellspacing="1">
  <tr>
  <td width="20" valign="bottom"> <b>Enter Contract ID: (101-0018398-001)</b> </td> 
  <td width="20" valign="bottom">  
     <%  out.println("<input name=\"id\" id=\"id\" type=\"text\" value=\"\"   />" );
     %>
  </td>
  </tr>
  

  <tr>
   <td  valign="bottom" class="a">
	<div id='ajaxDiv'> </div>
	</td>
	 <td> 
    <INPUT type="submit" value="Submit">  
    </td>
	
  </tr>
  </table>

</table>

 </form>
 <!-- ************ End First Table *************** -->
 <BR>
 <form name="actionform" method="get" action="ssbook">

<BR>


<table class="a" width="40%"  border="1" cellpadding="1" cellspacing="1">
<tr> <th class="theader"> Olympus FIS Sales Support Data by Application ID</th> </tr>
  <tr>
    <td class="table_cell">
    <!--  Inner Table -->
    <table class="a" width="100%"  border="1" cellpadding="1" cellspacing="1">
  <tr>
  <td width="20" valign="bottom"> <b>Enter Application ID: (15852)</b> </td> 
  <td width="20" valign="bottom">  
     <%  out.println("<input name=\"appid\" id=\"appid\" type=\"text\" value=\"\"   />" );
     %>
  </td>
  </tr>
  

  <tr>
   <td  valign="bottom" class="a">
	<div id='ajaxDiv'> </div>
	</td>
	 <td> 
    <INPUT type="submit" value="Submit">  
    </td>
	
  </tr>
  </table>

</table>

 </form>
 
 
 </BR>
 
</body>
</html>