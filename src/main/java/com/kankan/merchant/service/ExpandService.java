package com.kankan.merchant.service;

import com.kankan.merchant.module.merchant.Expand;
import java.util.List;

public interface ExpandService {

    void commitExpand (Expand expand);

    List<Expand> findExpand (Expand expand);
}
