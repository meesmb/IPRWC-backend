package com.meesmb.iprwc.dao;

import com.meesmb.iprwc.model.Account;
import com.meesmb.iprwc.model.ShoppingCart;
import com.meesmb.iprwc.model.ShoppingCartProduct;
import com.meesmb.iprwc.repository.AccountRepository;
import com.meesmb.iprwc.repository.ShoppingCartProductRepository;
import com.meesmb.iprwc.repository.ShoppingCartRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

import java.util.HashSet;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;

@Component
public class ShoppingCartDao {
    @Autowired
    ShoppingCartRepository shoppingCartRepository;
    @Autowired
    AccountRepository accountRepository;
    @Autowired
    ShoppingCartProductRepository shoppingCartProductRepository;

    public ResponseEntity<ShoppingCart> getShoppingCartByAccountId(String AccountEmail) {
        Account account = accountRepository.findByEmail(AccountEmail);
        if (account == null) return new ResponseEntity("account not found", HttpStatus.NOT_FOUND);
        return new ResponseEntity<ShoppingCart>(account.getShoppingCart(), HttpStatus.OK);
    }

    public ResponseEntity<ShoppingCart> setShoppingCart(ShoppingCart cart, String accountEmail) {
        Account account = accountRepository.findByEmail(accountEmail);

        if (account == null) return new ResponseEntity("account not found", HttpStatus.NOT_FOUND);

        if (account.getShoppingCart() == null) {
            ShoppingCart c = new ShoppingCart(new ShoppingCartProduct[0]);
            shoppingCartRepository.save(c);
            account.setShoppingCart(c);
        }
        // remove old products
        emptyShoppingCart(account);
        // put into db if not exist yet
        Set<ShoppingCartProduct> n = createAllShoppingCartProducts(cart.getProducts());
        // save them into the account
        account.getShoppingCart().setProducts(n);

        this.shoppingCartRepository.save(account.getShoppingCart());
        accountRepository.save(account);
        return new ResponseEntity<ShoppingCart>(account.getShoppingCart(), HttpStatus.OK);
    }

    Set<ShoppingCartProduct> createAllShoppingCartProducts(Set<ShoppingCartProduct> products) {
        for (ShoppingCartProduct p : products) {
            if (p.getId() == null) {
                p.setId(UUID.randomUUID().toString());
            }
            this.shoppingCartProductRepository.save(p);
        }
        return products;
    }

    // delete all products from a shopping cart
    void emptyShoppingCart(Account account) {
        Set<ShoppingCartProduct> old = account.getShoppingCart().getProducts();
        account.getShoppingCart().setProducts(new HashSet<>());
        this.shoppingCartRepository.save(account.getShoppingCart());
        // all shoppingCartProducts have to be deleted
        for (ShoppingCartProduct p : old) {
            Optional<ShoppingCartProduct> prod = this.shoppingCartProductRepository.findById(p.getId());
            prod.ifPresent(shoppingCartProduct -> this.shoppingCartProductRepository.delete(shoppingCartProduct));
        }
    }
}
