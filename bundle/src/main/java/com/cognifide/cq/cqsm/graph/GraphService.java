package com.cognifide.cq.cqsm.graph;

import java.util.Iterator;
import java.util.List;

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

import com.cognifide.cq.cqsm.graph.data.Edge;
import com.cognifide.cq.cqsm.graph.data.Graph;
import com.cognifide.cq.cqsm.graph.data.Node;
import com.google.common.collect.Lists;

@Component
@Service(GraphService.class)
public class GraphService {

	@Reference
	private transient ResourceResolverFactory resourceResolverFactory;

	public Graph createGraph(String groupId) {
		List<Node> nodes = Lists.newArrayList();
		List<Edge> edges = Lists.newArrayList();
		try {
			ResourceResolver resourceResolver = resourceResolverFactory.getAdministrativeResourceResolver(null);
			UserManager userManager = resourceResolver.adaptTo(UserManager.class);

			Group group = (Group)userManager.getAuthorizable(groupId);

			Iterator<Authorizable> children = group.getDeclaredMembers();
			Iterator<Group> parents = group.memberOf();

			for (Authorizable a : Lists.newArrayList(children)) {

			}

			for (Authorizable a : Lists.newArrayList(parents)) {

			}
		} catch(LoginException | RepositoryException exc ) {

		}
		return new Graph(nodes, edges);
	}
}
