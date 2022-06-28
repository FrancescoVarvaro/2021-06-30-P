package it.polito.tdp.genes.db;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import it.polito.tdp.genes.model.Coppie;
import it.polito.tdp.genes.model.Genes;
import it.polito.tdp.genes.model.Interactions;


public class GenesDao {
	
	public List<Genes> getAllGenes(){
		String sql = "SELECT DISTINCT GeneID, Essential, Chromosome FROM Genes";
		List<Genes> result = new ArrayList<Genes>();
		Connection conn = DBConnect.getConnection();

		try {
			PreparedStatement st = conn.prepareStatement(sql);
			ResultSet res = st.executeQuery();
			while (res.next()) {

				Genes genes = new Genes(res.getString("GeneID"), 
						res.getString("Essential"), 
						res.getInt("Chromosome"));
				result.add(genes);
			}
			res.close();
			st.close();
			conn.close();
			return result;
			
		} catch (SQLException e) {
			throw new RuntimeException("Database error", e) ;
		}
	}
	
	public List<String> getAllLocalization(){
		
		String sql = "SELECT distinct c.Localization "
				+ "FROM classification c ";
		List<String> result = new ArrayList<String>();
		Connection conn = DBConnect.getConnection();

		try {
			PreparedStatement st = conn.prepareStatement(sql);
			ResultSet res = st.executeQuery();
			while (res.next()) {
				result.add(new String(res.getString("c.Localization")));
			}
			res.close();
			st.close();
			conn.close();
			return result;
			
		} catch (SQLException e) {
			throw new RuntimeException("Database error", e) ;
		}
	}
	
	public List<Coppie> getArchi(){
		String sql = "SELECT  i.*, T.localization1,T.localization2 "
				+ "FROM interactions i, (SELECT c.Localization AS localization1, c1.Localization AS localization2, c.GeneID AS geneId1,c1.GeneID AS geneId2 "
				+ "FROM classification c, classification c1 "
				+ "WHERE c.GeneID<>c1.GeneID AND c.Localization<>c1.Localization) AS T "
				+ "WHERE i.GeneID1=T.geneId1 AND i.GeneID2=T.geneId2";
		
		List<Coppie> result = new ArrayList<Coppie>();
		Connection conn = DBConnect.getConnection();

		try {
			PreparedStatement st = conn.prepareStatement(sql);
			ResultSet res = st.executeQuery();
			while (res.next()) {
				result.add(new Coppie(res.getString("i.GeneID1"), res.getString("i.GeneID2"), res.getString("i.Type"),
						res.getString("T.localization1"), res.getString("T.localization2")));
			}
			res.close();
			st.close();
			conn.close();
			return result;
			
		} catch (SQLException e) {
			throw new RuntimeException("Database error", e) ;
		}
	}
	
	public int getPeso(String localization1, String localization2){
		String sql = "SELECT COUNT(DISTINCT `Type`) AS peso "
				+ "FROM (SELECT  i.*, T.localization1,T.localization2 "
				+ "FROM interactions i, (SELECT c.Localization AS localization1, c1.Localization AS localization2, c.GeneID AS geneId1,c1.GeneID AS geneId2 "
				+ "FROM classification c, classification c1 "
				+ "WHERE c.GeneID<>c1.GeneID AND c.Localization<>c1.Localization) AS T "
				+ "WHERE i.GeneID1=T.geneId1 AND i.GeneID2=T.geneId2) AS T_1 "
				+ "WHERE (T_1.localization1 = ? AND T_1.localization2 = ?) "
				+ "OR (T_1.localization1 = ? AND T_1.localization2 = ?) ";
		
		Connection conn = DBConnect.getConnection();
		int peso;
		try {
			PreparedStatement st = conn.prepareStatement(sql);
			st.setString(1, localization1);
			st.setString(2, localization2);
			st.setString(3, localization2);
			st.setString(4, localization1);
			ResultSet res = st.executeQuery();
			res.next();
			peso = res.getInt("peso");
			res.close();
			st.close();
			conn.close();
			return peso;
			
		} catch (SQLException e) {
			throw new RuntimeException("Database error", e) ;
		}
	}
	
}
