package ladysnake.views;

import ladysnake.helpers.events.I_Observer;
import ladysnake.models.PendingGraph;
import org.jgrapht.Graph;
import org.jgrapht.ListenableGraph;
import org.jgrapht.ext.JGraphXAdapter;

public class VisualPendingGraph extends JGraphXAdapter<String, String> implements I_Observer{
    public VisualPendingGraph(ListenableGraph<String, String> graph) { super(graph); }
    public VisualPendingGraph(Graph<String, String> graph) { super(graph); }

    @Override
    public void handleEvent(String eventName, Object... args) {
        if(!eventName.equals(PendingGraph.REFRESH))
            return;
        this.refresh();
    }
}
