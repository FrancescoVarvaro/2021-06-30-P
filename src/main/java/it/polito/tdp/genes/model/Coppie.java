package it.polito.tdp.genes.model;

public class Coppie {
	private String g1;
	private String g2;
	private String l1;
	private String l2;
	private String type;
	
	public Coppie(String g1, String g2, String type, String l1, String l2) {
		super();
		this.g1 = g1;
		this.g2 = g2;
		this.l1 = l1;
		this.l2 = l2;
		this.type = type;
	}
	public String getG1() {
		return g1;
	}
	public void setG1(String g1) {
		this.g1 = g1;
	}
	public String getG2() {
		return g2;
	}
	public void setG2(String g2) {
		this.g2 = g2;
	}
	public String getL1() {
		return l1;
	}
	public void setL1(String l1) {
		this.l1 = l1;
	}
	public String getL2() {
		return l2;
	}
	public void setL2(String l2) {
		this.l2 = l2;
	}
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	
	
}
