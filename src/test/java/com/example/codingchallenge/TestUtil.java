package com.example.codingchallenge;

import java.util.List;

public class TestUtil {
    public static List<User> getUserList() {
        return List.of(
                User.builder()
                        .id(3721)
                        .name("Chaturbhuj Chaturvedi")
                        .email("chaturvedi_chaturbhuj@greenholt-kertzmann.io")
                        .gender("male")
                        .status("inactive")
                        .build(),
                User.builder()
                        .id(3720)
                        .name("Gouranga Khatri")
                        .email("gouranga_khatri@oconnell-rau.info")
                        .gender("male")
                        .status("inactive")
                        .build(),
                User.builder()
                        .id(3718)
                        .name("Achintya Somayaji")
                        .email("achintya_somayaji@christiansen.biz")
                        .gender("female")
                        .status("inactive")
                        .build()

        );
    }

}
