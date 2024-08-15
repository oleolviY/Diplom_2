package model;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;

@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class Order {
    private Integer id;
    private Integer courierId;
    private String firstName;
    private String lastName;
    private String address;
    private String metroStation;
    private String phone;
    private Integer rentTime;
    private String deliveryDate;
    private Integer track;
    private String[] color;
    private String comment;
    private String createdAt;
    private String updatedAt;
    private Integer status;
}
