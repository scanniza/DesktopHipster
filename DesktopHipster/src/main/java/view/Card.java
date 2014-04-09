package view;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.GridLayout;

import javax.swing.*;

@SuppressWarnings("serial")
public abstract class Card extends JPanel {
	private JPanel north, south, center, west, east, 
		subNorth, subSouth, subCenter, subWest, subEast;
	
	public Card() {
		super();
		cardInitialize();
	}
	
	public void cardInitialize() {
		north = new JPanel();
		south = new JPanel();
		center = new JPanel();
		west = new JPanel();
		east = new JPanel();
		
		north.setLayout(new GridLayout(1,1));
		south.setLayout(new GridLayout(1,1));
		center.setLayout(new GridLayout(1,1));
		west.setLayout(new GridLayout(1,1));
		east.setLayout(new GridLayout(1,1));
		
		north.setPreferredSize(new Dimension(0,50));
		south.setPreferredSize(new Dimension(0,50));
		center.setPreferredSize(new Dimension(0,0));
		west.setPreferredSize(new Dimension(150,0));
		east.setPreferredSize(new Dimension(150,0));
		
		setLayout(new BorderLayout());
		add(north, BorderLayout.NORTH);
		add(south, BorderLayout.SOUTH);
		add(center, BorderLayout.CENTER);
		add(west, BorderLayout.WEST);
		add(east, BorderLayout.EAST);
	}
	
	protected void addNorth(JPanel north) {
		subNorth = north;
		this.north.add(north);
	}
	
	protected void addSouth(JPanel south) {	
		subSouth = south;
		this.south.add(south);
	}
	
	protected void addCenter(JPanel center) {
		subCenter = center;
		this.center.add(center);
	}
	
	protected void addWest(JPanel west) {
		subWest = west;
		this.west.add(west);
	}
	
	protected void addEast(JPanel east) {
		subEast = east;
		this.east.add(east);
	}
	
	@Override 
	public void setBackground(Color c) {
		if(east != null) {
			west.setBackground(c);
			east.setBackground(c);
			center.setBackground(c);
			north.setBackground(c);
			south.setBackground(c);
		}
		if(subNorth != null) {
			subNorth.setBackground(c);
		}
		if(subSouth != null) {
			subSouth.setBackground(c);
		}
		if(subCenter != null) {
			subCenter.setBackground(c);
		}
		if(subWest != null) {
			subWest.setBackground(c);
		}
		if(subEast != null) {
			subEast.setBackground(c);
		}
	}
}