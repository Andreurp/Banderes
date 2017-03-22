
package net.andreu.Banderes;

public class Pais {

	private String nom;
	private String[] colors;
	
	public Pais(String nom, String[] colors) {
		this.nom = nom;
		this.colors = colors;
	}

	public String getNom() {
		return nom;
	}

	public String[] getColors() {
		return colors;
	}
}
