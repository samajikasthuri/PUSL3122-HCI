package com.furnitureapp.views;

import java.awt.GraphicsConfiguration;

import javax.media.j3d.*;
import javax.vecmath.*;
import com.sun.j3d.utils.geometry.*;
import com.sun.j3d.utils.universe.*;
import javax.swing.*;
import com.sun.j3d.utils.geometry.Box;

public class Simple3DTest {
    public static void main(String[] args) {
        // Set up the 3D Canvas
        GraphicsConfiguration config = SimpleUniverse.getPreferredConfiguration();
        if (config == null) {
            System.err.println("Failed to get graphics configuration.");
            return;
        }

        // Create a Canvas3D object and add it to the JFrame
        Canvas3D canvas = new Canvas3D(config);
        JFrame frame = new JFrame("Simple 3D Test");
        frame.setSize(800, 600);
        frame.add(canvas);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setVisible(true);

        // Create a SimpleUniverse (the 3D universe) and add the scene
        SimpleUniverse universe = new SimpleUniverse(canvas);
        BranchGroup group = new BranchGroup();

        // Create a Box (3D object) and add it to the scene
        Box box = new Box(0.1f, 0.1f, 0.1f, new Appearance());
        group.addChild(box);

        // Add a light source for illumination
        addLight(group);

        // Add the group to the universe and start rendering
        universe.addBranchGraph(group);
    }

    // Add a light source to the scene for better visibility of the 3D objects
    private static void addLight(BranchGroup group) {
        DirectionalLight light = new DirectionalLight(true, new Color3f(1.0f, 1.0f, 1.0f), new Vector3f(1.0f, -1.0f, -1.0f));
        light.setInfluencingBounds(new BoundingSphere(new Point3d(0.0, 0.0, 0.0), 100.0));
        group.addChild(light);
    }
}
