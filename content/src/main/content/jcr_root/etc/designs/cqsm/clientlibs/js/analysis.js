/*-
 * ========================LICENSE_START=================================
 * AEM Permission Management
 * %%
 * Copyright (C) 2013 - 2016 Cognifide Limited
 * %%
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 * =========================LICENSE_END==================================
 */
var groupsGraph;

Cog.component.analysis = (function($) {

    var api = {};

    var options = {
        edges : {
            arrows : {
                to : {
                    enabled : true,
                    scaleFactor : 1,
                    type : 'arrow'
                },
                middle : {
                    enabled : false,
                    scaleFactor : 1,
                    type : 'arrow'
                },
                from : {
                    enabled : false,
                    scaleFactor : 1,
                    type : 'arrow'
                }
            },
            arrowStrikethrough: false
        },
        layout: {
            hierarchical: {
              enabled:true,
              direction: 'UD',        // UD, DU, LR, RL
              sortMethod: 'directed'   // hubsize, directed
            }
        },
        physics:{
            enabled: false,
        },
        configure: {
            enabled: true,
            filter: function (option, path) {
              return ((option === 'enabled') && (path.indexOf('physics') !== -1)) || ((path.indexOf('hierarchical') !== -1) && ((option === 'sortMethod') || (option === 'enabled') ));
            }
        },
        groups : {
            usergroups : {
                shape : 'icon',
                icon : {
                    face : 'Ionicons',
                    code : '\uf47c',
                    size : 50,
                    color : '#4b4a4c'
                },
                margin: 5
            },
            users : {
                shape : 'icon',
                icon : {
                    face : 'Ionicons',
                    code : '\uf47e',
                    size : 50,
                    color : '#4b4a4c'
                },
                margin: 5
            }
        }
    };

    var draw = function(data) {
        // create a network
        var container = document.getElementById('servlet-result');
        // initialize your network!
        groupsGraph = new vis.Network(container, data, options);
    };

    var helper = Cog.component.cqsmHelper;

    function getResult(groupId, showChildren, showParents) {
        this.groupId = groupId;
        $.ajax({
            type : "GET",
            url : "/bin/createGroupGraph?group=" + groupId + "&showChildren=" + showChildren + "&showParents=" + showParents,
            dataType : "json",
            success : function(data) {
                draw(data.data);
            },
            error : function() {
                $("#servlet-result").empty().append("error");
            }
        });
    };

    function getCsvResult(group, showChildren, showParents) {
		$.ajax({
			type : "GET",
			url : "/bin/createCsvGroupGraph?group=" + group + "&showChildren=" + showChildren + "&showParents=" + showParents,
			dataType : "text",
			success : function(data) {
				var anchor = $('<a/>');
				 anchor.attr({
					 href: 'data:attachment/csv;charset=utf-8,' + encodeURI(data),
					 target: '_blank',
					 download: 'graph.csv'
				 })[0].click();
			},
			error : function() {
				$("#servlet-result").empty().append("error");
			}
		});
	};

    api.init = function($elements) {
        $elements.each(function() {
            $(".analyse-button").click(function() {
                var group = $('input#group').val();
                var showChildren = $('input#showChildren').is(':checked');
                var showParents = $('input#showParents').is(':checked');
                getResult(group, showChildren, showParents);
            });
            $(".csv-button").click(function() {
				var group = $('input#group').val();
				getCsvResult(group);
			});
        });
    };

    return api;
}(COGjQuery));

Cog.register({
    name : 'analysis',
    api : Cog.component.analysis,
    selector : '#analysisPage'
});
