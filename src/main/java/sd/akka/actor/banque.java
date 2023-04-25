/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package sd.akka.actor;

/**
 *
 * @author trini
 */
import akka.actor.AbstractActor;
import akka.actor.ActorRef;
import akka.actor.Props;


import java.util.ArrayList;
import java.util.Hashtable;
public class banque extends AbstractActor  {
    Hashtable<Integer, Integer> lesClients;
    Hashtable<Integer, ActorRef> lesBanquiers;
    private final Connexion connexion;
      private banque(Connexion connexion){
        //Initialisation des hashtables et de la connexion
        lesClients = new Hashtable<>();
        lesBanquiers = new Hashtable<>();

        this.connexion = connexion;

        //Création des acteurs enfants
     
        this.lesClients = this.connexion.getAllClient();
      
        

//creation des acteurs banquier;
        
        ArrayList<Integer> lesBanquierBD = this.connexion.getAllBanquier();
        ActorRef banquier;
        for(int idBanquier : lesBanquierBD){
            banquier = getContext().actorOf(Banquier.props(this.connexion), "acteurClient"+idBanquier);
            this.lesBanquiers.put(idBanquier, banquier);
        }
    }
      
       public static Props props(Connexion connexion) {
        return Props.create(banque.class, connexion);
    }

      
      @Override
    public AbstractActor.Receive createReceive() {
        return receiveBuilder()
                .match(GetDemandeDepot.class, this::GetDemandeDepot)
                .match(GetDemandeRetrait.class, this::GetDemandeRetrait)
                .match(GetSolde.class, this::GetSolde)
                .build();
    }
      public void GetDemandeDepot(final GetDemandeDepot message){
         //Récupère l'id du client et le montant à déposer
        int idClient = message.idClient;
        int montant = message.montant;

        //Si le client existe bien
        if(lesClients.get(idClient) != null){
            //On récupère l'id du banquier
            int idBanquier = lesClients.get(idClient);

            //On récupère l'acteur banquier grace à son id
            ActorRef banquier = lesBanquiers.get(idBanquier);

            //Dit à l'acteur banquier de faire un dépot
            banquier.tell(new Banquier.GetDemandeDepot(idClient, montant), ActorRef.noSender());

        }else{
            System.out.println("Pas de client numéro : " + idClient);
        }
    }

    public void GetDemandeRetrait(final GetDemandeRetrait message){
      int idClient = message.idClient;
        int montant = message.montant;
        if(lesClients.get(idClient) != null){
         
            int nouveauSolde = this.connexion.getSoldeClient(idClient) - montant;
            if(nouveauSolde <= 10){
               System.out.println("Client "+idClient+"votre solde est de "+this.connexion.getSoldeClient(idClient)+" impossible de faire le retrait de "+montant);
          
            }else{
              
                int idBanquier = lesClients.get(idClient);
                ActorRef banquier = lesBanquiers.get(idBanquier);
                banquier.tell(new Banquier.GetDemandeRetrait(idClient, montant), ActorRef.noSender());
            }
        }else{
            System.out.println("Pas de client numéro : " + idClient);
        }
    }

    public void GetSolde(final GetSolde message){
         //Récupère l'id du client et le montant à déposer
        int idClient = message.idClient;

        //Si le client existe bien
        if(lesClients.get(idClient) != null){
            //On récupère l'id du banquier
            int idBanquier = lesClients.get(idClient);

            //On récupère l'acteur banquier grace à son id
            ActorRef banquier = lesBanquiers.get(idBanquier);
            
            //Dit à l'acteur banquier de faire un dépot
            banquier.tell(new Banquier.GetSolde(idClient), ActorRef.noSender());
          
 
        }

    }



    public static class GetDemandeDepot {
        public int idClient;
        public int montant;
        public GetDemandeDepot(int idClient, int montant) {
            this.idClient = idClient;
            this.montant = montant;
        }
    }
    public static class GetDemandeRetrait  {
        public int idClient;
        public int montant;
        public GetDemandeRetrait(int idClient, int montant) {
            this.idClient = idClient;
            this.montant = montant;
        }
    }
        public static class GetSolde  {
            public int idClient;
            public GetSolde (int idClient) {
                this.idClient = idClient;

            }
    }


}
