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
            requestedGroup : {
                shape : 'icon',
                icon : {
                    face : 'Ionicons',
                    code : '\uf47c',
                    size : 50,
                    color : '#00ff00'
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
        var settingsContainer = document.getElementById('graph-settings');
        $(settingsContainer).empty();
        options.configure.container = settingsContainer;
        // initialize your network!
        groupsGraph = new vis.Network(container, data, options);
        
        groupsGraph.on("click", function (params) {
            if (params.nodes.length > 0) {
                if ((typeof params.nodes[0] === 'string') && (params.nodes[0].indexOf('cluster') !== -1)) {
                    groupsGraph.clustering.openCluster(params.nodes[0]);
                } else {
                    groupsGraph.clustering.clusterByConnection(params.nodes[0], {clusterNodeProperties: {
                        label: "groups",
                        color: '#ebeaed'
                    }});
                }
            }
        });
    };

    function getResult(groupId, showChildren, showParents, maxDepth) {
        $("#results-message").empty().append("Waiting for results...");
        $.ajax({
            type : "GET",
            url : "/bin/createGroupGraph?group=" + groupId + "&showChildren=" + showChildren + "&showParents=" + showParents + "&maxDepth=" + maxDepth,
            dataType : "json",
            success : function(data) {
                draw(data.data);
                $("#results-message").empty().append("Analysis finished");
            },
            error : function(data) {
              var responseInfo = data.responseJSON || {};
              $("#results-message").empty().append(responseInfo.message || "Unexpected error occurred.");
            }
        });
    };

    function getCsvResult(group, showChildren, showParents, maxDepth) {
        $("#results-message").empty().append("Waiting for export...");
        $.ajax({
            type : "GET",
            url : "/bin/createCsvGroupGraph?group=" + group + "&showChildren=" + showChildren + "&showParents=" + showParents + "&maxDepth=" + maxDepth,
            dataType : "text",
            success : function(data) {
                var anchor = $('<a/>');
                 anchor.attr({
                     href: 'data:attachment/csv;charset=utf-8,' + encodeURI(data),
                     target: '_blank',
                     download: 'graph.csv'
                 })[0].click();
                $("#results-message").empty().append("Export finished");
            },
            error : function(data) {

        var responseInfo = JSON.parse(data.responseText || "{}");
        $("#results-message").empty().append(responseInfo.message || "Unexpected error occurred.");
            }
        });
    };

    api.init = function($elements) {

        $elements.each(function() {
            $(".analyse-button").click(function() {
                var group = $('input#group').val();
                var maxDepth = $('#maxDepth').val();
                var showChildren = $('input#showChildren').is(':checked');
                var showParents = $('input#showParents').is(':checked');
                getResult(group, showChildren, showParents, maxDepth);
            });
            $(".csv-button").click(function() {
                var group = $('input#group').val();
                var maxDepth = $('#maxDepth').val();
                var showChildren = $('input#showChildren').is(':checked');
                var showParents = $('input#showParents').is(':checked');
                getCsvResult(group, showChildren, showParents, maxDepth);
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
