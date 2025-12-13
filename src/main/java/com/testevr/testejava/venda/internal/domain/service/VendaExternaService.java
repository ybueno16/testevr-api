package com.testevr.testejava.venda.internal.domain.service;

import com.testevr.testejava.venda.internal.application.dto.VendaExternaRequest;
import com.testevr.testejava.venda.internal.application.dto.VendaExternaResponse;

public interface VendaExternaService {
    VendaExternaResponse processarVenda(VendaExternaRequest request);
}
