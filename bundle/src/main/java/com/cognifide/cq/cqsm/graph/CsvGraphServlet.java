package com.cognifide.cq.cqsm.graph;

import java.io.IOException;
import java.io.OutputStream;

import javax.servlet.ServletException;

import org.apache.felix.scr.annotations.Reference;
import org.apache.felix.scr.annotations.sling.SlingServlet;
import org.apache.sling.api.SlingHttpServletRequest;
import org.apache.sling.api.SlingHttpServletResponse;
import org.apache.sling.api.servlets.HttpConstants;
import org.apache.sling.api.servlets.SlingAllMethodsServlet;

@SlingServlet(
		methods = { HttpConstants.METHOD_GET},
		paths = {"/bin/createCsvGroupGraph"}
)
public class CsvGraphServlet extends SlingAllMethodsServlet {

	@Reference
	private CsvGraphService csvGraphService;

	@Reference
	private GraphService graphService;


	@Override
	protected void doGet(SlingHttpServletRequest request, SlingHttpServletResponse response)
			throws ServletException, IOException {
		CreateGroupsGraphParams groupsParams = CreateGroupsGraphParams.fromRequest(request);
		String csvGraph = null;
		try {
			csvGraph = getCsvGraph(groupsParams);
		} catch (CreateGraphException e) {
			//TODO:Do it better later
			csvGraph = "Failed to create graph " + e.getMessage();
		}
		writeResponse(response, csvGraph);
	}

	private void writeResponse(SlingHttpServletResponse response, String csvGraph)
			throws IOException {
		response.setContentType("text/csv");
		response.setHeader("Content-Disposition", "attachment; filename=\"graph.csv\"");
		try(OutputStream outputStream = response.getOutputStream())
		{
			String outputResult = csvGraph;
			outputStream.write(outputResult.getBytes());
			outputStream.flush();
		} catch (IOException e) {
			response.sendError(SlingHttpServletResponse.SC_INTERNAL_SERVER_ERROR);
		}
	}

	private String getCsvGraph(CreateGroupsGraphParams params) throws CreateGraphException {
		String everyone = params.getGroupsIds().stream().findFirst().orElse("everyone");
		return csvGraphService.createCsvGraphStructure(graphService.createGraph(everyone, params));
	}
}