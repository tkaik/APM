package com.cognifide.cq.cqsm.graph;

import java.util.Iterator;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

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
import com.drew.lang.StringUtil;
import com.google.common.collect.Lists;
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
			if (group != null) {
				addEdges(result, group, visitedGroups);
			}
		} catch(LoginException | RepositoryException exc ) {
			//TODO:Exception throw
		}
		return result;
	}

	private void addEdges(Graph graph, Group group, Set<Group> visitedGroups) throws RepositoryException {
		visitedGroups.add(group);

		List<Authorizable> children = Lists.newArrayList(group.getDeclaredMembers());
		List<Group> parents = Lists.newArrayList(group.memberOf());

		for (Authorizable child : children) {
			if (child.isGroup()) {
				Group childGroup = (Group)child;

				Node fromNode = new Node(group.getID(), group.getPrincipal().getName());
				Node toNode = new Node(childGroup.getID(), childGroup.getPrincipal().getName());
				graph.addEdge(fromNode, toNode);
				if(!visitedGroups.contains(childGroup)) {
					addEdges(graph, childGroup, visitedGroups);
				}
			}
		}

		for (Authorizable parent : parents) {
			Group parentGroup = (Group) parent;
			if (!"everyone".equals(parent.getID())) {
				Node fromNode = new Node(parentGroup.getID(), parentGroup.getPrincipal().getName());
				Node toNode = new Node(group.getID(), group.getPrincipal().getName());
				graph.addEdge(fromNode, toNode);
				if(!visitedGroups.contains(parentGroup)) {
					addEdges(graph, parentGroup, visitedGroups);
				}
			}
		}
	}
}
