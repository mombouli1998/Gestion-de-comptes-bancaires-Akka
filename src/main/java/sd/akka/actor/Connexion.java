/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package sd.akka.actor;

/**
 *
 * @author trini
 */
import java.sql.*;
import java.util.Hashtable;
import java.util.ArrayList;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
public class Connexion
{   
  List<String> listOperations= new ArrayList<String>();

    LocalDateTime myDateobj= LocalDateTime.now();
    DateTimeFormatter myformaobject = DateTimeFormatter.ofPattern("dd-MM-YYYY HH:mm:ss");
    Connection con;
   public void Connxion() {
       
       try
    {
      //étape 1: charger la classe de driver
      Class.forName("com.mysql.jdbc.Driver");

      //étape 2: créer l'objet de connexion
      con = DriverManager.getConnection(
      "jdbc:mysql://localhost:3306/connexion?characterEncoding=utf-8", "root", "");

   System.out.print("connexion");
        
    }
    catch(Exception e){ 
          System.out.print("toto");
      System.out.println(e);
    }
 
   }  
   
     public Hashtable<Integer, Integer> getAllClient(){
    //Hastable que l'on va renvoy
    Hashtable<Integer, Integer> lesClients = new Hashtable<>();
    try {
        Statement statement = con.createStatement();

        //Pour chaque ligne de la table Client
        ResultSet resultat = statement.executeQuery("SELECT idClient, idBanquier FROM Client");
        int idClient;
        int idBanquier;
        while (resultat.next()) {
            idClient = resultat.getInt("idClient");
            idBanquier = resultat.getInt("idBanquier");
            lesClients.put(idClient, idBanquier);
        }
        resultat.close();
        statement.close();
    } catch (SQLException e) {
        e.printStackTrace();
    }
    return lesClients;
}

   
     public ArrayList<Integer> getAllBanquier(){
    ArrayList<Integer> lesBanquiers = new ArrayList<>();
    try {
        Statement statement = con.createStatement();
        ResultSet resultat = statement.executeQuery("SELECT * FROM Banquier");
        int idBanquier;

        //Pour chaque ligne de la table Banquier
        while (resultat.next()) {
            idBanquier = resultat.getInt("idBanquier");
            lesBanquiers.add(idBanquier);
        }
        resultat.close();
        statement.close();
    } catch (SQLException e) {
        e.printStackTrace();
    }
    return lesBanquiers;
}
     public void updateSolde(int montant, int idClient){
    try {
        int id=idClient;
        int solde=montant;
       
        //Requête préparé
        PreparedStatement preparedStatement =con.prepareStatement("UPDATE Client" +  " SET solde = (solde+?) " +  "WHERE idClient = ?");

        //Donne les attributs à la requete préparé
        preparedStatement.setInt(1, montant);
        preparedStatement.setInt(2, idClient);

        //Executer la requete
        preparedStatement.executeUpdate();
         System.out.println("Le client "+id+" a fait un dépot de "+solde+" a "+myDateobj.format(myformaobject) );
         System.out.println(); 
        //Fermeture du preparedStatement
        preparedStatement.close();
       

        //System.out.println(idClient);
    } catch (SQLException e) {
        e.printStackTrace();
    }
}
       public void RetraitSolde(int montant, int idClient){
    try {
        //Re
        int id=idClient;
        int solde=(montant);
        
        int s=getSoldeClient(idClient);
        if(s-montant<=10){
            System.out.print("retrait impossible");
        }
        else{
        PreparedStatement preparedStatement =con.prepareStatement("UPDATE Client" +  " SET solde = (solde-?) " +  "WHERE idClient = ?");

        //Donne les attributs à la requete préparé
        preparedStatement.setInt(1, montant);
        preparedStatement.setInt(2, idClient);
        //Executer la requete
        preparedStatement.executeUpdate();
          System.out.println("Le client " +id+" a fait un Retrait de "+solde+" a "+myDateobj.format(myformaobject) );
          System.out.println(); 
        //Fermeture du preparedStatement
        preparedStatement.close();}

        //System.out.println(idClient);
    } catch (SQLException e) {
        e.printStackTrace();
    }
}

   
   public int getSoldeClient(int idClient){
    int solde = 0;
    int id=idClient;
    try{
        //Création de la requete préparé
        PreparedStatement preparedStatement = con.prepareStatement("SELECT solde FROM Client WHERE IDCLIENT=?");

        //Donne les attributs à la requete préparé
        preparedStatement.setInt(1, idClient);

        //Donne les attributs à la requête préparé
        ResultSet resultat = preparedStatement.executeQuery();

        while (resultat.next()){
            solde = resultat.getInt("solde");
           System.out.println("Le client" +id+" a consulté son solde qui est de "+solde+" à "+myDateobj.format(myformaobject) );
        }
 
 
        //fermeture du ResultSet
       // resultat.close();

        //Fermeture du PreparedStatement
        preparedStatement.close();
    } catch (SQLException e) {
        e.printStackTrace();
    }
    return solde ;
     
   }

   
   
}