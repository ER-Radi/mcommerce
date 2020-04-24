package com.clientui.controller;

import com.clientui.beans.CommandeBean;
import com.clientui.beans.ProductBean;
import com.clientui.proxies.MicroserviceCommandeProxy;
import com.clientui.proxies.MicroserviceProduitsProxy;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.Date;
import java.util.List;

@Controller
public class ClientController {

    @Autowired
    MicroserviceProduitsProxy mProduitProxy;

    @Autowired
    MicroserviceCommandeProxy mCommandeProxy;


    /*
     * Étape (1)
     * Opération qui récupère la liste des produits et on les affichent dans la page d'accueil.
     * Les produits sont récupérés grâce à ProduitsProxy
     * On fini par retourner la page Accueil.html à laquelle on passe la liste d'objets "produits" récupérés.
     * */
    @RequestMapping("/")
    public String accueil(Model pModel) {

        List<ProductBean> produits = mProduitProxy.listeDesProduits();

        pModel.addAttribute("produits", produits);

        return "Accueil";
    }

    /*
     * Étape (2)
     * Opération qui récupère les détails d'un produit
     * On passe l'objet "produit" récupéré et qui contient les détails en question à  FicheProduit.html
     * */
    @RequestMapping("/details-produit/{id}")
    public String ficheProduit(@PathVariable int id, Model pModel) {

        ProductBean produit = mProduitProxy.recupererUnProduit(id);
        pModel.addAttribute("produit", produit);

        return "FicheProduit";
    }


    /*
     * Étape (3) et (4)
     * Opération qui fait appel au microservice de commande pour placer une commande et récupérer les détails de la commande créée
     * */
    @RequestMapping("/commander-produit/{idProduit}/{montant}")
    public String passerCommande(@PathVariable int pIdProduit, @PathVariable Double pMontant, Model pModel) {

        CommandeBean commande = new CommandeBean();

        //On renseigne les propriétés de l'objet de type CommandeBean que nous avons crée
        commande.setProductId(pIdProduit);
        commande.setQuantite(1);
        commande.setDateCommande(new Date());

        //appel du microservice commandes grâce à Feign et on récupère en retour les détails de la commande créée, notamment son ID (étape 4).
        CommandeBean commandeAjoutee = mCommandeProxy.ajouterCommande(commande);

        //on passe à la vue l'objet commande et le montant de celle-ci afin d'avoir les informations nécessaire pour le paiement
        pModel.addAttribute("commande", commandeAjoutee);
        pModel.addAttribute("montant", pMontant);

        return "Paiement";
    }

}
