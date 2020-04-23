package com.clientui.controller;

import com.clientui.beans.ProductBean;
import com.clientui.proxies.MicroserviceProduitsProxy;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

@Controller
public class ClientController {

    @Autowired
    MicroserviceProduitsProxy mProduitProxy;

    @RequestMapping("/")
    public String accueil(Model pModel) {

        List<ProductBean> produits = mProduitProxy.listeDesProduits();

        pModel.addAttribute("produits", produits);

        return "Accueil";
    }

    @RequestMapping("/details-produit/{id}")
    public String ficheProduit(@PathVariable int id, Model pModel) {

        ProductBean produit = mProduitProxy.recupererUnProduit(id);
        pModel.addAttribute("produit", produit);

        return "FicheProduit";
    }


}
