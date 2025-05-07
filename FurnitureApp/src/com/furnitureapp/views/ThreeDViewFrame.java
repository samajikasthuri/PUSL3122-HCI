package com.furnitureapp.views;
import com.furnitureapp.models.FurnitureItem;
import javax.swing.*;
import javax.media.j3d.*;
import javax.vecmath.*;
import com.sun.j3d.utils.geometry.*;
import com.sun.j3d.utils.universe.*;
import java.awt.*;
import java.util.List;
import com.sun.j3d.utils.geometry.Box;
import com.sun.j3d.utils.behaviors.mouse.*;

public class ThreeDViewFrame extends JFrame {
    private Canvas3D canvas;
    private SimpleUniverse universe;
    private List<FurnitureItem> furnitureItems;
    private TransformGroup viewTransformGroup;

    public ThreeDViewFrame(List<FurnitureItem> items) {
        this.furnitureItems = items;
        initializeWindow();
        setup3DEnvironment();
        createScene();
    }

    private void initializeWindow() {
        setTitle("3D Furniture Viewer");
        setSize(1000, 700);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
    }

    private void setup3DEnvironment() {
        // 1. Configure 3D graphics
        GraphicsConfiguration config = SimpleUniverse.getPreferredConfiguration();
        
        // 2. Create 3D canvas
        canvas = new Canvas3D(config);
        canvas.setPreferredSize(new Dimension(900, 600));
        getContentPane().add(canvas, BorderLayout.CENTER);
        
        // 3. Create universe
        universe = new SimpleUniverse(canvas);
        universe.getViewingPlatform().setNominalViewingTransform();
    }

    private void createScene() {
        // Create the root of the scene graph
        BranchGroup sceneRoot = new BranchGroup();
        
        // Create a transform group for the entire scene (for mouse behaviors)
        viewTransformGroup = new TransformGroup();
        viewTransformGroup.setCapability(TransformGroup.ALLOW_TRANSFORM_WRITE);
        viewTransformGroup.setCapability(TransformGroup.ALLOW_TRANSFORM_READ);
        
        // Create a content branch for all your objects
        BranchGroup contentBranch = new BranchGroup();
        
        // 1. Add lighting
        addLighting(contentBranch);
        
        // 2. Create room environment
        createRoom(contentBranch);
        
        // 3. Add furniture items
        for (FurnitureItem item : furnitureItems) {
            Node furnitureNode = createFurnitureNode(item);
            TransformGroup itemTg = positionFurniture(item);
            itemTg.addChild(furnitureNode);
            contentBranch.addChild(itemTg);
        }
        
        // Add content to the transform group
        viewTransformGroup.addChild(contentBranch);
        
        // Add the transform group to the scene root
        sceneRoot.addChild(viewTransformGroup);
        
        // 4. Compile and show scene
        sceneRoot.compile();
        universe.addBranchGraph(sceneRoot);
        
        // 5. Set camera position
        setCameraView();
        
        // 6. Setup mouse controls
        setupMouseControls();
    }

    private void addLighting(BranchGroup scene) {
        BoundingSphere bounds = new BoundingSphere(new Point3d(0.0, 0.0, 0.0), 100.0);
        
        // Ambient light
        AmbientLight ambient = new AmbientLight(new Color3f(0.4f, 0.4f, 0.4f));
        ambient.setInfluencingBounds(bounds);
        scene.addChild(ambient);
        
        // Directional light
        DirectionalLight light = new DirectionalLight(
            new Color3f(0.9f, 0.9f, 0.9f),
            new Vector3f(-1f, -1f, -1f)
        );
        light.setInfluencingBounds(bounds);
        scene.addChild(light);
    }

    private void createRoom(BranchGroup scene) {
        // Floor
        Appearance floorApp = new Appearance();
        floorApp.setMaterial(new Material(
            new Color3f(0.8f, 0.8f, 0.8f),
            new Color3f(0.0f, 0.0f, 0.0f),
            new Color3f(0.7f, 0.7f, 0.7f),
            new Color3f(0.5f, 0.5f, 0.5f),
            10.0f
        ));
        
        Box floor = new Box(5.0f, 0.1f, 5.0f, Primitive.GENERATE_NORMALS, floorApp);
        scene.addChild(floor);
    }

    private Node createFurnitureNode(FurnitureItem item) {
        Appearance app = new Appearance();
        app.setMaterial(new Material(
            new Color3f(item.getColor3f()),
            new Color3f(0.0f, 0.0f, 0.0f),
            new Color3f(item.getColor3f()),
            new Color3f(1.0f, 1.0f, 1.0f),
            64.0f
        ));
        
        float width = item.getWidth() / 100f;
        float height = item.getHeight() / 100f;
        float depth = width * 0.5f;
        
        switch(item.getType().toLowerCase()) {
            case "chair":
                return createChair(width, height, depth, app);
            case "table":
                return createTable(width, height, depth, app);
            case "sofa":
                return createSofa(width, height, depth, app);
            default:
                return new Box(width, height, depth, Primitive.GENERATE_NORMALS, app);
        }
    }
    private TransformGroup positionFurniture(FurnitureItem item) {
        TransformGroup tg = new TransformGroup();
        tg.setCapability(TransformGroup.ALLOW_TRANSFORM_WRITE);
        
        Transform3D transform = new Transform3D();
        float x = (item.getX() - 400) / 100f;
        float y = item.getHeight() / 200f;
        float z = (item.getY() - 300) / 100f;
        
        transform.setTranslation(new Vector3f(x, y, z));
        tg.setTransform(transform);
        return tg;
    }

    // Additional furniture creation methods that were referenced but not shown previously
    private Node createTable(float width, float height, float depth, Appearance appearance) {
    	   BranchGroup tableGroup = new BranchGroup();
    	    
    	    // Table top
    	    Box top = new Box(width, height/10, depth, Primitive.GENERATE_NORMALS, appearance);
    	    TransformGroup topTg = new TransformGroup();
    	    Transform3D topTransform = new Transform3D();
    	    topTransform.setTranslation(new Vector3f(0.0f, height - height/10, 0.0f));
    	    topTg.setTransform(topTransform);
    	    topTg.addChild(top);  // Add this line to actually include the table top
    	    tableGroup.addChild(topTg);
        
        // Legs
        float legRadius = Math.min(width, depth) / 10;
        float legHeight = height - height/5;
        
        addLeg(tableGroup, width-legRadius*2, legHeight/2, depth-legRadius*2, 
               legRadius, legHeight, appearance);
        addLeg(tableGroup, -width+legRadius*2, legHeight/2, depth-legRadius*2, 
               legRadius, legHeight, appearance);
        addLeg(tableGroup, width-legRadius*2, legHeight/2, -depth+legRadius*2, 
               legRadius, legHeight, appearance);
        addLeg(tableGroup, -width+legRadius*2, legHeight/2, -depth+legRadius*2, 
               legRadius, legHeight, appearance);
        
        return tableGroup;
    }

    private Node createSofa(float width, float height, float depth, Appearance appearance) {
        BranchGroup sofaGroup = new BranchGroup();
        
        // Make sofa wider than chair (3 seats wide)
        float seatWidth = width / 3;
        
        // Base (extends full width)
        Box base = new Box(width, height/4, depth, Primitive.GENERATE_NORMALS, appearance);
        TransformGroup baseTg = new TransformGroup();
        Transform3D baseTransform = new Transform3D();
        baseTransform.setTranslation(new Vector3f(0.0f, height/4, 0.0f));
        baseTg.setTransform(baseTransform);
        baseTg.addChild(base);
        sofaGroup.addChild(baseTg);
        
        // Back (extends full width)
        Box back = new Box(width, height/2, depth/4, Primitive.GENERATE_NORMALS, appearance);
        TransformGroup backTg = new TransformGroup();
        Transform3D backTransform = new Transform3D();
        backTransform.setTranslation(new Vector3f(0.0f, height/2, -depth + depth/4));
        backTg.setTransform(backTransform);
        backTg.addChild(back);
        sofaGroup.addChild(backTg);
        
        // Arms (thicker than chair legs)
        float armThickness = depth/3;
        Box leftArm = new Box(width/8, height/2, armThickness, Primitive.GENERATE_NORMALS, appearance);
        TransformGroup leftArmTg = new TransformGroup();
        Transform3D leftArmTransform = new Transform3D();
        leftArmTransform.setTranslation(new Vector3f(width - width/8, height/2, 0.0f));
        leftArmTg.setTransform(leftArmTransform);
        leftArmTg.addChild(leftArm);
        sofaGroup.addChild(leftArmTg);
        
        Box rightArm = new Box(width/8, height/2, armThickness, Primitive.GENERATE_NORMALS, appearance);
        TransformGroup rightArmTg = new TransformGroup();
        Transform3D rightArmTransform = new Transform3D();
        rightArmTransform.setTranslation(new Vector3f(-width + width/8, height/2, 0.0f));
        rightArmTg.setTransform(rightArmTransform);
        rightArmTg.addChild(rightArm);
        sofaGroup.addChild(rightArmTg);
        
        // Legs (four legs like chair/table but thicker)
        float legRadius = Math.min(width, depth) / 10;
        float legHeight = height/4;
        
        addLeg(sofaGroup, width-legRadius*3, legHeight/2, depth-legRadius*2, 
               legRadius, legHeight, appearance);
        addLeg(sofaGroup, -width+legRadius*3, legHeight/2, depth-legRadius*2, 
               legRadius, legHeight, appearance);
        addLeg(sofaGroup, width-legRadius*3, legHeight/2, -depth+legRadius*2, 
               legRadius, legHeight, appearance);
        addLeg(sofaGroup, -width+legRadius*3, legHeight/2, -depth+legRadius*2, 
               legRadius, legHeight, appearance);
        
        return sofaGroup;
    }
    private void addLeg(BranchGroup group, float x, float y, float z, 
                       float radius, float height, Appearance appearance) {
        Cylinder leg = new Cylinder(radius, height, Primitive.GENERATE_NORMALS, appearance);
        TransformGroup legTg = new TransformGroup();
        Transform3D legTransform = new Transform3D();
        legTransform.setTranslation(new Vector3f(x, y, z));
        legTg.setTransform(legTransform);
        group.addChild(legTg);
        legTg.addChild(leg);
    }

    private Node createChair(float width, float height, float depth, Appearance appearance) {
        BranchGroup chairGroup = new BranchGroup();
        
        // Seat
        Box seat = new Box(width, height/10, depth, Primitive.GENERATE_NORMALS, appearance);
        TransformGroup seatTg = new TransformGroup();
        Transform3D seatTransform = new Transform3D();
        seatTransform.setTranslation(new Vector3f(0.0f, height/2, 0.0f));
        seatTg.setTransform(seatTransform);
        seatTg.addChild(seat);
        chairGroup.addChild(seatTg);
        
        // Back
        Box back = new Box(width, height/2, depth/5, Primitive.GENERATE_NORMALS, appearance);
        TransformGroup backTg = new TransformGroup();
        Transform3D backTransform = new Transform3D();
        backTransform.setTranslation(new Vector3f(0.0f, height, -depth + depth/5));
        backTg.setTransform(backTransform);
        backTg.addChild(back);
        chairGroup.addChild(backTg);
        
        // Legs - Adding four legs like the table
        float legRadius = Math.min(width, depth) / 12;
        float legHeight = height/2 - height/10;
        
        addLeg(chairGroup, width-legRadius*2, legHeight/2, depth-legRadius*2, 
               legRadius, legHeight, appearance);
        addLeg(chairGroup, -width+legRadius*2, legHeight/2, depth-legRadius*2, 
               legRadius, legHeight, appearance);
        addLeg(chairGroup, width-legRadius*2, legHeight/2, -depth+legRadius*2, 
               legRadius, legHeight, appearance);
        addLeg(chairGroup, -width+legRadius*2, legHeight/2, -depth+legRadius*2, 
               legRadius, legHeight, appearance);
        
        return chairGroup;
    }
    private void setupMouseControls() {
        // Create bounds for mouse behaviors
        BoundingSphere bounds = new BoundingSphere(new Point3d(0.0, 0.0, 0.0), 100.0);
        
        // Mouse rotate behavior
        MouseRotate mouseRotate = new MouseRotate();
        mouseRotate.setTransformGroup(viewTransformGroup);
        mouseRotate.setSchedulingBounds(bounds);
        mouseRotate.setFactor(0.005); // Adjust rotation speed
        
        // Mouse zoom behavior
        MouseZoom mouseZoom = new MouseZoom();
        mouseZoom.setTransformGroup(viewTransformGroup);
        mouseZoom.setSchedulingBounds(bounds);
        mouseZoom.setFactor(0.0005); // Adjust zoom speed
        
        // Mouse translate behavior
        MouseTranslate mouseTranslate = new MouseTranslate();
        mouseTranslate.setTransformGroup(viewTransformGroup);
        mouseTranslate.setSchedulingBounds(bounds);
        mouseTranslate.setFactor(0.05); // Adjust translation speed
        
        // Add behaviors to a branch group
        BranchGroup behaviorGroup = new BranchGroup();
        behaviorGroup.addChild(mouseRotate);
        behaviorGroup.addChild(mouseZoom);
        behaviorGroup.addChild(mouseTranslate);
        behaviorGroup.compile();
        
        universe.addBranchGraph(behaviorGroup);
    }

    
    private void setCameraView() {
        ViewingPlatform vp = universe.getViewingPlatform();
        Transform3D view = new Transform3D();
        view.lookAt(
            new Point3d(8, 5, 10),  // Increased eye position for better viewing
            new Point3d(0, 1, 0),
            new Vector3d(0, 1, 0)
        );
        view.invert();
        vp.getViewPlatformTransform().setTransform(view);
    }

} 