<%--
  ========================LICENSE_START=================================
  AEM Permission Management
  %%
  Copyright (C) 2013 - 2016 Cognifide Limited
  %%
  Licensed under the Apache License, Version 2.0 (the "License");
  you may not use this file except in compliance with the License.
  You may obtain a copy of the License at
  
       http://www.apache.org/licenses/LICENSE-2.0
  
  Unless required by applicable law or agreed to in writing, software
  distributed under the License is distributed on an "AS IS" BASIS,
  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
  See the License for the specific language governing permissions and
  limitations under the License.
  =========================LICENSE_END==================================
  --%>
<%@page import="org.apache.commons.lang.StringEscapeUtils"%><%
%><%@page contentType="text/html" pageEncoding="utf-8"%><%
%><%@include file="/apps/cqsm/global.jsp"%><%
%>

<div class="inner" id="analysisPage">
	<div class="navigation">
		<a href="/etc/cqsm.html" class="cqsm-button"><div class="link-icon import">Go to import</div></a>
	</div>

	<h3>Analyse groups</h3>
	<div>
		Group name(s):<br/>
		<input id="group" name="group" size="100"/> <br>
		<button class="cqsm-button analyse-button" type="submit">Analyse</button>
	</div>

	<div id="status-message"></div>
	<div id="servlet-result"></div>

</div>