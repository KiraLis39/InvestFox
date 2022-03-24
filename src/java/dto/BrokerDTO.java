package dto;

import lombok.*;

import java.util.ArrayList;

@Data
@EqualsAndHashCode
@AllArgsConstructor
@ToString
@NonNull
public class BrokerDTO {
    String name;
    ArrayList<Float> inputList = new ArrayList<>(4) {
        {
            add(0f);
            add(0f);
            add(0f);
            add(0f);
        }
    };
    ArrayList<Float> outputList = new ArrayList<>(4) {
        {
            add(0f);
            add(0f);
            add(0f);
            add(0f);
        }
    };

    public BrokerDTO() {

    }
}
