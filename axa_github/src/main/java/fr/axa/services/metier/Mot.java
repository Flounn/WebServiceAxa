package fr.axa.services.metier;

public class Mot {

	private long id;
	private String mot;
	private long idConnexion;


	/**
	 * @return the id
	 */
	public long getId() {
		return id;
	}
	/**
	 * @param id the id to set
	 */
	public void setId(long id) {
		this.id = id;
	}
	/**
	 * @return the mot
	 */
	public String getMot() {
		return mot;
	}
	/**
	 * @param mot the mot to set
	 */
	public void setMot(String mot) {
		this.mot = mot;
	}
	/**
	 * @return the idConnexion
	 */
	public long getIdConnexion() {
		return idConnexion;
	}
	/**
	 * @param idConnexion the idConnexion to set
	 */
	public void setIdConnexion(long idConnexion) {
		this.idConnexion = idConnexion;
	}

}
