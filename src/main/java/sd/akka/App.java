package sd.akka;


import akka.actor.ActorRef;
import akka.actor.ActorSystem;
import sd.akka.actor.Connexion;
import sd.akka.actor.banque;



public class App {
	public static void main(String[] args) {
		  Connexion c= new Connexion();
                   c.Connxion();
 //Création de l'acteur system pour pouvoir crée des acteurs
		ActorSystem actorSystem = ActorSystem.create();
        //Création d'un acteur banque en lui donnant la connexion
       ActorRef Banque = actorSystem.actorOf(banque.props(c), "banque");
      
      for(int i=0;i<5;i++){
          int id=(int) (1+Math.random()*(4)); 
          int op=(int) (1+Math.random()*(3));
          switch(op){
               case 1:
                    
                        Banque.tell(new banque.GetDemandeRetrait(id, 100), ActorRef.noSender());
                 break;
                 case 2:
                   Banque.tell(new banque.GetDemandeDepot(id, 10), ActorRef.noSender());
                     
                     
                       
                 break;
                 case 3:
                     
                      
                  Banque.tell(new banque.GetSolde(id), ActorRef.noSender());
                 break;
          }
      }
     
}
}
      
    