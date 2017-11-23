package com.cognifide.cq.cqsm.graph;

import java.util.Iterator;

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

import com.google.common.collect.Lists;

@Component
@Service(GraphService.class)
public class GraphService {

	@Reference
	private transient ResourceResolverFactory resourceResolverFactory;

	public void createGraph(String groupId) {
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
	}
}
