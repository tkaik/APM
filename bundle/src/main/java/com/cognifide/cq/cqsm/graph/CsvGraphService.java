package com.cognifide.cq.cqsm.graph;

import org.apache.felix.scr.annotations.Component;
import org.apache.felix.scr.annotations.Reference;
import org.apache.felix.scr.annotations.Service;

import com.cognifide.cq.cqsm.graph.data.Edge;
import com.cognifide.cq.cqsm.graph.data.Graph;

@Component
@Service(CsvGraphService.class)
public class CsvGraphService {

	@Reference
	GraphService graphService;

	public String createCsvGraphStructure(String groupId) {
		StringBuilder result = new StringBuilder();
		Graph graph = graphService.createGraph(groupId);
		for(Edge edge : graph.getEdges()) {
			result.append(edge.getFrom().getId());
			result.append(";");
			result.append(edge.getTo().getId());
			result.append("\n");
		}
		return result.toString();
	}
}
