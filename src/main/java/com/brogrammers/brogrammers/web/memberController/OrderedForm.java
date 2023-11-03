package com.brogrammers.brogrammers.web.memberController;

import com.brogrammers.brogrammers.domain.order.Orders;
import lombok.Data;

@Data
public class OrderedForm {
    Orders orders;
    String fileName;
}
