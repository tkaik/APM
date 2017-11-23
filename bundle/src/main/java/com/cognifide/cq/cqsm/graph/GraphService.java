package com.cognifide.cq.cqsm.graph;

import java.util.Arrays;
import java.util.List;
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
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.cognifide.cq.cqsm.graph.data.Graph;
import com.cognifide.cq.cqsm.graph.data.Node;
import com.google.common.collect.Lists;
import com.google.common.collect.Sets;

@Component
@Service(GraphService.class)
public class GraphService {

	private static final Logger LOGGER = LoggerFactory.getLogger(GraphService.class);

	private static final List<String> IGNORED_GROUPS = Arrays.asList("everyone"
			/*, "content-authors", "mp-editors", "forms-users", "workflow-users"*/);

	@Reference
	private transient ResourceResolverFactory resourceResolverFactory;

	public Graph createGraph(String groupId, CreateGroupsGraphParams params) throws CreateGraphException {
		Graph result = new Graph();
		Set<Group> visitedGroups = Sets.newHashSet();
		try {
			ResourceResolver resourceResolver = resourceResolverFactory.getAdministrativeResourceResolver(null);

			UserManager userManager = resourceResolver.adaptTo(UserManager.class);
			if (userManager == null) {
				throw new CreateGraphException("Failed to create user manager");
			}

			Group group = (Group)userManager.getAuthorizable(groupId);
			result.addRequestedGroup(group.getID(), group.getPrincipal().getName());

			if (group != null) {
				addEdges(result, group, visitedGroups, params);
			}
		} catch(LoginException | RepositoryException exc ) {
			throw new CreateGraphException("Failed to create groups with error.", exc);
		}
		return result;
	}

	private void addEdges(Graph graph, Group group, Set<Group> visitedGroups, CreateGroupsGraphParams params) throws RepositoryException {
		visitedGroups.add(group);

		if (params.isShowChildren()) {
			addChildren(graph, group, visitedGroups);
		}
		if (params.isShowParents()) {
			addParents(graph, group, visitedGroups);
		}
	}

	private void addParents(Graph graph, Group group, Set<Group> visitedGroups) throws RepositoryException {
		List<Group> parents = Lists.newArrayList(group.declaredMemberOf());
		for (Authorizable parent : parents) {
			Group parentGroup = (Group) parent;
			if (!isIgnoredGroup(parent.getID())) {
				Node fromNode = new Node(parentGroup.getID(), parentGroup.getPrincipal().getName());
				Node toNode = new Node(group.getID(), group.getPrincipal().getName());
				graph.addEdge(fromNode, toNode);
				if (!visitedGroups.contains(parentGroup)) {
					addParents(graph, parentGroup, visitedGroups);
				}
			}
		}
	}

	private void addChildren(Graph graph, Group group, Set<Group> visitedGroups) throws RepositoryException {
		List<Authorizable> children = Lists.newArrayList(group.getDeclaredMembers());
		for (Authorizable child : children) {
			if (child.isGroup() && !isIgnoredGroup(child.getID())) {
				Group childGroup = (Group) child;

				Node fromNode = new Node(group.getID(), group.getPrincipal().getName());
				Node toNode = new Node(childGroup.getID(), childGroup.getPrincipal().getName());
				graph.addEdge(fromNode, toNode);
				if (!visitedGroups.contains(childGroup)) {
					addChildren(graph, childGroup, visitedGroups);
				}
			}
		}
	}

	private boolean isIgnoredGroup(String name) {
		return IGNORED_GROUPS.contains(name);
	}
}
