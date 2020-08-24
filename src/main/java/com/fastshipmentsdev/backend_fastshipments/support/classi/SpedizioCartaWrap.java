package com.fastshipmentsdev.backend_fastshipments.support.classi;

import com.fastshipmentsdev.backend_fastshipments.d_entity.CartaCredito;
import com.fastshipmentsdev.backend_fastshipments.d_entity.Spedizione;

public class SpedizioCartaWrap {

    private Spedizione spedizione;

    private CartaCredito cartaCredito;

    public Spedizione getSpedizione() {
        return spedizione;
    }

    public CartaCredito getCartaCredito() {
        return cartaCredito;
    }

    public void setSpedizione(Spedizione spedizione) {
        this.spedizione = spedizione;
    }

    public void setCartaCredito(CartaCredito cartaCredito) {
        this.cartaCredito = cartaCredito;
    }
}
