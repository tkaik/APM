package com.cognifide.cq.cqsm.graph;

import java.util.Iterator;
import java.util.Set;

import javax.jcr.RepositoryException;

import org.apache.felix.scr.annotations.Component;
import org.apache.felix.scr.annotations.Reference;
import org.apache.felix.scr.annotations.Service;
import org.apache.jackrabbit.api.security.user.Authorizable;
import org.apache.jackrabbit.api.security.user.Group;
import org.apache.jackrabbit.api.security.user.UserManager;
import org.apache.sling.api.resource.LoginException;
import org.apache.sling.api.resource.ResourceResolver;
import org.apache.sling.api.resource.ResourceResolverFactory;

import com.cognifide.cq.cqsm.graph.data.Graph;
import com.cognifide.cq.cqsm.graph.data.Node;
import com.google.common.collect.Sets;

@Component
@Service(GraphService.class)
public class GraphService {

	@Reference
	private transient ResourceResolverFactory resourceResolverFactory;

	public Graph createGraph(String groupId) {
		Graph result = new Graph();
		Set<Group> visitedGroups = Sets.newHashSet();
		try {
			ResourceResolver resourceResolver = resourceResolverFactory.getAdministrativeResourceResolver(null);
			UserManager userManager = resourceResolver.adaptTo(UserManager.class);

			Group group = (Group)userManager.getAuthorizable(groupId);

			addEdges(result, group, visitedGroups);
		} catch(LoginException | RepositoryException exc ) {

		}
		return result;
	}

	private void addEdges(Graph graph, Group group, Set<Group> visitedGroups) throws RepositoryException {
		visitedGroups.add(group);

		Iterator<Authorizable> children = group.getDeclaredMembers();
		Iterator<Group> parents = group.memberOf();

		while(children.hasNext()) {
			Group childGroup = (Group)children.next();
			Node fromNode = new Node(group.getID(), "");
			Node toNode = new Node(childGroup.getID(), "");
			graph.addEdge(fromNode, toNode);
			if(!visitedGroups.contains(childGroup)) {
				addEdges(graph, childGroup, visitedGroups);
			}
		}
		while(parents.hasNext()) {
			Group parentGroup = parents.next();
			Node fromNode = new Node(parentGroup.getID(), "");
			Node toNode = new Node(group.getID(), "");
			graph.addEdge(fromNode, toNode);
			if(!visitedGroups.contains(parentGroup)) {
				addEdges(graph, parentGroup, visitedGroups);
			}
		}
	}
}
