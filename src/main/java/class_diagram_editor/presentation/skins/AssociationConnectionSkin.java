package class_diagram_editor.presentation.skins;

import class_diagram_editor.presentation.skins.arrows.AssociationArrowHead;
import class_diagram_editor.presentation.skins.arrows.UMLArrow;
import de.tesis.dynaware.grapheditor.GConnectionSkin;
import de.tesis.dynaware.grapheditor.GJointSkin;
import de.tesis.dynaware.grapheditor.model.GConnection;
import de.tesis.dynaware.grapheditor.utils.Arrow;
import javafx.geometry.Point2D;
import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.transform.Rotate;

import java.util.List;
import java.util.Map;

public class AssociationConnectionSkin extends GConnectionSkin {

    public static final String TYPE = "association-connection";

    private static final String STYLE_CLASS = "association-connection";

    private static final double OFFSET_FROM_CONNECTOR = ConnectorSkin.RADIUS;

    private final Arrow arrow;
    private final Group root;
    private final Label identifier;

    public AssociationConnectionSkin(GConnection connection) {
        super(connection);

        this.root = new Group();

        this.arrow = new UMLArrow(new AssociationArrowHead());
        arrow.setManaged(false);
        arrow.getStyleClass().setAll(STYLE_CLASS);
        arrow.setHeadWidth(28);
        arrow.setHeadLength(15);

        identifier = new Label(connection.getId());

        root.getChildren().addAll(arrow, identifier);
    }

    @Override
    protected void selectionChanged(boolean isSelected) {

    }

    @Override
    public void setJointSkins(List<GJointSkin> jointSkins) {

    }

    @Override
    public void draw(final Map<GConnectionSkin, Point2D[]> allPoints) {
        final Point2D[] points = allPoints == null ? null : allPoints.get(this);

        if (points != null && points.length >= 2)
        {
            final Point2D start = points[0];
            final Point2D end = points[points.length - 1];

            ArrowUtils.draw(arrow, start, end, OFFSET_FROM_CONNECTOR);

            final double angleInRadians = getAngleInRadians(start, end);

            final double distance = start.distance(end);

            final double verticalLineOffset = 10;
            identifier.setTranslateX(start.getX() + distance / 2 * Math.sin(angleInRadians));
            identifier.setTranslateY(start.getY() + (distance / 2 + verticalLineOffset) * Math.cos(angleInRadians));
        }
    }

    private double getAngleInRadians(Point2D start, Point2D end) {
        final double deltaX = end.getX() - start.getX();
        final double deltaY = end.getY() - start.getY();

        return Math.atan2(deltaX, deltaY);
    }

    @Override
    public Node getRoot() {
        return root;
    }
}