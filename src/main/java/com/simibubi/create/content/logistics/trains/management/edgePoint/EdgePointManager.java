package com.simibubi.create.content.logistics.trains.management.edgePoint;

import com.simibubi.create.content.logistics.trains.TrackEdge;
import com.simibubi.create.content.logistics.trains.TrackGraph;
import com.simibubi.create.content.logistics.trains.TrackNode;
import com.simibubi.create.content.logistics.trains.TrackNodeLocation;
import com.simibubi.create.content.logistics.trains.management.signal.TrackEdgePoint;
import com.simibubi.create.foundation.utility.Couple;
import com.simibubi.create.foundation.utility.Iterate;

public class EdgePointManager {

	public static <T extends TrackEdgePoint> void onEdgePointAdded(TrackGraph graph, T point, EdgePointType<T> type) {
		Couple<TrackNodeLocation> edgeLocation = point.edgeLocation;
		Couple<TrackNode> startNodes = edgeLocation.map(graph::locateNode);
		Couple<TrackEdge> startEdges = startNodes.mapWithParams((l1, l2) -> graph.getConnectionsFrom(l1)
			.get(l2), startNodes.swap());

		for (boolean front : Iterate.trueAndFalse) {
			TrackNode node1 = startNodes.get(front);
			TrackNode node2 = startNodes.get(!front);
			TrackEdge startEdge = startEdges.get(front);
			startEdge.getEdgeData()
				.addPoint(node1, node2, startEdge, point);
		}
	}

	public static <T extends TrackEdgePoint> void onEdgePointRemoved(TrackGraph graph, T point, EdgePointType<T> type) {
		point.onRemoved(graph);
		Couple<TrackNodeLocation> edgeLocation = point.edgeLocation;
		Couple<TrackNode> startNodes = edgeLocation.map(graph::locateNode);
		startNodes.forEachWithParams((l1, l2) -> {
			TrackEdge trackEdge = graph.getConnectionsFrom(l1)
				.get(l2);
			trackEdge.getEdgeData()
				.removePoint(l1, l2, trackEdge, point);
		}, startNodes.swap());
	}

}
