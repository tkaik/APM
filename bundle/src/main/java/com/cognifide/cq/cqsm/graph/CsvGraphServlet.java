package com.cognifide.cq.cqsm.graph;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.ServletException;

import org.apache.felix.scr.annotations.Reference;
import org.apache.felix.scr.annotations.sling.SlingServlet;
import org.apache.sling.api.SlingHttpServletRequest;
import org.apache.sling.api.SlingHttpServletResponse;
import org.apache.sling.api.servlets.HttpConstants;
import org.apache.sling.api.servlets.SlingAllMethodsServlet;

import com.google.common.collect.ImmutableMap;
import com.google.gson.GsonBuilder;

@SlingServlet(
		methods = { HttpConstants.METHOD_GET},
		paths = {"/bin/createCsvGroupGraph"}
)
public class CsvGraphServlet extends SlingAllMethodsServlet {

	@Reference
	private CsvGraphService csvGraphService;

	@Override
	protected void doGet(SlingHttpServletRequest request, SlingHttpServletResponse response)
			throws ServletException, IOException {
		CreateGroupsGraphParams groupsParams = CreateGroupsGraphParams.fromRequest(request);
		String csvGraph = getCsvGraph(groupsParams);
		writeResponse(response, csvGraph, groupsParams);
	}

	private void writeResponse(SlingHttpServletResponse response, String csvGraph, CreateGroupsGraphParams params)
			throws IOException {
		try (PrintWriter out = response.getWriter()) {
			new GsonBuilder()
					.create()
					.toJson(
							ImmutableMap.builder()
										.put("data", csvGraph)
										.put("params", params)
										.build(),
							out
					);
		} catch (IOException e) {
			response.sendError(SlingHttpServletResponse.SC_INTERNAL_SERVER_ERROR);
		}
	}

	private String getCsvGraph(CreateGroupsGraphParams params) {
		String everyone = params.getGroupsIds().stream().findFirst().orElse("everyone");
		return csvGraphService.createCsvGraphStructure(everyone);
	}
}
