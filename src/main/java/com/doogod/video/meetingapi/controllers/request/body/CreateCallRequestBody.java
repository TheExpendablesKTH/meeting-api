package com.doogod.video.meetingapi.controllers.request.body;

import java.beans.ConstructorProperties;
import java.util.List;

public class CreateCallRequestBody {
    private List<Integer> relatives;

    @ConstructorProperties({"relatives"})
    public CreateCallRequestBody(List<Integer> relatives) {
        this.relatives = relatives;
    }

    public List<Integer> getRelatives() {
        return relatives;
    }

    public void setRelatives(List<Integer> relatives) {
        this.relatives = relatives;
    }
}
