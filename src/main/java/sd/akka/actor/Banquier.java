package sd.akka.actor;

import akka.actor.AbstractActor;
import akka.actor.Props;



public class Banquier extends AbstractActor {
    private final Connexion connexion;


    private Banquier(Connexion connexion){
        this.connexion = connexion;

    }
    @Override
    public AbstractActor.Receive createReceive() {
        return receiveBuilder()
                .match(GetDemandeDepot.class, this::GetDemandeDepot)
                .match(GetDemandeRetrait.class, this::GetDemandeRetrait)
                .match(GetSolde.class, this::GetSolde)
                .build();
    }

    public static Props props(Connexion connexion) {
        return Props.create(Banquier.class, connexion);
    }

    public void GetDemandeDepot(final GetDemandeDepot message){
        int idClient = message.idClient;
        int montant = message.montant;
        this.connexion.updateSolde(montant, idClient);
    }

    public void GetDemandeRetrait(final GetDemandeRetrait message){
        int idClient = message.idClient;
        int montant = message.montant;
       this.connexion.RetraitSolde(montant, idClient);

    }

    public void GetSolde(final GetSolde message){
        int idClient = message.idClient;
int t=this.connexion.getSoldeClient(idClient);
       

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
